package com.ree.mizer.wetalk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ree.mizer.wetalk.room.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter {

    private static final int TYPE_MESSAGE_SENT = 0;
    private static final int TYPE_MESSAGE_RECEIVED = 1;

    private final LayoutInflater inflater;
    private final List<JSONObject> messages = new ArrayList<>();
    private List<Message> mMessages = new ArrayList<>();

    public MessageAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public int getItemViewType(int position) {
        JSONObject message = messages.get(position);

        try {
            if (message.getBoolean("isSent")) {
                if (message.has("message")) {
                    return TYPE_MESSAGE_SENT;
                }
            } else {
                if (message.has("message")) {
                    return TYPE_MESSAGE_RECEIVED;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_MESSAGE_SENT:
                view = inflater.inflate(R.layout.custom_sent_msg, parent, false);
                return new SentMessageHolder(view);
            case TYPE_MESSAGE_RECEIVED:
                view = inflater.inflate(R.layout.custome_recieved_msg, parent, false);
                return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        JSONObject message = messages.get(position);

        try {
            if (message.getBoolean("isSent")) {
                if (message.has("message")) {
                    SentMessageHolder sentMessageHolder = (SentMessageHolder) holder;
                    sentMessageHolder.tvMessage.setText(message.getString("message"));
                }

            } else {
                if (message.has("message")) {
                    ReceivedMessageHolder receivedMessageHolder = (ReceivedMessageHolder) holder;
                    receivedMessageHolder.tvName.setText(message.getString("username"));
                    receivedMessageHolder.tvMessage.setText(message.getString("message"));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setMessages(List<Message> messages) {
        mMessages = messages;
        notifyDataSetChanged();
    }

    public void addItem(JSONObject jsonObject) {
        messages.add(jsonObject);
        Message messageObj = null;
        try {
            String uuid = jsonObject.getString("uuid");
            String textMessage = jsonObject.getString("message");
            messageObj = new Message(uuid, textMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mMessages.add(messageObj);
        notifyDataSetChanged();
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;

        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);

            tvMessage = itemView.findViewById(R.id.tv_sent_smg);
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMessage;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_sender_name);
            tvMessage = itemView.findViewById(R.id.tv_received_msg);
        }
    }
}
