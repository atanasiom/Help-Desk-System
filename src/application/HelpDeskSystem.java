package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class HelpDeskSystem extends Application {

	public static final int WIDTH = 1000, HEIGHT = 600;

	private int cellHeight;
	private double yOffset, xOffset;
	private double visibleListHeight;

	private ArrayList<ServiceRequest> masterList = new ArrayList<ServiceRequest>();

	private Stage primaryStage;

	private BorderPane root = new BorderPane();
	private ContextMenu toolbarMenu = new ContextMenu();
	private IconView iconImage;
	private Label createTicketLabel = new Label("Create new ticket");
	private MenuItem itemRestore = new MenuItem("Restore");
	private MenuItem itemMinimize = new MenuItem("Minimize");
	private MenuItem itemMaximize = new MenuItem("Maximize");
	private MenuItem itemExit = new MenuItem("Close");
	private Pane buttonExit = new Pane();
	private Pane buttonWindowed = new Pane();
	private Pane buttonMinimize = new Pane();
	private Pane titleSpacer = new Pane();
	private ScrollBar mainScrollBar = new ScrollBar();
	private Text textNoItems = new Text("No Help Requests Found!");
	private Text textTitle = new Text("Ticket System");
	private ToolBar toolbarMain = new ToolBar();
	private VBox addTicketPane = new VBox();
	private VBox mainList = new VBox();
	private VBox topBarBox = new VBox();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.initStyle(StageStyle.UNDECORATED);

		masterList = RequestIO.parseList(ClassLoader.getSystemResourceAsStream("tickets.csv"));

		this.setIds();
		this.setMainButtons();
		this.setStageListeners();
		this.createMainToolBar();
		this.createTicketStack();
		this.createColumnNames();
		this.createList(masterList, false);

		root.setRight(mainScrollBar);
		root.setTop(topBarBox);

		Scene scene = new Scene(root, WIDTH, HEIGHT);
		scene.getStylesheets().addAll(ClassLoader.getSystemResource("application.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.setResizable(true);
		primaryStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("icon.png")));
		primaryStage.setTitle("HelpDesk Request System");
		primaryStage.show();
		this.finishUp();
	}

	private void createList(ArrayList<ServiceRequest> itemList, boolean newRow) {
		int idx = 0;
		mainList.getChildren().clear();
		if (itemList.size() == 0)
			root.setCenter(textNoItems);
		else
			root.setCenter(mainList);
		for (ServiceRequest item : itemList) {

			ListRow listRow = new ListRow(item, idx);

			listRow.setOnMouseClicked(e -> {
				if (e.getButton() == MouseButton.PRIMARY) {
					boolean clickedDesc = "desc-pane".equals(e.getPickResult().getIntersectedNode().getId());
					if (!clickedDesc)
						listRow.setOpen(!listRow.isOpen());
					for (Node n : mainList.getChildren())
						if (((ListRow) n).isOpen() && (n != listRow))
							((ListRow) n).setOpen(false);
				} else if (e.getButton() == MouseButton.SECONDARY) {
					System.out.println("right");
				}
			});

			mainList.getChildren().add(listRow);
			idx++;
		}

		if (newRow) {
			TicketPane temp = (TicketPane) ((ListRow) mainList.getChildren().get(0)).getChildren().get(0);
			temp.setStyle("-fx-background-color: LightGreen;");
			RGBChanger changer = new RGBChanger(temp);
			SimpleAnimation newRowAnimation = new SimpleAnimation(changer, 750, 220);
			newRowAnimation.setOnFinished(e -> temp.setStyle("-fx-background-color: Gainsboro;"));
			newRowAnimation.play();
		}

	}

	private void addRow(ServiceRequest request) {
		masterList.add(0, request);
		this.createList(masterList, true);
	}


	private void setIds() {
		addTicketPane.setId("add-stack");
		buttonExit.setId("exit-button");
		buttonWindowed.setId("windowed-button");
		buttonMinimize.setId("minimize-button");
		createTicketLabel.setId("label-create-ticket");
		itemRestore.setId("item-restore");
		itemMinimize.setId("item-minimize");
		itemMaximize.setId("item-maximize");
		itemExit.setId("item-exit");
		mainScrollBar.setId("main-bar");
		textNoItems.setId("no-items-text");
		textTitle.setId("title-text");
		toolbarMain.setId("main-toolbar");
		toolbarMenu.setId("toolbar-menu");
	}

	private void setMainButtons() {
		buttonExit.setOnMouseClicked(e -> closeApplication());

		buttonWindowed.setOnMouseClicked(e -> this.primaryStage.setMaximized(!this.primaryStage.isMaximized()));

		buttonMinimize.setOnMouseClicked(e -> this.primaryStage.setIconified(true));
	}

	private void setStageListeners() {
		this.primaryStage.iconifiedProperty().addListener((e, t1, iconified) -> {
			if (!iconified) {
				root.setBottom(new Text());
			}
		});

		this.primaryStage.maximizedProperty().addListener((e, t1, maximized) -> {
			if (maximized) {
				itemRestore.setDisable(false);
				itemMaximize.setDisable(true);
			} else {
				itemRestore.setDisable(true);
				itemMaximize.setDisable(false);
			}
		});
	}

	private void createMainToolBar() throws IOException {
		HBox.setHgrow(titleSpacer, Priority.ALWAYS);

		iconImage = new IconView("icon-small.png");

		toolbarMain.getItems().addAll(buttonExit, buttonWindowed, buttonMinimize, titleSpacer, textTitle, iconImage);
		toolbarMain.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

		toolbarMain.setOnMousePressed(event -> {
			if (event.isPrimaryButtonDown()) {
				xOffset = this.primaryStage.getX() - event.getScreenX();
				yOffset = this.primaryStage.getY() - event.getScreenY();
			}
		});

		toolbarMain.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2)
				this.primaryStage.setMaximized(!this.primaryStage.isMaximized());
		});

		toolbarMain.setOnMouseDragged(event -> {
			if (event.isPrimaryButtonDown()) {
				if (this.primaryStage.isMaximized())
					this.primaryStage.setMaximized(false);
				this.primaryStage.setX(event.getScreenX() + xOffset);
				this.primaryStage.setY(event.getScreenY() + yOffset);
			}
		});

		toolbarMain.setMaxHeight(30);

		topBarBox.getChildren().add(toolbarMain);

		this.createMainToolBarContextMenu();
	}

	private void createMainToolBarContextMenu() throws IOException {
		itemRestore.setGraphic(new IconView("restore.png"));
		itemRestore.setDisable(true);
		itemRestore.setOnAction(e -> this.primaryStage.setMaximized(!this.primaryStage.isMaximized()));

		itemMinimize.setGraphic(new IconView("minimize.png"));
		itemMinimize.setOnAction(e -> this.primaryStage.setIconified(true));

		itemMaximize.setGraphic(new IconView("maximize.png"));
		itemMaximize.setOnAction(e -> this.primaryStage.setMaximized(!this.primaryStage.isMaximized()));

		itemExit.setGraphic(new IconView("close.png"));
		itemExit.setOnAction(e -> closeApplication());

		toolbarMenu.getItems().addAll(itemRestore, itemMinimize, itemMaximize, itemExit);
		toolbarMain.setContextMenu(toolbarMenu);
	}


	/**
	 * Sets the scrolling of the main list
	 */
	private void setScrolling() {

		mainScrollBar.setOrientation(Orientation.VERTICAL);
		mainScrollBar.setMin(0);
		mainScrollBar.setMax(mainList.getHeight() - visibleListHeight);
		mainScrollBar.setMaxHeight(visibleListHeight);
		mainScrollBar.setUnitIncrement(20);
		mainScrollBar.setBlockIncrement(20);
		mainScrollBar.setVisibleAmount(mainList.getHeight() / masterList.size());

		mainList.setOnScroll(e -> {
			double delta = e.getDeltaY() > 1 ? 40 : -40;
			double trans = mainList.getTranslateY() + delta;
			if (trans < visibleListHeight - mainList.getHeight()) {
				mainList.setTranslateY(visibleListHeight - mainList.getHeight());
				new SimpleAnimation(mainScrollBar.valueProperty(), 25, Math.abs(visibleListHeight - mainList.getHeight
						())).play();
			} else if (trans >= 0) {
				mainList.setTranslateY(0);
				new SimpleAnimation(mainScrollBar.valueProperty(), 25, 0).play();
			} else {
				new SimpleAnimation(mainList.translateYProperty(), 25, trans).play();
				new SimpleAnimation(mainScrollBar.valueProperty(), 25, Math.abs(trans)).play();
			}
		});

		topBarBox.heightProperty().addListener(e -> {
			mainScrollBar.setMaxHeight(visibleListHeight - 11);
			mainScrollBar.setMax(mainList.getHeight() - visibleListHeight);
			mainScrollBar.setVisibleAmount(visibleListHeight / masterList.size());
		});

		mainScrollBar.valueProperty().addListener((ov, oldVal, newVal) -> mainList.setTranslateY(-newVal.doubleValue
				()));
	}

	private void createTicketStack() {
		addTicketPane.setMaxHeight(50);
		addTicketPane.setMinHeight(50);
		addTicketPane.getChildren().add(createTicketLabel);
		addTicketPane.setAlignment(Pos.TOP_CENTER);
		addTicketPane.setPadding(new Insets(0, 10, 0, 10));

		GridPane gp = new GridPane();
		ColumnConstraints colConstraint = new ColumnConstraints();
		colConstraint.setPercentWidth(100 / 3);
		gp.add(new Label("Date Created:   " + RequestIO.DATE_FORMATTER.format(new Date())), 0, 0);
		gp.add(new Label("Technician"), 1, 0);
		gp.add(new Label("Description"), 2, 0);
		gp.add(new TextArea(), 2, 1);
		gp.getColumnConstraints().addAll(colConstraint, colConstraint, colConstraint);

		gp.setVisible(false);

		addTicketPane.setOnMouseClicked(e -> {
			SimpleAnimation openAddPaneAnimation = new SimpleAnimation(addTicketPane.minHeightProperty(), 100, 200);
			//openAddPaneAnimation.setOnFinished(ae -> gp.setVisible(true));
			openAddPaneAnimation.play();
			gp.setVisible(true);
			//ServiceRequest req = new ServiceRequest(new Date(), null, "New", "Nue");
			//this.addRow(req);
			Toast.makeText(primaryStage, "New Ticket Created!", 2000, 250, 500);
		});


		addTicketPane.getChildren().add(gp);

		topBarBox.getChildren().add(addTicketPane);
	}

	private void createColumnNames() throws IOException {

		ColumnNameBar pane = new ColumnNameBar("Date Created", "Status", "Date Completed", "Description",
				"Technician");

		pane.setId("column-pane");

		topBarBox.getChildren().add(pane);

	}


	/**
	 * Performs any necessary actions that must be done after the stage has been shown. Things that are affected by
	 * the heights of objects would be performed here, since until the stage is shown, their heights don't exist. For
	 * example, scroll functions must be done here since it relies on actual visible heights
	 */
	private void finishUp() {

		visibleListHeight = HEIGHT - topBarBox.getHeight();
		this.setScrolling();
		cellHeight = 40;

		topBarBox.heightProperty().addListener(e -> visibleListHeight = HEIGHT - topBarBox.getHeight());

		mainList.heightProperty().addListener(e -> {
			mainScrollBar.setMax(mainList.getHeight() - visibleListHeight);
			mainScrollBar.setVisibleAmount(mainList.getHeight() / masterList.size());
		});
	}

	/**
	 * Closes the application with a simple fadeout
	 */
	private void closeApplication() {
		SimpleAnimation closeAnimation = new SimpleAnimation(this.primaryStage.getScene().getRoot().opacityProperty(),
				150, 0);
		closeAnimation.setOnFinished(e -> this.primaryStage.close());
		closeAnimation.play();
	}

	public ArrayList<ServiceRequest> getMasterList() {
		return this.masterList;
	}

}