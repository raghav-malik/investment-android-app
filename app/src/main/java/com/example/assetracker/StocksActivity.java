package com.example.assetracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assetracker.API.api;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Locale;

public class StocksActivity extends AppCompatActivity {

    TextView textPortfolio;
    TextView investedAmt;
    TextView textViewreturnsEquity;
    TextView textViewEquityInvested;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_stocks);

        textPortfolio = findViewById(R.id.textPortfolio);
        investedAmt = findViewById(R.id.textView4);
        textViewreturnsEquity=findViewById(R.id.textViewreturnsEquity);
        textViewEquityInvested=findViewById(R.id.textViewEquityInvested);
        fetchDataForPortfolio();
    }

    private void fetchDataForPortfolio() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "admin");
        String password = sharedPreferences.getString("password", "123456789");
        JsonObject requestData = new JsonObject();
        requestData.addProperty("username", username);
        requestData.addProperty("password", password);
        JsonElement response = api.callGenericApi(requestData, "portfolio_information", "get", null, null, null);

        if (response != null) {
            JsonObject responseData = response.getAsJsonObject();

            // Equity
            JsonObject equity = responseData.getAsJsonObject("equity");
            if(equity != null) {
                String equityInvestedStr = equity.get("invested").getAsString();
                double equityInvested = Double.parseDouble(equityInvestedStr);

                String equityReturnsValueStr = equity.get("returns_value").getAsString();
                double equityReturnsValue = Double.parseDouble(equityReturnsValueStr);

                String equityReturnsPercentageStr = equity.get("returns_percentage").getAsString();
                double equityReturnsPercentage = Double.parseDouble(equityReturnsPercentageStr);

                String equityInvestedAmountString = String.format(Locale.US, "%.2f", equityInvested);
                textViewEquityInvested.setText(equityInvestedAmountString);

                String equityreturnsString = String.format(Locale.US, "%.2f (%s%%)", equityReturnsValue, equityReturnsPercentage);
                textViewreturnsEquity.setText(equityreturnsString);
            }
            else{
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Failed to fetch data for portfolio", Toast.LENGTH_SHORT).show();
        }
    }

}