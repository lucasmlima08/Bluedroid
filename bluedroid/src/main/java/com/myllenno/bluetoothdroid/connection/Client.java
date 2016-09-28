package com.myllenno.bluetoothdroid.connection;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Client {

    /**
     * Comunicações da classe de eventos.
     */
    public final String dialogConnectionOpened = "CONNECTION_OPENED";
    public final String dialogConnectionClosed = "CONNECTION_CLOSED";
    public final String dialogDataSent = "DATA_SENT";
    public final String dialogDataReceived = "DATA_RECEIVED";
    public final String dialogEmptyDataReceived = "EMPTY_DATA_RECEIVED";

    /**
     * Instância responsável por controlar a conexão de socket do cliente.
     */
    private BluetoothSocket bluetoothSocket;

    /**
     * Fluxo de leitura do socket do client.
     */
    private InputStream inputStream;

    /**
     * Fluxo de escrita no socket do cliente.
     */
    private OutputStream outputStream;

    /**
     * Instância responsável por controlar os eventos da classe.
     */
    private Handler handler;

    /**
     * Instância do dentificador único universal da aplicação.
     */
    private UUID uuid;

    /**
     * Recebe o identificador único universal da aplicação.
     *
     * @param strUUID
     */
    public Client(String strUUID){
        uuid = UUID.fromString(strUUID);
    }

    /**
     * Define o socket do novo cliente.
     *
     * @param bluetoothSocket
     */
    public void setClient(BluetoothSocket bluetoothSocket){
        this.bluetoothSocket = bluetoothSocket;
    }

    /**
     * Retorna a instância do socket do cliente.
     *
     * @return
     */
    public BluetoothSocket getClient(){
        return bluetoothSocket;
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
     * Abre a conexão com o cliente.
     * Abre uma conexão com um dispositivo informado.
     *
     * @param bluetoothDevice
     */
    public void openConnection(BluetoothDevice bluetoothDevice) {
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            inputStream = bluetoothSocket.getInputStream();
            outputStream = bluetoothSocket.getOutputStream();
            handlerPublish(Level.INFO, dialogConnectionOpened);
        } catch (Exception e){
            handlerPublish(Level.SEVERE, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Abre a conexão com o cliente.
     * Abre uma conexão com um socket informado.
     */
    public void openConnection() {
        try {
            if (bluetoothSocket != null & !bluetoothSocket.isConnected()) {
                bluetoothSocket.connect();
            }
            inputStream = bluetoothSocket.getInputStream();
            outputStream = bluetoothSocket.getOutputStream();
            handlerPublish(Level.INFO, dialogConnectionOpened);
        } catch (Exception e){
            handlerPublish(Level.SEVERE, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Fecha a conexao.
     */
    public void closeConnection() {
        try {
            inputStream.close();
            outputStream.close();
            bluetoothSocket.close();
            handlerPublish(Level.INFO, dialogConnectionClosed);
        } catch (Exception e){
            handlerPublish(Level.SEVERE, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Envia o objeto por socket para o cliente.
     * Transforma o objeto para JSON antes de converter para bytes e enviar.
     * Pode ser utilizado em um Thread, ou um método de loop enquanto estiver conectado.
     *
     * @param objectType
     */
    public void sendData(Object objectType) {
        try {
            // Conversão para JSON.
            Gson gson = new Gson();
            String json = gson.toJson(objectType);

            // Conversão para bytes.
            byte[] bytes = json.getBytes();
            outputStream.write(bytes);

            handlerPublish(Level.INFO, dialogDataSent);

        } catch (Exception e){
            handlerPublish(Level.SEVERE, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Recebe um objeto do cliente.
     * Transforma o JSON para o tipo de objeto utilizado.
     * Pode ser utilizado em um Thread ou um método de loop enquanto estiver conectado.
     *
     * @param objectType
     * @return
     */
    public Object receiveData(Object objectType) {
        try {
            // Processamento para string.
            byte[] bytesReader = new byte[20000];
            int reader = 20000;
            StringBuilder stringBuilder = new StringBuilder();
            while (reader >= 1010) {
                reader = inputStream.read(bytesReader, 0, bytesReader.length);
                stringBuilder.append(new String(bytesReader, 0, reader, "UTF-8"));
            }

            if (stringBuilder.length() > 0) {

                // Conversão para JSON.
                Gson gson = new Gson();
                Object object = gson.fromJson(stringBuilder.toString(), objectType.getClass());

                handlerPublish(Level.INFO, dialogDataReceived);
                return object;

            } else {
                handlerPublish(Level.INFO, dialogEmptyDataReceived);
                return null;
            }
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
