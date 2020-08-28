package com.example.dispenser;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
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

public class CalendarActivity extends AppCompatActivity
{
    private CalendarView calendarView;
    int idDispenser;
    Context context = this;
    //Aktualnie zaznaczona data
    String ActualDate = "";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_calendar);
        Bundle b = getIntent().getExtras();
        if(b != null) idDispenser = b.getInt("idDispenser");

        calendarView = findViewById(R.id.calendarView);
        JSONObject json = new JSONObject();
        try
        {
            json.put("idDispenser",idDispenser);
            json.put("month",Calendar.getInstance().get(Calendar.MONTH)+1);
            json.put("year",Calendar.getInstance().get(Calendar.YEAR));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Tworzenie połączenia z serwerem
        Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Android/GetCallendarInfo","POST",json,true);
        connection.Connect();

        if(connection.getResponseCode()!=200)
        {
            MyDialog dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
        else
        {
            JSONArray jsonArray = connection.JsonArrayAnswer();

            //Odczytywanie danych z jsona i dodawanie kontrolek do konkretnych dat
            //DO ZROBIENIA TO JEST!!!!
            for (int i=0;i<jsonArray.length();i++)
            {

            }
        }

        //Czytanie danych z aktualnego dnia
        //MONTH jest liczone od 0 dlatego +1 trzeba
        ActualDate = Calendar.getInstance().get(Calendar.YEAR) + "-" + (Calendar.getInstance().get(Calendar.MONTH)+1) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        RefreshDayInfo(ActualDate);

        //To co się dzieje po kliknięciu w kalendarz
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

    private void RefreshDayInfo(String Date)
    {
        //Czyszczenie danych z linearLayout
        LinearLayout linearLayout = findViewById(R.id.LinearLayout);
        linearLayout.removeAllViews();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 20, 30, 0);

        //Tworzenie buttonów
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
                intent.putExtra("idRecord",tmp.getId());
                intent.putExtra("IdDispenser",idDispenser);
                startActivity(intent);
            }
        });
        linearLayout.addView(button,layoutParams);

        //Tworzenie jsona do wysłania na serwer
        JSONObject json = new JSONObject();
        try
        {
            json.put("idDispenser",idDispenser);
            json.put("dateAndTime",Date);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Wysyłanie jsona na serwer
        Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Android/GetDayInfo","POST",json,true);
        connection.Connect();

        //Odczytywanie odpowiedzi z serwera
        if(connection.getResponseCode()!=200)
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            finish();
        }
        //jeśli jest odpowiedź
        else
        {
            //Odczytywanie odpowiedzi z serwera
            JSONArray jsonArray = connection.JsonArrayAnswer();
            JSONObject tmp = null;
            String str1 = "";
            String str2 = "";
            String str3 = "";

            int IdRecord = -2;
            for(int i=0;i<jsonArray.length();i++)
            {
                try
                {
                    tmp = jsonArray.getJSONObject(i);
                    if (tmp != null)
                    {
                        str1 = tmp.getString("hours");
                        if (tmp.getString("minutes").length() == 1) str2 = 0 + tmp.getString("minutes");
                        else str2 = tmp.getString("minutes");
                        str3 = tmp.getString("description");
                        IdRecord = Integer.parseInt(tmp.getString("idRecord"));
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                //Tworzenie kontrolki
                button = new Button(this);
                button.setText(str1 + ":" + str2 + " - " + str3);
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
                        intent.putExtra("idRecord",tmp.getId());
                        intent.putExtra("IdDispenser",idDispenser);
                        startActivity(intent);
                    }
                });

                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v)
                    {
                        //Tworzenie jsona, który muszę wysłać na serwer, żeby usunął dane
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

                        //Odświerzenie aktualnie zaznaczonej daty
                        RefreshDayInfo(ActualDate);
                        return true;
                    }
                });
                linearLayout.addView(button,layoutParams);
            }
        }
    }
}
