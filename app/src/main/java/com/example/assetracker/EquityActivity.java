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
import com.example.assetracker.misc.Misc_handler;
import com.example.assetracker.misc.Refresher;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Locale;

public class EquityActivity extends Refresher {

    TextView textViewstockspercentage,textViewstocksprice,textViewmutualfundspercentage,textViewmutualfundsprice,textViewinvestedquity;
    Button buttoncurrentvalue,buttonpercentagereturns,buttoncurrentvalue_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equity);

        textViewstockspercentage=findViewById(R.id.textViewStockspercentage);
        textViewstocksprice=findViewById(R.id.textViewStockprice);
        textViewmutualfundspercentage=findViewById(R.id.textViewMFpercentage);
        textViewmutualfundsprice=findViewById(R.id.textViewmutualfundsprice);
        textViewinvestedquity=findViewById(R.id.textViewinvestedquity_3);

        buttoncurrentvalue=findViewById(R.id.buttoncurrentvalue_3);
        buttonpercentagereturns=findViewById(R.id.buttonpercentagereturns_1);
        buttoncurrentvalue_1=findViewById(R.id.buttonreturnvaluequity);

        fetchDataForPortfolio();

        ButtonHandlerEquity buttonhandlerhome  = new ButtonHandlerEquity(this);
        findViewById(R.id.stocksbtn).setOnClickListener(buttonhandlerhome);
        findViewById(R.id.equityMutualFundsbtn).setOnClickListener(buttonhandlerhome);
    }

    private void fetchDataForPortfolio() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);


        String username = sharedPreferences.getString("username", "admin").replace("\"", "");
        String password = sharedPreferences.getString("password", "123456789").replace("\"", "");
        JsonObject requestData = new JsonObject();
        requestData.addProperty("username", username);
        requestData.addProperty("password", password);
        JsonElement response = api.callGenericApi(requestData, "portfolio_information", "get", "asset_type=equity", null, null);

        JsonObject responseData = response.getAsJsonObject();
        if (responseData != null) {
            String inv_equity = responseData.get("invested").getAsString();
            double equity_inv = Double.parseDouble(inv_equity);
            String equity_invested = String.format(Locale.US, "%.2f", equity_inv);
            textViewinvestedquity.setText(equity_invested);

            String current_val = responseData.get("current_value").getAsString();
            double current_value = Double.parseDouble(current_val);
            String curr = String.format(Locale.US, "%.2f", current_value);
            buttoncurrentvalue.setText(curr);

            String returns_val = responseData.get("returns_value").getAsString();
            double returns_value = Double.parseDouble(returns_val);
            String returns = String.format(Locale.US, "%.2f", returns_value);
            buttoncurrentvalue_1.setText(returns);

            String returns_per = responseData.get("returns_percentage").getAsString();
            double returns_percent = Double.parseDouble(returns_per);
            String percent = String.format(Locale.US, "%.2f", returns_percent);
            buttonpercentagereturns.setText(percent);

            JsonObject stocks = responseData.getAsJsonObject("stock");
            if(stocks != null) {
                String stocksInvestedStr = stocks.get("invested").getAsString();
                double stocksInvested = Double.parseDouble(stocksInvestedStr);

                String stocksReturnsValueStr = stocks.get("returns_value").getAsString();
                double stocksReturnsValue = Double.parseDouble(stocksReturnsValueStr);

                String stocksReturnsPercentageStr = stocks.get("returns_percentage").getAsString();
                double stocksReturnsPercentage = Double.parseDouble(stocksReturnsPercentageStr);

                String stocksInvestedAmountString = String.format(Locale.US, "%.2f", stocksInvested);
                textViewstocksprice.setText(stocksInvestedAmountString);

                textViewstockspercentage.setText(Misc_handler.to_returns(stocksReturnsValue ,stocksReturnsPercentage));

                if (stocksReturnsValue >= 0) {
                    textViewstockspercentage.setTextColor(Color.GREEN);
                } else {
                    textViewstockspercentage.setTextColor(Color.RED);
                }
            }
            else{
                Toast.makeText(this, "Failed to get Stocks data", Toast.LENGTH_SHORT).show();
            }

            JsonObject MF = responseData.getAsJsonObject("mutual_fund");
            if(MF != null) {
                String MFInvestedStr = MF.get("invested").getAsString();
                double MFInvested = Double.parseDouble(MFInvestedStr);

                String MFReturnsValueStr = MF.get("returns_value").getAsString();
                double MFReturnsValue = Double.parseDouble(MFReturnsValueStr);

                String MFReturnsPercentageStr = MF.get("returns_percentage").getAsString();
                double MFReturnsPercentage = Double.parseDouble(MFReturnsPercentageStr);

                String MFInvestedAmountString = String.format(Locale.US, "%.2f", MFInvested);
                textViewmutualfundsprice.setText(MFInvestedAmountString);

                textViewmutualfundspercentage.setText(Misc_handler.to_returns(MFReturnsValue ,MFReturnsPercentage));


                if (MFReturnsValue >= 0) {
                    textViewmutualfundspercentage.setTextColor(Color.GREEN);
                } else {
                    textViewmutualfundspercentage.setTextColor(Color.RED);
                }
            }
            else{
                Toast.makeText(this, "Failed to get Mutual funds data", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Failed to fetch data for Equity!", Toast.LENGTH_SHORT).show();
        }
    }
}

class ButtonHandlerEquity implements View.OnClickListener
{
    private Context context;
    ButtonHandlerEquity(Context context)
    {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        if (id == R.id.stocksbtn)
        {
            intent = new Intent(this.context, StocksActivity2.class);
            intent.putExtra("asset_type", "equity");
            intent.putExtra("holding_type", "stock");
        }
        else if (id == R.id.equityMutualFundsbtn)
        {
            intent = new Intent(this.context, StocksActivity2.class);
            intent.putExtra("asset_type", "equity");
            intent.putExtra("holding_type", "mutual_fund");

        }

        context.startActivity(intent);

    }
}