package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.widget.Toast;
import com.google.android.gms.vision.barcode.Barcode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import info.androidhive.barcode.BarcodeReader;

public class QrScannerActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    private BarcodeReader barcodeReader;
    private String idDispenser;
    private String login;
    private boolean user;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_qr_scanner);

        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);

        //Odczytywanie informacji o dispenserze
        Bundle b= getIntent().getExtras();
        if(b!=null)
        {
            idDispenser = b.getString("idDispenser");
            login = b.getString("login");
            user = b.getBoolean("user");
        }

        Intent intent = new Intent(this,AddDispenserActivity.class);
        intent.putExtra("QrScan","12345");
        intent.putExtra("login",login);
        intent.putExtra("idDispenser",idDispenser);
        intent.putExtra("user",true);
        startActivity(intent);
    }

    @Override
    public void onScanned(Barcode barcode)
    {
        barcodeReader.playBeep();
        Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

        //Sprawdzanie czy zapamiętany użytkownik posiada już ten dispenser.
        if(idDispenser!=null)
        {
            try
            {
                JSONArray JsonArray = new JSONArray(idDispenser);
                for(int i=0;i<JsonArray.length();i++)
                {
                    JSONObject json = JsonArray.getJSONObject(i);
                    int tmp = json.getInt("idDispenser");
                    if(tmp == Integer.parseInt(barcode.displayValue))
                    {
                        //jeśli jest to user
                        if(user=true)
                        {
                            Toast toast = Toast.makeText(this, R.string.dispenser_belongs, Toast.LENGTH_LONG);
                            toast.show();
                        }
                        //jeśli jest to lekarz
                        else
                        {
                            finish();
                            Intent intent = new Intent(this,MainMenuDoctorActivity.class);
                            intent.putExtra("idDispenser",tmp);
                            startActivity(intent);
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        //jeśli jest to nowy dispenser dla użytkownika
        else
        {
            //jeszcze się zastanów
            finish();

            Intent intent = new Intent(this,AddDispenserActivity.class);
            intent.putExtra("QrScan",barcode.displayValue);
            intent.putExtra("idDispenser",idDispenser);
            intent.putExtra("login",login);
            intent.putExtra("user",user);
            startActivity(intent);
        }
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes)
    {

    }
    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }
    @Override
    public void onScanError(String errorMessage) {

    }
    @Override
    public void onCameraPermissionDenied()
    {
        finish();
    }
}