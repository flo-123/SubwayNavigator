package hci.glasgow.subwaynavigator;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Flo on 31/10/15.
 */
public class MyApp extends Application {
    private static Application instance;
    private static Context context;
    private static String pathToSoundFiles;
    private static SharedPreferences prefs = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();
        //prefs = getSharedPreferences("hci.glasgow.subwaynavigator_preferences", MODE_PRIVATE);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static synchronized boolean getDisabledMode() {
        return prefs.getBoolean("disabled_mode", true);
    }

    public static synchronized void setDisabledMode(boolean b) {
        prefs.edit().putBoolean("disabled_mode", b).commit();
    }

    public static Context getContext() {
        return context;
    }

    public static String getPathToSoundFiles() {
        return pathToSoundFiles;
    }

    public static void setPathToSoundFiles(String pathToSoundFiles) {
        MyApp.pathToSoundFiles = pathToSoundFiles;
    }

    public static synchronized boolean getDebugMode() {
        return prefs.getBoolean("debug_mode", true);
    }


    public static long getSampleReadingRate() {
        try {
            String s = prefs.getString("sample_length","1");
            Long l = new Long(s);
            return l;
        } catch (Exception e) {
            return 1;
        }
    }


    public static float getMatchScore() {
        try {
            String s = prefs.getString("match_score","0.5");
            Float f = new Float(s);
            return f;
        } catch (Exception e) {
            return 0.5f;
        }
    }


    public static int getTimeBetweenStops() {
        try {
            String s = prefs.getString("time_between_stops","10");
            Integer i = new Integer(s);
            return i;
        } catch (Exception e) {
            return 10;
        }
    }


    public static int getDetectedSoundsThreshold() {
        try {
            String s = prefs.getString("detected_sounds_threshold","2");
            Integer i = new Integer(s);
            return i;
        } catch (Exception e) {
            return 2;
        }
    }


    public static int getHighPassFilter() {
        try {
            String s = prefs.getString("high_pass_filter","3000");
            Integer i = new Integer(s);
            return i;
        } catch (Exception e) {
            return 3000;
        }
    }

    public static int getLine() {
        try {
            String s = prefs.getString("selected_line","1");
            Integer i = new Integer(s);
            return i;
        } catch (Exception e) {
            return 3000;
        }
    }

}