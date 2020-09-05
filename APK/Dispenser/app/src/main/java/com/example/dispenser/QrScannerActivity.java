package com.example.dispenser;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.View;
import com.google.android.gms.vision.barcode.Barcode;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.List;
import java.util.Objects;

import info.androidhive.barcode.BarcodeReader;

public class QrScannerActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    private BarcodeReader barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
    private String idDispensers;
    private String login;
    private boolean user;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Objects.requireNonNull(getSupportActionBar()).hide();
        }
        setContentView(R.layout.activity_qr_scanner);

        //Reafing information about dispensers
        Bundle b= getIntent().getExtras();
        if(b!=null)
        {
            idDispensers = b.getString("idDispenser");
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

    //Adding new dispenser from button
    public void fAddDispenserButton(View v)
    {
        finish();
        Intent intent = new Intent(this,AddDispenserActivity.class);
        intent.putExtra("QrScan",0).putExtra("login",login).putExtra("idDispenser",idDispensers);
        startActivity(intent);
    }

    @Override
    public void onScanned(Barcode barcode)
    {
        barcodeReader.playBeep();
        Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Objects.requireNonNull(vibrator).vibrate(1000);
        }

        //Checking dispensers in user list
        if(idDispensers!=null)
        {
            try
            {
                JSONArray JsonArray = new JSONArray(idDispensers);
                for(int i=0;i<JsonArray.length();i++)
                {
                    int tmp = JsonArray.getJSONObject(i).getInt("idDispenser");
                    //If this dispenser is list
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
        //If is new dispenser for thi user
        else
        {
            finish();
            Intent intent = new Intent(this,AddDispenserActivity.class);
            intent.putExtra("QrScan",barcode.displayValue).putExtra("idDispenser",idDispensers).putExtra("login",login);
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
    public void onCameraPermissionDenied() { finish(); }
}