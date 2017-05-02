package application;

import javafx.scene.control.ComboBox;

/**
 * This is the box that shows the status of the tickets
 */
public class StatusBox extends ComboBox<String> {

	/**
	 * Creates a new box
	 *
	 * @param request  the ServiceRequest
	 * @param idNumber id of the request
	 */
	public StatusBox(ServiceRequest request, int idNumber) {
		super();
		this.getItems().addAll("Open", "Closed");
		this.getSelectionModel().select(request.getServiceStatus() == ServiceStatus.OPEN ? 0 : 1);
		this.setId("ticket-status");
		//Makes it the correct color
		this.setStyle("-fx-background-color: " + (idNumber % 2 == 0 ? "Gainsboro" : "WhiteSmoke"));
	}

}