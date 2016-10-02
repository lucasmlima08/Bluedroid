package com.myllenno.bluetoothdroid.devices;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.logging.Handler;

public interface DevicesPaired_Document {

    /**
     * Adiciona a classe de eventos para receber informações de quando um evento ocorrer.
     *
     * @param handler
     */
    void setHandler(Handler handler);

    /**
     * Retorna o dispositivo selecionado.
     *
     * @return
     */
    BluetoothDevice getDeviceSelected();

    /**
     * Retorna a lista de dispositivos pareados.
     *
     * @return
     */
    ArrayList<BluetoothDevice> getListDevicesPaired();

    /**
     * Chama um diálogo para a escolha de um dispositivo pareado.
     *
     * @param activity
     */
    void dialogDevicesPaired(Activity activity);

    /**
     * Retorna a lista de dispositivos pareados.
     *
     * @return
     */
    ArrayList<BluetoothDevice> listPairedDevices();

    /**
     * Remove o pareamento de um dispositivo.
     *
     * @param device
     */
    void unpairDevice(BluetoothDevice device);
}
