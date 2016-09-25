package com.myllenno.bluetoothdroid.connection;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.util.ArrayList;

public class RequestReceived {

    private Object object;
    private BluetoothSocket device;

    public RequestReceived(Object object, BluetoothSocket device){
        this.object = object;
        this.device = device;
    }
    // Requisição enviada.
    public Object getObject(){
        return object;
    }
    // Dispositivo que enviou a requisição.
    public BluetoothSocket getListDevices(){
        return device;
    }
}
