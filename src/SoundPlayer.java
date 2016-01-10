import javax.sound.sampled.*;
import java.nio.ByteBuffer;

public class SoundPlayer extends Thread{
    public static final int SAMPLING_RATE = 44100;
    public static final int SAMPLE_SIZE = 2;
    public static final double BUFFER_DURATION = 0.10;
    public static final int PACKET_SIZE = (int)(BUFFER_DURATION*SAMPLING_RATE*SAMPLE_SIZE);
    private Mixer mixer;
    private SourceDataLine line;
    private boolean bExitThread = false;
    public SoundPlayer(Mixer mix){
        mixer = mix;
    }
    private int getLineSampleCount() {
        return line.getBufferSize() - line.available();
    }

    public void run(){
        try{
            AudioFormat format = new AudioFormat(SAMPLING_RATE, 16, 1, true, true);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format, PACKET_SIZE*2);

            if(!AudioSystem.isLineSupported(info))
                throw  new LineUnavailableException();

            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
        }catch (LineUnavailableException ex){
            ex.printStackTrace();
        }
        ByteBuffer cBuff = ByteBuffer.allocate(PACKET_SIZE);
        while (!bExitThread) {
            cBuff.clear();
            for (int i = 0; i < PACKET_SIZE / SAMPLE_SIZE; ++i) {
                cBuff.putShort((short) (Short.MAX_VALUE * mixer.getSoundValue()));
            }
            line.write(cBuff.array(), 0, cBuff.position());
            try {
                while (getLineSampleCount() > PACKET_SIZE)
                    Thread.sleep(1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        line.drain();
        line.close();
    }
    public void exit(){
        bExitThread = true;
    }
}