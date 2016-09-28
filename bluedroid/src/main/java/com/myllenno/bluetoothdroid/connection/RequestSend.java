package com.myllenno.bluetoothdroid.connection;

import java.util.ArrayList;

public class RequestSend {

    private Object object;
    private ArrayList<Client> listClients;

    public RequestSend(Object object, ArrayList<Client> listDevices){
        this.object = object;
        this.listClients = listDevices;
    }
    // Requisição enviada.
    public Object getObject(){
        return object;
    }
    // Dispositivos que receberão a requisição.
    public ArrayList<Client> getListDevices(){
        return listClients;
    }
}
