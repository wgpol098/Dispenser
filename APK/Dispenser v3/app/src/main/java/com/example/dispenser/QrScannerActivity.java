package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class QrScannerActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    BarcodeReader barcodeReader;
    String idDispenser;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_qr_scanner);

        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);

        //Odczytywanie informacji o dispenserze
        Bundle b= getIntent().getExtras();
        idDispenser = b.getString("idDispenser");

//        Intent intent = new Intent(this,AddDispenserActivity.class);
//        intent.putExtra("QrScan","12345");
//        intent.putExtra("idDispenser",idDispenser);
//        startActivity(intent);
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
            JSONArray JsonArray = null;
            try
            {
                JsonArray = new JSONArray(idDispenser);
                for(int i=0;i<JsonArray.length();i++)
                {
                    JSONObject json = JsonArray.getJSONObject(i);
                    int tmp = json.getInt("idDispenser");
                    if(tmp == Integer.parseInt(barcode.displayValue)) finish();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
//            DialogFragment dialog = new MyDialog(getResources().getString(R.string.error),"connection.Error()");
//            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
        else
        {
            Intent intent = new Intent(this,AddDispenserActivity.class);
            intent.putExtra("QrScan",barcode.displayValue);
            intent.putExtra("idDispenser",idDispenser);
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
