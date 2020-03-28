package com.example.dispenser;

import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Connections {
    private String URL;
    private String Method;
    private JSONObject Json;
    private JSONObject JsonAnswer=null;
    private JSONArray JsonArrayAnswer=null;
    private Boolean answer;
    private int ResponseCode = 0;
    private Context context;

    Connections(Context context, String _URL, String _Method, JSONObject _Json, Boolean _answer)
    {
        URL = _URL;
        Method = _Method;
        Json = _Json;
        answer = _answer;
        this.context = context;
    }

    public void Connect()
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run()
            {
                HttpURLConnection ASPNETConnection = null;
                try {
                    URL ASPNETURL = new URL(URL);
                    ASPNETConnection = (HttpURLConnection) ASPNETURL.openConnection();

                    //Żądania połączenia
                    ASPNETConnection.setRequestProperty("Content-Type", "application/json");
                    ASPNETConnection.setRequestProperty("accept", "application/json");
                    ASPNETConnection.setRequestMethod(Method);
                    ASPNETConnection.setDoOutput(true);

                    //Wprowadzanie Jsona do wysłania
                    DataOutputStream wr = new DataOutputStream(ASPNETConnection.getOutputStream());
                    wr.writeBytes(Json.toString());
                    wr.flush();
                    wr.close();

                    if (answer == true)
                    {
                        InputStream responseBody = ASPNETConnection.getInputStream();

                        StringBuilder sb = new StringBuilder();

                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(responseBody));
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        try
                        {
                            JsonAnswer = new JSONObject(sb.toString());
                        }
                        catch (JSONException e)
                        {
                            //Jeśli tablica jsonów to trzeba to wychwycić
                            try
                            {
                                JsonArrayAnswer = new JSONArray(sb.toString());
                            }
                            catch (JSONException ex)
                            {
                                ex.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    ResponseCode = ASPNETConnection.getResponseCode();
                }
                catch (MalformedURLException e)
                {
                    //Zły URL
                    ResponseCode = -1;
                }
                catch (IOException e)
                {
                    //Problemy z internetem
                    ResponseCode = -2;
                }
                finally
                {
                    if (ASPNETConnection != null) ASPNETConnection.disconnect();
                }
            }
        });

        //To powoduje te dziwne rzeczy, które się tu dzieją jak naciśniesz drugi raz
        try
        {
            while (ResponseCode==0) Thread.sleep(50);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public int getResponseCode()
    {
        return ResponseCode;
    }

    public JSONObject JsonAnswer()
    {
        return JsonAnswer;
    }

    public JSONArray JsonArrayAnswer()
    {
        return JsonArrayAnswer;
    }

    public String Error()
    {
        if(ResponseCode==-1) return context.getResources().getString(R.string.server_not_responding);
        else if(ResponseCode==-2) return context.getResources().getString(R.string.no_internet);
        else return context.getResources().getString(R.string.undefinied_error);
    }
}
