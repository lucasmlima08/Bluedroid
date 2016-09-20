package com.myllenno.bluetoothdroid;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.Set;

public class Controller {

    //- Verifica se o dispositivo tem suporte ao bluetooth.
    public Boolean isSupported(){
        if (BluetoothAdapter.getDefaultAdapter() == null){
            return false;
        } else {
            return true;
        }
    }

    // Verifica se o Bluetooth está ativado.
    public Boolean isEnabled(){
        if (BluetoothAdapter.getDefaultAdapter().isEnabled()){
            return true;
        } else {
            return false;
        }
    }

    //- Pede permissão para ativar o bluetoth.
    public void permissionToActivate(Activity activity){
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent bluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(bluetooth, 1);
        }
    }

    // Pede permissão para tornar o dispositivo visível por 300 segundos.
    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    public void enableVisibility(Activity activity){
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        activity.startActivity(discoverableIntent);
    }

    // Retorna a lista de dispositivos pareados.
    public ArrayList<BluetoothDevice> listPairedDevices(){
        Set<BluetoothDevice> setDispositivos = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        ArrayList<BluetoothDevice> dispositivos = new ArrayList<BluetoothDevice>();
        for (BluetoothDevice device: setDispositivos){
            dispositivos.add(device);
        }
        return dispositivos;
    }

    // Inicia a procura por dispositivos não pareados e os guarda na lista.
    private ArrayList<BluetoothDevice> unknownDevices;
    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    public void startSearchNewDevices(Activity activity) {
        unknownDevices = new ArrayList<BluetoothDevice>();
        activity.registerReceiver(broadcastReceiver, filter);
    }

    // Evento de procura por dispositivos não pareados.
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        // Descobrindo dispositivos para parear e incluir no array.
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                unknownDevices.add(device);
            }
        }
    };

    // Interrompe a busca por dispositivos não pareados.
    public void stopSearchNewDevices() {
        broadcastReceiver.abortBroadcast();
    }

    // Retorna a lista de dispositivos encontrados na busca por dispositivos não pareados.
    public ArrayList<BluetoothDevice> listOfUnknownDevices() {
        return unknownDevices;
    }
}
