package com.myllenno.bluetoothdroid.configuration;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.myllenno.bluetoothdroid.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

public class Devices {

    private Context context;
    private BluetoothAdapter bluetoothAdapter;

    private AlertDialog alertDialog;

    private int[] indexClickAlertBuilder = {0};

    private BluetoothDevice deviceSelectedPaired;
    private BluetoothDevice deviceSelectedUnpaired;

    private ArrayList<BluetoothDevice> listDevicesUnpaired;
    private ArrayList<BluetoothDevice> listDevicesPaired;

    public Devices(Context context) {
        this.context = context;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public ArrayList<BluetoothDevice> getListDevicesPaired() {
        return listDevicesPaired;
    }

    public ArrayList<BluetoothDevice> getListDevicesUnpaired() {
        return listDevicesUnpaired;
    }

    // Chama o dialog de dispositivos pareados.
    public void dialogDevicesPaired() {
        choiceDialogDevicesPaired();
    }

    // Apresenta um diálogo para escolha de um dispositivo pareado.
    private void choiceDialogDevicesPaired() {
        // Cria um novo alert dialog builder.
        AlertDialog.Builder alertDialogBuilder = configureAlertDialogBuilder(listDevicesPaired);
        // Cria lista de dispositivos pareados.
        listDevicesPaired = listPairedDevices();
        // Adiciona os eventos no novo alert builder.
        alertDialogBuilder.setPositiveButton(context.getResources().getString(R.string.acceptChoiceDialogSelectDevicePaired),
                new DialogInterface.OnClickListener() { //- Escolheu dispositivo.
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            BluetoothDevice device = listDevicesPaired.get(indexClickAlertBuilder[0]); // Dispositivo escolhido.
                            deviceSelectedPaired = device;
                        } catch (Exception e) {
                            Log.e("Error", e.toString());
                        }
                    }
                });
        alertDialogBuilder.setNegativeButton(context.getResources().getString(R.string.rejectChoiceDialogSelectDevicePaired),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            alertDialog.cancel();
                            deviceSelectedPaired = null;
                        } catch (Exception e) {
                            Log.e("Error", e.toString());
                        }
                    }
                });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Retorna a lista de dispositivos pareados.
    public ArrayList<BluetoothDevice> listPairedDevices() {
        Set<BluetoothDevice> setDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        ArrayList<BluetoothDevice> devices = new ArrayList<>();
        for (BluetoothDevice device : setDevices) {
            devices.add(device);
        }
        return devices;
    }

    // Inicia a procura por dispositivos não pareados e os guarda na lista.
    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

    // Chama o dialog de dispositivos não pareados.
    public void dialogDevicesUnpaired(Activity activity) {
        listDevicesUnpaired = new ArrayList<>();
        activity.registerReceiver(broadcastReceiver, filter);
    }

    // Evento de procura por dispositivos não pareados.
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        // Descobrindo dispositivos para parear e incluir no array.
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                listDevicesUnpaired.add(device);
                choiceDialogDevicesUnpaired();
            }
        }
    };

    // Apresenta um diálogo para escolha de um dispositivo para pareamento.
    private void choiceDialogDevicesUnpaired() {
        // Cria um novo alert dialog builder.
        AlertDialog.Builder alertDialogBuilder = configureAlertDialogBuilder(listDevicesUnpaired);
        // Adiciona os eventos no novo alert builder.
        alertDialogBuilder.setPositiveButton(context.getResources().getString(R.string.acceptChoiceDialogSelectDeviceToPairing),
                new DialogInterface.OnClickListener() { // Escolheu o dispositivo.
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            BluetoothDevice device = listDevicesUnpaired.get(indexClickAlertBuilder[0]); // Dispositivo escolhido.
                            deviceSelectedUnpaired = device;
                            if (bluetoothAdapter.isDiscovering()) { // Cancela a descoberta de dispositivos.
                                bluetoothAdapter.cancelDiscovery();
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.toString());
                        }
                    }
                });
        alertDialogBuilder.setNegativeButton(context.getResources().getString(R.string.rejectChoiceDialogSelectDeviceToPairing),
                new DialogInterface.OnClickListener() { // Cancelou.
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();
                        //- Cancela a descoberta de dispositivos.
                        if (bluetoothAdapter.isDiscovering()) {
                            bluetoothAdapter.cancelDiscovery();
                        }
                    }
                });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Realiza o pareamento de um dispositivo.
    public void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Remove o pareamento de um dispositivo.
    public void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Configura um novo alert dialog builder, apresentando uma lista de dispositivos.
    private AlertDialog.Builder configureAlertDialogBuilder(ArrayList<BluetoothDevice> devices) {
        //- Cancela o alert dialog aberto.
        if (alertDialog != null) {
            alertDialog.cancel();
        }
        //- Transforma ArrayList em CharSequence, para apresentar no dialog.
        CharSequence[] charSequence = new CharSequence[devices.size()];
        for (int i = 0; i < charSequence.length; i++) {
            charSequence[i] = devices.get(i).getName();
        }
        //- Cria um novo alert dialog builder.
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(context.getResources().getString(R.string.titleChoiceDialogSelectDeviceToPairing));
        alertDialogBuilder.setSingleChoiceItems(charSequence, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                indexClickAlertBuilder[0] = which;
            }
        });
        return alertDialogBuilder;
    }
}
