package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    public void fHistoryButton(View v)
    {
        Intent intent = new Intent(this,HistoryActivity.class);
        startActivity(intent);
    }

    public void fHoursButton(View v)
    {
        Intent intent = new Intent(this,HoursActivity.class);
        startActivity(intent);
    }
    Button hisButton;
    Button hourButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        hisButton = (Button) findViewById(R.id.HistoryButton);
        hourButton = (Button) findViewById(R.id.HoursButton);
    }
}
