package com.myllenno.bluedroid_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myllenno.bluetoothdroid.configuration.Devices;
import com.myllenno.bluetoothdroid.configuration.Support;
import com.myllenno.bluetoothdroid.connection.Client;
import com.myllenno.bluetoothdroid.connection.Server;
import com.myllenno.bluetoothdroid.connection.Controller;

public class MainActivity extends AppCompatActivity {

    public Devices devices;
    public Support support;

    public Client client;
    public Server server;
    public Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        devices = new Devices(this.getApplicationContext());
        support = new Support();

        client = new Client("");
        server = new Server("","");
        controller = new Controller();
    }
}
