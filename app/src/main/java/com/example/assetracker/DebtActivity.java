package com.example.assetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assetracker.API.api;
import com.example.assetracker.misc.Refresher;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Locale;

public class DebtActivity extends Refresher {

    TextView textViewreturnsMF,textViewInvestedMF, textViewinvestedquity_debt;
    Button buttonpercentagereturns_debt,buttoncurrentvalue_debt,buttoncurrentvalue_1_debt, Mutualfundsbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt);

        textViewinvestedquity_debt = findViewById(R.id.textViewinvestedquity_debt);
        textViewreturnsMF=findViewById(R.id.textViewreturnsMF);
        textViewInvestedMF=findViewById(R.id.textViewInvestedMF);
        buttoncurrentvalue_1_debt = findViewById(R.id.buttoncurrentvalue_1_debt);
        buttoncurrentvalue_debt = findViewById(R.id.buttoncurrentvalue_debt);
        buttonpercentagereturns_debt = findViewById(R.id.buttonpercentagereturns_debt);
        Mutualfundsbtn = (Button) findViewById(R.id.Mutualfundsbtn);
        Mutualfundsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DebtActivity.this, StocksActivity2.class);
                intent1.putExtra("asset_type", "debt");
                intent1.putExtra("holding_type", "mutual_fund");
                startActivity(intent1);
            }
        });

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
            String inv_equity = responseData.get("invested").getAsString();
            double equity_inv = Double.parseDouble(inv_equity);
            String equity_invested = String.format(Locale.US, "%.2f", equity_inv);
            textViewinvestedquity_debt.setText(equity_invested);

            String current_val = responseData.get("current_value").getAsString();
            double current_value = Double.parseDouble(current_val);
            String curr = String.format(Locale.US, "%.2f", current_value);
            buttoncurrentvalue_debt.setText(curr);

            String returns_val = responseData.get("returns_value").getAsString();
            double returns_value = Double.parseDouble(returns_val);
            String returns = String.format(Locale.US, "%.2f", returns_value);
            buttoncurrentvalue_1_debt.setText(returns);

            String returns_per = responseData.get("returns_percentage").getAsString();
            double returns_percent = Double.parseDouble(returns_per);
            String percent = String.format(Locale.US, "%.2f", returns_percent);
            buttonpercentagereturns_debt.setText(percent);


            JsonObject MF = responseData.getAsJsonObject("mutual_fund");
            if(MF != null) {
                String MFInvestedStr = MF.get("invested").getAsString();
                double MFInvested = Double.parseDouble(MFInvestedStr);

                String MFReturnsValueStr = MF.get("returns_value").getAsString();
                double MFReturnsValue = Double.parseDouble(MFReturnsValueStr);

                String MFReturnsPercentageStr = MF.get("returns_percentage").getAsString();
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