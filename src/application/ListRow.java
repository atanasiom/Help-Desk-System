package application;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Custom list rows for the main list. This was much easier than trying to do it any other way.
 */
public class ListRow extends VBox {

	private boolean isOpen = false;
	private int idNumber;

	private ServiceRequest request;

	private Label expandedDescLabel;

	private DescriptionPane descPane;
	private StatusBox statusBox;
	private TicketPane ticketPane;


	/**
	 * Creates the list row
	 *
	 * @param request  the service request that the row is using
	 * @param idNumber id of the row
	 */
	public ListRow(ServiceRequest request, int idNumber) {
		super();

		this.request = request;
		this.idNumber = idNumber;
		this.ticketPane = new TicketPane(request, idNumber);
		this.statusBox = ticketPane.getStatusBox();
		this.descPane = new DescriptionPane(request.getDescription());
		this.expandedDescLabel = descPane.getLabel();

		this.getChildren().addAll(this.ticketPane, this.descPane);
	}

	/**
	 * Returns {@code true} if the row is open
	 *
	 * @return {@code true} if the row is open
	 */
	public boolean isOpen() {
		return this.isOpen;
	}

	/**
	 * Opens or closes the row.
	 *
	 * @param open {@code true} if you want the row to open, or {@code false} if you want it to close
	 */
	public void setOpen(boolean open) {
		if (open) {
			//Sets the color of the row
			ticketPane.setStyle("-fx-background-color: LightGrey");
			statusBox.setStyle("-fx-background-color: LightGrey");
			//Sets the row to be visible
			expandedDescLabel.setVisible(true);
			//Animates opening
			new SimpleAnimation(descPane.minHeightProperty(), 100, 200).play();
			this.isOpen = true;
		} else {
			//Changes the color back to normal
			statusBox.setStyle("-fx-background-color: " + (idNumber % 2 == 0 ? "Gainsboro" : "WhiteSmoke"));
			ticketPane.setStyle("-fx-background-color: " + (idNumber % 2 == 0 ? "Gainsboro" : "WhiteSmoke"));
			//Animates closing
			SimpleAnimation anim = new SimpleAnimation(descPane.minHeightProperty(), 100, 0);
			anim.setOnFinished(e -> expandedDescLabel.setVisible(false));
			anim.play();
			this.isOpen = false;
		}
	}
}