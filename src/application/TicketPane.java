package application;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class TicketPane extends GridPane {

	private ServiceRequest request;

	private Label createdLabel = new Label();
	private Label closedLabel = new Label();
	private Label descLabel = new Label();
	private Label techLabel = new Label();
	private StatusBox statusBox;

	public TicketPane(ServiceRequest request, int idNumber) {
		super();

		this.request = request;
		this.statusBox = new StatusBox(request, idNumber);

		createdLabel.setText(RequestIO.DATE_FORMATTER.format(request.getDateRequested()));
		closedLabel.setText(request.getServiceStatus() == ServiceStatus.OPEN ? "Currently Open" : RequestIO
				.DATE_FORMATTER.format(request.getDateCompleted()));
		descLabel.setText(request.getDescription());
		techLabel.setText(request.getTechnician());

		this.add(createdLabel, 0, 0);
		this.add(statusBox, 1, 0);
		this.add(closedLabel, 2, 0);
		this.add(descLabel, 3, 0);
		this.add(techLabel, 4, 0);

		this.setHgap(20);

		ColumnConstraints column = new ColumnConstraints();
		column.setPercentWidth(20);
		this.getColumnConstraints().addAll(column, column, column, column, column);
		this.setMinHeight(40);
		this.setPadding(new Insets(7, 7, 7, 7));

		this.setId("row");
		this.setStyle("-fx-background-color: " + (idNumber % 2 == 0 ? "Gainsboro" : "WhiteSmoke"));
	}

	public StatusBox getStatusBox() {
		return this.statusBox;
	}

}