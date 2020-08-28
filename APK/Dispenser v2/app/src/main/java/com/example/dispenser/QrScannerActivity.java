package com.example.dispenser;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.View;
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

//        //To jest jedynie do testowania
//        Intent intent = new Intent(this,AddDispenserActivity.class);
//        intent.putExtra("QrScan","12345");
//        intent.putExtra("login",login);
//        intent.putExtra("idDispenser",idDispenser);
//        startActivity(intent);
    }

    //Dodawanie nowego Dispensera za pomocą guzika
    public void fAddDispenserButton(View v)
    {
        finish();
        Intent intent = new Intent(this,AddDispenserActivity.class);
        intent.putExtra("QrScan",0);
        intent.putExtra("login",login);
        intent.putExtra("idDispenser",idDispenser);
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
                    //jeśli jest na liście to
                    if(tmp == Integer.parseInt(barcode.displayValue))
                    {
                        finish();
                        Intent intent;
                        if(user) intent = new Intent(this,CalendarActivity.class);
                        else intent = new Intent(this,MainMenuDoctorActivity.class);
                        intent.putExtra("idDispenser",tmp);
                        startActivity(intent);
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
            finish();
            Intent intent = new Intent(this,AddDispenserActivity.class);
            intent.putExtra("QrScan",barcode.displayValue);
            intent.putExtra("idDispenser",idDispenser);
            intent.putExtra("login",login);
            startActivity(intent);
        }
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {}
    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {}
    @Override
    public void onScanError(String errorMessage) {}
    @Override
    public void onCameraPermissionDenied()
    {
        finish();
    }
}