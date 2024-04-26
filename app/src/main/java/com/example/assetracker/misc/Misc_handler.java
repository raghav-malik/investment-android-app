package com.example.assetracker.misc;

import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class Misc_handler {
    public static ArrayList<TextView> is_textview_empty(TextView arr[])
    {
        ArrayList<TextView> empty_text_views = new ArrayList<>();

        for (TextView element: arr)
        {
            if (element.getText().toString().isEmpty())
            {
                empty_text_views.add(element);
            }
        }
        return empty_text_views;
    }

    public static  ArrayList<TextView> is_textview_empty(TextView textView)
    {
        ArrayList<TextView> empty_text_views = new ArrayList<>();
        if (textView.getText().toString().isEmpty())
        {
            empty_text_views.add(textView);
        }
        return empty_text_views;
    }

    public static JsonObject json_text_view_builder(TextView arr[])
    {
        JsonObject json_object = new JsonObject();
        for (TextView textView : arr) {
            Object tagObject = textView.getTag();
            if (tagObject != null) {
                String textViewName = tagObject.toString();
                String text = textView.getText().toString().trim();
                if (!text.isEmpty()) {
                    json_object.addProperty(textViewName, text);
                }
            }
        }
        return json_object;

    }

    public static String backend_error_string_builder(JsonObject json, TextView[] textViewarr)
    {
        String textViewName;
        StringBuilder string = new StringBuilder();
        JsonObject json_errors = json.get("error").getAsJsonObject();
        for(TextView textView: textViewarr)
        {
            Object tagObject = textView.getTag();
            if (json_errors.get(tagObject.toString()) != null)
            {
                textViewName = tagObject.toString();
                JsonArray error_strings = json_errors.getAsJsonArray(textViewName);
                string.setLength(0);

                string.append(textViewName +":\n");

                for (JsonElement element : error_strings) {
                    string.append(element.getAsString()).append("\n");
                }
            }

        }
        return string.toString();

    }

}


