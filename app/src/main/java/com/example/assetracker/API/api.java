package com.example.assetracker.API;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class api {

    private static String getEncodedCredentials(JsonObject user) {
        String credentials = user.get("username").getAsString() + ":" + user.get("password").getAsString();
        return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
    }

    private static String urlBuilder(String resource, String queryParams, String id) {
        String baseUrl = "http://10.7.10.157:8000/api/" + resource + "/";
        if (queryParams != null && id != null) {
            throw new IllegalArgumentException("queryParams and id both cannot have value at the same time.");
        }
        if (queryParams != null) {
            baseUrl += "?" + queryParams;
        } else if (id != null) {
            baseUrl += id;
        }
        return baseUrl;
    }

    private static JsonElement jsonConverter(String responseBody) {
        Gson gson = new Gson();
        return gson.fromJson(responseBody, JsonElement.class);
    }

    public static JsonElement callGenericApi(final JsonObject user, final String resource, final String requestMethod,
                                             final String queryParams, final String id, final String requestBody) {
        final String authHeaderValue = getEncodedCredentials(user);
        final String url = urlBuilder(resource, queryParams, id);

        final StringBuilder response = new StringBuilder();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL apiUrl = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                    connection.setRequestMethod(requestMethod.toUpperCase());
                    if (authHeaderValue != null) {
                        connection.setRequestProperty("Authorization", authHeaderValue);
                    }
                    if (requestMethod.equalsIgnoreCase("post")) {
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.setDoOutput(true);
                        OutputStream os = connection.getOutputStream();
                        os.write(requestBody.getBytes());
                        os.flush();
                        os.close();
                    }

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line;
                        while ((line = in.readLine()) != null) {
                            response.append(line);
                        }
                        in.close();
                    } else {
                        response.append("Failed to retrieve data. Response code: ").append(responseCode);
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    response.append("Failed to retrieve data. Exception: ").append(e.getMessage());
                }
            }
        });
        thread.start();
        try {
            thread.join(); // Wait for the thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return jsonConverter(response.toString());
    }
}
