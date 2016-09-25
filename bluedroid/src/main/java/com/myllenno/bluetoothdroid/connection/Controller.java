package com.myllenno.bluetoothdroid.connection;

import android.bluetooth.BluetoothSocket;

import java.util.ArrayList;

public class Controller {

    private Client client;
    private Server server;

    private ArrayList<BluetoothSocket> listDevices;
    private ArrayList<RequestSend> listRequestsSend;
    private ArrayList<RequestReceived> listRequestsReceived;

    public Controller(String uuidClient, String uuidServer, String pin){
        listDevices = new ArrayList<>();
        listRequestsSend = new ArrayList<>();
        listRequestsReceived = new ArrayList<>();

        client = new Client(uuidClient);
        server = new Server(uuidServer, pin);
    }

    // Adicionar um dispositivo a lista de dispositivos.
    public void addDevice(BluetoothSocket bluetoothSocket){
        listDevices.add(bluetoothSocket);
    }

    // Adicionar uma requisição para envio.
    public void addRequestSend(RequestSend request){
        listRequestsSend.add(request);
    }

    // Retorna a lista de todos os dispositivos conectados.
    public ArrayList<BluetoothSocket> getAllDevices(){
        return listDevices;
    }

    // Retorna a lista de todas as requisições para envio.
    public ArrayList<RequestSend> getAllRequestsSend(){
        return listRequestsSend;
    }

    // Retorna a lista de todas as requisições recebidas.
    public ArrayList<RequestReceived> getAllRequestsReceived(){
        return listRequestsReceived;
    }

    // Retorna a primeira requisição recebia e a remove da lista.
    public Object getRequestReceived(){
        Object object = null;
        if (!listRequestsReceived.isEmpty()){
            object = listRequestsReceived.get(0);
            listRequestsReceived.remove(0);
        }
        return object;
    }

    // Recebe as requisições de todos os dispositivos conectados.
    public void receiveRequestsAll() {
        for (int i = 0; i < listDevices.size(); i++) {
            // Verifica se está conectado.
            if (listDevices.get(i).isConnected()) {
                // Recebe a requisição do dispositivo conectado.
                receiveRequest(listDevices.get(i));
            } else {
                // Remove o device da lista.
                listDevices.remove(i);
                i--;
            }
        }
    }

    // Recebe apenas uma requisição do dispositivo.
    public void receiveRequest(final BluetoothSocket bluetoothSocket) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Recebe a requisição do dispositivo conectado e salva na lista.
                    Object object = server.receiveData(bluetoothSocket);
                    RequestReceived requestReceived = new RequestReceived(object, bluetoothSocket);
                    listRequestsReceived.add(requestReceived);
                } catch (Exception e) {}
            }
        });
        thread.start();
    }

    // Envia as requisições para todos os dispositivos da lista.
    public void sendRequestsAll() {
        while (!listRequestsSend.isEmpty()){
            sendRequest(listRequestsSend.get(0));
            listRequestsSend.remove(0);
        }
    }

    // Envia apenas uma requisição para o dispositivo.
    public void sendRequest(final RequestSend request) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i < request.getListDevices().size(); i++){
                    try {
                        // Verifica se está conectado
                        if (request.getListDevices().get(i).isConnected()){
                            // Envia a requisição para o dispositivo da lista.
                            client.sendData(request.getListDevices().get(i), request.getObject());
                        } else {
                            // Não implementado: Guarda no banco de dados para enviar depois.
                        }
                    } catch (Exception e) {}
                }
            }
        });
        thread.start();
    }
}
