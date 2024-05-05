package com.example.assetracker.misc;

import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assetracker.API.api;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Refresher extends AppCompatActivity {
    protected static SharedPreferences sharedPreferences;
    private boolean came_back = false;

    @Override
    protected void onPause() {
        super.onPause();
        came_back = true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (came_back == true)
        {
            recreate();
            came_back = false;
        }
    }
    protected void updateUserPrice()
    {
        JsonObject json_object = Misc_handler.get_sharedpreferencesob(sharedPreferences);
        JsonElement response = api.callGenericApi(json_object, "update_user_prices", "get", null, null, null);

        if (response != null)
        {
            recreate();
        }
        else
        {
            Toast.makeText(this, "Null Response received on Refreshing stocks", Toast.LENGTH_SHORT).show();
        }
    }

}
