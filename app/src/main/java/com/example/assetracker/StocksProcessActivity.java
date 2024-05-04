package com.example.assetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assetracker.API.api;
import com.example.assetracker.misc.Misc_handler;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Locale;


public class StocksProcessActivity extends AppCompatActivity  {

    String id = null;
    TextView stockbtnactivities, quantitytextview, salespricetextview, textViewinvestedquity_3, equityassetstext, stocknametext;
    Button buy, sell, buttonpercentagereturns_1, buttoncurrentvalue_3, buttonreturnvaluequity;
    EditText operation_quantity; // Is temporary
    ButtonHandlerStocksProcess btnhandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks_process);

        stockbtnactivities = (TextView) findViewById(R.id.stockbtnactivities);
        quantitytextview = (TextView) findViewById(R.id.quantitytextview);
        salespricetextview = (TextView) findViewById(R.id.salespricetextview);
        textViewinvestedquity_3 = (TextView) findViewById(R.id.textViewinvestedquity_3);
        buttonpercentagereturns_1 = (Button) findViewById(R.id.buttonpercentagereturns_1);
        buttoncurrentvalue_3 = (Button) findViewById(R.id.buttoncurrentvalue_3);
        buttonreturnvaluequity = (Button) findViewById(R.id.buttonreturnvaluequity);
        equityassetstext = (TextView) findViewById(R.id.equityassetstext);
        stocknametext = (TextView) findViewById(R.id.stocknametext);


        buy = (Button) findViewById(R.id.buystocksbtn);
        sell = (Button) findViewById(R.id.sellstocksbtn);
        operation_quantity = (EditText) findViewById(R.id.quantity_operation);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            id = intent.getStringExtra("id");
        }
        fetchDataForPortfolio();
    }
    private void fetchDataForPortfolio() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);


        String username = sharedPreferences.getString("username", "admin").replace("\"", "");
        String password = sharedPreferences.getString("password", "123456789").replace("\"", "");
        JsonObject requestData = new JsonObject();
        requestData.addProperty("username", username);
        requestData.addProperty("password", password);
        JsonElement response = api.callGenericApi(requestData, "asset", "get", null, id, null);
        JsonObject responseData = response.getAsJsonObject();

        if (responseData != null)
        {
            stockbtnactivities.setText(responseData.get("show_name").toString().replace("\"", ""));
            Double current_price = Double.parseDouble(responseData.get("current_price").toString().replace("\"", ""));
            String string_current_price = String.format(Locale.US, "%.2f", current_price);
            salespricetextview.setText(string_current_price);
            equityassetstext.setText(Misc_handler.to_Title(responseData.get("holding_type").toString().replace("\"", "")) + " Information");
            stocknametext.setText("Current " + Misc_handler.to_Title(responseData.get("holding_type").toString().replace("\"", "")) + " Name");

            JsonObject user_information = responseData.get("user_information").getAsJsonObject();

            textViewinvestedquity_3.setText(user_information.get("invested").toString());
            buttonpercentagereturns_1.setText(user_information.get("returns_percentage").toString());
            buttoncurrentvalue_3.setText(user_information.get("current_value").toString());
            buttonreturnvaluequity.setText(user_information.get("returns_value").toString());
            quantitytextview.setText(user_information.get("quantity").toString());

            btnhandler = new ButtonHandlerStocksProcess(this, buy, sell, operation_quantity, id, requestData, this);

            buy.setOnClickListener(btnhandler);
            sell.setOnClickListener(btnhandler);
        }
        else{
            Toast.makeText(this, "Response data is empty", Toast.LENGTH_SHORT).show();
        }
    }

}

class ButtonHandlerStocksProcess implements View.OnClickListener
{

    Context context;
    Button buy, sell;
    EditText quantity_operation;
    String id;
    JsonObject requestData;
    Intent intent;
    AppCompatActivity app_combat;
    ButtonHandlerStocksProcess(Context context, Button buy, Button sell)
    {
        this.context = context;
        this.buy = buy;
        this.sell = sell;
    }
    ButtonHandlerStocksProcess(Context context, Button buy,
                               Button sell, EditText quantity_operation,
                               String id, JsonObject requestData, AppCompatActivity app_combat)
    {
        this.context = context;
        this.buy = buy;
        this.sell = sell;
        this.quantity_operation = quantity_operation;
        this.id = id;
        this.requestData = requestData;
        this.app_combat = app_combat;

    }
    @Override
    public void onClick(View v) {
        String string_quantity = quantity_operation.getText().toString();

        try {
            int int_quantity = Integer.parseInt(string_quantity);
            if (int_quantity <= 0)
            {
                throw new ArithmeticException();
            }
            JsonObject json_obj = new JsonObject();
            json_obj.addProperty("asset", id);
            json_obj.addProperty("quantity", int_quantity);
            JsonElement response = null;
            if (v.getId() == buy.getId()) {

                Toast.makeText(context, "Buy Action Registered", Toast.LENGTH_SHORT).show();
                json_obj.addProperty("action", "buy");
                response = api.callGenericApi(requestData,
                        "asset_action",
                        "post",
                        null,
                        null,
                        json_obj.toString());

            } else if (v.getId() == sell.getId()) {
                Toast.makeText(context, "Sell Action Registered", Toast.LENGTH_SHORT).show();
                json_obj.addProperty("action", "sell");
                response = api.callGenericApi(requestData,
                        "asset_action",
                        "post",
                        null,
                        null,
                        json_obj.toString());
            }

            if (response != null) {
                JsonObject final_responseData = response.getAsJsonObject();
                app_combat.recreate();

            }
            else {
                Toast.makeText(context, "Quantity not available right now.", Toast.LENGTH_SHORT).show();
            }

        }
        catch (ArithmeticException e)
        {
            Toast.makeText(context, "Quantity must be bigger than 0", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(context, "Quantity must be integer", Toast.LENGTH_SHORT).show();
        }
    }

}