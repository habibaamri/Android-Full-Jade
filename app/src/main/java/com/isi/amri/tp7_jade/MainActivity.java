package com.isi.amri.tp7_jade;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.content.Context;
import android.widget.Toast;



import jade.android.MicroRuntimeService;
import jade.android.MicroRuntimeServiceBinder;
import jade.android.RuntimeCallback;

import jade.core.MicroRuntime;
import jade.core.Profile;
import jade.util.leap.Properties;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText longitude;
    private EditText latitude;
    private Button btnRechercher;
    private Button btnConnexion;
    private static int POSITION_REQUEST = 1;

    private ServiceConnection serviceConnection;
    private MicroRuntimeServiceBinder microRuntimeServiceBinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longitude = (EditText) findViewById(R.id.logText);
        latitude = (EditText) findViewById(R.id.latText);
        btnRechercher = (Button) findViewById(R.id.btnChercherPos);
        btnConnexion = (Button) findViewById(R.id.btcConnexionJade);

        btnRechercher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GeoLocationActivity.class);
                startActivityForResult(intent, POSITION_REQUEST);
            }
        });

        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"click connexion jade ok");
                startService();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "start service");
        if (requestCode == POSITION_REQUEST) {
            if (resultCode == RESULT_OK) {
                String log = data.getStringExtra("longitude");
                String lat = data.getStringExtra("latitude");

                longitude.setText(log);
                latitude.setText(lat);
            }

        }
    }

    /**
     * Create JADE Main Container
      */
    private void startService() {

        //Check runtime service
        if (microRuntimeServiceBinder == null) {
            //Create Runtime Service Binder here

            serviceConnection = new ServiceConnection() {

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    Log.i(TAG, "###Gateway successfully bound to RuntimeService");
                    microRuntimeServiceBinder = (MicroRuntimeServiceBinder) service;
                    startContainer();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    // Bind unsuccessful
                    Log.i(TAG, "###Gateway unbound from RuntimeService");
                    microRuntimeServiceBinder = null;
                }
            };
            Log.i(TAG, "###Binding Gateway to RuntimeService...");

            bindService(new Intent(getApplicationContext(), MicroRuntimeService.class),
                    serviceConnection,
                    Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * start container
     */
    private void startContainer() {
        Properties pp = new Properties();
        pp.setProperty(Profile.MAIN_HOST, "192.168.43.91");
        pp.setProperty(Profile.MAIN_PORT, "1099");
        pp.setProperty(Profile.JVM, Profile.ANDROID);
        Log.i("Bind service:", "Properties Set");
        if(!MicroRuntime.isRunning()) {
            microRuntimeServiceBinder.startAgentContainer(pp,
                    new RuntimeCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Split container startup successful
                            Toast.makeText(getBaseContext(), "Container created", Toast.LENGTH_LONG).show();
                            Log.i("Bind service:", "Container creation");
                            startAgent("ANDROID", AndroidAgent.class.getName());

                        }
                        @Override
                        public void onFailure(Throwable throwable) {
                            // Split container startup error
                            Log.i(TAG, "###Failed to create Main Container");
                        }
                    });

        }

    }

    /**
     * START AGENT
     * @param name
     * @param className
     */
    private void startAgent(String name, String className) {
        if (microRuntimeServiceBinder != null) {
            microRuntimeServiceBinder.startAgent(name, className,
                    new Object[]{getApplicationContext(),"latitude.getText().toString","longitude.getText().toString"},
                    new RuntimeCallback<Void>() {
                        @Override
                        public void onSuccess(Void thisIsNull) {

                            Log.i(TAG,"START agent OK");

                        }
                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.i(TAG, "start agent container fail");
                        }
                    });
        }
    }

}