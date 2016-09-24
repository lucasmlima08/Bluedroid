package com.myllenno.bluetoothdroid.functions;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.widget.ListAdapter;

import com.myllenno.bluetoothdroid.R;

import java.util.ArrayList;

public class DialogBuilder extends AlertDialog.Builder {

    public int indexClick;

    public DialogBuilder(Activity activity, ArrayList<BluetoothDevice> list) {
        super(activity);

        // Formata o indexador do clique.
        indexClick = -1;

        // Transforma em array de sequência de caracteres para apresentar no dialog.
        CharSequence[] charSequence = new CharSequence[list.size()];
        for (int i = 0; i < charSequence.length; i++) {
            charSequence[i] = list.get(i).getName();
        }

        // Título do diálogo.
        setTitle(activity.getApplicationContext().getResources().getString(R.string.titleChoiceDialogSelectDeviceToPairing));

        // Inclui os itens e o evento de clique do diálogo.
        setSingleChoiceItems(charSequence, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                indexClick = which;
            }
        });

        // Desativa o cancelamento do diálogo.
        setCancelable(false);
    }
}