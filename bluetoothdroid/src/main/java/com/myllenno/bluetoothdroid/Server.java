package com.myllenno.bluetoothdroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class Server {

    private InputStream inputStream;

    private UUID uuid;
    private String pin;

    private BluetoothServerSocket bluetoothServerSocket;

    public Server(String strUUID, String pin){
        uuid = UUID.fromString(strUUID);
        this.pin = pin;
    }

    // Abre a conexão do servidor.
    // Retorna um valor booleano que verifica se foi aberto com sucesso.
    public void openConnection() throws IOException {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(pin, uuid);
    }

    // Fecha a conexão.
    public void closeConnection() throws IOException {
        inputStream.close();
        bluetoothServerSocket.close();
    }

    // Recebe clientes que fizerem a conexão.
    // Permite que o servidor possa receber mais de um cliente para controlar a sua conexão.
    public BluetoothSocket receiveClients(){
        try {
            BluetoothSocket bluetoothSocket = bluetoothServerSocket.accept();
            return bluetoothSocket;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Recebe os dados do cliente conectado.
    // Pode ser utilizado em um Thread ou um método de loop enquanto estiver conectado.
    public Object receiveData(BluetoothSocket bluetoothSocket) throws Exception {
        inputStream = bluetoothSocket.getInputStream();
        Object dados = serializerRead(inputStream);
        return dados;
    }

    // Realiza a leitura dos dados serializados.
    private Object serializerRead(InputStream inputStream) throws Exception {
        Serializer serializer = new Persister();
        Object dados = serializer.read(Object.class, inputStream);
        return dados;
    }
}
