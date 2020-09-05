package com.example.dispenser;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.Objects;

public class CalendarActivity extends AppCompatActivity
{
    int idDispenser;
    Context context = this;
    //Actual Date
    String ActualDate = "";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Objects.requireNonNull(getSupportActionBar()).hide();
        }
        setContentView(R.layout.activity_calendar);

        Bundle b = getIntent().getExtras();
        if(b != null) idDispenser = b.getInt("idDispenser");

        CalendarView calendarView = findViewById(R.id.calendarView);
        JSONObject json = new JSONObject();
        try
        {
            json.put("idDispenser",idDispenser).put("month",Calendar.getInstance().get(Calendar.MONTH)+1).put("year",Calendar.getInstance().get(Calendar.YEAR));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Creating connection with server
        Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Android/GetCallendarInfo","POST",json,true);
        connection.Connect();

        if(connection.getResponseCode()!=200)
        {
            MyDialog dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
//        else
//        {
//            //JSONArray jsonArray = connection.JsonArrayAnswer();
//            //Reading data from json and adding controls to specific date
//            //TO DO!!!!
//            //for (int i=0;i<jsonArray.length();i++) {}
//        }

        //Reading data from actual date
        //MONTH is 0-11, so month+1
        ActualDate = Calendar.getInstance().get(Calendar.YEAR) + "-" + (Calendar.getInstance().get(Calendar.MONTH)+1) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        RefreshDayInfo(ActualDate);

        //After click in calendar
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                //Jeśli miesiąc się zmieni to trzeba też odświeżyć dane z danego miesiąca
                //!!!!!!!!!!!!!!!!
                ////////-----------------------------------------------------------------
                ActualDate = year + "-" + (month+1) + "-" + dayOfMonth;
                RefreshDayInfo(ActualDate);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        RefreshDayInfo(ActualDate);
    }

    @SuppressLint("SetTextI18n")
    private void RefreshDayInfo(String Date)
    {
        //Cleaning data from linearLayout
        LinearLayout linearLayout = findViewById(R.id.LinearLayout);
        linearLayout.removeAllViews();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 20, 30, 0);

        //Creating buttons
        Button button = new Button(this);
        button.setText(getString(R.string.add));
        button.setId(-1);
        button.setTextSize(20);
        button.setGravity(Gravity.CENTER);
        button.setBackgroundResource(R.drawable.bg_rounded_control);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Button  tmp = (Button) v;
                Intent intent = new Intent(context,ChangeHourActivity.class);
                intent.putExtra("idRecord",tmp.getId()).putExtra("IdDispenser",idDispenser);
                startActivity(intent);
            }
        });
        linearLayout.addView(button,layoutParams);

        //Creating json to send to server
        JSONObject json = new JSONObject();
        try
        {
            json.put("idDispenser",idDispenser).put("dateAndTime",Date);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Sending json to server
        Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Android/GetDayInfo","POST",json,true);
        connection.Connect();

        //Reading answer from server
        if(connection.getResponseCode()!=200)
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            finish();
        }
        //If all done, reading answer
        else
        {
            JSONArray jsonArray = connection.JsonArrayAnswer();
            JSONObject tmp;
            StringBuilder all = null;

            int IdRecord = -2;
            for(int i=0;i<jsonArray.length();i++)
            {
                all = new StringBuilder();
                try
                {
                    tmp = jsonArray.getJSONObject(i);
                    if (tmp != null)
                    {
                        if(tmp.getString("hours").length() == 1) all.append(0).append(tmp.getString("hours")).append(":");
                        else all.append(tmp.getString("hours")).append(":");
                        if (tmp.getString("minutes").length() == 1) all.append(0).append(tmp.getString("minutes"));
                        else all.append(tmp.getString("minutes"));
                        all.append(" ").append(tmp.getString("description"));
                        IdRecord = Integer.parseInt(tmp.getString("idRecord"));
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                //Creating control
                button = new Button(this);
                button.setText(all);
                button.setId(IdRecord);
                button.setTextSize(20);
                button.setGravity(Gravity.CENTER);
                button.setBackgroundResource(R.drawable.bg_rounded_control);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Button  tmp = (Button) v;
                        Intent intent = new Intent(context,ChangeHourActivity.class);
                        intent.putExtra("idRecord",tmp.getId()).putExtra("IdDispenser",idDispenser);
                        startActivity(intent);
                    }
                });

                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v)
                    {
                        //Creating json to send to server to deleting data in database
                        Button tmp = (Button) v;
                        JSONObject json = new JSONObject();
                        try
                        {
                            json.put("idRecord",tmp.getId());
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        Connections connection = new Connections(context,"http://panda.fizyka.umk.pl:9092/api/Android","DELETE",json,false);
                        connection.Connect();

                        if(connection.getResponseCode()==200)
                        {
                            Toast toast = Toast.makeText(context,R.string.delete_hour,Toast.LENGTH_LONG);
                            toast.show();
                        }
                        else
                        {
                            MyDialog dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
                            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                        }

                        //Refreshing checked date
                        RefreshDayInfo(ActualDate);
                        return true;
                    }
                });
                linearLayout.addView(button,layoutParams);
            }
        }
    }
}