package com.example.assetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assetracker.API.api;
import com.example.assetracker.misc.Misc_handler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class StocksActivity2 extends AppCompatActivity {
    LinearLayout stocks_linearlayout_dynamic;
    TextView textstocks, textyourstocks;
    Button btnbuystocks;
    String asset_type, holding_type, query;
    private boolean isResumedFromBackStack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks2);

        stocks_linearlayout_dynamic = (LinearLayout) findViewById(R.id.stocks_linearlayout_dynamic);
        stocks_linearlayout_dynamic.removeAllViews();
        btnbuystocks = (Button) findViewById(R.id.btnbuystocks);
        textstocks = (TextView) findViewById(R.id.textstocks);
        textyourstocks = (TextView) findViewById(R.id.textyourstocks);

        Intent intent = getIntent();
        if (intent != null)
        {
            asset_type = intent.getStringExtra("asset_type");
            holding_type = intent.getStringExtra("holding_type");
        }

        textstocks.setText(Misc_handler.to_Title(holding_type) + "s");
        textyourstocks.setText("Your " + Misc_handler.to_Title(asset_type) + " " + Misc_handler.to_Title(holding_type) + "s");
        btnbuystocks.setText("BUY " + Misc_handler.to_Title(holding_type.toUpperCase()) + "S");
        query = "asset_type=" + asset_type + "&" + "holding_type=" + holding_type;
        btnbuystocks.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(StocksActivity2.this, AllAssetsBuy.class);
                        intent1.putExtra("asset_type", asset_type);
                        intent1.putExtra("holding_type", holding_type);
                        startActivity(intent1);
                    }
                }
        );
        fetchDataForPortfolio();
    }




    private void fetchDataForPortfolio() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);


        String username = sharedPreferences.getString("username", "admin").replace("\"", "");
        String password = sharedPreferences.getString("password", "123456789").replace("\"", "");
        JsonObject requestData = new JsonObject();
        requestData.addProperty("username", username);
        requestData.addProperty("password", password);
        JsonElement response = api.callGenericApi(requestData, "portfolio_information", "get", query, null, null);
        JsonArray responseData = response.getAsJsonArray();

        if (responseData != null)
        {
            LinearLayout.LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            textParams.weight = 0.3f;
            textParams.gravity = Gravity.END;

            for (JsonElement json_element: responseData) {
                JsonObject json_object = json_element.getAsJsonObject();
                LinearLayout rowLayout = new LinearLayout(this);
                rowLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);


                Button button = new Button(this);
                LayoutParams buttonParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f);
                buttonParams.setMargins(0, 25, 0, 0);
                button.setLayoutParams(buttonParams);
                button.setText(json_object.get("show_name").toString().replace("\"",""));
                button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


                button.setTextColor(getResources().getColor(R.color.black));
                button.setBackgroundResource(R.drawable.btnforpurposes);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(StocksActivity2.this, StocksProcessActivity.class);
                        intent.putExtra("id", json_object.get("id").toString());
                        startActivity(intent);
                    }
                });

                rowLayout.addView(button);

                stocks_linearlayout_dynamic.addView(rowLayout);
            }

        }
        else{
            Toast.makeText(this, "Response data is empty", Toast.LENGTH_SHORT).show();
        }
    }
}