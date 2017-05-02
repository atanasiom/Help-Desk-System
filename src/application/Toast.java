package application;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Creates the popup toast message when you save a new ticket
 */
public final class Toast {

	private static final int OFFSET = 7;

	public static void makeText(Stage ownerStage, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay) {
		//Creates a new stage
		Stage toastStage = new Stage();
		toastStage.initOwner(ownerStage);
		toastStage.setResizable(false);
		toastStage.initStyle(StageStyle.TRANSPARENT);

		//Message to be displayed
		Label text = new Label(toastMsg);

		StackPane root = new StackPane(text);
		root.setOpacity(0);

		Scene scene = new Scene(root);
		scene.getStylesheets().addAll(ClassLoader.getSystemResource("toast.css").toExternalForm());
		scene.setFill(Color.TRANSPARENT);
		toastStage.setScene(scene);
		toastStage.setHeight(50);

		//Sets location and allowws the toast to move with the stage
		ownerStage.xProperty().addListener(e -> toastStage.setX(ownerStage.getX() + ownerStage.getWidth() / 2 - toastStage.getWidth() / 2));
		ownerStage.yProperty().addListener(e -> toastStage.setY(ownerStage.getY() + ownerStage.getHeight() - toastStage.getHeight() - OFFSET));

		//Shows the toast
		toastStage.show();

		//Sets the location of the
		toastStage.setX(ownerStage.getX() + ownerStage.getWidth() / 2 - toastStage.getWidth() / 2);
		toastStage.setY(ownerStage.getY() + ownerStage.getHeight() - toastStage.getHeight() - OFFSET);

		//Animates the showing of the toast
		Timeline fadeInTimeline = new Timeline();
		KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 1));
		fadeInTimeline.getKeyFrames().add(fadeInKey1);
		fadeInTimeline.setOnFinished((ae) -> {
			new Thread(() -> {
				try {
					Thread.sleep(toastDelay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Timeline fadeOutTimeline = new Timeline();
				KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 0));
				fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
				fadeOutTimeline.setOnFinished(e -> toastStage.close());
				fadeOutTimeline.play();
			}).start();
		});
		fadeInTimeline.play();
	}
}