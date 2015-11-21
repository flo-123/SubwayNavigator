package hci.glasgow.subwaynavigator;

import android.app.Activity;
import android.app.AlertDialog;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.List;

import helpers.HelperFunctions;
import helpers.NotificationManager;
import model.GlasgowSubwayStops;
import model.ScotrailStops;
import model.Stop;
import model.Stops;

public class RoutePicker extends AppCompatActivity implements StartStopPickerFragement.OnFragmentInteractionListener, DestinationStopPickerFragement.OnFragmentInteractionListener, FragmentManager.OnBackStackChangedListener {

    private Stops stops;
    private  Stop startStop;
    private Stop destStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_picker);
    }

    private void init() {

        int line = MyApp.getLine();

        switch (line) {
            case 1: //Glasgow Subway
                stops = new GlasgowSubwayStops();
                break;
            case 2: //Scotrail
                stops = new ScotrailStops();
                break;
            default:
                stops = new GlasgowSubwayStops();
        }


        StartStopPickerFragement newFragment = new StartStopPickerFragement();
        newFragment.stops = stops;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        getSupportFragmentManager().addOnBackStackChangedListener((FragmentManager.OnBackStackChangedListener) this);
        shouldDisplayHomeUp();

        transaction.replace(R.id.fragment_container, newFragment);

        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FragmentManager fm = this.getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==R.id.action_settings) {
            Intent i = new Intent(this, AppSettingsActivity.class);
            startActivityForResult(i, 1);
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void startStopSelected(Stop stop) {
        startStop = stop;
        DestinationStopPickerFragement newFragment = new DestinationStopPickerFragement();
        newFragment.stops = stops;
        newFragment.startStop = stop;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void destinatinStopSelected(Stop stop, boolean inbound) {

        destStop = stop;
        stops.setInbound(inbound);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("start", startStop);
        intent.putExtra("destination", destStop);
        intent.putExtra("stops", stops);
        MyApp.setPathToSoundFiles(stops.getPathToSoundFiles());
        intent.setClassName("hci.glasgow.subwaynavigator", "hci.glasgow.subwaynavigator.NavigatorActivity");
        startActivity(intent);

    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canback = getSupportFragmentManager().getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }
}
