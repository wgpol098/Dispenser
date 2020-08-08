package com.example.dispenser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

public class CalendarActivity extends AppCompatActivity
{
    private CalendarView calendarView;
    int idDispenser;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_calendar);
        Bundle b = getIntent().getExtras();
        if(b != null) idDispenser = b.getInt("idDispenser");

        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                Intent intent = new Intent(context,HoursActivity.class);
                intent.putExtra("IdDispenser",idDispenser);
                startActivity(intent);
            }
        });
    }
}
