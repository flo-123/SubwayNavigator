/*
 * Copyright (C) 2012 Jacquet Wong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * musicg api in Google Code: http://code.google.com/p/musicg/
 * Android Application in Google Play: https://play.google.com/store/apps/details?id=com.whistleapp
 *
 */

package helpers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.util.Log;

import com.musicg.wave.Wave;
import com.musicg.wave.WaveHeader;

import hci.glasgow.subwaynavigator.MyApp;
import interfaces.OnSignalsDetectedListener;


public class DetectorThread extends Thread{

    private RecorderThread recorder;
    private WaveHeader waveHeader;
    private volatile Thread _thread;
    private List<Wave> soundDatabase;
    public static long SAMPLE_READING_RATE = MyApp.getSampleReadingRate();
    private static float LEAST_AMOUNT_OF_TIME_IN_BETWEEN_STOPS = 5; //in seconds
    private static int DETECTED_SOUNDS_THRESHOLD = 2;
    private static float HIGHPASS_FILTER = 3000f;
    private long lastSoundDetectedAt;
    private int detectedSounds;


    private OnSignalsDetectedListener onSignalsDetectedListener;

    public DetectorThread(RecorderThread recorder){
        this.recorder = recorder;
        AudioRecord audioRecord = recorder.getAudioRecord();

        int bitsPerSample = 0;
        if (audioRecord.getAudioFormat() == AudioFormat.ENCODING_PCM_16BIT){
            bitsPerSample = 16;
        }
        else if (audioRecord.getAudioFormat() == AudioFormat.ENCODING_PCM_8BIT){
            bitsPerSample = 8;
        }

        int channel = 0;
        if (audioRecord.getChannelConfiguration() == AudioFormat.CHANNEL_IN_MONO){
            channel = 1;
        }

        waveHeader = new WaveHeader();
        waveHeader.setChannels(channel);
        waveHeader.setBitsPerSample(bitsPerSample);
        waveHeader.setSampleRate(audioRecord.getSampleRate());

        //set sound database
        Integer resID = 0;
        Integer index = 1;
        soundDatabase = new ArrayList<Wave>();
        do {
            String resourceString = MyApp.getPathToSoundFiles() + "_" + index.toString();

            resID = MyApp.getContext().getResources().getIdentifier(resourceString, "raw", MyApp.getContext().getPackageName());

            if(resID > 0) {
                InputStream stream = MyApp.getContext().getResources().openRawResource(resID);
                Wave wave = new Wave(stream);

                byte[] bytes = HelperFunctions.highPassFilter(wave.getBytes(), wave.getWaveHeader(), HIGHPASS_FILTER);
                //HelperFunctions.writeWavFile(HelperFunctions.PCMtoWav(bytes,wave.getWaveHeader().getSampleRate(),wave.getWaveHeader().getChannels(),wave.getWaveHeader().getBitsPerSample()));
                wave = new Wave(wave.getWaveHeader(),bytes);

                soundDatabase.add(wave);
            }

            index++;

        } while(resID != 0);

        lastSoundDetectedAt = System.currentTimeMillis();
        detectedSounds = 0;
    }

    public void start() {
        _thread = new Thread(this);
        _thread.start();
    }

    public void stopDetection(){
        _thread = null;
    }

    public void run() {
        try {
            byte[] buffer;

            Thread thisThread = Thread.currentThread();
            while (_thread == thisThread) {
                // detect sound

                buffer = recorder.getFrameBytes();

                long timeStamp = System.currentTimeMillis();
                byte[] combined = new byte[0];
                while((System.currentTimeMillis() - timeStamp) / 1000 < MyApp.getSampleReadingRate()) {

                    byte[] current = recorder.getFrameBytes();

                    combined = new byte[buffer.length + current.length];

                    System.arraycopy(buffer,0,combined,0,buffer.length);
                    System.arraycopy(current,0,combined,buffer.length,current.length);
                    buffer = new byte[combined.length];
                    System.arraycopy(combined,0,buffer,0,combined.length);

                }


                Log.d("detect","run");

                int totalAbsValue = 0;
                short sample = 0;
                float averageAbsValue = 0.0f;

                for (int i = 0; i < buffer.length; i += 2) {
                    sample = (short)((buffer[i]) | buffer[i + 1] << 8);
                    totalAbsValue += Math.abs(sample);
                }
                averageAbsValue = totalAbsValue / buffer.length / 2;

                //System.out.println(averageAbsValue);

                // no input
                if (averageAbsValue < 30){
                    buffer = null;
                }

                // audio analyst
                if (buffer != null && buffer.length > 0) {

                    //buffer = trim(buffer); //remove trailing zeros

                    Log.d("detect", "sound detected");

                    buffer = HelperFunctions.highPassFilter(buffer, waveHeader, HIGHPASS_FILTER);

                    byte[] data = HelperFunctions.PCMtoWav(buffer, waveHeader.getSampleRate(), waveHeader.getChannels(), waveHeader.getBitsPerSample());
                    //HelperFunctions.writeWavFile(data);

                    Wave wave = new Wave(new ByteArrayInputStream(data));

                    for(Wave fromSoundDB : soundDatabase) {
                        float similarity = fromSoundDB.getFingerprintSimilarity(wave).getSimilarity();
                        Log.d("Fshort", "Value: " + Float.toString(similarity));

                        if (similarity > 0.5 ) {
                            if((System.currentTimeMillis() - lastSoundDetectedAt)  * 1000 > LEAST_AMOUNT_OF_TIME_IN_BETWEEN_STOPS) {
                                if(detectedSounds++ >= DETECTED_SOUNDS_THRESHOLD) {
                                    onSoundDetected();
                                }
                            }
                            break;
                        }
                    }
                }
                //sleep(SAMPLE_READING_RATE * 1000);
                //Thread.currentThread().sleep(SAMPLE_READING_RATE * 10000);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void onSoundDetected(){
        if (onSignalsDetectedListener != null){
            onSignalsDetectedListener.onSoundDetected();
        }
        detectedSounds = 0;
        lastSoundDetectedAt = System.currentTimeMillis();
    }

    public void setOnSignalsDetectedListener(OnSignalsDetectedListener listener){
        onSignalsDetectedListener = listener;
    }

    static byte[] trim(byte[] bytes)
    {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0)
        {
            --i;
        }

        return Arrays.copyOf(bytes, i + 1);
    }

}
