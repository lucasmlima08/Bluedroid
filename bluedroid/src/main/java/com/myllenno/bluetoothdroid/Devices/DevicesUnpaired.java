package com.myllenno.bluetoothdroid.devices;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import com.myllenno.bluetoothdroid.R;
import com.myllenno.bluetoothdroid.functions.DialogBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class DevicesUnpaired {

    public final String dialogDeviceSelected = "SELECTED DEVICE";
    public final String dialogDeviceRefused = "REFUSED DEVICES";
    public final String dialogDeviceCancel = "CANCEL DISCOVERING";

    private Handler handler;
    private AlertDialog alertDialog;
    private DialogBuilder dialogBuilder;
    private BluetoothDevice deviceSelectedUnpaired;
    private ArrayList<BluetoothDevice> listDevicesUnpaired;
    private BluetoothAdapter bluetoothAdapter;

    public DevicesUnpaired(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }

    public BluetoothDevice getDeviceSelected(){
        return deviceSelectedUnpaired;
    }

    public ArrayList<BluetoothDevice> getListDevicesUnpaired() {
        return listDevicesUnpaired;
    }

    // Chama um diálogo de dispositivos não pareados.
    public void dialogDevicesUnpaired(Activity activity){
        listDevicesUnpaired = new ArrayList<>();
        // Chama o broadcast que recebe novos dispositivos desconhecidos.
        broadcastReceiverDevices(activity);
        // Inicia a descoberta de dispositivos.
        bluetoothAdapter.startDiscovery();
        // Chama o diálogo de dispositivos.
        dialogDevicesUnpairedAux(activity);
    }

    // Apresenta um diálogo para escolha de um dispositivo para pareamento.
    private void dialogDevicesUnpairedAux(Activity activity) {
        // Cria um novo alert dialog builder.
        dialogBuilder = new DialogBuilder(activity, listDevicesUnpaired);
        if (!listDevicesUnpaired.isEmpty()) { // Existe dispositivos na lista.
            // Adiciona os eventos no novo alert builder.
            dialogBuilder.setPositiveButton(activity.getString(R.string.acceptChoiceDialogSelectDevice),
                    new DialogInterface.OnClickListener() { // Escolheu o dispositivo.
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelDiscoveringDevices(); // Cancela a descoberta de dispositivos.
                            BluetoothDevice device;
                            if (dialogBuilder.indexClick == -1) { // Pega o primeiro da lista.
                                device = listDevicesUnpaired.get(0);
                            } else {
                                device = listDevicesUnpaired.get(dialogBuilder.indexClick); // Dispositivo escolhido.
                            }
                            deviceSelectedUnpaired = device;
                            handler.publish(new LogRecord(Level.INFO, dialogDeviceSelected));
                        }
                    });
            dialogBuilder.setNegativeButton(activity.getString(R.string.rejectChoiceDialogSelectDevice),
                    new DialogInterface.OnClickListener() { // Cancelou.
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelDiscoveringDevices(); // Cancela a descoberta de dispositivos.
                            deviceSelectedUnpaired = null;
                            handler.publish(new LogRecord(Level.INFO, dialogDeviceRefused));
                        }
                    });
        } else { // Não existe dispositivos na lista.
            dialogBuilder.setMessage(activity.getString(R.string.messageChoiceDialogSelectDeviceUnpairedEmpty));
            dialogBuilder.setNegativeButton(activity.getString(R.string.buttonChoiceDialogSelectDeviceUnpairedEmpty),
                    new DialogInterface.OnClickListener() { // Cancelou.
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelDiscoveringDevices(); // Cancela a descoberta de dispositivos.
                            deviceSelectedUnpaired = null;
                            handler.publish(new LogRecord(Level.INFO, dialogDeviceCancel));
                        }
                    });
        }

        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    // Evento de procura por dispositivos não pareados.
    private void broadcastReceiverDevices(final Activity activity) {
        // Inicia a procura por dispositivos não pareados e os guarda na lista.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            // Descobrindo dispositivos para parear e incluir no array.
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    listDevicesUnpaired.add(device);
                    alertDialog.cancel();
                    dialogDevicesUnpairedAux(activity);
                }
            }
        };
        activity.registerReceiver(broadcastReceiver, filter);
    }

    // Cancela a descoberta de dispositivos.
    private void cancelDiscoveringDevices(){
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        alertDialog.cancel();
    }

    // Realiza o pareamento de um dispositivo.
    public void pairDevice(BluetoothDevice device) throws Exception {
        Method method = device.getClass().getMethod("createBond", (Class[]) null);
        method.invoke(device, (Object[]) null);
    }
}
