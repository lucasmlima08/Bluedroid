package com.myllenno.bluetoothdroid.devices;

import com.myllenno.bluetoothdroid.functions.DialogBuilder;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;

import com.myllenno.bluetoothdroid.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class DevicesPaired {

    public final String dialogDeviceSelected = "DEVICE_SELECTED";
    public final String dialogDeviceRefused = "DEVICE_REFUSED";
    public final String dialogDeviceEmpty = "EMPTY";

    private Handler handler;
    private AlertDialog alertDialog;
    private DialogBuilder dialogBuilder;
    private BluetoothDevice deviceSelectedPaired;
    private ArrayList<BluetoothDevice> listDevicesPaired;

    public void setHandler(Handler handler){
        this.handler = handler;
    }

    public BluetoothDevice getDeviceSelected(){
        return deviceSelectedPaired;
    }

    public ArrayList<BluetoothDevice> getListDevicesPaired() {
        listDevicesPaired = listPairedDevices();
        return listDevicesPaired;
    }

    // Chama um diálogo para a escolha de um dispositivo pareado.
    public void dialogDevicesPaired(Activity activity) {
        // Chama o array de dispositivos pareados.
        listDevicesPaired = listPairedDevices();
        // Cria um novo alert dialog builder.
        dialogBuilder = new DialogBuilder(activity, listDevicesPaired);
        if (!listDevicesPaired.isEmpty()) { // Existe dispositivos na lista.
            // Adiciona os eventos no novo alert builder.
            dialogBuilder.setPositiveButton(activity.getString(R.string.acceptChoiceDialogSelectDevice),
                    new DialogInterface.OnClickListener() { //- Escolheu dispositivo.
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BluetoothDevice device;
                            if (dialogBuilder.indexClick == -1) { // Pega o primeiro da lista.
                                device = listDevicesPaired.get(0);
                            } else {
                                device = listDevicesPaired.get(dialogBuilder.indexClick); // Dispositivo escolhido.
                            }
                            alertDialog.cancel();
                            deviceSelectedPaired = device;
                            handler.publish(new LogRecord(Level.INFO, dialogDeviceSelected));
                        }
                    });
            dialogBuilder.setNegativeButton(activity.getString(R.string.rejectChoiceDialogSelectDevice),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                            deviceSelectedPaired = null;
                            handler.publish(new LogRecord(Level.INFO, dialogDeviceRefused));
                        }
                    });
        } else { // Não existe dispositivos na lista.
            dialogBuilder.setMessage(activity.getString(R.string.messageChoiceDialogSelectDevicePairedEmpty));
            dialogBuilder.setNegativeButton(activity.getString(R.string.buttonChoiceDialogSelectDevicePairedEmpty),
                    new DialogInterface.OnClickListener() { // Aceitou
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                            deviceSelectedPaired = null;
                            handler.publish(new LogRecord(Level.INFO, dialogDeviceEmpty));
                        }
                    });
        }
        if (alertDialog != null && alertDialog.isShowing()){
            alertDialog.cancel();
        }
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    // Retorna a lista de dispositivos pareados.
    private ArrayList<BluetoothDevice> listPairedDevices() {
        Set<BluetoothDevice> setDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        ArrayList<BluetoothDevice> devices = new ArrayList<>();
        for (BluetoothDevice device : setDevices) {
            devices.add(device);
        }
        return devices;
    }

    // Remove o pareamento de um dispositivo.
    public void unpairDevice(BluetoothDevice device) throws Exception {
        Method method = device.getClass().getMethod("removeBond", (Class[]) null);
        method.invoke(device, (Object[]) null);
    }
}
