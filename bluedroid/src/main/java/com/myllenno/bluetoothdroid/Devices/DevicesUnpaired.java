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
import com.myllenno.bluetoothdroid.report.HandlerDialog;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class DevicesUnpaired {

    /**
     * Classe de comunicação do evento ocorrido.
     */
    private HandlerDialog handlerDialog;

    /**
     * Instância responsável por controlar os eventos da classe.
     */
    private Handler handler;

    /**
     * Instância do diálogo.
     */
    private AlertDialog alertDialog;

    /**
     * Instância do diálogo.
     */
    private DialogBuilder dialogBuilder;

    /**
     * Dispositivo não-pareado selecionado.
     */
    private BluetoothDevice deviceSelectedUnpaired;

    /**
     * Lista de dispositivos não pareados.
     */
    private ArrayList<BluetoothDevice> listDevicesUnpaired;

    /**
     * Instância do adapter do bluetooth.
     */
    private BluetoothAdapter bluetoothAdapter;

    /**
     * Define o suporte do bleutooth e inicia a classe de comunicação do evento.
     */
    public DevicesUnpaired() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        handlerDialog = new HandlerDialog();
    }

    /**
     * Adiciona a classe de eventos para receber informações de quando um evento ocorrer.
     *
     * @param handler
     */
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     * Retorna o dispositivo selecionado.
     *
     * @return
     */
    public BluetoothDevice getDeviceSelected() {
        return deviceSelectedUnpaired;
    }

    /**
     * Retorna a lista de dispositivos não pareados.
     *
     * @return
     */
    public ArrayList<BluetoothDevice> getListDevicesUnpaired() {
        return listDevicesUnpaired;
    }

    /**
     * Chama um diálogo para a escolha de um dispositivo não pareado.
     *
     * @param activity
     */
    public void dialogDevicesUnpaired(Activity activity) {
        // Primeiro passo: Inicia o array que receberá os dispositivos pareados.
        listDevicesUnpaired = new ArrayList<>();
        // Segundo passo: Chama o broadcast que recebe novos dispositivos desconhecidos.
        broadcastReceiverDevices(activity);
        // Terceiro passo: Inicia a descoberta de dispositivos.
        bluetoothAdapter.startDiscovery();
        // Quarto passo: Chama o diálogo de dispositivos.
        dialogDevicesUnpairedAux(activity);
    }

    /**
     * Apresenta um diálogo para escolha de um dispositivo para pareamento.
     *
     * @param activity
     */
    private void dialogDevicesUnpairedAux(Activity activity) {
        // Primeiro passo: Cria um novo alert handlerDialog builder.
        dialogBuilder = new DialogBuilder(activity, listDevicesUnpaired);
        // segundo passo: Verifica se existe dispositivos na lista.
        if (!listDevicesUnpaired.isEmpty()) {
            // Terceiro passo: Adiciona os eventos no novo alert builder.
            dialogBuilder.setPositiveButton(activity.getString(R.string.acceptChoiceDialogSelectDevice),
                    new DialogInterface.OnClickListener() { // Escolheu o dispositivo.
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancela a descoberta de dispositivos.
                            cancelDiscoveringDevices();
                            BluetoothDevice device;
                            // Pega o primeiro da lista.
                            if (dialogBuilder.indexClick == -1) {
                                device = listDevicesUnpaired.get(0);
                                // Pega o dispositivo escolhido.
                            } else {
                                device = listDevicesUnpaired.get(dialogBuilder.indexClick);
                            }
                            deviceSelectedUnpaired = device;
                            handler.publish(new LogRecord(Level.INFO, handlerDialog.DEVICE_SELECTED));
                        }
                    });
            dialogBuilder.setNegativeButton(activity.getString(R.string.rejectChoiceDialogSelectDevice),
                    new DialogInterface.OnClickListener() { // Cancelou.
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancela a descoberta de dispositivos.
                            cancelDiscoveringDevices();
                            deviceSelectedUnpaired = null;
                            handler.publish(new LogRecord(Level.INFO, handlerDialog.DEVICE_REFUSED));
                        }
                    });
            // Caso não exista dispositivos na lista.
        } else {
            // Terceiro passo: Adiciona os eventos no novo alert builder.
            dialogBuilder.setMessage(activity.getString(R.string.messageChoiceDialogSelectDeviceUnpairedEmpty));
            dialogBuilder.setNegativeButton(activity.getString(R.string.buttonChoiceDialogSelectDeviceUnpairedEmpty),
                    new DialogInterface.OnClickListener() { // Cancelou.
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelDiscoveringDevices(); // Cancela a descoberta de dispositivos.
                            deviceSelectedUnpaired = null;
                        }
                    });
        }
        // Quarto passo: Verifica se há um alerta em exibição e o fecha.
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.cancel();
        }
        // Quinto passo: Define e exibe o novo alerta.
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Broadcast de procura por dispositivos não pareados.
     *
     * @param activity
     */
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

    /**
     * Cancela o broadcast da descoberta de dispositivos.
     */
    private void cancelDiscoveringDevices() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        alertDialog.cancel();
        handler.publish(new LogRecord(Level.INFO, handlerDialog.CANCEL_DISCOVERING));
    }

    /**
     * Realiza o pareamento de um dispositivo.
     *
     * @param device
     * @throws Exception
     */
    public void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            handler.publish(new LogRecord(Level.INFO, handlerDialog.DEVICE_PAIRED));
            e.printStackTrace();
        }
    }
}
