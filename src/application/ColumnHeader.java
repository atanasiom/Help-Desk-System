package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ColumnHeader extends HBox {

	private Label columnNameLabel;
	private IconView sortIcon = new IconView("up-down-arrow.png");

	public ColumnHeader(String columnName) throws IOException {
		super();

		this.columnNameLabel = new Label(columnName);
		this.columnNameLabel.setPadding(new Insets(0, 20, 0, 0));
		this.getChildren().addAll(columnNameLabel, sortIcon);
		this.setAlignment(Pos.CENTER);
	}

}