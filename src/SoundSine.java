
public class SoundSine {
    private Sine sine;
    private double fCyclePos = 0.0;

    public SoundSine(Sine sine){
        this.sine = sine;
    }
    public double getfCyclePos() {
        return fCyclePos;
    }

    public void nextCyclePos(){
        fCyclePos += (sine.getFreq()*1000000)/SoundPlayer.SAMPLING_RATE;
        if(fCyclePos >1 )
            fCyclePos -= 1;
    }
    public double getSoundValue(){
        return sine.getScale()*Math.sin(sine.getFreq()*fCyclePos);
    }
    public Sine getSine() {
        return sine;
    }
}
