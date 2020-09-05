package com.example.dispenser;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import static java.lang.StrictMath.abs;

public class MainMenuDoctorActivity extends AppCompatActivity implements View.OnClickListener
{
    int idDispenser=0;
    JSONArray ArrayHistory;
    JSONArray ArrayPlans;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Objects.requireNonNull(getSupportActionBar()).hide();
        }
        setContentView(R.layout.activity_main_menu_doctor);

        //Downloading data from before activity
        Bundle b = getIntent().getExtras();
        if(b!=null) idDispenser = b.getInt("idDispenser");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onResume()
    {
        super.onResume();
        //Information about data
        //0 - all ok
        //1 - empty plan or history
        //2 - empty plan and history
        byte blank=0;

        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();
        TextView tv;

        JSONObject json = new JSONObject();
        try
        {
            json.put("idDispenser",idDispenser);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Tworzenie buttona do dodawania danych
        Button button = new Button(this);
        button.setId(-1);
        button.setTextSize(20);
        button.setText(R.string.add);
        button.setGravity(Gravity.CENTER);
        button.setOnClickListener(this);
        button.setBackgroundResource(R.drawable.bg_rounded_control);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 20, 30, 0);
        linearLayout.addView(button,layoutParams);

        //GETDOCTORPLANS
        Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Android/GetDoctorPlan","POST",json,true);
        connection.Connect();

        if(connection.getResponseCode()!=200) blank++;
        else
        {
            JSONArray JsonArray = connection.JsonArrayAnswer();
            ArrayPlans = JsonArray;
            if(JsonArray.length()==0) blank++;
            else
            {
                //Plan Label
                tv = new TextView(this);
                tv.setTextSize(20);
                tv.setTextColor(Color.WHITE);
                tv.setText(R.string.plan);
                linearLayout.addView(tv,layoutParams);

                //Creating and adding line
                TableRow tablerow = new TableRow(this);
                tablerow.setBackgroundColor(Color.WHITE);
                tablerow.setMinimumHeight(4);
                linearLayout.addView(tablerow,layoutParams);
            }

            //Creating controls to showing data from server
            for(int i=0;i<JsonArray.length();i++)
            {
                //Reading drug data
                String description = "", start = "", firsthour = "";
                int periodicity = 0, idRecord = 0, daysLeft = 0;
                JSONArray didntTakeArray = new JSONArray();
                try
                {
                    JSONObject json1 = JsonArray.getJSONObject(i);
                    description = json1.getString("description");
                    start = json1.getString("start");
                    firsthour = json1.getString("firstHour");
                    periodicity = json1.getInt("periodicity");
                    didntTakeArray = json1.getJSONArray("tabDidnttake");
                    idRecord = json1.getInt("idRecord") * -1;
                    daysLeft = json1.getInt("daysLeft");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                //Creating control
                tv = new TextView(this);
                tv.setTextColor(Color.WHITE);
                tv.setId(idRecord);
                tv.setText(R.string.description);
                tv.append(": " + description + System.getProperty("line.separator"));
                tv.append(getResources().getString(R.string.start_date));
                tv.append(": ");
                String[] tmp = start.split("-");
                for(int k=0;k<3;k++) if(tmp[k].length() == 1) tmp[k] = "0" + tmp[k];
                String[] tmp1 = firsthour.split("-");
                for(int k=0;k<2;k++) if(tmp1[k].length() == 1) tmp1[k] = "0" + tmp1[k];
                tv.append(tmp[0] + "." + tmp[1] + "." + tmp[2] + " " + tmp1[0] + ":" + tmp1[1] + System.getProperty("line.separator"));

                //Drug days left
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy.MM.dd");
                Date date = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(daysLeft));
                tv.append(getResources().getString(R.string.end_date) + ": " + formatter.format(date) + System.getProperty("line.separator"));
                tv.append(getResources().getString(R.string.periodicity) + ": " + periodicity);
                tv.setOnClickListener(this);

                //Showing when they didn't take a drug
                JSONObject jtmp;
                if(didntTakeArray.length() != 0)
                {
                    tv.append(System.getProperty("line.separator"));
                    tv.append(getResources().getString(R.string.didnt_take));
                }
                for(int j=0;j<didntTakeArray.length();j++)
                {
                    try
                    {
                        jtmp = didntTakeArray.getJSONObject(j);
                        tmp = jtmp.getString("date").split("-");
                        for(int k=1;k<tmp.length;k++) if(tmp[k].length() == 1) tmp[k] = "0" + tmp[k];
                        tv.append(System.getProperty("line.separator") + tmp[0] + "." + tmp[1] + "." + tmp[2] + " " + tmp[3] + ":" + tmp[4]);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                linearLayout.addView(tv,layoutParams);
            }
        }

        //GETDOCTORHISTORY
        connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Android/GetDoctorHistory","POST",json,true);
        connection.Connect();

        if(connection.getResponseCode()!=200) blank++;
        else
        {
            JSONArray JsonArray = connection.JsonArrayAnswer();
            ArrayHistory = JsonArray;
            if(JsonArray.length()==0) blank++;
            else
            {
                //Creating history label
                tv = new TextView(this);
                tv.setTextSize(20);
                tv.setTextColor(Color.WHITE);
                tv.setText(R.string.history);
                linearLayout.addView(tv,layoutParams);

                TableRow tablerow = new TableRow(this);
                tablerow.setBackgroundColor(Color.WHITE);
                tablerow.setMinimumHeight(4);
                linearLayout.addView(tablerow,layoutParams);
            }

            //Creating controls to showing data from server
            for(int i=JsonArray.length()-1;0<=i;i--)
            {
                //Odczytywanie danych o konkretnym leku
                String description = "", start = "", end = "", firsthour = "";
                int periodicity = 0;// count = 0;
                JSONArray didntTakeArray = new JSONArray();
                try
                {
                    JSONObject json1 = JsonArray.getJSONObject(i);
                    description = json1.getString("description");
                    start = json1.getString("start");
                    end = json1.getString("end");
                    firsthour = json1.getString("firstHour");
                    periodicity = json1.getInt("periodicity");
                    //count = json1.getInt("count");
                    didntTakeArray = json1.getJSONArray("tabDidnttake");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                //Modifying data
                firsthour = firsthour.replace("-",":");
                //Creating control
                tv = new TextView(this);
                tv.setText(R.string.description);
                tv.append(": ");
                tv.append(description + System.getProperty("line.separator"));
                tv.append(getResources().getString(R.string.start_date) + ": " + start.replace('-','.') + " " + firsthour + System.getProperty("line.separator"));
                tv.append(getResources().getString(R.string.end_date) + ": " + end.replace('-','.'));
                if(periodicity != 0)
                {
                    tv.append(System.getProperty("line.separator"));
                    tv.append(getResources().getString(R.string.periodicity) + ": " + periodicity);
                }
                tv.setPadding(10,10,10,10);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10,10,10,10);
                params.gravity = Gravity.CENTER;
                params.width = 1000;
                tv.setLayoutParams(params);

                //!!!!!!!!!!!!!!
                //TRZEBA ZROBIC LICZENIE KIEDY SIE SPÓŹNIL Z LEKIEM!!!!!!!!!!!!!!!
                //!!!!!!!!!!!!!!

                //Creating gradient to textview
                GradientDrawable gd = new GradientDrawable();
                gd.setCornerRadius(45);
                if(didntTakeArray.length() == 0) gd.setColor(Color.GREEN);
                else
                {
                    gd.setColor(Color.rgb(255,50,50));
                    tv.setTextColor(Color.WHITE);
                }
                gd.setStroke(20,Color.alpha(0));
                gd.setStroke(2, Color.BLACK);
                tv.setBackground(gd);

                //Showing when user didn't take this drug
                if(didntTakeArray.length() != 0) tv.append(System.getProperty("line.separator") + System.getProperty("line.separator") + getResources().getString(R.string.didnt_take));
                for(int j=0;j<didntTakeArray.length();j++)
                {
                    try
                    {
                        JSONObject jtmp = didntTakeArray.getJSONObject(j);
                        tv.append(System.getProperty("line.separator"));
                        String[] tmp = jtmp.getString("date").split("-");
                        for(int k=1;k<5;k++) if(tmp[k].length() == 1) tmp[k] = "0" + tmp[k];
                        tv.append(tmp[0] + "." + tmp[1] + "." + tmp[2] + " " + tmp[3] + ":" + tmp[4]);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                tv.setId(i);
                linearLayout.addView(tv);
            }
        }

        //If dispenser don't have any plan and history
        if(blank==2)
        {
            tv = new TextView(this);
            tv.setText(R.string.no_history_plans);
            tv.setTextSize(20);
            tv.setTextColor(Color.WHITE);
            tv.setPadding(0,20,0,20);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            linearLayout.addView(tv);
        }
    }
    @Override
    public void onClick(View v)
    {
        //Button ID
        //-1 - adding button
        // <-1 - Plan item
        int id = v.getId();

        //If it is add button
        if(id==-1)
        {
            Intent intent = new Intent(this,ChangeHourActivity.class);
            intent.putExtra("idRecord",-1).putExtra("IdDispenser",idDispenser);
            startActivity(intent);
        }
        //if it is Plan Item
        else
        {
            id = abs(id);
            Intent intent = new Intent(this,ChangeHourActivity.class);
            intent.putExtra("idRecord",id).putExtra("IdDispenser",idDispenser);
            startActivity(intent);
        }
    }
}