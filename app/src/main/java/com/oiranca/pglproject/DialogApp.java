package com.oiranca.pglproject;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class DialogApp extends DialogFragment {

    @Override
    public Dialog onCreateDialog (Bundle saveInstanceState){

        AlertDialog.Builder provicional = new AlertDialog.Builder(getActivity());
        provicional.setMessage("Provisional elige una opción").setPositiveButton("Admin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent adminIntent = new Intent(getActivity(),NavigationAdmin.class);
                startActivity(adminIntent);

            }
        }).setNegativeButton("Familiar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Intent famIntent = new Intent(getActivity(),ActivityFam.class);
                startActivity(famIntent);

            }
        });
        return provicional.create();

    }


}
