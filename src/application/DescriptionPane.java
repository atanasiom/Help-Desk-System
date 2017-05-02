package application;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * The label used to display the description of the tickets.
 */
public class DescriptionPane extends Pane {

	private Label descLabel;

	public DescriptionPane(String description) {
		super();

		this.descLabel = new Label(description);
		// Sets propery spacing, and hides the label by default.
		this.descLabel.setPadding(new Insets(7, 7, 7, 7));
		this.descLabel.setVisible(false);
		this.setMaxHeight(0);
		this.setMinHeight(0);

		this.setId("desc-pane");
		this.getChildren().add(this.descLabel);
	}

	public Label getLabel() {
		return this.descLabel;
	}

}