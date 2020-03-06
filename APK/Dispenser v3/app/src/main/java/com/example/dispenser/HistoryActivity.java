package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Takie do testowania
        JSONObject hour1 = new JSONObject();
        JSONObject hour2 = new JSONObject();
        try
        {
            hour1.put("data","2020-03-02-13:45");
            hour1.put("wzięta",true);
            hour2.put("data","2022-03-02-13:45");
            hour2.put("wzięta",false);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();

        jsonArray.put(hour1);
        jsonArray.put(hour2);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(linearLayout.VERTICAL);

        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject tmp=null;
            String str="";
            boolean bool=false;
            try
            {
                 tmp = jsonArray.getJSONObject(i);
                 if(tmp!=null)
                 {
                     bool = (boolean) tmp.get("wzięta");
                     str = (String) tmp.get("data");
                 }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            if(tmp!=null)
            {
                //String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                TextView tv = new TextView(this);
                tv.setText(str + " wzięta:" + bool);
                tv.setTextSize(20);
                tv.setGravity(Gravity.CENTER_HORIZONTAL);

                if(bool==false) tv.setBackgroundColor(Color.RED);

                linearLayout.addView(tv);
            }
        }


        this.setContentView(linearLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }
}
