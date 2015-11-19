package hci.glasgow.subwaynavigator;

import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import com.musicg.wave.Wave;

import java.io.InputStream;
import java.util.List;

import gui.VerticalSeekBar;
import helpers.DetectorThread;
import helpers.RecorderThread;
import interfaces.OnSignalsDetectedListener;
import model.Stop;
import model.Stops;

public class NavigatorActivity extends AppCompatActivity implements OnSignalsDetectedListener {

    private TextView stopsLeftText;
    private TextView currentStopText;
    private Integer stopsLeft;

    private DetectorThread detectorThread;
    private RecorderThread recorderThread;
    private List<Stop> stopsInBetween;
    private VerticalSeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_navigator);

        Intent intent  = getIntent();
        Bundle res = intent.getExtras();

        Stop startStop = (Stop)res.getSerializable("start");
        Stop destStop = (Stop)res.getSerializable("destination");
        Stops stops = (Stops)res.getSerializable("stops");

        stopsInBetween = stops.stopsInBetween(startStop, destStop);
        stopsLeft = stopsInBetween.size();

        stopsLeftText = (TextView)findViewById(R.id.stops_left);
        stopsLeftText.setText(stopsLeft.toString());

        TextView startStopText = (TextView)findViewById(R.id.start);
        startStopText.setText(startStop.getName());

        TextView destStopText = (TextView)findViewById(R.id.destination);
        destStopText.setText(destStop.getName());

        currentStopText = (TextView)findViewById(R.id.current);
        currentStopText.setText(startStop.getName());

        seekBar = (VerticalSeekBar) findViewById(R.id.seekBarProgress);
        seekBar.setMax(100);

        recorderThread = new RecorderThread();
        recorderThread.start();
        detectorThread = new DetectorThread(recorderThread);
        detectorThread.setOnSignalsDetectedListener(this);
        detectorThread.start();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (recorderThread != null) {
            recorderThread.stopRecording();
            recorderThread = null;
        }
        if (detectorThread != null) {
            detectorThread.stopDetection();
            detectorThread = null;
        }
    }

    @Override
    public void onSoundDetected() {
        Log.d("detected", "sound");

        runOnUiThread(new Runnable() {
            public void run() {
                if (stopsLeft > 0) {
                    stopsLeft--;
                    stopsLeftText = (TextView) findViewById(R.id.stops_left);
                    stopsLeftText.setText(stopsLeft.toString());
                    seekBar.setProgress(100 - (100 / stopsInBetween.size() * stopsLeft));
                    seekBar.updateThumb();
                    int index = stopsInBetween.size() - stopsLeft - 1;
                    if (index >= 0 && index < stopsInBetween.size()) {
                        currentStopText.setText(stopsInBetween.get(index).getName());
                    }
                } else {
                    //reached destination
                    if (recorderThread != null) {
                        recorderThread.stopRecording();
                        recorderThread = null;
                    }
                    if (detectorThread != null) {
                        detectorThread.stopDetection();
                        detectorThread = null;
                    }

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }

        if (id==R.id.action_settings) {
            Intent i = new Intent(this, AppSettingsActivity.class);
            startActivityForResult(i, 1);
        }
        return super.onOptionsItemSelected(item);
    }
}
