package com.example.assetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assetracker.API.api;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Locale;

public class DebtActivity extends AppCompatActivity {

    TextView textViewreturnsMF,textViewInvestedMF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt);

        textViewreturnsMF=findViewById(R.id.textViewreturnsMF);
        textViewInvestedMF=findViewById(R.id.textViewInvestedMF);

        fetchDataForPortfolio();
    }

    private void fetchDataForPortfolio() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);


        String username = sharedPreferences.getString("username", "admin").replace("\"", "");
        String password = sharedPreferences.getString("password", "123456789").replace("\"", "");
        JsonObject requestData = new JsonObject();
        requestData.addProperty("username", username);
        requestData.addProperty("password", password);
        JsonElement response = api.callGenericApi(requestData, "portfolio_information", "get", "asset_type=debt", null, null);

        JsonObject responseData = response.getAsJsonObject();
        if (responseData != null) {

            // Equity
            JsonObject equity = responseData.getAsJsonObject("mutual_fund");
            if(equity != null) {
                String MFInvestedStr = equity.get("invested").getAsString();
                double MFInvested = Double.parseDouble(MFInvestedStr);

                String MFReturnsValueStr = equity.get("returns_value").getAsString();
                double MFReturnsValue = Double.parseDouble(MFReturnsValueStr);

                String MFReturnsPercentageStr = equity.get("returns_percentage").getAsString();
                double MFReturnsPercentage = Double.parseDouble(MFReturnsPercentageStr);

                String MFInvestedAmountString = String.format(Locale.US, "%.2f", MFInvested);
                textViewInvestedMF.setText(MFInvestedAmountString);

                String MFreturnsString = String.format(Locale.US, "%.2f (%s%%)", MFReturnsValue, MFReturnsPercentage);
                textViewreturnsMF.setText(MFreturnsString);

                if (MFReturnsValue >= 0) {
                    textViewreturnsMF.setTextColor(Color.GREEN);
                } else {
                    textViewreturnsMF.setTextColor(Color.RED);
                }
            }
            else{
                Toast.makeText(this, "Failed to get Mutual funds data", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Failed to fetch data for Debt", Toast.LENGTH_SHORT).show();
        }
    }
}