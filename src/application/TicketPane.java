package application;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * The actual meat of the rows
 */
public class TicketPane extends GridPane {

	private Label createdLabel = new Label();
	private Label closedLabel = new Label();
	private Label descLabel = new Label();
	private Label techLabel = new Label();
	private StatusBox statusBox;

	/**
	 * Creates the actual view that displays the ticket's information
	 *
	 * @param request  The request
	 * @param idNumber
	 */
	public TicketPane(ServiceRequest request, int idNumber) {
		super();

		//New StatusBox
		this.statusBox = new StatusBox(request, idNumber);

		//Displays when it was created, closed, it's description, and the tech associated.
		createdLabel.setText(RequestIO.DATE_FORMATTER.format(request.getDateRequested()));
		closedLabel.setText(request.getServiceStatus() == ServiceStatus.OPEN ? "Currently Open" : RequestIO.DATE_FORMATTER.format(request.getDateCompleted()));
		descLabel.setText(request.getDescription());
		techLabel.setText(request.getTechnician());

		this.add(createdLabel, 0, 0);
		this.add(statusBox, 1, 0);
		this.add(closedLabel, 2, 0);
		this.add(descLabel, 3, 0);
		this.add(techLabel, 4, 0);

		//Spacing
		this.setHgap(20);

		ColumnConstraints column = new ColumnConstraints();
		column.setPercentWidth(20);
		//Spacing
		this.getColumnConstraints().addAll(column, column, column, column, column);
		this.setMinHeight(40);
		this.setPadding(new Insets(7, 7, 7, 7));

		this.setId("row");
		//Sets it the proper color
		this.setStyle("-fx-background-color: " + (idNumber % 2 == 0 ? "Gainsboro" : "WhiteSmoke"));
	}

	public StatusBox getStatusBox() {
		return this.statusBox;
	}

}