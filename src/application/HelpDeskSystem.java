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

import java.io.File;
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
	private BorderPane createTicketLabelPane = new BorderPane();
	private ContextMenu toolbarMenu = new ContextMenu();
	private IconView iconImage;
	private Label createTicketLabel = new Label("Create New Ticket");
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
	private TextArea areaDescription = new TextArea();
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

		masterList = RequestIO.parseList(new File("tickets.csv"));

		//Creates the views on the stage
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

		//Creates and shows the stage
		primaryStage.setScene(scene);
		primaryStage.setResizable(true);
		primaryStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("icon.png")));
		primaryStage.setTitle("HelpDesk Request System");
		primaryStage.show();
		this.finishUp();
	}

	/**
	 * Creates the list of tickets from the array of ServiceRequests. If {@code newRow} is {@code true}, the list will
	 * recreate itself and the very first list item will flash green, indicating it is a newly created row
	 *
	 * @param itemList list of ServiceRequests to create the list from
	 * @param newRow   {@code true} if you are adding a new row to the list
	 */
	private void createList(ArrayList<ServiceRequest> itemList, boolean newRow) {
		int idx = 0;
		mainList.getChildren().clear();
		if (itemList.size() == 0)
			root.setCenter(textNoItems);
		else
			root.setCenter(mainList);
		for (ServiceRequest item : itemList) {

			ListRow listRow = new ListRow(item, idx);

			//When a user clicks a row, the row will expand.
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

		//Only used when the user is creating a new ticket. It makes the row turn green then fade to grey
		if (newRow) {
			TicketPane temp = (TicketPane) ((ListRow) mainList.getChildren().get(0)).getChildren().get(0);
			temp.setStyle("-fx-background-color: LightGreen;");
			RGBChanger changer = new RGBChanger(temp);
			SimpleAnimation newRowAnimation = new SimpleAnimation(changer, 750, 220);
			newRowAnimation.setOnFinished(e -> temp.setStyle("-fx-background-color: Gainsboro;"));
			newRowAnimation.play();
		}

	}

	/**
	 * Method to add a new row to the main list. This method really only serves to make the code clearer.
	 *
	 * @param request the new ServiceRequest being added to the list
	 */
	private void addRow(ServiceRequest request) throws IOException {
		masterList.add(0, request);
		this.createList(masterList, true);
		//Saves the list back to the CSV
		RequestIO.saveList(masterList, new File("tickets.csv"));
	}


	/**
	 * Sets the Id's of all the objects so that the .css file can properly apply styling. This method serves to
	 * clean up the main start method.
	 */
	private void setIds() {
		addTicketPane.setId("add-stack");
		areaDescription.setId("description-area");
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


	/**
	 * This is the method for the three buttons at the top: close, minimize, and restore
	 */
	private void setMainButtons() {
		buttonExit.setOnMouseClicked(e -> closeApplication());

		buttonWindowed.setOnMouseClicked(e -> this.primaryStage.setMaximized(!this.primaryStage.isMaximized()));

		buttonMinimize.setOnMouseClicked(e -> this.primaryStage.setIconified(true));
	}

	/**
	 * Used for basic stage listening. This method is mostly for cleaning up the code
	 */
	private void setStageListeners() {

		//For some reason I need this method, or else when the program is unminimized, nothing happens
		this.primaryStage.iconifiedProperty().addListener((e, t1, iconified) -> {
			if (!iconified) {
				root.setBottom(new Text());
			}
		});

		//Set so that the user can't maximize an already maximized stage, or cant restore an already restored stage
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

	/**
	 * This method creates the main toolbar with the logo and main buttons
	 *
	 * @throws IOException
	 */
	private void createMainToolBar() throws IOException {
		HBox.setHgrow(titleSpacer, Priority.ALWAYS);

		//Logo
		iconImage = new IconView("icon-small.png");

		//Adds the main buttons
		toolbarMain.getItems().addAll(buttonExit, buttonWindowed, buttonMinimize, titleSpacer, textTitle, iconImage);
		toolbarMain.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

		toolbarMain.setOnMousePressed(event -> {
			if (event.isPrimaryButtonDown()) {
				xOffset = this.primaryStage.getX() - event.getScreenX();
				yOffset = this.primaryStage.getY() - event.getScreenY();
			}
		});

		//Maximize on double click
		toolbarMain.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2)
				this.primaryStage.setMaximized(!this.primaryStage.isMaximized());
		});

		//Allows the stage to be moved when the user clicks and holds the bar
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

	/**
	 * Creates the context menu for the top toolbar. Allows user to close, maximize, minimize, and restore the window.
	 * Only shows up when right clicking
	 *
	 * @throws IOException if the image resources are not found
	 */
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

		//Stupid math I have to use since I'm not using a scrollpane. I need this for proper scrolling also since I
		// have expanding rows.
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

		//Resizes the scrollbar when maximizing
		topBarBox.heightProperty().addListener(e -> {
			mainScrollBar.setMaxHeight(visibleListHeight - 11);
			mainScrollBar.setMax(mainList.getHeight() - visibleListHeight);
			mainScrollBar.setVisibleAmount(visibleListHeight / masterList.size());
		});

		mainScrollBar.valueProperty().addListener((ov, oldVal, newVal) -> mainList.setTranslateY(-newVal.doubleValue
				()));
	}

	/**
	 * This method creates the panel that appears when you create a new ticket
	 */
	private void createTicketStack() {

		addTicketPane.setMaxHeight(40);
		addTicketPane.setMinHeight(40);
		addTicketPane.setAlignment(Pos.TOP_CENTER);
		addTicketPane.setPadding(new Insets(0, 10, 0, 10));
		createTicketLabelPane.setCenter(createTicketLabel);
		addTicketPane.getChildren().add(createTicketLabelPane);

		//This is where the user is able to write the description
		GridPane gp = new GridPane();
		ColumnConstraints colConstraint = new ColumnConstraints();
		colConstraint.setPercentWidth(100 / 2);
		gp.add(new Label("Date Created:   " + RequestIO.DATE_FORMATTER.format(new Date())), 0, 0);
		gp.add(new Label("Description:"), 1, 0);
		gp.add(areaDescription, 1, 1);
		gp.getColumnConstraints().addAll(colConstraint, colConstraint);

		//Starts off invisible and only appears when the user wishes to create a new ticket
		gp.setVisible(false);

		BorderPane saveTicket = new BorderPane();
		Label labelSaveTicket = new Label("Save Ticket");
		saveTicket.setCenter(labelSaveTicket);

		saveTicket.setId("save-ticket-pane");
		labelSaveTicket.setId("save-ticket-label");

		//When the user clicks the label, it expands so that the user can start typing
		labelSaveTicket.setOnMouseClicked(e -> {
			SimpleAnimation closeAddPaneAnimation = new SimpleAnimation(addTicketPane.minHeightProperty(), 100, 40);
			closeAddPaneAnimation.setOnFinished(ae -> {
				gp.setVisible(false);
				saveTicket.setVisible(false);
			});
			closeAddPaneAnimation.play();

			String desc = areaDescription.getText().trim();

			//Creates the new ticket from what is typed.
			ServiceRequest req = new ServiceRequest(new Date(), null, desc.length() != 0 ? desc : "No description",
					"Unassigned");
			try {
				this.addRow(req);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		saveTicket.setVisible(false);

		boolean isAddTicketOpen = false;

		//Closes the pane
		createTicketLabelPane.setOnMouseClicked(e -> {
			SimpleAnimation openAddPaneAnimation = new SimpleAnimation(addTicketPane.minHeightProperty(), 100, 200);
			openAddPaneAnimation.play();
			gp.setVisible(true);
			saveTicket.setVisible(true);
		});


		addTicketPane.getChildren().addAll(gp, saveTicket);

		topBarBox.getChildren().add(addTicketPane);
	}

	/**
	 * Creates the column names on for the list
	 * @throws IOException
	 */
	private void createColumnNames() throws IOException {

		ColumnNameBar pane = new ColumnNameBar("Date Created", "Status", "Date Completed", "Description", "Technician");

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

		//Helps adjust scrollbar height. Must be done after the stage is shown becuase the height isn't set until then.
		mainList.heightProperty().addListener(e -> {
			mainScrollBar.setMax(mainList.getHeight() - visibleListHeight);
			mainScrollBar.setVisibleAmount(mainList.getHeight() / masterList.size());
		});

		System.out.println(primaryStage.getWidth());
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