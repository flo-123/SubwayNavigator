package helpers;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;
import java.util.Locale;

import hci.glasgow.subwaynavigator.MyApp;

/**
 * Created by Flo on 20/11/15.
 */
public class SpeechManager implements AudioManager.OnAudioFocusChangeListener, TextToSpeech.OnUtteranceCompletedListener, TextToSpeech.OnInitListener {
    private TextToSpeech speech;
    private AudioManager audioManager;

    public SpeechManager() {
        audioManager = (AudioManager) MyApp.getContext().getSystemService(Context.AUDIO_SERVICE);
        speech=new TextToSpeech(MyApp.getContext(),this);
    }

    public void speak(String text) {
        if(MyApp.getDisabledMode()) {
            audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID");
            speech.speak(text, TextToSpeech.QUEUE_FLUSH, map);
        }
    }


    public void onInit(int status) {
        if(status != TextToSpeech.ERROR) {
            speech.setLanguage(Locale.UK);
            speech.setSpeechRate(0.8f);
            speech.setOnUtteranceCompletedListener(this);
        }
    }

    public void onAudioFocusChange(int focusChange) {

    }

    public void onUtteranceCompleted(String utteranceId) {
        audioManager.abandonAudioFocus(this);
    }

}
