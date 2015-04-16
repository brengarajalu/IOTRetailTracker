package com.example.brengarajulu.gimbaltrackerv3;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.gimbal.android.Gimbal;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.Place;
import com.gimbal.android.Visit;

import com.gimbal.android.CommunicationManager;
import com.gimbal.android.CommunicationListener;
import com.gimbal.android.Communication;
import com.gimbal.android.Push;

import java.io.Console;
import java.sql.Date;
import java.util.Collection;
import java.util.List;
import android.util.Log;
import android.widget.TextView;

import com.gimbal.android.BeaconEventListener;
import com.gimbal.android.BeaconManager;
import com.gimbal.android.BeaconSighting;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;


public class MainActivity extends ActionBarActivity {

    private PlaceEventListener placeEventListener;
    private CommunicationListener communicationListener;
    private BeaconEventListener beaconSightingListener;
    private BeaconManager beaconManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Gimbal.setApiKey(this.getApplication(), "a83cfaa5-2a28-400e-a3d2-9f7bdfa5c0f1");
       // Gimbal.resetApplicationInstanceIdentifier();

        placeEventListener = new PlaceEventListener() {
            @Override
            public void onVisitStart(Visit visit) {
                // This will be invoked when a place is entered. Example below shows a simple log upon enter
                Log.i("Info:", "Enter: " + visit.getPlace().getName() + ", at: " + new Date(visit.getArrivalTimeInMillis()));
                TextView txtplace=(TextView) findViewById(R.id.txtplace);
                txtplace.setText("Welcome to "+visit.getPlace().getName());

                TextView txtVisitTime=(TextView) findViewById(R.id.txtVisitTime);
                txtVisitTime.setText("Visited time : "+new Date(visit.getArrivalTimeInMillis()));
            }

            @Override
            public void onVisitEnd(Visit visit) {
                // This will be invoked when a place is exited. Example below shows a simple log upon exit
                Log.i("Info:", "Exit: " + visit.getPlace().getName() + ", at: " + new Date(visit.getDepartureTimeInMillis()));

            }
        };
        PlaceManager.getInstance().addListener(placeEventListener);

        communicationListener = new CommunicationListener() {
            @Override
            public Collection<Communication> presentNotificationForCommunications(Collection<Communication> communications, Visit visit) {
                String strCommunication="";
                String strTitle="";
                for (Communication comm : communications) {
                    Log.i("INFO", "Place Communication: " + visit.getPlace().getName() + ", message: " + comm.getTitle());
                    strCommunication+=comm.getTitle()+":"+comm.getDescription();


                }
                //allow Gimbal to show the notification for all communications
                TextView txtCommunication=(TextView) findViewById(R.id.txtCommunication);
                txtCommunication.setText(strCommunication);
                return communications;
            }

            @Override
            public Collection<Communication> presentNotificationForCommunications(Collection<Communication> communications, Push push) {
                for (Communication comm : communications) {
                    Log.i("INFO", "Received a Push Communication with message: " + comm.getTitle());

                    TextView txtCommunication=(TextView) findViewById(R.id.txtCommunication);
                    txtCommunication.setText("You have received the offers.Check out!");
                }
                //allow Gimbal to show the notification for all communications
                return communications;
            }

            @Override
            public void onNotificationClicked(List<Communication> communications) {
                Log.i("INFO", "Notification was clicked on");
            }
        };
        CommunicationManager.getInstance().addListener(communicationListener);

        beaconSightingListener = new BeaconEventListener() {
            @Override
            public void onBeaconSighting(BeaconSighting sighting) {
                Log.i("INFO", sighting.getBeacon().getName().toString());
                TextView txtbeacon=(TextView) findViewById(R.id.txtbeacon);
                txtbeacon.setText("Beacon Tracked: "+ sighting.getBeacon().getName()+" Proximity :"+sighting.getRSSI().toString());
                Log.i("INFO", sighting.toString());

            }
        };
        beaconManager = new BeaconManager();
        beaconManager.addListener(beaconSightingListener);

        PlaceManager.getInstance().startMonitoring();
        beaconManager.startListening();
        CommunicationManager.getInstance().startReceivingCommunications();
    }

    private void MakeSpringRESTCall()
    {
        try {
            final String url = "http://rest-service.guides.spring.io/greeting";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            // restTemplate.po(url, Greeting.class);

        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
