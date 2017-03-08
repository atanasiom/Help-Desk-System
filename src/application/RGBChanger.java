package application;

import javafx.beans.value.WritableValue;
import javafx.scene.layout.GridPane;

/**
 * Used to change the color of a newly created ticket. Never to be used for anything else
 *
 * @author Michael Atanasio
 */
public class RGBChanger implements WritableValue<Integer> {

    private GridPane box;

    private int redBlue = 144;
    private double green = 238;
    private double ratio = 18.0 / 100.0;

    /**
     * Creates a new RGB Changer that will change the colors of {@code box}
     *
     * @param box the bx to have its color changed
     */
    public RGBChanger(GridPane box) {
        super();
        this.box = box;
    }

    @Override
    public Integer getValue() {
        return redBlue;
    }

    @Override
    public void setValue(Integer value) {
        this.redBlue = value;
        this.green -= ratio;
        StringBuilder sb = new StringBuilder();
        sb.append("-fx-background-color: rgb(");
        sb.append(this.redBlue);
        sb.append(", ");
        sb.append(this.green);
        sb.append(", ");
        sb.append(this.redBlue);
        sb.append(");");
        this.box.setStyle(sb.toString());
    }
}
