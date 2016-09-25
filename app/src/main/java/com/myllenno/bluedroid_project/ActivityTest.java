package com.myllenno.bluedroid_project;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.myllenno.bluetoothdroid.devices.DevicesPaired;
import com.myllenno.bluetoothdroid.devices.DevicesUnpaired;
import com.myllenno.bluetoothdroid.configuration.Support;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class ActivityTest extends AppCompatActivity {

    private final String messageError = "Error";
    private final String messageSelectDevicePaired = "SelectDevicePaired";
    private final String messageSelectDeviceUnpaired = "SelectDeviceUnpaired";

    public Support support;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void clickEvent(View view){
        try {
            //selectDevicePaired();
            selectDeviceUnpaired();

        } catch (Exception e){
            Log.e(messageError, e.toString());
        }
    }

    private DevicesPaired devicesPaired;

    // Evento que apresenta um diálogo de dispositivos pareados e apresenta o escolhido pelo usuário.
    private void selectDevicePaired(){
        devicesPaired = new DevicesPaired();

        Handler handler = new Handler(){
            @Override
            public void publish(LogRecord record) {

                if (record.getMessage().equals(devicesPaired.dialogDeviceSelected)){ // Selecionou um dispositivo

                    BluetoothDevice device = devicesPaired.getDeviceSelected();
                    Log.i(messageSelectDevicePaired, device.getName());

                } else if (record.getMessage().equals(devicesPaired.dialogDeviceRefused)){ // Recusou o diálogo.

                    Log.i(messageSelectDevicePaired, "Null");

                } else if (record.getMessage().equals(devicesPaired.dialogDeviceEmpty)){ // Não há dispositivos pareados.

                    Log.i(messageSelectDevicePaired, "Null");

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

    private DevicesUnpaired devicesUnpaired;

    // Evento que apresenta um diálogo de dispositivos não pareados e apresenta o escolhido pelo usuário.
    private void selectDeviceUnpaired(){
        devicesUnpaired = new DevicesUnpaired();

        Handler handler = new Handler(){
            @Override
            public void publish(LogRecord record) {

                if (record.getMessage().equals(devicesUnpaired.dialogDeviceSelected)){ // Selecionou um dispositivo

                    BluetoothDevice device = devicesUnpaired.getDeviceSelected();
                    Log.i(messageSelectDeviceUnpaired, device.getName());

                } else if (record.getMessage().equals(devicesUnpaired.dialogDeviceRefused)){ // Recusou o dialogo.

                    Log.i(messageSelectDeviceUnpaired, "Null");

                } else if (record.getMessage().equals(devicesUnpaired.dialogDeviceCancel)){ // Cancelou a busca por dispositivos.

                    Log.i(messageSelectDeviceUnpaired, "Null");

                }
            }
            @Override
            public void flush() {

            }
            @Override
            public void close() throws SecurityException {

            }
        };

        devicesUnpaired.setHandler(handler);
        devicesUnpaired.dialogDevicesUnpaired(this);
    }
}
