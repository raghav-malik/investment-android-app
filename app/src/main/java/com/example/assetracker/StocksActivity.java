package com.example.assetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assetracker.API.api;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Locale;

public class StocksActivity extends AppCompatActivity {

    TextView textPortfolio, investedAmt, textViewreturnsEquity, textViewEquityInvested
    , textViewdebtreturns, textViewdebtprice, textViewgoldreturns, textViewgoldprice
    , textViewrealestatereturns, textViewrealestateprice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_stocks);

        textPortfolio = findViewById(R.id.textPortfolio);
        investedAmt = findViewById(R.id.textView4);
        textViewreturnsEquity=findViewById(R.id.textViewreturnsEquity);
        textViewEquityInvested=findViewById(R.id.textViewEquityInvested);
        textViewdebtreturns=findViewById(R.id.textViewdebtreturns);
        textViewdebtprice=findViewById(R.id.textViewdebtprice);
        textViewgoldreturns=findViewById(R.id.textViewgoldreturns);
        textViewgoldprice=findViewById(R.id.textViewgoldprice);
        textViewrealestatereturns=findViewById(R.id.textViewrealestatereturns);
        textViewrealestateprice=findViewById(R.id.textViewrealestateprice);
        fetchDataForPortfolio();


        ButtonHandlerHome buttonhandlerhome  = new ButtonHandlerHome(this);
        findViewById(R.id.btnEquity).setOnClickListener(buttonhandlerhome);
        findViewById(R.id.btndebt).setOnClickListener(buttonhandlerhome);
        findViewById(R.id.btnrealestate).setOnClickListener(buttonhandlerhome);
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
            String InvestedStr = responseData.get("invested").getAsString();
            double totalInvested = Double.parseDouble(InvestedStr);
            String totalInvestedAmountString = String.format(Locale.US, "%.2f", totalInvested);
            investedAmt.setText(totalInvestedAmountString);
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

            //realEstate
            JsonObject realEstate = responseData.getAsJsonObject("real_estate");
            if(realEstate != null) {
                String realEstateInvestedStr = realEstate.get("invested").getAsString();
                double realEstateInvested = Double.parseDouble(realEstateInvestedStr);

                String realEstateReturnsValueStr = realEstate.get("returns_value").getAsString();
                double realEstateReturnsValue = Double.parseDouble(realEstateReturnsValueStr);

                String realEstateReturnsPercentageStr = realEstate.get("returns_percentage").getAsString();
                double realEstateReturnsPercentage = Double.parseDouble(realEstateReturnsPercentageStr);

                String realEstateInvestedAmountString = String.format(Locale.US, "%.2f", realEstateInvested);
                textViewrealestateprice.setText(realEstateInvestedAmountString);

                String realEstatereturnsString = String.format(Locale.US, "%.2f (%s%%)", realEstateReturnsValue, realEstateReturnsPercentage);
                textViewrealestatereturns.setText(realEstatereturnsString);

                if (realEstateReturnsValue >= 0) {
                    textViewrealestatereturns.setTextColor(Color.GREEN);
                } else {
                    textViewrealestatereturns.setTextColor(Color.RED);
                }
            }
            else{
                Toast.makeText(this, "Failed to get real estate data", Toast.LENGTH_SHORT).show();
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
        else if (id == R.id.btnrealestate)
        {
            intent = new Intent(this.context, DebtActivity.class);
        }
        else if (id == R.id.btngold)
        {
            intent = new Intent(this.context, DebtActivity.class);
        }

        context.startActivity(intent);

    }
}