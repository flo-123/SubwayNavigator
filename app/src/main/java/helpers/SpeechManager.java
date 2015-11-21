package helpers;

import android.speech.tts.TextToSpeech;

import java.util.Locale;

import hci.glasgow.subwaynavigator.MyApp;

/**
 * Created by Flo on 20/11/15.
 */
public class SpeechManager {
    private TextToSpeech speech;

    public SpeechManager() {
        speech=new TextToSpeech(MyApp.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    speech.setLanguage(Locale.UK);
                    speech.setSpeechRate(0.8f);
                }
            }
        });
    }
    public void speak(String text) {
        if(MyApp.getDisabledMode()) {
            speech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
