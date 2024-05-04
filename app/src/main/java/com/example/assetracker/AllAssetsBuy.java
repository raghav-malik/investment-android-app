package com.example.assetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assetracker.API.api;
import com.example.assetracker.misc.Misc_handler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AllAssetsBuy extends AppCompatActivity {

    String asset_type;
    String holding_type;
    String query;
    TextView text_stock_12;
    LinearLayout stocks_linearlayout_dynamic_buy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_assets_buy);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("asset_type") && intent.hasExtra("asset_type") ) {
            asset_type = intent.getStringExtra("asset_type");
            holding_type = intent.getStringExtra("holding_type");
        }

        query = "asset_type=" + asset_type + "&" + "holding_type=" + holding_type;
        text_stock_12 = (TextView) findViewById(R.id.text_stock_12);
        text_stock_12.setText("Buy " + Misc_handler.to_Title(asset_type) + " " + Misc_handler.to_Title(holding_type));
        stocks_linearlayout_dynamic_buy = (LinearLayout) findViewById(R.id.stocks_linearlayout_dynamic_buy);
        stocks_linearlayout_dynamic_buy.removeAllViews();
        fetchDataForPortfolio();
    }
    private void fetchDataForPortfolio() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);


        String username = sharedPreferences.getString("username", "admin").replace("\"", "");
        String password = sharedPreferences.getString("password", "123456789").replace("\"", "");
        JsonObject requestData = new JsonObject();
        requestData.addProperty("username", username);
        requestData.addProperty("password", password);
        JsonElement response = api.callGenericApi(requestData, "assets", "get", query, null, null);
        JsonArray responseData = response.getAsJsonArray();

        if (responseData != null)
        {
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textParams.weight = 0.3f;
            textParams.gravity = Gravity.END;

            for (JsonElement json_element: responseData) {
                JsonObject json_object = json_element.getAsJsonObject();
                LinearLayout rowLayout = new LinearLayout(this);
                rowLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);


                Button button = new Button(this);
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                buttonParams.setMargins(0, 25, 0, 0);
                button.setLayoutParams(buttonParams);
                button.setText(json_object.get("show_name").toString().replace("\"",""));
                button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


                button.setTextColor(getResources().getColor(R.color.black));
                button.setBackgroundResource(R.drawable.btnforpurposes);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AllAssetsBuy.this, StocksProcessActivity.class);
                        intent.putExtra("id", json_object.get("id").toString());
                        startActivity(intent);
                    }
                });

                rowLayout.addView(button);


                stocks_linearlayout_dynamic_buy.addView(rowLayout);
            }

        }
        else{
            Toast.makeText(this, "Response data is empty", Toast.LENGTH_SHORT).show();
        }
    }
}