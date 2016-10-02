package com.myllenno.bluetoothdroid.connection;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.google.gson.Gson;
import com.myllenno.bluetoothdroid.report.HandlerDialog;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Client {

    /**
     * Classe de comunicação do evento ocorrido.
     */
    private HandlerDialog handlerDialog;

    /**
     * Instância responsável por controlar os eventos da classe.
     */
    private Handler handler;

    /**
     * Instância do dentificador único universal da aplicação.
     */
    private UUID uuid;

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
     * Recebe o identificador único universal da aplicação.
     *
     * @param strUUID
     */
    public Client(String strUUID){
        uuid = UUID.fromString(strUUID);
        handlerDialog = new HandlerDialog();
    }

    /**
     * Adiciona a classe de eventos para receber informações de quando um evento ocorrer.
     *
     * @param handler
     */
    public void setHandler(Handler handler){
        this.handler = handler;
    }

    /**
     * Define o cliente a partir de uma nova instância do socket.
     *
     * @param bluetoothSocket
     */
    public void setClient(BluetoothSocket bluetoothSocket){
        // Primeiro passo: Verifica se o cliente está conectado e fecha a conexão.
        if (isAvailable()){
            closeConnection();
        }
        // Segundo passo: define o novo cliente.
        this.bluetoothSocket = bluetoothSocket;
    }

    /**
     * Define o cliente a partir de uma nova instância do dispositivo.
     *
     * @param bluetoothDevice
     */
    public void setClient(BluetoothDevice bluetoothDevice) {
        // Primeiro passo: Verifica se o cliente está conectado e fecha a conexão.
        if (isAvailable()){
            closeConnection();
        }
        // Segundo passo: define o novo cliente.
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            handlerPublish(Level.INFO, handlerDialog.DEVICE_DEFINED);
        } catch (Exception e){
            handlerPublish(Level.SEVERE, e.toString());
            e.printStackTrace();
        }
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
     * Abre a conexão com o cliente da classe.
     */
    public void openConnection() {
        try {
            // Primeiro passo: Verifica se há um cliente definido.
            if (bluetoothSocket != null) {
                // Segundo passo: Conecta ao cliente.
                bluetoothSocket.connect();
                // Terceiro passo: Inicia as classes de leitura e escrita do socket do cliente.
                inputStream = bluetoothSocket.getInputStream();
                outputStream = bluetoothSocket.getOutputStream();
                handlerPublish(Level.INFO, handlerDialog.CONNECTION_OPENED);

            // Caso o cliente não tenha sido definido.
            } else {
                handlerPublish(Level.INFO, handlerDialog.DEVICE_NOT_AVAILABLE);
            }
        } catch (Exception e){
            handlerPublish(Level.SEVERE, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Verifica se o cliente está disponível e conectado.
     *
     * @return
     */
    public boolean isAvailable(){
        if (bluetoothSocket != null && bluetoothSocket.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * Recebe um objeto para envio ao cliente.
     * Codifica para JSON e em seguida converte para bytes.
     * Escreve os bytes no socket do cliente.
     *
     * Pode ser utilizado em um Thread, ou um método de loop enquanto estiver conectado.
     *
     * @param object
     */
    public void sendData(Object object) {
        try {
            // Caso o cliente esteja disponível.
            if (isAvailable()) {

                // Primeiro passo: Conversão do objeto para string JSON.
                Gson gson = new Gson();
                String json = gson.toJson(object);

                // Segundo passo: Conversão da string JSON para para array de bytes.
                byte[] bytes = json.getBytes();

                // Terceiro passo: Escrever o array de bytes no socket do cliente.
                outputStream.write(bytes);

                handlerPublish(Level.INFO, handlerDialog.DATA_SENT);

            // Caso o cliente não esteja disponível.
            } else {
                handlerPublish(Level.INFO, handlerDialog.DEVICE_NOT_AVAILABLE);
            }
        } catch (Exception e){
            handlerPublish(Level.SEVERE, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Recebe o tipo de objeto que o usuário deseja decodificar.
     * Faz a leitura do socket do cliente.
     * Decodifica a leitura do JSON para o tipo de objeto especificado.
     * Retorna o objeto lido.
     *
     * Pode ser utilizado em um Thread ou um método de loop enquanto estiver conectado.
     *
     * @param objectType
     * @return
     */
    public Object receiveData(Object objectType) {
        try {
            // Caso o cliente esteja disponível.
            if (isAvailable()) {

                // Primeiro passo: faz a leitura completa do socket por bytes e guarda como string.
                byte[] bytesReader = new byte[20000];
                int reader = 20000;
                StringBuilder stringBuilder = new StringBuilder();
                while (reader >= 1010) {
                    reader = inputStream.read(bytesReader, 0, bytesReader.length);
                    stringBuilder.append(new String(bytesReader, 0, reader, "UTF-8"));
                }

                // Segundo passo: Verifica se algo foi recebido.
                if (stringBuilder.length() > 0) {

                    // Terceiro passo: Converte a string de JSON para o objeto especificado.
                    Gson gson = new Gson();
                    Object object = gson.fromJson(stringBuilder.toString(), objectType.getClass());

                    handlerPublish(Level.INFO, handlerDialog.DATA_RECEIVED);
                    return object;

                // Caso não haja requisição recebida.
                } else {
                    handlerPublish(Level.INFO, handlerDialog.DATA_EMPTY);
                    return null;
                }

            // Caso o cliente não esteja disponível.
            } else {
                handlerPublish(Level.INFO, handlerDialog.DEVICE_NOT_AVAILABLE);
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

    /**
     * Fecha a conexão com o cliente.
     */
    public void closeConnection() {
        try {
            // Primeiro passo: Verifica se o cliente está conectado.
            if (isAvailable()) {
                // Segundo passo: Fecha as classes de leitura/escrita e a conexão com o cliente.
                inputStream.close();
                outputStream.close();
                bluetoothSocket.close();
                bluetoothSocket = null;
            }
            handlerPublish(Level.INFO, handlerDialog.CONNECTION_CLOSED);
        } catch (Exception e){
            handlerPublish(Level.SEVERE, e.toString());
            e.printStackTrace();
        }
    }
}
