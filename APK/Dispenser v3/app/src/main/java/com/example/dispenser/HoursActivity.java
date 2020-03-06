package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HoursActivity extends AppCompatActivity implements View.OnClickListener{


    JSONArray hoursArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hours);


        //Takie do testowania
        JSONObject hour1 = new JSONObject();
        JSONObject hour2 = new JSONObject();
        JSONObject hour3 = new JSONObject();
        try
        {
            hour1.put("godzina","12:45");
            hour2.put("godzina","13:45");
            hour3.put("godzina","18:45");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();

        jsonArray.put(hour1);
        jsonArray.put(hour2);
        jsonArray.put(hour3);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(linearLayout.VERTICAL);

        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject tmp=null;
            String str="";
            try
            {
                tmp = jsonArray.getJSONObject(i);
                if(tmp!=null)
                {
                    str = tmp.getString("godzina");
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }


            Button button = new Button(this);
            button.setText(str);
            button.setTextSize(20);
            button.setGravity(Gravity.CENTER);
            button.setOnClickListener(this);

            linearLayout.addView(button);

            hoursArray = jsonArray;
        }


        this.setContentView(linearLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent(this,ChangeHourActivity.class);
        //intent.putExtra("hour",);
        startActivity(intent);
    }
}
