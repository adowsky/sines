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
import java.util.ArrayList;
import java.util.List;

public class Window extends Application {
    private List<Sine> sines;
    private Mixer mixer;
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
        Mixer mixer = new Mixer(sines);
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
        SoundPlayer sp = new SoundPlayer(mixer);
        sp.setDaemon(true);
        sp.start();
        primaryStage.show();
    }
}
