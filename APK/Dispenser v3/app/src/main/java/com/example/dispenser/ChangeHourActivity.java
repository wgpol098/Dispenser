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

    int IdDrug;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_hour);

        Bundle b = getIntent().getExtras();
        IdDrug = b.getInt("IdDrug");
        int hour=0;
        int minutes=0;
        String description="";
        int count=0;
        int periodicity=0;

        JSONObject json = new JSONObject();
        if(IdDrug > 0) {

            //Odczytywanie danych z zapytania
            json = new JSONObject();
            try
            {
                json.put("hour", 12);
                json.put("minutes", 33);
                json.put("description", "Eutanazol");
                json.put("count", 3);
                json.put("periodicity", 1);

                DialogFragment dialog = new MyDialog("Wysyłam IdRekordu odbieram GET:", json.toString());
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        if(IdDrug!=-1)
        {
            try
            {
                hour = json.getInt("hour");
                minutes = json.getInt("minutes");
                description = json.getString("description");
                count = json.getInt("count");
                periodicity = json.getInt("periodicity");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Calendar rightNow = Calendar.getInstance();
            hour = rightNow.get(Calendar.HOUR_OF_DAY);
            minutes = rightNow.get(Calendar.MINUTE);
        }

        EditText h = findViewById(R.id.HoursTextBox);
        EditText m = findViewById(R.id.MinutesTextBox);
        EditText c = findViewById(R.id.CountTextBox);
        EditText p = findViewById(R.id.PeriodicityTextBox);
        EditText d = findViewById(R.id.DescriptionTextBox);
        h.setText(Integer.toString(hour));
        m.setText(Integer.toString(minutes));
        c.setText(Integer.toString(count));
        p.setText(Integer.toString(periodicity));
        d.setText(description);

    }

    public void fOkButton(View v)
    {
        EditText h = findViewById(R.id.HoursTextBox);
        EditText m = findViewById(R.id.MinutesTextBox);
        EditText d = findViewById(R.id.DescriptionTextBox);
        EditText c = findViewById(R.id.CountTextBox);
        EditText p = findViewById(R.id.PeriodicityTextBox);

        //Dodaj sprawdzanie przy add
        int tmphour = Integer.parseInt(h.getText().toString());
        int tmpminutes = Integer.parseInt(m.getText().toString());
        String tmpdescription = d.getText().toString();
        int tmpcount = Integer.parseInt(c.getText().toString());
        int tmpperiodicity = Integer.parseInt(p.getText().toString());


        if(tmphour>23 || tmphour<0 || tmpminutes>59 || tmpminutes < 0)
        {
            DialogFragment dialog = new MyDialog("Bład","Co Ty mnie tu za godzinę podajesz");
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
        else
        {
            //Updatowanie danych
            //Wywala jak nic nie wpiszesz
            if(IdDrug!=-1)
            {
                JSONObject json = new JSONObject();
                try
                {
                    json.put("IdDrug",IdDrug);
                    json.put("hour",tmphour);
                    json.put("minutes",tmpminutes);
                    json.put("count",tmpcount);
                    json.put("periodicity",tmpperiodicity);
                    json.put("description",tmpdescription);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                DialogFragment dialog = new MyDialog("Wysyłam UPDATE",json.toString());
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }
            //Dodawanie danych
            else
            {
                JSONObject json = new JSONObject();
                try
                {
                    json.put("hour",tmphour);
                    json.put("minutes",tmpminutes);
                    json.put("count",tmpcount);
                    json.put("periodicity",tmpperiodicity);
                    json.put("description",tmpdescription);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                DialogFragment dialog = new MyDialog("Wysyłam ADD",json.toString());
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }
            //finish();
        }
    }
}
