package com.example.dispenser;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;

public class ChangeHourActivity extends AppCompatActivity {

    int IdRecord = -1;
    int IdDispenser;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_change_hour);

        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            IdRecord = b.getInt("idRecord");
            IdDispenser = b.getInt("IdDispenser");
        }
        int hour=0;
        int minutes=0;

        final EditText h = findViewById(R.id.HoursTextBox);
        final EditText m = findViewById(R.id.MinutesTextBox);
        EditText c = findViewById(R.id.CountTextBox);
        EditText p = findViewById(R.id.PeriodicityTextBox);
        EditText d = findViewById(R.id.DescriptionTextBox);
        EditText de = findViewById(R.id.DaysTextBox);

        JSONObject json = new JSONObject();
        if(IdRecord > 0)
        {
            //Dane, które wysyłam na serwer
            JSONObject zap = new JSONObject();
            try
            {
                zap.put("idRecord",IdRecord);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            //Wysyłanie zapytania na serwer
            Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Android/GetPlanRecord","POST",zap,true);
            connection.Connect();

            //Jeśli błąd to nie czytam odpowiedzi od serwera
            if(connection.getResponseCode()!=200)
            {
                DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                finish();
            }
            //Jeśli wszystko poszło i serwer działa to trzeba odczytać odpowiedź
            else json = connection.JsonAnswer();
        }
        //Wyciąganie danych z Jsona jesli jest to update
        if(IdRecord>0)
        {
            try
            {
                hour = json.getInt("hour");
                minutes = json.getInt("minutes");
                c.setText(Integer.toString(json.getInt("count")));
                p.setText(Integer.toString(json.getInt("periodicity")));
                d.setText(json.getString("description"));
                de.setText(Integer.toString(json.getInt("days")));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        //Jesli jest to dodawanie danych to nie warto wyświetlać jakiś danych
        else
        {
            Calendar rightNow = Calendar.getInstance();
            hour = rightNow.get(Calendar.HOUR_OF_DAY);
            minutes = rightNow.get(Calendar.MINUTE);
        }

        //Wpisywanie danych wyciągniętych z jsona do kontrolek
        h.setText(Integer.toString(hour));
        m.setText(Integer.toString(minutes));

        h.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                int hour=0;
                if(s.length()>0) hour = Integer.parseInt(s.toString());
                if(hour>23 || s.length() > 2)
                {
                    h.setText(s.toString().substring(0,s.length()-1));
                    h.setSelection(s.length()-1);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        m.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                int minutes=0;
                if(s.length()>0) minutes = Integer.parseInt(s.toString());
                if(minutes>59 || s.length()>2)
                {
                    m.setText(s.toString().substring(0,s.length()-1));
                    m.setSelection(s.length()-1);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void fOkButton(View v)
    {
        EditText h = findViewById(R.id.HoursTextBox);
        EditText m = findViewById(R.id.MinutesTextBox);
        EditText d = findViewById(R.id.DescriptionTextBox);
        EditText c = findViewById(R.id.CountTextBox);
        EditText p = findViewById(R.id.PeriodicityTextBox);
        EditText de = findViewById(R.id.DaysTextBox);

        //Sprawdzanie czy są podane jakieś dane
        if(h.getText().toString().isEmpty() || m.getText().toString().isEmpty() || d.getText().toString().isEmpty() || c.getText().toString().isEmpty() || p.getText().toString().isEmpty())
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),"connect.Error()");
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
        else
        {
            //Tworzenie jsona do wysyłania danych
            final JSONObject json = new JSONObject();
            try
            {
                json.put("hour",Integer.parseInt(h.getText().toString()));
                json.put("minutes",Integer.parseInt(m.getText().toString()));
                json.put("count",Integer.parseInt(c.getText().toString()));
                json.put("periodicity",Integer.parseInt(p.getText().toString()));
                json.put("description",d.getText().toString());
                json.put("days",Integer.parseInt(de.getText().toString()));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            //Updatowanie danych ---- Metoda PUT
            //Wywala jak nic nie wpiszesz
            if(IdRecord>0)
            {
                //Dodawanie idRecord do Jsona
                try
                {
                    json.put("idRecord",IdRecord);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                Connections connect = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Android","PUT",json,false);
                connect.Connect();

                //Czytanie odpowiedzi połączenia
                if(connect.getResponseCode()==200)
                {
                    Toast toast = Toast.makeText(this,R.string.update_hour,Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                }
                else
                {
                    MyDialog dialog = new MyDialog(getResources().getString(R.string.error),connect.Error());
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                }
            }
            //Dodawanie danych - METODA POST
            //To już prawie elegancko zrobione jest ogarnij json i będzie giciorek
            else
            {
                //Dodawanie idDispenser do Jsona
                try
                {
                    json.put("idDispenser",IdDispenser);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                //Wysyłanie zapytania do serwera
                Connections connect = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Android","POST",json,false);
                connect.Connect();

                //Czytanie odpowiedzi połączenia
                if(connect.getResponseCode()==200)
                {
                    Toast toast = Toast.makeText(this,R.string.add_hour,Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                }
                else
                {
                    DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connect.Error());
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                }
            }
        }
    }
}
