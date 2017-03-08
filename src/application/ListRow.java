package application;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ListRow extends VBox {

    private boolean isOpen = false;
    private int idNumber;

    private ServiceRequest request;
    private Stage ownerStage;

    private Label expandedDescLabel;

    private DescriptionPane descPane;
    private StatusBox statusBox;
    private TicketPane ticketPane;


    public ListRow(Stage ownerStage, ServiceRequest request, int idNumber) {
        super();
        this.ownerStage = ownerStage;
        this.request = request;
        this.idNumber = idNumber;
        this.ticketPane = new TicketPane(request, idNumber);
        this.statusBox = ticketPane.getStatusBox();
        this.descPane = new DescriptionPane(request.getDescription());
        this.expandedDescLabel = descPane.getLabel();

        this.getChildren().addAll(this.ticketPane, this.descPane);
    }

    public void setOpen(boolean open) {
        if (open) {
            ticketPane.setStyle("-fx-background-color: LightGrey");
            statusBox.setStyle("-fx-background-color: LightGrey");
            expandedDescLabel.setVisible(true);
            new SimpleAnimation(descPane.minHeightProperty(), 100, 200).play();
            this.isOpen = true;
        } else {
            statusBox.setStyle("-fx-background-color: " + (idNumber % 2 == 0 ? "Gainsboro" : "WhiteSmoke"));
            ticketPane.setStyle("-fx-background-color: " + (idNumber % 2 == 0 ? "Gainsboro" : "WhiteSmoke"));
            SimpleAnimation anim = new SimpleAnimation(descPane.minHeightProperty(), 100, 0);
            anim.setOnFinished(e -> expandedDescLabel.setVisible(false));
            anim.play();
            this.isOpen = false;
        }
    }

    public boolean isOpen() {
        return this.isOpen;
    }
}