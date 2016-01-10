import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Mixer {
    List<SoundSine> sines;

    public Mixer(List<Sine> sines){
        this.sines = new ArrayList<>();
        sines.forEach((e) -> this.sines.add(new SoundSine(e)));
    }
    public double getSoundValue(){
        double val =0;
        for(SoundSine s:sines){
            val += s.getSoundValue();
            s.nextCyclePos();
        }
        if(val>100)
            return 0.99;
        else
            return val/100;
    }
}
