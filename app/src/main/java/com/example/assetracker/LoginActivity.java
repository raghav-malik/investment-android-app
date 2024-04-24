package com.example.assetracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assetracker.API.api;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button signup_btn= findViewById(R.id.SignUpButton);
        Button login_btn = findViewById(R.id.btnLogin);

        TextView usernameView = findViewById(R.id.inputLoginUsername);
        TextView passwordView = findViewById(R.id.inputPassword);

        ButtonHandler btnHandler = new ButtonHandler(this, usernameView, passwordView);

        signup_btn.setOnClickListener(btnHandler);
        login_btn.setOnClickListener(btnHandler);


        //        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
//            }
//        });

    }
}

class ButtonHandler implements View.OnClickListener
{
    private Context context;
    private TextView username, password;
    ButtonHandler(Context context, TextView username, TextView password)
    {
        this.context = context;
        this.username = username;
        this.password = password;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.SignUpButton)
        {
            Intent intent;
            intent = new Intent(this.context, RegisterActivity.class);
            context.startActivity(intent);
        }
        else if (id == R.id.btnLogin)
        {
            JsonObject user =  new JsonObject();
            user.addProperty("username", username.getText().toString());
            user.addProperty("password",password.getText().toString());
//            JsonObject json= (JsonObject) api.callGenericApi(user, "login", "get",null,null, null);
            JsonElement obj = api.callGenericApi(user, "assets", "get", null, null, null);
            System.out.println(obj);
            System.out.println("Hello Guys, Purav this side");

        }

    }
}