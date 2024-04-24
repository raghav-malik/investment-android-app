package com.example.assetracker;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assetracker.API.api;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView btn= findViewById(R.id.TextViewSignUp);
        JsonObject user1 =  new JsonObject();
        user1.addProperty("username", "admin");
        user1.addProperty("password","123456789");
        JsonArray json= (JsonArray) api.callGenericApi(user1, "assets", "get",null,null,null);
        System.out.println(json);
        //        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
//            }
//        });

    }
}