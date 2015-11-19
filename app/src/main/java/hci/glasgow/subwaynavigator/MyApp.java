package hci.glasgow.subwaynavigator;

import android.app.Application;
import android.content.Context;

/**
 * Created by Flo on 31/10/15.
 */
public class MyApp extends Application {
    private static Application instance;
    private static Context context;
    private static String pathToSoundFiles;
    public static long SAMPLE_READING_RATE = (long) 1; //  in seconds

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MyApp.context = getApplicationContext();
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

    public static long getSampleReadingRate() {
        return SAMPLE_READING_RATE;
    }

    public static void setSampleReadingRate(long sampleReadingRate) {
        SAMPLE_READING_RATE = sampleReadingRate;
    }
}