package com.example.pokemongroupapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.Socket;

public class Client extends Activity
{
    private Socket socket;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    class ClientThread implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                InetAddress server = InetAddress.getByName("SERVER IP");

                socket = new Socket(server, 2005);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
