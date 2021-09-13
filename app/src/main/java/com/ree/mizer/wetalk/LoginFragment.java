package com.ree.mizer.wetalk;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class LoginFragment extends Fragment implements TextWatcher {

    public Context context;
    private EditText txtUsername;
    private Button btnLogin;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        txtUsername = view.findViewById(R.id.tv_username);
        btnLogin = view.findViewById(R.id.btn_login);

        txtUsername.addTextChangedListener(this);

        btnLogin.setOnClickListener(v -> {
            String username = txtUsername.getText().toString().trim();

            if (username.isEmpty() || username.equals(" ")) {
                Toast.makeText(context, "Enter Username", Toast.LENGTH_SHORT).show();
            } else {
                MainActivity.USER_NAME = username;
                Fragment fragment2 = new HomeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        return view;

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}