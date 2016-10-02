package com.myllenno.bluetoothdroid.devices;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.logging.Handler;

public interface DevicesUnpaired_Document {

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
     * Retorna a lista de dispositivos não pareados.
     *
     * @return
     */
    ArrayList<BluetoothDevice> getListDevicesUnpaired();

    /**
     * Chama um diálogo para a escolha de um dispositivo não pareado.
     *
     * @param activity
     */
    void dialogDevicesUnpaired(Activity activity);

    /**
     * Realiza o pareamento de um dispositivo.
     *
     * @param device
     * @throws Exception
     */
    void pairDevice(BluetoothDevice device);
}
