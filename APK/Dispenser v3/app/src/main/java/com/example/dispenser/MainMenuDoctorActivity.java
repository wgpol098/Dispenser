package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainMenuDoctorActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_menu_doctor);

        //Pobieranie przekazanych danych
        int idDispenser;
        Bundle b = getIntent().getExtras();
        if(b!=null) idDispenser = b.getInt("idDispenser");
    }
}
