package application;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.io.IOException;

/**
 * This is the bar that goes across the top with the column names and the buttons to let the user sort the list of items
 */
public class ColumnNameBar extends HBox {

	private String[] columnNames;

	/**
	 * Creates a bar containing the column names with the buttons to sort the list. In theory, since it is using a
	 * var-args for the column names, I could make this expandable to however many column names I need, but really, it
	 * is just one less step since I want these in an array anyway
	 *
	 * @param columnNames the list of column names
	 *
	 * @throws IOException if the icon for the sorting is not found
	 */
	public ColumnNameBar(String... columnNames) throws IOException {
		super();

		this.columnNames = columnNames;
		for (String name : columnNames) {
			ColumnHeader header = new ColumnHeader(name);
			header.setMaxWidth(HelpDeskSystem.WIDTH / columnNames.length);
			HBox.setHgrow(header, Priority.ALWAYS);
			this.getChildren().add(header);
		}
	}

}