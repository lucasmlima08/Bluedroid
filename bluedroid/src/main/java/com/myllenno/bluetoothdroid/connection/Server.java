package com.myllenno.bluetoothdroid.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import com.myllenno.bluetoothdroid.report.HandlerDialog;

import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Server {

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
     * Instância do dentificador pin da comunicação.
     */
    private String pin;

    /**
     * Instância responsável por controlar a conexão de socket do servidor.
     */
    private BluetoothServerSocket bluetoothServerSocket;

    /**
     * Recebe o identificador único universal e o pin da comunicação.
     *
     * @param strUUID
     * @param pin
     */
    public Server(String strUUID, String pin){
        uuid = UUID.fromString(strUUID);
        this.pin = pin;
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
     * Define o socket do novo servidor.
     *
     * @param bluetoothServerSocket
     */
    public void setServer(BluetoothServerSocket bluetoothServerSocket){
        // Primeiro passo: Verifica se o servidor já está aberto e o fecha.
        if (isAvailable()){
            closeConnection();
        }
        // Segundo passo: Define a nova instância do servidor.
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
     * Abre a conexão do servidor.
     */
    public void openConnection() {
        try {
            // Primeiro passo: Verifica se o servidor já está aberto e o fecha.
            if (isAvailable()){
                closeConnection();
            }
            // Segundo passo: Abre uma nova conexão do servidor.
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(pin, uuid);
            handlerPublish(Level.INFO, handlerDialog.CONNECTION_OPENED);
        } catch (Exception e){
            handlerPublish(Level.SEVERE, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Verifica se o servidor está aberto e disponível.
     *
     * @return
     */
    public boolean isAvailable(){
        if (bluetoothServerSocket != null) {
            return true;
        }
        return false;
    }

    /**
     * Recebe novos clientes que venham a se conectar.
     *
     * Pode ser usado em um thread para permitir que o servidor possa receba
     * um ou mais de um clientes para controlar a sua conexão.
     *
     * @return
     */
    public Client receiveClient() {
        try {
            // Primeiro passo: Verifica se o servidor está aberto.
            if (isAvailable()) {
                // Segundo passo: Inicia a espera por um novo cliente
                BluetoothSocket bluetoothSocket = bluetoothServerSocket.accept();
                // Terceiro passo: Cria a classe com o novo cliente.
                Client client = new Client(uuid.toString());
                client.setClient(bluetoothSocket);
                handlerPublish(Level.INFO, handlerDialog.CLIENT_RECEIVED);
                return client;

            // Caso o servidor não esteja disponível.
            } else {
                handlerPublish(Level.INFO, handlerDialog.CONNECTION_CLOSED);
                return null;
            }
        } catch (Exception e){
            handlerPublish(Level.SEVERE, e.toString());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Recebe novos clientes que venham a se conectar.
     * Recebe o tempo de espera que deverá aguardar o cliente.
     *
     * Pode ser usado em um thread para permitir que o servidor possa receba
     * um ou mais de um clientes para controlar a sua conexão.
     *
     * @param timeout
     * @return
     */
    public Client receiveClient(int timeout) {
        try {
            // Primeiro passo: Verifica se o servidor está aberto.
            if (isAvailable()) {
                // Segundo passo: Inicia a espera por um novo cliente
                BluetoothSocket bluetoothSocket = bluetoothServerSocket.accept(timeout);
                // Terceiro passo: Cria a classe com o novo cliente.
                Client client = new Client(uuid.toString());
                client.setClient(bluetoothSocket);
                handlerPublish(Level.INFO, handlerDialog.CLIENT_RECEIVED);
                return client;

            // Caso o servidor não esteja disponível.
            } else {
                handlerPublish(Level.INFO, handlerDialog.CONNECTION_CLOSED);
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
     * Fecha a conexão do servidor.
     */
    public void closeConnection() {
        try {
            if (isAvailable()) {
                bluetoothServerSocket.close();
            }
            handlerPublish(Level.INFO, handlerDialog.CONNECTION_CLOSED);
        } catch (Exception e){
            handlerPublish(Level.SEVERE, e.toString());
            e.printStackTrace();
        }
    }
}
