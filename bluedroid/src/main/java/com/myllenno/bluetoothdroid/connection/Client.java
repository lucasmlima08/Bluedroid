package com.myllenno.bluetoothdroid.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class Client {

    private OutputStream outputStream;

    private UUID uuid;

    public Client(String strUUID){
        uuid = UUID.fromString(strUUID);
    }

    // Abre a conexão com o servidor.
    // Permite que o cliente possa abrir uma conexão com mais de um servidor para controlar a sua comunicação.
    public BluetoothSocket openConnection(String addressMAC) throws IOException {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(addressMAC);
        BluetoothSocket bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
        bluetoothSocket.connect();
        return bluetoothSocket;
    }

    // Fecha a conexao.
    public void closeConnection() throws IOException {
        outputStream.close();
    }

    // Envia o objeto por socket para o servidor.
    // Pode ser utilizado em um Thread ou um método de loop enquanto estiver conectado.
    public void sendData(BluetoothSocket bluetoothSocket, Object dados) throws Exception {
        outputStream = bluetoothSocket.getOutputStream();
        byte[] bytes = serializerWrite(dados);
        outputStream.write(bytes);
    }

    // Realiza a serialização dos dados.
    private byte[] serializerWrite(Object dados) throws Exception {
        OutputStream outputStream = null;
        Serializer serializer = new Persister();
        serializer.write(dados, outputStream);
        byte[] bytes = new byte[100000];
        outputStream.write(bytes);
        return bytes;
    }
}
