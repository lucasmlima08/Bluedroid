package com.myllenno.bluedroid_project;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.myllenno.bluetoothdroid.connection.Client;
import com.myllenno.bluetoothdroid.connection.Server;
import com.myllenno.bluetoothdroid.devices.DevicesPaired;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Minichat extends Activity {

    private EditText etMessageSend, etMessageReceived;
    private TextView tvStatus, idUser, idFriend;

    private DevicesPaired devicesPaired;

    //private Client userClient;
    private Server userServer;

    private Client friend;

    private final String stringUUID = "2af2a96d-d588-4ad7-8a72-1cf83464b74d";
    private final String stringPin = "1234";

    private boolean statusConverse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minichat);

        etMessageSend = (EditText) findViewById(R.id.etMessageSend);
        etMessageReceived = (EditText) findViewById(R.id.etMessageReceived);

        tvStatus = (TextView) findViewById(R.id.tvStatus);

        idUser = (TextView) findViewById(R.id.idUser);
        idFriend = (TextView) findViewById(R.id.idFriend);

        // Remove o foco no textview.
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        devicesPaired = new DevicesPaired();
        userServer = new Server(stringUUID, stringPin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (friend != null){ // Remove o usuário anterior, caso esteja conectado.
            statusReceiveMessages = false;
            statusReceiveFriend = false;
            statusConverse = false;
            friend.closeConnection();
            friend = null;
        }
        openConnectionUser(); // Abre a conexão do usuário.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        statusReceiveMessages = false;
        statusReceiveFriend = false;
        statusConverse = false;
    }

    private void openConnectionUser() {
        try {
            userServer.openConnection();
            if (userServer.getServer() != null) {
                statusConverse = true;
                receiveFriend(); // Inicia o recebimento de amigos.
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // Recebe o amigo.
    private boolean statusReceiveFriend;
    private void receiveFriend() throws Exception {
        Thread thread  = new Thread(new Runnable() {
            @Override
            public void run() {
                Client friendReceive = userServer.receiveClients(); // Aguarda um amigo.
                if (friendReceive != null) {
                    friend = friendReceive;
                    friend.openConnection();
                    friend.setClient(friendReceive.getClient());
                    refreshTextViewThread(tvStatus, "Converse: " + friendReceive.getClient().getRemoteDevice().getName());
                    statusReceiveMessages = true;
                    receiveMessages();
                }
            }
        });
        thread.start();
    }

    // Conecta a um amigo.
    private void connectFriend(BluetoothDevice device){
        try {
            if (friend == null) {
                friend = new Client(stringUUID);
                friend.openConnection(device);
                refreshTextViewThread(tvStatus, "Converse: " + device.getName());
                statusReceiveMessages = true;
                receiveMessages();
            } else {
                refreshTextViewThread(tvStatus, "There is already a friend!");
            }
        } catch (Exception e){
            e.printStackTrace();
            statusReceiveMessages = true;
            refreshTextViewThread(tvStatus, "Error in Connection!");
        }
    }

    // Recebe mensagens de um amigo.
    private boolean statusReceiveMessages;
    private void receiveMessages(){
        Thread thread  = new Thread(new Runnable() {
            @Override
            public void run() {
                while (statusReceiveMessages){
                    if (friend != null) {
                        MinichatObject object = new MinichatObject();
                        Object objectRead = friend.receiveData(object);
                        if (objectRead != null) {
                            MinichatObject minichat = (MinichatObject) objectRead;
                            Log.w("LeuMensagem", "Mensagem: " + minichat.message);
                            refreshTextViewThread(etMessageReceived, minichat.message);
                        }
                    }
                }
            }
        });
        thread.start();
    }

    // Envio de mensagens para o amigo conectado.
    public void sendMessage(View view){
        MinichatObject object = new MinichatObject();
        if (etMessageSend.getText() == null){
            object.message = "";
        } else {
            object.message = etMessageSend.getText().toString();
        }
        try {
            friend.sendData(object);
        } catch (Exception e){
            e.printStackTrace();
            tvStatus.setText("Error in Send Message!");
        }
    }

    // Evento que apresenta um diálogo de dispositivos pareados e apresenta o escolhido pelo usuário.
    public void selectDevice(View view){
        Handler handler = new Handler(){
            @Override
            public void publish(LogRecord record) {
                if (record.getMessage().equals(devicesPaired.dialogDeviceSelected)){ // Selecionou um dispositivo
                    BluetoothDevice device = devicesPaired.getDeviceSelected();
                    connectFriend(device); // Conecta ao dispositivo escolhido.
                    idFriend.setText(device.getName());
                }
            }
            @Override
            public void flush() {

            }
            @Override
            public void close() throws SecurityException {

            }
        };

        devicesPaired.setHandler(handler);
        devicesPaired.dialogDevicesPaired(this);
    }

    private void refreshTextViewThread(final TextView textView, final String string){
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(string.toString());
            }
        });
    }
}
