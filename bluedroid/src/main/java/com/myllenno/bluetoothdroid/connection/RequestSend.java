package com.myllenno.bluetoothdroid.connection;

import android.bluetooth.BluetoothSocket;

import java.util.ArrayList;

public class RequestSend {

    private Object object;
    private ArrayList<BluetoothSocket> listDevices;

    public RequestSend(Object object, ArrayList<BluetoothSocket> listDevices){
        this.object = object;
        this.listDevices = listDevices;
    }
    // Requisição enviada.
    public Object getObject(){
        return object;
    }
    // Dispositivos que receberão a requisição.
    public ArrayList<BluetoothSocket> getListDevices(){
        return listDevices;
    }
}
