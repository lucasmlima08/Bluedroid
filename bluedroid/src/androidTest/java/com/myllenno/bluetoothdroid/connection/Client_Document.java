package com.myllenno.bluetoothdroid.connection;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.util.logging.Handler;

public interface Client_Document {

    /**
     * Adiciona a classe de eventos para receber informações de quando um evento ocorrer.
     *
     * @param handler
     */
    void setHandler(Handler handler);

    /**
     * Define o cliente a partir de uma nova instância do socket.
     *
     * @param bluetoothSocket
     */
    void setClient(BluetoothSocket bluetoothSocket);

    /**
     * Define o cliente a partir de uma nova instância do dispositivo.
     *
     * @param bluetoothDevice
     */
    void setClient(BluetoothDevice bluetoothDevice);

    /**
     * Retorna a instância do socket do cliente.
     *
     * @return
     */
    BluetoothSocket getClient();

    /**
     * Abre a conexão com o cliente da classe.
     */
    void openConnection();

    /**
     * Verifica se o cliente está disponível e conectado.
     *
     * @return
     */
    boolean isAvailable();

    /**
     * Recebe um objeto para envio ao cliente.
     * Codifica para JSON e em seguida converte para bytes.
     * Escreve os bytes no socket do cliente.
     *
     * Pode ser utilizado em um Thread, ou um método de loop enquanto estiver conectado.
     *
     * @param object
     */
    void sendData(Object object);

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
    Object receiveData(Object objectType);

    /**
     * Fecha a conexão com o cliente.
     */
    void closeConnection();
}
