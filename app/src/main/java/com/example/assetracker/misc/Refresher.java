package com.example.assetracker.misc;

import androidx.appcompat.app.AppCompatActivity;

public class Refresher extends AppCompatActivity {
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
}
