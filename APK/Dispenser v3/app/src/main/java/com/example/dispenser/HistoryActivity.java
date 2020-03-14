package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
        getSupportActionBar().hide();
        setContentView(R.layout.activity_history);

        //Tworzenie json, który jest wysyłany do serwera
        SharedPreferences sharedPref = this.getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
        int dispenserID = sharedPref.getInt("IdDispenser",-1);

        JSONObject json = new JSONObject();
        try
        {
            json.put("IdDispenser",dispenserID);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Tworzenie jsona do testowania danych
        JSONObject hour1 = new JSONObject();
        JSONObject hour2 = new JSONObject();
        JSONObject hour3 = new JSONObject();
        try
        {
            hour1.put("datetime","2020-03-02-13:45");
            hour1.put("nr_window",4);
            hour1.put("description","Eutanazol");
            hour1.put("flag",1);

            hour2.put("datetime","2022-03-04-13:45");
            hour2.put("nr_window",3);
            hour2.put("description","Apap");
            hour2.put("flag",0);

            hour3.put("datetime","2022-03-05-13:45");
            hour3.put("nr_window",3);
            hour3.put("description","Apap");
            hour3.put("flag",-1);
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
        linearLayout.setBackgroundResource(R.drawable.bg_gradient);
        linearLayout.setPadding(20,40,20,20);

        DialogFragment dialog = new MyDialog("Wysyłam IdDispensera, a odbieram GET:",jsonArray.toString());
        dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");

        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject tmp=null;
            String datetime="";
            int nr_window=-1;
            String description="";
            int flag=-10;
            try
            {
                 tmp = jsonArray.getJSONObject(i);
                 if(tmp!=null)
                 {
                     datetime = tmp.getString("datetime");
                     nr_window = tmp.getInt("nr_window");
                     description = tmp.getString("description");
                     flag = tmp.getInt("flag");
                 }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            if(tmp!=null)
            {
                TextView tv = new TextView(this);
                tv.setText(i+1 + ": " + datetime + ", " + nr_window + ", " + description + ", " + flag);
                tv.setTextSize(20);
                tv.setPadding(0,20,0,20);
                tv.setGravity(Gravity.CENTER_HORIZONTAL);

                //Tworzenie gradientu do tła
                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setColor(Color.RED);
                shape.setCornerRadius(30);

                if(flag==1) shape.setColor(Color.GREEN);
                else if(flag==0) shape.setColor(Color.RED);
                else shape.setColor(Color.YELLOW);

                tv.setBackgroundDrawable(shape);

                linearLayout.addView(tv);
            }
        }

        this.setContentView(linearLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }
}
