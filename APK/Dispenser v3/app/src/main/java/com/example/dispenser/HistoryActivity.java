package com.example.dispenser;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.apache.http.params.CoreProtocolPNames.USER_AGENT;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_history);

        //Tworzenie json, który jest wysyłany do serwera
        SharedPreferences sharedPref = this.getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
        int dispenserID = sharedPref.getInt("IdDispenser",-1);

        final JSONObject json = new JSONObject();
        try
        {
            json.put("idDispenser",5);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        AsyncTask.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                HttpURLConnection ASPNETConnection = null;
                try
                {
                    URL ASPNETURL = new URL("http://panda.fizyka.umk.pl:9092/api/Android/GetHistory/");
                    ASPNETConnection = (HttpURLConnection) ASPNETURL.openConnection();
                    ASPNETConnection.setRequestProperty("Content-Type","application/json");
                    ASPNETConnection.setRequestProperty("accept", "application/json");
                    ASPNETConnection.setRequestProperty("Content",json.toString());
//                    ASPNETConnection.setDoOutput(true);
//                    DataOutputStream wr = new DataOutputStream(ASPNETConnection.getOutputStream());
//                    wr.writeBytes(json.toString());
//                    wr.flush();
//                    wr.close();
                    ASPNETConnection.setRequestMethod("GET");
                    //ASPNETConnection.setRequestMethod("GET");

//                    ASPNETConnection.setDoInput(true);
//                    InputStream responseBody = ASPNETConnection.getInputStream();
//                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
//                    JsonReader jsonReader = new JsonReader(responseBodyReader);
//
//                    StringBuilder textBuilder = new StringBuilder();
//                    try (Reader reader = new BufferedReader(new InputStreamReader
//                            (responseBody, Charset.forName(StandardCharsets.UTF_8.name())))) {
//                        int c = 0;
//                        while ((c = reader.read()) != -1) {
//                            textBuilder.append((char) c);
//                        }
//                    }

////ASPNETConnection.setRequestMethod("GET");
//                    if(ASPNETConnection!=null)
//                    {
//                        //ASPNETConnection.setRequestMethod("GET");
//                        ASPNETConnection.setRequestProperty("User-Agent", USER_AGENT);
//                        ASPNETConnection.setRequestProperty("Accept", "application/json");
//                        ASPNETConnection.setRequestProperty("Content-Type","application/json");
//                        ASPNETConnection.setDoOutput(true);
//
//                        ASPNETConnection.getOutputStream().write(json.toString().getBytes());
//                        DialogFragment dialog1 = new MyDialog("Błąd",json.toString().getBytes().toString());
//                        dialog1.show(getSupportFragmentManager(), "MyDialogFragmentTag");
//                    }



                    if (ASPNETConnection.getResponseCode() == 200)
                    {
                        DialogFragment dialog = new MyDialog("Błąd",String.valueOf(ASPNETConnection.getResponseCode()));
                        dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                    }
                    else
                    {
                        //Connection not successfull
                        DialogFragment dialog = new MyDialog("Błąd",String.valueOf(ASPNETConnection.getErrorStream()));
                        dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                    }

                }
                catch (MalformedURLException e)
                {
                    //bad  URL, tell the user
                    DialogFragment dialog = new MyDialog("Błąd","Zły adres URL");
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                }
                catch (IOException e)
                {
                    //network error/ tell the user
                    DialogFragment dialog = new MyDialog("Błąd","Aplikacja nie ma dostępu do internetu!");
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                }
                finally
                {
                    if(ASPNETConnection != null) ASPNETConnection.disconnect();
                }
            }
        });

        //Odczytywanie jsona z serwera
        DialogFragment dialog = new MyDialog("Błąd","Aplikacja nie ma dostępu do internetu!");
        dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");

        //Tworzenie jsona do testowania danych
        JSONObject hour1 = new JSONObject();
        JSONObject hour2 = new JSONObject();
        JSONObject hour3 = new JSONObject();
        try
        {
            hour1.put("datetime","2020-03-02-13:45");
            hour1.put("nr_window",4);
            hour1.put("description","Eutanazol");
            hour1.put("flag",1);

            hour2.put("datetime","2022-03-04-13:45");
            hour2.put("nr_window",3);
            hour2.put("description","Apap");
            hour2.put("flag",0);

            hour3.put("datetime","2022-03-05-13:45");
            hour3.put("nr_window",3);
            hour3.put("description","Apap");
            hour3.put("flag",-1);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();

        jsonArray.put(hour1);
        jsonArray.put(hour2);
        jsonArray.put(hour3);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(linearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.drawable.bg_gradient);
        linearLayout.setPadding(20,40,20,20);

//        dialog = new MyDialog("Wysyłam IdDispensera, a odbieram GET:",jsonArray.toString());
//        dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");

        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject tmp=null;
            String datetime="";
            int nr_window=-1;
            String description="";
            int flag=-10;
            try
            {
                 tmp = jsonArray.getJSONObject(i);
                 if(tmp!=null)
                 {
                     datetime = tmp.getString("datetime");
                     nr_window = tmp.getInt("nr_window");
                     description = tmp.getString("description");
                     flag = tmp.getInt("flag");
                 }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            if(tmp!=null)
            {
                TextView tv = new TextView(this);
                tv.setText(i+1 + ": " + datetime + ", " + nr_window + ", " + description + ", " + flag);
                tv.setTextSize(20);
                tv.setPadding(0,20,0,20);
                tv.setGravity(Gravity.CENTER_HORIZONTAL);

                //Tworzenie gradientu do tła
                GradientDrawable shape = new GradientDrawable();
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setColor(Color.RED);
                shape.setCornerRadius(30);

                if(flag==1) shape.setColor(Color.GREEN);
                else if(flag==0) shape.setColor(Color.RED);
                else shape.setColor(Color.YELLOW);

                tv.setBackgroundDrawable(shape);

                linearLayout.addView(tv);
            }
        }

        this.setContentView(linearLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }
}
