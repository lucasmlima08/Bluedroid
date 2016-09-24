package com.myllenno.bluetoothdroid.configuration;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

public class Support {

    private BluetoothAdapter bluetoothAdapter;
    private AlertDialog alertDialog = null;

    public Support() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    //- Verifica se o dispositivo tem suporte ao bluetooth.
    public Boolean isSupported() {
        if (bluetoothAdapter == null) {
            return false;
        } else {
            return true;
        }
    }

    // Verifica se o Bluetooth está ativado.
    public Boolean isEnabled() {
        if (bluetoothAdapter.isEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    //- Pede permissão para ativar o bluetoth.
    public void enable(Activity activity) {
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent bluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(bluetooth, 1);
        }
    }

    // Pede permissão para tornar o dispositivo visível por 300 segundos.
    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

    public void enableVisibility(Activity activity) {
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        activity.startActivity(discoverableIntent);
    }
}
