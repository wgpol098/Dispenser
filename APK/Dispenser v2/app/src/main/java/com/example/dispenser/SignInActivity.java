package com.example.dispenser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void fSignInButton(View v)
    {
        EditText userName = findViewById(R.id.EmailTextBox);
        EditText password = findViewById(R.id.PasswordTextBox);
        EditText dispenserID = findViewById(R.id.DispenserIDTextBox);
        DialogFragment dialog = new MyDialog("Sukces",userName.getText()+" "+password.getText()+" "+dispenserID.getText());
        dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
    }

}
