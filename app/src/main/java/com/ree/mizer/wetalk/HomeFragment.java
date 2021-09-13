package com.ree.mizer.wetalk;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ree.mizer.wetalk.room.Message;
import com.ree.mizer.wetalk.room.MessageViewModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;


public class HomeFragment extends Fragment implements TextWatcher {

    private final String TAG = this.getClass().getSimpleName();
    //Web socket
    private final String SERVER_PATH = "ws://10.100.6.137:3000";
    //The context
    public Context context;
    //My views
    private RecyclerView recyclerView;
    private View view;
    private EditText edtMessage;
    private TextView tvSend;
    private String username;
    private WebSocket webSocket;
    //Adapters
    private MessageAdapter messageAdapter;
    //View Model
    private MessageViewModel messageViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //Username of the logged in user
        username = MainActivity.USER_NAME;

        initiateSocketConnection();

        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);

        messageAdapter = new MessageAdapter(getLayoutInflater());
        //check here if doe not run
        messageViewModel.getAllMessages().observe(getActivity(), new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messageAdapter.setMessages(messages);
            }
        });

        return view;

    }

    private void initiateSocketConnection() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String message = s.toString().trim();

        if (message.isEmpty()) {
            resetMessageEdit();
            tvSend.setVisibility(View.INVISIBLE);
        } else {
            tvSend.setVisibility(View.VISIBLE);
        }
    }

    //this method avoid infinite loop
    private void resetMessageEdit() {
        edtMessage.removeTextChangedListener(this);
        edtMessage.setText("");
        edtMessage.addTextChangedListener(this);
    }

    private void initializeMyViews() {
        recyclerView = view.findViewById(R.id.recyclerview);
        edtMessage = view.findViewById(R.id.edt_message);
        tvSend = view.findViewById(R.id.tv_send);

        messageAdapter = new MessageAdapter(getLayoutInflater());
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //make the send btn visible when user enters text
        edtMessage.addTextChangedListener(this);

        tvSend.setOnClickListener(v -> {
            JSONObject jsonObject = new JSONObject();
            String txtMessage = edtMessage.getText().toString();
            final String message_id = UUID.randomUUID().toString();

            try {
                if (!txtMessage.isEmpty()) {
                    jsonObject.put("uuid", message_id);
                    jsonObject.put("username", username);
                    jsonObject.put("message", txtMessage);

                    //send message to socket
                    webSocket.send(jsonObject.toString());
                    jsonObject.put("isSent", true);
                    messageAdapter.addItem(jsonObject);
                    resetMessageEdit();

                    //save message to Room DB
                    Message message = new Message(message_id, txtMessage);
                    messageViewModel.insert(message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);
            //initialise my views when the socket connection is success
            getActivity().runOnUiThread(() -> {

                Toast.makeText(context, "Online", Toast.LENGTH_SHORT).show();

                initializeMyViews();
            });
        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            Log.d("Testing:>>>", "Failed to connect" + t.getMessage());
        }


        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            super.onMessage(webSocket, text);

            getActivity().runOnUiThread(() -> {
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    jsonObject.put("isSent", false);
                    messageAdapter.addItem(jsonObject);

                    Message messageObj = null;
                    try {
                        String uuid = jsonObject.getString("uuid");
                        String textMessage = jsonObject.getString("message");
                        messageObj = new Message(uuid, textMessage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }

    }
}