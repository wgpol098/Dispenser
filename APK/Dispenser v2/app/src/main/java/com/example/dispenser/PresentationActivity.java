package com.example.dispenser;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PresentationActivity extends AppCompatActivity
{

    Context elo = this;
    Handler handler;
    Runnable task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_presentation);

        //Tworzenie wątku do odświeżania danych
        handler = new Handler();
        task = new Runnable()
        {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                Refresh();
            }
        };
        task.run();
    }

    //Wysyłanie POST, który zmienia w tabeli flagi w oknach
    //Ja tylko wysyłam numer kontroki i ona zmienia status na zapalony
    //Nic więcej nie robię
    public void fSendButton(View v)
    {
        //Tworzenie jsona
        JSONObject json = new JSONObject();

        //Pobranie danych
        EditText tmp = findViewById(R.id.NumberEditText);
        try
        {
            json.put("numberWindow", Integer.valueOf(tmp.getText().toString()));
            json.put("windowFlag", 1);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Wysyłanie zapytania na serwer
        Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Disp/PresentationPost","POST",json,false);
        connection.Connect();

        //Jeśli jest błąd to wyświetl
        if(connection.getResponseCode()!=200)
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
    }

    private void Refresh()
    {
        //Trzeba pobrać aktualnie przechowywane w bazie danych dane
        //Wysyłanie zapytania o aktualne dane
        Connections connection = new Connections(elo,"http://panda.fizyka.umk.pl:9092/api/Disp/PresentationGet","GET",new JSONObject(),true);
        connection.Connect();

        //Czytanie odpowiedzi od serwera
        JSONArray JsonArrayAnswer;

        if(connection.getResponseCode() != 200)
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
        //Tutaj ładne wyświetlanie tych danych w TextView
        else {
            JsonArrayAnswer = connection.JsonArrayAnswer();
            StringBuilder answer = new StringBuilder();
            String tmp = null;
            JSONObject json;
            for (int i = 0; i < JsonArrayAnswer.length(); i++)
            {
                try
                {
                    json = JsonArrayAnswer.getJSONObject(i);
                    //Informacja jakie jest to okno
                    //1 - ma się zapalić
                    // 0 - nie pali się
                    //-1 ktoś otworzył pudełko i nie pali się
                    if (json.getInt("flag") == 0) tmp = "OFF";
                    else if (json.getInt("flag") == 1) tmp = "ON";
                    else if (json.getInt("flag") == -1) tmp = "OPEN";
                    answer.append("LED ").append(json.getInt("id")).append(" ").append(tmp).append("\n");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            TextView info = findViewById(R.id.InformationTextView);
            info.setText(answer.toString());
        }
    }
}