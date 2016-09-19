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

public class Suporte {

    private BluetoothAdapter bluetoothAdapter;

    public Suporte(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    //- Verifica se o dispositivo tem suporte ao bluetooth.
    public Boolean verificarSuporteBluetooth(){
        if (bluetoothAdapter == null){
            return false;
        } else {
            return true;
        }
    }

    // Verifica se o Bluetooth está ativado.
    public Boolean verificarAtivacaoDoBluetooth(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isEnabled()){
            return true;
        } else {
            return false;
        }
    }

    //- Pede permissão para ativar o bluetoth.
    public void permissaoParaAtivarBluetooth(Activity activity){
        if (!bluetoothAdapter.isEnabled()) {
            Intent bluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(bluetooth, 1);
        }
    }

    // Pede permissão para tornar o dispositivo visível por 300 segundos.
    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    public void ativarVisibilidadeDoBluetooth(Activity activity){
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        activity.startActivity(discoverableIntent);
    }

    // Retorna a lista de dispositivos pareados.
    public ArrayList<BluetoothDevice> listaDeDispositivosPareados(){
        Set<BluetoothDevice> setDispositivos = bluetoothAdapter.getBondedDevices();
        ArrayList<BluetoothDevice> dispositivos = new ArrayList<BluetoothDevice>();
        for (BluetoothDevice device: setDispositivos){
            dispositivos.add(device);
        }
        return dispositivos;
    }

    // Inicia a procura por dispositivos não pareados e os guarda na lista.
    private ArrayList<BluetoothDevice> dispositivosDesconhecidos;
    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    public void iniciarBuscaPorDispositivosDesconhecidos(Activity activity) {
        dispositivosDesconhecidos = new ArrayList<BluetoothDevice>();
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
                dispositivosDesconhecidos.add(device);
            }
        }
    };

    // Interrompe a busca por dispositivos não pareados.
    public void interromperBuscaPorDispositivosDesconhecidos() {
        broadcastReceiver.abortBroadcast();
    }

    // Retorna a lista de dispositivos encontrados na busca por dispositivos não pareados.
    public ArrayList<BluetoothDevice> listaDeDispositivosDesconhecidos() {
        return dispositivosDesconhecidos;
    }
}
