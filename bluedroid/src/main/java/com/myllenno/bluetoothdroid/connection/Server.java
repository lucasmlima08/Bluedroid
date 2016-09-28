package com.myllenno.bluetoothdroid.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import com.google.gson.Gson;
import java.io.InputStream;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Server {

    /**
     * Comunicações da classe de eventos.
     */
    public final String dialogConnectionOpened = "CONNECTION_OPENED";
    public final String dialogConnectionClosed = "CONNECTION_CLOSED";
    public final String dialogClientReceived = "CLIENT_RECEIVED";
    public final String dialogDataReceived = "DATA_RECEIVED";
    public final String dialogEmptyDataReceived = "EMPTY_DATA_RECEIVED";

    /**
     * Instância responsável por controlar a conexão de socket do servidor.
     */
    private BluetoothServerSocket bluetoothServerSocket;

    /**
     * Instância responsável por controlar os eventos da classe.
     */
    private Handler handler;

    /**
     * Instância do dentificador único universal da aplicação.
     */
    private UUID uuid;

    /**
     * Instância do dentificador pin da comunicação.
     */
    private String pin;

    /**
     * Recebe o identificador único universal e o pin da comunicação.
     *
     * @param strUUID
     * @param pin
     */
    public Server(String strUUID, String pin){
        uuid = UUID.fromString(strUUID);
        this.pin = pin;
    }

    /**
     * Define o socket do novo servidor.
     *
     * @param bluetoothServerSocket
     */
    public void setServer(BluetoothServerSocket bluetoothServerSocket){
        this.bluetoothServerSocket = bluetoothServerSocket;
    }

    /**
     * Retorna a instância do socket do servidor.
     *
     * @return
     */
    public BluetoothServerSocket getServer(){
        return bluetoothServerSocket;
    }

    /**
     * Adiciona a classe de eventos para receber informações de quando um evento ocorre.
     *
     * @param handler
     */
    public void setHandler(Handler handler){
        this.handler = handler;
    }

    /**
     * Abre a conexão do servidor.
     */
    public void openConnection() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(pin, uuid);
            handlerPublish(Level.INFO, dialogConnectionOpened);
        } catch (Exception e){
            handlerPublish(Level.SEVERE, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Fecha a conexão.
     */
    public void closeConnection() {
        try {
            bluetoothServerSocket.close();
            handlerPublish(Level.INFO, dialogConnectionClosed);
        } catch (Exception e){
            handlerPublish(Level.SEVERE, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Recebe novos clientes.
     * Permite que o servidor possa receber um ou mais de um clientes e controlar sua conexão.
     *
     * @return
     */
    public Client receiveClients() {
        try {
            BluetoothSocket bluetoothSocket = bluetoothServerSocket.accept();
            Client client = new Client(uuid.toString());
            client.setClient(bluetoothSocket);
            handlerPublish(Level.INFO, dialogClientReceived);
            return client;
        } catch (Exception e){
            handlerPublish(Level.SEVERE, e.toString());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Publica o evento ocorrido.
     *
     * @param level
     * @param dialog
     */
    private void handlerPublish(Level level, String dialog){
        if (handler != null){
            handler.publish(new LogRecord(level, dialog));
        }
    }
}
