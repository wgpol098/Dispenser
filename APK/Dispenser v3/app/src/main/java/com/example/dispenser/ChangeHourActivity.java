package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class ChangeHourActivity extends AppCompatActivity {

    int hours;
    int minutes;
    boolean add;
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
            add=true;
        }
        else
        {
            Calendar rightNow = Calendar.getInstance();
            hours = rightNow.get(Calendar.HOUR_OF_DAY);
            minutes = rightNow.get(Calendar.MINUTE);
            add=false;
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
            DialogFragment dialog = new MyDialog("Bład","Co Ty mnie tu za godzinę podajesz");
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
        else
        {
            //Dodawanie danych
            //Wywala jak nic nie wpiszesz
            if(add==false)
            {
                EditText d = findViewById(R.id.DescriptionTextBox);
                EditText c = findViewById(R.id.CountTextBox);
                EditText p = findViewById(R.id.PeriodicityTextBox);
                String description = d.getText().toString();
                String count = c.getText().toString();
                String periodicity = p.getText().toString();

                JSONObject json = new JSONObject();

                try
                {
                    json.put("hour",hours);
                    json.put("minutes",minutes);
                    json.put("count",Integer.parseInt(count));
                    json.put("periodicity",Integer.parseInt(periodicity));
                    json.put("description",description);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                DialogFragment dialog = new MyDialog("Bład","Dodajesz dane: " + json);
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }
            //Updatowanie danych
            else
            {

            }
            hours=tmphours;
            minutes=tmpminutes;
            //finish();
        }
    }
}
