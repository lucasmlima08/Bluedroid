package com.myllenno.bluetoothdroid.devices;

import com.myllenno.bluetoothdroid.functions.DialogBuilder;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;

import com.myllenno.bluetoothdroid.R;
import com.myllenno.bluetoothdroid.report.HandlerDialog;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class DevicesPaired {

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
     * Dispositivo pareado selecionado.
     */
    private BluetoothDevice deviceSelectedPaired;

    /**
     * Lista de dispositivos pareados.
     */
    private ArrayList<BluetoothDevice> listDevicesPaired;

    /**
     * Inicia a classe de comunicação do evento.
     */
    public DevicesPaired() {
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
        return deviceSelectedPaired;
    }

    /**
     * Retorna a lista de dispositivos pareados.
     *
     * @return
     */
    public ArrayList<BluetoothDevice> getListDevicesPaired() {
        listDevicesPaired = listPairedDevices();
        return listDevicesPaired;
    }

    /**
     * Chama um diálogo para a escolha de um dispositivo pareado.
     *
     * @param activity
     */
    public void dialogDevicesPaired(Activity activity) {
        // Primeiro passo: Inicia o array que receberá os dispositivos pareados.
        listDevicesPaired = listPairedDevices();
        // Segundo passo: Cria um novo alert handlerDialog builder.
        dialogBuilder = new DialogBuilder(activity, listDevicesPaired);
        // Terceiro passo: Verifica se existe dispositivos na lista.
        if (!listDevicesPaired.isEmpty()) {
            // Quarto passo: Adiciona os eventos no novo alert builder.
            dialogBuilder.setPositiveButton(activity.getString(R.string.acceptChoiceDialogSelectDevice),
                    new DialogInterface.OnClickListener() { //- Escolheu dispositivo.
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BluetoothDevice device;
                            // Pega o primeiro da lista.
                            if (dialogBuilder.indexClick == -1) {
                                device = listDevicesPaired.get(0);
                                // Dispositivo escolhido.
                            } else {
                                device = listDevicesPaired.get(dialogBuilder.indexClick);
                            }
                            alertDialog.cancel();
                            deviceSelectedPaired = device;
                            handler.publish(new LogRecord(Level.INFO, handlerDialog.DEVICE_SELECTED));
                        }
                    });
            dialogBuilder.setNegativeButton(activity.getString(R.string.rejectChoiceDialogSelectDevice),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                            deviceSelectedPaired = null;
                            handler.publish(new LogRecord(Level.INFO, handlerDialog.DEVICE_REFUSED));
                        }
                    });

            // Caso não exista dispositivos na lista.
        } else {
            // Quarto passo: Adiciona os eventos no novo alert builder.
            dialogBuilder.setMessage(activity.getString(R.string.messageChoiceDialogSelectDevicePairedEmpty));
            dialogBuilder.setNegativeButton(activity.getString(R.string.buttonChoiceDialogSelectDevicePairedEmpty),
                    new DialogInterface.OnClickListener() { // Aceitou
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();
                            deviceSelectedPaired = null;
                            handler.publish(new LogRecord(Level.INFO, handlerDialog.EMPTY));
                        }
                    });
        }
        // Quinto passo: Verifica se há um alerta em exibição e o fecha.
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.cancel();
        }
        // Sexto passo: Define e exibe o novo alerta.
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Retorna a lista de dispositivos pareados.
     *
     * @return
     */
    public ArrayList<BluetoothDevice> listPairedDevices() {
        // Primeiro passo: Define as listas que receberão os dispositivos.
        Set<BluetoothDevice> setDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        ArrayList<BluetoothDevice> devices = new ArrayList<>();
        // Segundo passo: Percorre a primeira lista de dispositivos e salva na segunda lista.
        for (BluetoothDevice device : setDevices) {
            devices.add(device);
        }
        return devices;
    }

    /**
     * Remove o pareamento de um dispositivo.
     *
     * @param device
     */
    public void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            handler.publish(new LogRecord(Level.INFO, handlerDialog.DEVICE_PAIRED_REMOVED));
            e.printStackTrace();
        }
    }
}
