package com.example.dispenser;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;

public class AddDispenserActivity extends AppCompatActivity {

    private int idDispenser=0, ControlSum=0;
    private String idDispensers, login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Objects.requireNonNull(getSupportActionBar()).hide();
        }
        setContentView(R.layout.activity_add_dispenser);

        //Reading given data
        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            idDispenser = b.getInt("QrScan");
            idDispensers = b.getString("idDispenser");
            login = b.getString("login");
        }
        if(idDispenser != 0)
        {
            EditText EditText = findViewById(R.id.idDispenser);
            EditText.setText(String.valueOf(idDispenser));
        }

        //Generating checksum
        ValidationCodeGenerator val = new ValidationCodeGenerator(idDispenser);
        val.Generate();
        ControlSum = val.getValidationCode();

        //Making lisener, when text in control is changed
        final EditText code = findViewById(R.id.MD5TextBox);
        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length()>3)
                {
                    StringBuilder tmp= new StringBuilder();
                    for(int i=0;i<3;i++) tmp.append(s.charAt(i));
                    code.setText(tmp.toString());
                    code.setSelection(3);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        //Making lisener, when text in idDispenser control is changed
        @SuppressLint("CutPasteId") final EditText IdDispenser = findViewById(R.id.idDispenser);
        IdDispenser.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() != 0) idDispenser = Integer.parseInt(s.toString());
                else idDispenser = 0;
                ValidationCodeGenerator val = new ValidationCodeGenerator(idDispenser);
                val.Generate();
                ControlSum = val.getValidationCode();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    //Action after click ADD
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void fAddDispenserButton(View v)
    {
        EditText tv = findViewById(R.id.MD5TextBox);
        EditText DispenserName = findViewById(R.id.dispenserNameTextBox);
        boolean flag = false;

        try
        {
            JSONArray jsonArray = new JSONArray(idDispensers);
            for(int i=0;i<jsonArray.length();i++)
            {
                if(jsonArray.getJSONObject(i).getInt("idDispenser") == idDispenser)
                {
                    flag = true;
                    break;
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Checking user given checksum
        if(!tv.getText().toString().isEmpty() && ControlSum == Integer.parseInt(tv.getText().toString()) && !flag)
        {
            JSONObject json = new JSONObject();
            try
            {
                json.put("login", login).put("idDispenser", idDispenser);
                if(!DispenserName.getText().toString().isEmpty()) json.put("name",DispenserName.getText().toString());
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            //Adding dispenser to user
            Connections connection = new Connections(this, "http://panda.fizyka.umk.pl:9092/api/Dispenser", "POST", json, false);
            connection.Connect();

            if (connection.getResponseCode() != 200)
            {
                DialogFragment dialog = new MyDialog(getResources().getString(R.string.error), connection.Error());
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }
            else
            {
                //Adding new dispenser to user dispenser list in app
                JSONArray JsonArray = null;
                json = new JSONObject();
                try
                {
                    JsonArray = new JSONArray(idDispensers);
                    json.put("idDispenser", idDispenser);
                    if(!DispenserName.getText().toString().isEmpty()) json.put("name",DispenserName.getText().toString());
                    else json.put("name",String.valueOf(idDispenser));
                    JsonArray.put(json);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                Intent intent = new Intent(this,DispenserMenuDoctorActivity.class);
                intent.putExtra("IdDispensers", JsonArray != null ? JsonArray.toString() : null).putExtra("login",login);
                
                //Finish after activities
                finishAffinity();

                //Creating new activity and show toast
                startActivity(intent);
                Toast toast = Toast.makeText(this, R.string.add_dispenser, Toast.LENGTH_LONG);
                toast.show();
            }
        }
        else
        {
            if(!flag)
            {
                DialogFragment dialog = new MyDialog(getResources().getString(R.string.error), getString(R.string.wrong_validation_code));
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }
            else
            {
                DialogFragment dialog = new MyDialog(getResources().getString(R.string.error), getString(R.string.dispenser_belongs));
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }
        }
    }
}