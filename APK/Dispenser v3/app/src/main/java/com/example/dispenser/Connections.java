package com.example.dispenser;

import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connections {
    private String URL;
    private String Method;
    private JSONObject Json;
    private Boolean answer;
    private int ResponseCode = 0;
    private Context context;

    Connections(Context context, String _URL, String _Method, JSONObject _Json, Boolean _answer) {
        URL = _URL;
        Method = _Method;
        Json = _Json;
        answer = _answer;
        this.context = context;
    }

    //W tej metodzie siedzi błąd, który nie pozwala drugi raz wcisnąć guzika
    public void Connect() {
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

                    if (answer == true) {
                        ASPNETConnection.setDoInput(true);
                        //Tutaj dodaj, jeśli użytkownik oczekuje jakiejś odpowiedzi od serwera.
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
//        while (ResponseCode == 0) {
//            //XDDDDDDDDDDDDDDDDDDDDD
//            //żeby działało
//            //XDDDDDDDDDDDDDDDDDDDDD
//        }
    }

    public int getResponseCode()
    {
        return ResponseCode;
    }

    public String Error()
    {
        if(ResponseCode==-1) return context.getResources().getString(R.string.server_not_responding);
        else if(ResponseCode==-2) return context.getResources().getString(R.string.no_internet);
        else return context.getResources().getString(R.string.undefinied_error);
    }
}
