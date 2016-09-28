package com.myllenno.bluedroid_project;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.myllenno.bluetoothdroid.devices.DevicesPaired;
import com.myllenno.bluetoothdroid.devices.DevicesUnpaired;
import com.myllenno.bluetoothdroid.configuration.Support;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class DialogDevices extends AppCompatActivity {

    private final String messageError = "Error";
    private final String messageSelectDevicePaired = "SelectDevicePaired";
    private final String messageSelectDeviceUnpaired = "SelectDeviceUnpaired";

    public Support support;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_devices);
    }

    private DevicesPaired devicesPaired;

    // Evento que apresenta um diálogo de dispositivos pareados e apresenta o escolhido pelo usuário.
    public void selectDevicePaired(View view){
        devicesPaired = new DevicesPaired();

        Handler handler = new Handler(){
            @Override
            public void publish(LogRecord record) {

                if (record.getMessage().equals(devicesPaired.dialogDeviceSelected)){ // Selecionou um dispositivo

                    BluetoothDevice device = devicesPaired.getDeviceSelected();

                    Toast.makeText(getApplicationContext(), "Device Selected: " + device.getName(), Toast.LENGTH_SHORT).show();

                } else if (record.getMessage().equals(devicesPaired.dialogDeviceRefused)){ // Recusou o diálogo.

                    Toast.makeText(getApplicationContext(), "Devices Refused!", Toast.LENGTH_SHORT).show();

                } else if (record.getMessage().equals(devicesPaired.dialogDeviceEmpty)){ // Não há dispositivos pareados.

                    Toast.makeText(getApplicationContext(), "No Paired Devices!", Toast.LENGTH_SHORT).show();

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
    public void selectDeviceUnpaired(View view){
        devicesUnpaired = new DevicesUnpaired();

        Handler handler = new Handler(){
            @Override
            public void publish(LogRecord record) {

                if (record.getMessage().equals(devicesUnpaired.dialogDeviceSelected)){ // Selecionou um dispositivo

                    BluetoothDevice device = devicesUnpaired.getDeviceSelected();

                    Toast.makeText(getApplicationContext(), "Device Selected: " + device.getName(), Toast.LENGTH_SHORT).show();

                } else if (record.getMessage().equals(devicesUnpaired.dialogDeviceRefused)){ // Recusou o dialogo.

                    Toast.makeText(getApplicationContext(), "Devices Refused!", Toast.LENGTH_SHORT).show();

                } else if (record.getMessage().equals(devicesUnpaired.dialogDeviceCancel)){ // Cancelou a busca por dispositivos.

                    Toast.makeText(getApplicationContext(), "Cancel Discovering Devices!", Toast.LENGTH_SHORT).show();

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
