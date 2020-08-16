package com.example.dispenser;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;


public class MyDialog extends DialogFragment
{
    String Title;
    String Message;
    String Type="OK";
    public MyDialog(String Title_, String Message_)
    {
        Title = Title_;
        Message = Message_;
    }

    public MyDialog(String Title_, String Message_, String Type_)
    {
        Title = Title_;
        Message = Message_;
        Type = Type_;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(Title);
        builder.setMessage(Message);
        if(Type.equals("OK"))
        {
            builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog,int id)
                {

                }
            });
        }
        if(Type.equals("YESNO"))
        {
            builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                }
            });
            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.cancel();
                }
            });
        }
        return builder.create();
    }
}
