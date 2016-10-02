package com.myllenno.bluetoothdroid.configuration;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Support {

    /**
     * Instância do bluetooth adapter do dispositivo.
     */
    private BluetoothAdapter bluetoothAdapter;

    /**
     * Define o bluetoothAdapter do dispositivo.
     */
    public Support() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * Retorna um valor booleano que indica se o dispositivo tem suporte ao bluetooth.
     *
     * @return
     */
    public Boolean isSupported() {
        if (bluetoothAdapter == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Verifica se o bluetooth está ativado no dispositivo.
     *
     * @return
     */
    public Boolean isEnabled() {
        if (bluetoothAdapter.isEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Pede permissão para ativar o bluetooth no dispositivo.
     *
     * @param activity
     */
    public void enable(Activity activity) {
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent bluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(bluetooth, 1);
        }
    }

    /**
     * Pede permissão para tornar o dispositivo visível por 300 segundos.
     *
     * @param activity
     */
    public void enableVisibility(Activity activity) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        activity.startActivity(discoverableIntent);
    }
}
