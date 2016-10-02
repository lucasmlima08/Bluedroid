package com.myllenno.bluetoothdroid.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public interface Server_Document {

    /**
     * Adiciona a classe de eventos para receber informações de quando um evento ocorre.
     *
     * @param handler
     */
    void setHandler(Handler handler);

    /**
     * Define o socket do novo servidor.
     *
     * @param bluetoothServerSocket
     */
    void setServer(BluetoothServerSocket bluetoothServerSocket);

    /**
     * Retorna a instância do socket do servidor.
     *
     * @return
     */
    BluetoothServerSocket getServer();

    /**
     * Abre a conexão do servidor.
     */
    void openConnection();

    /**
     * Verifica se o servidor está aberto e disponível.
     *
     * @return
     */
    boolean isAvailable();

    /**
     * Recebe novos clientes que venham a se conectar.
     *
     * Pode ser usado em um thread para permitir que o servidor possa receba
     * um ou mais de um clientes para controlar a sua conexão.
     *
     * @return
     */
    Client_Document receiveClient();

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
    Client_Document receiveClient(int timeout);

    /**
     * Fecha a conexão do servidor.
     */
    void closeConnection();
}
