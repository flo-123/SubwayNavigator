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

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import hci.glasgow.subwaynavigator.MyApp;

public class RecorderThread extends Thread {
	
	private AudioRecord audioRecord;
	private boolean isRecording;
	private int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
	private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	private int sampleRate = 44100;
	//private int sampleRate = 11025;
	//private int frameByteSize = 2048; // for 1024 fft size (16bit sample size)
	//private int frameByteSize = 110250;
	//private int frameByteSize = 220500;
	//private int frameByteSize = 32768;
	//private int frameByteSize = 8192;
	//private int frameByteSize = (int)(sampleRate * 2 * 2 * 1);
	//private int frameByteSize = (sampleRate*16*1)/8;
	private int frameByteSize = 11025;
	byte[] buffer;

	private AudioRecord.OnRecordPositionUpdateListener updateListener = new AudioRecord.OnRecordPositionUpdateListener(){

		@Override
		public void onPeriodicNotification(AudioRecord recorder){
			int result = audioRecord.read(buffer, 0, buffer.length);
		}

		@Override
		public void onMarkerReached(AudioRecord recorder)
		{}
	};
	
	public RecorderThread(){
		int recBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfiguration, audioEncoding); // need to be larger than size of a frame
		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfiguration, audioEncoding, recBufSize);
		//audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfiguration, audioEncoding, frameByteSize);
		frameByteSize = recBufSize;
		buffer = new byte[frameByteSize];
	}
	
	public AudioRecord getAudioRecord(){
		return audioRecord;
	}
	
	public boolean isRecording(){
		return this.isAlive() && isRecording;
	}
	
	public void startRecording(){
		try{
			audioRecord.startRecording();
			isRecording = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopRecording(){
		try{
			audioRecord.stop();
			isRecording = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public byte[] getFrameBytes(){
		audioRecord.read(buffer, 0, frameByteSize);
		// analyze sound
		/*
		int totalAbsValue = 0;
        short sample = 0; 
        float averageAbsValue = 0.0f;

        for (int i = 0; i < frameByteSize; i += 2) {
            sample = (short)((buffer[i]) | buffer[i + 1] << 8);
            totalAbsValue += Math.abs(sample);
        }
        averageAbsValue = totalAbsValue / frameByteSize / 2;

        //System.out.println(averageAbsValue);
        
        // no input
        if (averageAbsValue < 30){
        	return null;
        }
		*/
		return buffer;
	}
	
	public void run() {
		startRecording();
	}
}