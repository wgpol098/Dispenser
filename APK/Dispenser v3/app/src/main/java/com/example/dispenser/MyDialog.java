package com.example.dispenser;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;


public class MyDialog extends DialogFragment
{
    String Title;
    String Message;
    public MyDialog(String Title_, String Message_)
    {
        Title=Title_;
        Message=Message_;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int id)
            {

            }
        });

        return builder.create();
    }
}
