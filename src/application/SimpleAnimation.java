package application;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.WritableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class SimpleAnimation {

    private Timeline timeline = new Timeline();
    private KeyFrame keyFrame;

    private WritableValue value;
    private double millis;
    private Number endValue;
    private TicketPane pane;

    public SimpleAnimation(WritableValue value, double millis, Number endValue) {
        this.value = value;
        this.millis = millis;
        this.endValue = endValue;
        keyFrame = new KeyFrame(Duration.millis(millis), new KeyValue(value, endValue));
        timeline.getKeyFrames().add(keyFrame);
    }

    public void setOnFinished(EventHandler<ActionEvent> value) {
        timeline.setOnFinished(value);
    }

    public void play() {
        this.timeline.play();
    }
}
