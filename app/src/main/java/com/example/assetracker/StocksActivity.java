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

public class StocksActivity extends Refresher {

    TextView  investedAmt, textViewreturnsEquity, textViewEquityInvested,
            textViewdebtreturns, textViewdebtprice,
            textViewgoldreturns, textViewgoldprice, textPortfolio;
    Button buttonpercentagereturns_home, buttoncurrentvalue_home,buttoncurrentvalue_1_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_stocks);
        investedAmt=findViewById(R.id.textViewinvested_home);
        buttonpercentagereturns_home=findViewById(R.id.buttonpercentagereturns_home);
        buttoncurrentvalue_home=findViewById(R.id.buttoncurrentvalue_home);
        buttoncurrentvalue_1_home=findViewById(R.id.buttoncurrentvalue_1_home);
        textViewreturnsEquity=findViewById(R.id.textViewreturnsEquity);
        textViewEquityInvested=findViewById(R.id.textViewEquityInvested);
        textViewdebtreturns=findViewById(R.id.textViewdebtreturns);
        textViewdebtprice=findViewById(R.id.textViewdebtprice);
        textViewgoldreturns=findViewById(R.id.textViewgoldreturns);
        textViewgoldprice=findViewById(R.id.textViewgoldprice);
        textPortfolio = findViewById(R.id.textPortfolio);

        fetchDataForPortfolio();


        ButtonHandlerHome buttonhandlerhome  = new ButtonHandlerHome(this);
        findViewById(R.id.btnEquity).setOnClickListener(buttonhandlerhome);
        findViewById(R.id.btndebt).setOnClickListener(buttonhandlerhome);
        findViewById(R.id.btngold).setOnClickListener(buttonhandlerhome);

    }

    private void fetchDataForPortfolio() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);


        String username = sharedPreferences.getString("username", "admin").replace("\"", "");
        String password = sharedPreferences.getString("password", "123456789").replace("\"", "");
        JsonObject requestData = new JsonObject();
        requestData.addProperty("username", username);
        requestData.addProperty("password", password);
        JsonElement response = api.callGenericApi(requestData, "portfolio_information", "get", null, null, null);

        JsonObject responseData = response.getAsJsonObject();


        if (responseData != null) {
            textPortfolio.setText(
                    Misc_handler.to_Title(sharedPreferences.getString("first_name", "raghav").replace("\"", "")) + "'s Portfolio"
            );
            String InvestedStr = responseData.get("invested").getAsString();
            double totalInvested = Double.parseDouble(InvestedStr);
            String totalInvestedAmountString = String.format(Locale.US, "%.2f", totalInvested);
            investedAmt.setText(totalInvestedAmountString);

            String current_val = responseData.get("current_value").getAsString();
            double current_value = Double.parseDouble(current_val);
            String curr = String.format(Locale.US, "%.2f", current_value);
            buttoncurrentvalue_home.setText(curr);

            String returns_val = responseData.get("returns_value").getAsString();
            double returns_value = Double.parseDouble(returns_val);
            String returns = String.format(Locale.US, "%.2f", returns_value);
            buttoncurrentvalue_1_home.setText(returns);

            String returns_per = responseData.get("returns_percentage").getAsString();
            double returns_percent = Double.parseDouble(returns_per);
            String percent = String.format(Locale.US, "%.2f", returns_percent);
            buttonpercentagereturns_home.setText(percent);


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

//                String equityreturnsString = String.format(Locale.US, "%.2f (%s%%)", equityReturnsValue, equityReturnsPercentage);
                textViewreturnsEquity.setText(Misc_handler.to_returns(equityReturnsValue ,equityReturnsPercentage));

                if (equityReturnsValue >= 0) {
                    textViewreturnsEquity.setTextColor(Color.GREEN);
                } else {
                    textViewreturnsEquity.setTextColor(Color.RED);
                }
            }
            else{
                Toast.makeText(this, "Failed to get equity data", Toast.LENGTH_SHORT).show();
            }

            //Debt
            JsonObject debt = responseData.getAsJsonObject("debt");
            if(debt != null) {
                String debtInvestedStr = debt.get("invested").getAsString();
                double debtInvested = Double.parseDouble(debtInvestedStr);

                String debtReturnsValueStr = debt.get("returns_value").getAsString();
                double debtReturnsValue = Double.parseDouble(debtReturnsValueStr);

                String debtReturnsPercentageStr = debt.get("returns_percentage").getAsString();
                double debtReturnsPercentage = Double.parseDouble(debtReturnsPercentageStr);

                String debtInvestedAmountString = String.format(Locale.US, "%.2f", debtInvested);
                textViewdebtprice.setText(debtInvestedAmountString);

                String debtreturnsString = String.format(Locale.US, "%.2f (%s%%)", debtReturnsValue, debtReturnsPercentage);
                textViewdebtreturns.setText(debtreturnsString);

                if (debtReturnsValue >= 0) {
                    textViewdebtreturns.setTextColor(Color.GREEN);
                } else {
                    textViewdebtreturns.setTextColor(Color.RED);
                }
            }
            else{
                Toast.makeText(this, "Failed to get debt data", Toast.LENGTH_SHORT).show();
            }

            //gold
            JsonObject gold = responseData.getAsJsonObject("gold");
            if(gold != null) {
                String goldInvestedStr = gold.get("invested").getAsString();
                double goldInvested = Double.parseDouble(goldInvestedStr);

                String goldReturnsValueStr = gold.get("returns_value").getAsString();
                double goldReturnsValue = Double.parseDouble(goldReturnsValueStr);

                String goldReturnsPercentageStr = gold.get("returns_percentage").getAsString();
                double goldReturnsPercentage = Double.parseDouble(goldReturnsPercentageStr);

                String goldInvestedAmountString = String.format(Locale.US, "%.2f", goldInvested);
                textViewgoldprice.setText(goldInvestedAmountString);

                String goldreturnsString = String.format(Locale.US, "%.2f (%s%%)", goldReturnsValue, goldReturnsPercentage);
                textViewgoldreturns.setText(goldreturnsString);

                if (goldReturnsValue >= 0) {
                    textViewgoldreturns.setTextColor(Color.GREEN);
                } else {
                    textViewgoldreturns.setTextColor(Color.RED);
                }
            }
            else{
                Toast.makeText(this, "Failed to get gold data", Toast.LENGTH_SHORT).show();
            }



        } else {
            Toast.makeText(this, "Failed to fetch data for portfolio", Toast.LENGTH_SHORT).show();
        }
    }

}

class ButtonHandlerHome implements View.OnClickListener
{
    private Context context;
    ButtonHandlerHome(Context context)
    {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        if (id == R.id.btnEquity)
        {
            intent = new Intent(this.context, EquityActivity.class);
        }
        else if (id == R.id.btndebt)
        {
            intent = new Intent(this.context, DebtActivity.class);
        }
        else if (id == R.id.btngold)
        {
            intent = new Intent(this.context, GoldActivity.class);
        }

        context.startActivity(intent);

    }
}