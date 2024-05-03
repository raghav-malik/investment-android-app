package com.example.assetracker;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Locale;

public class GoldActivity extends AppCompatActivity {

    TextView textViewinvestedquity_gold,textViewgoldetfpercentage,textViewgoldetfprice;
    Button buttonpercentagereturns_1,buttoncurrentvalue_2,buttoncurrentvalue_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold);

        textViewinvestedquity_gold=findViewById(R.id.textViewinvestedGold_3);
        textViewgoldetfpercentage=findViewById(R.id.textViewgoldETFpercentage);
        textViewgoldetfprice=findViewById(R.id.textViewgoldETFprice);

        buttonpercentagereturns_1=findViewById(R.id.buttonpercentagereturnsGold_1);
        buttoncurrentvalue_2=findViewById(R.id.buttonreturnsvalueGold_2);
        buttoncurrentvalue_3=findViewById(R.id.buttoncurrentvalueGold_3);

        fetchDataForPortfolio();


        ButtonHandlerGold buttonhandlerhome  = new ButtonHandlerGold(this);
        findViewById(R.id.btngoldeft).setOnClickListener(buttonhandlerhome);
    }

    private void fetchDataForPortfolio() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);


        String username = sharedPreferences.getString("username", "admin").replace("\"", "");
        String password = sharedPreferences.getString("password", "123456789").replace("\"", "");
        JsonObject requestData = new JsonObject();
        requestData.addProperty("username", username);
        requestData.addProperty("password", password);
        JsonElement response = api.callGenericApi(requestData, "portfolio_information", "get", "asset_type=gold", null, null);

        JsonObject responseData = response.getAsJsonObject();
        if (responseData != null) {
            String inv_equity = responseData.get("invested").getAsString();
            double equity_inv = Double.parseDouble(inv_equity);
            String equity_invested = String.format(Locale.US, "%.2f", equity_inv);
            textViewinvestedquity_gold.setText(equity_invested);

            String current_val = responseData.get("current_value").getAsString();
            double current_value = Double.parseDouble(current_val);
            String curr = String.format(Locale.US, "%.2f", current_value);
            buttoncurrentvalue_3.setText(curr);

            String returns_val = responseData.get("returns_value").getAsString();
            double returns_value = Double.parseDouble(returns_val);
            String returns = String.format(Locale.US, "%.2f", returns_value);
            buttoncurrentvalue_2.setText(returns);

            String returns_per = responseData.get("returns_percentage").getAsString();
            double returns_percent = Double.parseDouble(returns_per);
            String percent = String.format(Locale.US, "%.2f", returns_percent);
            buttonpercentagereturns_1.setText(percent);

            JsonObject stocks = responseData.getAsJsonObject("etf");
            if(stocks != null) {
                String stocksInvestedStr = stocks.get("invested").getAsString();
                double stocksInvested = Double.parseDouble(stocksInvestedStr);

                String stocksReturnsValueStr = stocks.get("returns_value").getAsString();
                double stocksReturnsValue = Double.parseDouble(stocksReturnsValueStr);

                String stocksReturnsPercentageStr = stocks.get("returns_percentage").getAsString();
                double stocksReturnsPercentage = Double.parseDouble(stocksReturnsPercentageStr);

                String stocksInvestedAmountString = String.format(Locale.US, "%.2f", stocksInvested);
                textViewgoldetfprice.setText(stocksInvestedAmountString);

                String stocksreturnsString = String.format(Locale.US, "%.2f (%s%%)", stocksReturnsValue, stocksReturnsPercentage);
                textViewgoldetfpercentage.setText(stocksreturnsString);

                if (stocksReturnsValue >= 0) {
                    textViewgoldetfpercentage.setTextColor(Color.GREEN);
                } else {
                    textViewgoldetfpercentage.setTextColor(Color.RED);
                }
            }
            else{
                Toast.makeText(this, "Failed to get ETF's data", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Failed to fetch data for gold assets!", Toast.LENGTH_SHORT).show();
        }
    }
}

class ButtonHandlerGold implements View.OnClickListener
{
    private Context context;
    ButtonHandlerGold(Context context)
    {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
//        if (id == R.id.btngoldeft)
//        {
//            intent = new Intent(this.context, ETFActivity.class);
//        }

        context.startActivity(intent);

    }
}