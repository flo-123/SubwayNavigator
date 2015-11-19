package helpers;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.util.Log;
import android.widget.NumberPicker;

import com.musicg.wave.WaveHeader;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.filters.HighPass;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import model.Stop;
import model.Stops;

/**
 * Created by Flo on 05/11/15.
 */
public abstract class HelperFunctions {

    private static int currentSoundFileID = 1000;

    public static Stop getStopFromPicker(NumberPicker picker, Stops stops) {
        String selection = picker.getDisplayedValues()[picker.getValue()];
        for (Stop s : stops.getStops()) {
            if(s.getName().equals(selection)) {
                return s;
            }
        }
        return  stops.getStops().get(0);
    }

    public static byte[] PCMtoWav(byte[] data, int srate, int channel, int format){
        byte[] header = new byte[44];

        long totalDataLen = data.length + 36;
        long bitrate = srate * channel * format;

        header[0] = 'R';
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f';
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = (byte) format;
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1;
        header[21] = 0;
        header[22] = (byte) channel;
        header[23] = 0;
        header[24] = (byte) (srate & 0xff);
        header[25] = (byte) ((srate >> 8) & 0xff);
        header[26] = (byte) ((srate >> 16) & 0xff);
        header[27] = (byte) ((srate >> 24) & 0xff);
        header[28] = (byte) ((bitrate / 8) & 0xff);
        header[29] = (byte) (((bitrate / 8) >> 8) & 0xff);
        header[30] = (byte) (((bitrate / 8) >> 16) & 0xff);
        header[31] = (byte) (((bitrate / 8) >> 24) & 0xff);
        header[32] = (byte) ((channel * format) / 8);
        header[33] = 0;
        header[34] = 16;
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (data.length  & 0xff);
        header[41] = (byte) ((data.length >> 8) & 0xff);
        header[42] = (byte) ((data.length >> 16) & 0xff);
        header[43] = (byte) ((data.length >> 24) & 0xff);

        byte[] combined = new byte[header.length + data.length];

        System.arraycopy(header,0,combined,0,header.length);
        System.arraycopy(data,0,combined,header.length,data.length);

        return combined;
    }

    public static byte[] highPassFilter(byte[] buffer, WaveHeader w, float frequency) {
        HighPass highPass = new HighPass(frequency, w.getSampleRate());
        TarsosDSPAudioFormat format = new TarsosDSPAudioFormat(w.getSampleRate(),w.getBitsPerSample(),w.getChannels(),true, false);
        AudioEvent audioEvent = new AudioEvent(format);
        float[] f_buffer = bytesToFloats(buffer);
        audioEvent.setFloatBuffer(f_buffer);
        highPass.process(audioEvent);
        buffer = audioEvent.getByteBuffer();
        return buffer;
    }


    public static float[] bytesToFloats(byte[] bytes) {
        float[] floats = new float[bytes.length / 2];
        short[] shorts = new short[bytes.length/2];
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
        for(int i=0; i < bytes.length; i+=2) {
            floats[i/2] = shorts[i/2] / 32768f;
        }
        return floats;
    }



    public static void writeWavFile(byte[] data) {

        File mydir = new File(Environment.getExternalStorageDirectory() + "/wav/");
        if(!mydir.exists())
            mydir.mkdirs();

        File file = new File(Environment.getExternalStorageDirectory() + "/wav/", "wav" + new Integer(currentSoundFileID++).toString() + ".wav" );

        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream fos=new FileOutputStream(file.getPath());

            fos.write(data);
            fos.close();
        }
        catch (java.io.IOException e) {
            Log.e("writeWavFile", "Exception in writeWavFile", e);
        }
        Log.d("writeWavFile", "file written");

    }

    public static void setDividerColor(NumberPicker picker, int color) {
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
