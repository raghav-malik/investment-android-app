package com.example.assetracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assetracker.API.api;
import com.example.assetracker.misc.Misc_handler;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView username, password, email, confirm_password, first_name, last_name;
        username = findViewById(R.id.inputUsername);
        password = findViewById(R.id.inputPassword1);
        email = findViewById(R.id.inputEmail1);
        confirm_password = findViewById(R.id.inputConfirmPassword);
        first_name = findViewById(R.id.inputFirstName);
        last_name = findViewById(R.id.inputLastName);

        ButtonHandlerRegister btnRegisterHandler = new ButtonHandlerRegister(this,
                username, email, password, confirm_password, first_name, last_name
        );

        TextView already_have_account= findViewById(R.id.Alreadyhaveanaccount);
        TextView register= findViewById(R.id.btnRegister);

        already_have_account.setOnClickListener(btnRegisterHandler);
        register.setOnClickListener(btnRegisterHandler);
    }
}

class ButtonHandlerRegister implements View.OnClickListener
{
    private Context context;
    private TextView username, password, email, confirm_password, first_name, last_name;
    private TextView[] textview_arr;
    ButtonHandlerRegister(Context context, TextView username, TextView email,
                          TextView password, TextView confirm_password, TextView first_name, TextView last_name)
    {
        this.context = context;
        this.username = username;
        this.password = password;
        this.email = email;
        this.confirm_password = confirm_password;
        this.first_name = first_name;
        this.last_name = last_name;
        textview_arr = new TextView[]{this.username, this.password, this.email, this.confirm_password, this.first_name, this.last_name};
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.Alreadyhaveanaccount)
        {
            Intent intent;
            intent = new Intent(this.context, LoginActivity.class);
            context.startActivity(intent);
        }
        else if (id == R.id.btnRegister)
        {
            ArrayList<TextView> empty_fields = Misc_handler.is_textview_empty(textview_arr);
            if (empty_fields.size() != 0)
            {
                StringBuilder string = new StringBuilder("Some of your fields are empty:\n");
                for (TextView textView: empty_fields)
                {
                    string.append(textView.getHint().toString()).append("\n");
                }
                Toast.makeText(context, string.toString(), Toast.LENGTH_SHORT).show();
            }
            else {
                JsonObject user = Misc_handler.json_text_view_builder(textview_arr);
                JsonElement json = api.callGenericApi(null, "register", "post", null, null, user.toString());
                if (json != null) {
                    JsonObject json_obj = json.getAsJsonObject();
                    if (json_obj.get("error") != null)
                    {
                        Toast.makeText(context, Misc_handler.backend_error_string_builder(json_obj, textview_arr), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent intent;
                        intent = new Intent(this.context, LoginActivity.class);
                        context.startActivity(intent);
                    }

                }
                else {
                    Toast.makeText(context, "Something went Wrong on our side.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}