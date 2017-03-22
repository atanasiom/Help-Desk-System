package application;

import javafx.scene.control.ComboBox;

public class StatusBox extends ComboBox<String> {

	public StatusBox(ServiceRequest request, int idNumber) {
		super();
		this.getItems().addAll("Open", "Closed");
		this.getSelectionModel().select(request.getServiceStatus() == ServiceStatus.OPEN ? 0 : 1);
		this.setId("ticket-status");
		this.setStyle("-fx-background-color: " + (idNumber % 2 == 0 ? "Gainsboro" : "WhiteSmoke"));
	}

}