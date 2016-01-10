import javafx.scene.paint.Color;

public class Sine{
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