package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

public class ChangeHourActivity extends AppCompatActivity {

    int hours;
    int minutes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_hour);

        Bundle b = getIntent().getExtras();
        String str = b.getString("hour");

        if(!str.equals("Add"))
        {
            String[] strings = str.split(":");
            hours = Integer.parseInt(strings[0]);
            minutes = Integer.parseInt(strings[1]);
        }
        else
        {
            Calendar rightNow = Calendar.getInstance();
            int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
            int currentMinute = rightNow.get(Calendar.MINUTE);
            //Calendar.ZONE_OFFSET

            hours=currentHour;
            minutes=currentMinute;
        }

        EditText h = findViewById(R.id.HoursTextBox);
        EditText m = findViewById(R.id.MinutesTextBox);
        h.setText(Integer.toString(hours));
        m.setText(Integer.toString(minutes));
    }

    public void fOkButton(View v)
    {
        EditText h = findViewById(R.id.HoursTextBox);
        EditText m = findViewById(R.id.MinutesTextBox);

        //Dodaj sprawdzanie przy add
        int tmphours = Integer.parseInt(h.getText().toString());
        int tmpminutes = Integer.parseInt(m.getText().toString());

        if(tmphours>23 || tmphours<0 || tmpminutes>59 || tmpminutes < 0)
        {
            DialogFragment dialog = new MyDialog("Bład","Podano błędne dane");
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
        else
        {
            hours=tmphours;
            minutes=tmpminutes;
            finish();
        }
    }
}
