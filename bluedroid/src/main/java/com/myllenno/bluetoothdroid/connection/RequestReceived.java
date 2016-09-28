package com.myllenno.bluetoothdroid.connection;

public class RequestReceived {

    private Object object;
    private Client client;

    public RequestReceived(Object object, Client device){
        this.object = object;
        this.client = device;
    }
    // Requisição enviada.
    public Object getObject(){
        return object;
    }
    // Dispositivo que enviou a requisição.
    public Client getListDevices(){
        return client;
    }
}
