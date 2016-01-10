import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Window extends Application {
    private List<Sine> sines;
    private int iter;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Group pane = new Group();
        final Canvas canvas = new Canvas(200, 300);
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        Scene scene = new Scene(pane, 200, 300, false, SceneAntialiasing.BALANCED);
        pane.getChildren().add(canvas);
        primaryStage.setScene(scene);
        sines = new ArrayList<>();
        Sine sin = new Sine(201);
        sin.setColor(Color.ALICEBLUE);
        sines.add(sin);
        sin = new Sine(201);
        sin.setColor(Color.RED);
        sin.setFreqChange(0.002);
        sin.setFreqBotBound(0.005f);
        sin.setScaleTopBound(90.0f);
        sines.add(sin);

        new AnimationTimer(){

            @Override
            public void handle(long now) {
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, 201, 300);
                gc.setLineWidth(0.3);
                gc.setLineCap(StrokeLineCap.ROUND);
                gc.setLineJoin(StrokeLineJoin.ROUND);
                for (Sine s:sines) {
                    if (s.reachedFreqCondition())
                        s.reverseFreqChange();
                    if(s.rechedScaleCond())
                        s.reverseScaleChange();
                    s.update();
                    gc.beginPath();
                    gc.moveTo(0, 0);
                    gc.setStroke(s.getColor());
                    for (int i = 0; i < s.getValues().length/2; i++) {
                        float[] vals = s.getValues();
                        vals[(vals.length/2) + i] = (float)(Math.sin((s.getFreq() * i)));
                        vals[(vals.length/2) - i] = (float)(-Math.sin((s.getFreq() * i)));
                    }
                    for(int i=0;i<s.getValues().length;++i)
                        gc.lineTo(i*s.getStep(),s.getValues()[i]*s.getScale() + 100);
                    gc.moveTo(201, 0);
                    gc.closePath();
                    gc.stroke();
                }
            }
        }.start();
        SoundPlayer sp = new SoundPlayer(this);
        sp.setDaemon(true);
        sp.start();
        primaryStage.show();
    }
    public double getValue(){
        double val =0;
        for(Sine s:sines){
            val += s.getScale()*Math.sin(s.getFreq()*s.getfCyclePos());
            s.nextCyclePos();
        }
        if(val>100)
            return 1;
        else
            return val/100;
    }
}
class Sine{
    private Color color = Color.RED;
    private float scale = 9.0f;
    private float scaleChange = 0.1f;
    private float scaleTopBound = 80.0f;
    private float scaleBotBound = 5.0f;
    private float step =1.0f;
    private double freq =0.01;
    private double freqChange = 0.001;
    private double freqTopBound = 0.1;
    private double freqBotBound = 0.001;
    private double fCyclePos =0.0;

    public double getfCyclePos() {
        return fCyclePos;
    }

    public void nextCyclePos(){
        fCyclePos += (freq*1000000)/SoundPlayer.SAMPLING_RATE;
        if(fCyclePos >1 )
            fCyclePos -= 1;
    }

    public float getScaleTopBound() {
        return scaleTopBound;
    }

    public void setScaleTopBound(float scaleTopBound) {
        this.scaleTopBound = scaleTopBound;
    }

    public float getScaleBotBound() {
        return scaleBotBound;
    }

    public void setScaleBotBound(float scaleBotBound) {
        this.scaleBotBound = scaleBotBound;
    }

    public double getFreqTopBound() {
        return freqTopBound;
    }

    public void setFreqTopBound(double freqTopBound) {
        this.freqTopBound = freqTopBound;
    }

    public double getFreqBotBound() {
        return freqBotBound;
    }

    public void setFreqBotBound(double freqBotBound) {
        this.freqBotBound = freqBotBound;
    }

    public float getScaleChange() {
        return scaleChange;
    }

    public void setScaleChange(float scaleChange) {
        this.scaleChange = scaleChange;
    }

    public double getFreqChange() {
        return freqChange;
    }

    public void setFreqChange(double freqChange) {
        this.freqChange = freqChange;
    }

    private float[] values;

    public Sine(int size){
        values = new float[(int)(size/step)];

    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getIncrease() {
        return scaleChange;
    }

    public void setIncrease(float increase) {
        this.scaleChange = increase;
    }
    public void reverseScaleChange(){
        scaleChange = -scaleChange;
    }
    public void reverseFreqChange(){
        freqChange = -freqChange;
    }
    public float getStep() {
        return step;
    }

    public void setStep(float step) {
        this.step = step;
    }

    public double getFreq() {
        return freq;
    }

    public void setFreq(double freq) {
        this.freq = freq;
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }
    public boolean reachedFreqCondition(){
        if(freqChange == 0)
            return false;
        if(freq >= freqTopBound || freq <= freqBotBound)
            return true;
        return false;
    }
    public boolean rechedScaleCond(){
        if(scaleChange == 0)
            return false;
        if(scale >= scaleTopBound || scale <= scaleBotBound)
            return true;
        return false;
    }
    public void update(){
        scale += scaleChange;
        freq += freqChange;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
class SoundPlayer extends Thread{
    public static final int SAMPLING_RATE = 44100;
    public static final int SAMPLE_SIZE = 2;
    public static final double BUFFER_DURATION = 0.10;
    public static final int PACKET_SIZE = (int)(BUFFER_DURATION*SAMPLING_RATE*SAMPLE_SIZE);
    private Window window;
    private SourceDataLine line;
    private boolean bExitThread = false;
    public SoundPlayer(Window wind){
        window = wind;
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
                cBuff.putShort((short) (Short.MAX_VALUE * window.getValue()));
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
