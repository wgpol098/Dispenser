package com.example.dispenser;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class PresentationActivity extends AppCompatActivity
{

    Context elo = this;
    Handler handler;
    Runnable task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Objects.requireNonNull(getSupportActionBar()).hide();
        }
        setContentView(R.layout.activity_presentation);

        //New thread to refreshing data
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

    //Sending POST to change LED status
    public void fSendButton(View v)
    {
        JSONObject json = new JSONObject();

        //Download data
        EditText tmp = findViewById(R.id.NumberEditText);
        try
        {
            json.put("numberWindow", Integer.valueOf(tmp.getText().toString())).put("windowFlag", 1);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        Connections connection = new Connections(this,"http://panda.fizyka.umk.pl:9092/api/Disp/PresentationPost","POST",json,false);
        connection.Connect();

        if(connection.getResponseCode()!=200)
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
    }

    private void Refresh()
    {
        //Sending request for actual LED status data
        Connections connection = new Connections(elo,"http://panda.fizyka.umk.pl:9092/api/Disp/PresentationGet","GET",new JSONObject(),true);
        connection.Connect();

        JSONArray JsonArrayAnswer;
        if(connection.getResponseCode() != 200)
        {
            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),connection.Error());
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
        else {
            JsonArrayAnswer = connection.JsonArrayAnswer();
            StringBuilder answer = new StringBuilder();
            String tmp = null;
            JSONObject json;
            for (int i = 1; i < JsonArrayAnswer.length(); i++)
            {
                try
                {
                    json = JsonArrayAnswer.getJSONObject(i);
                    //1 - LED ON
                    // 0 - LED OFF
                    //-1 LED OPEN
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