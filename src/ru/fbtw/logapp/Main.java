package ru.fbtw.logapp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Date;

public class Main extends Application {
    public static final int WIDTH = 900;
    public static final int HEIGHT = 600;

    private static int itemCount = 0;

    private  ArrayList<EventItem> events;
    private  ObservableList<GridPane> eventItems;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();

        events = new ArrayList<>();
        eventItems = FXCollections.observableList(new ArrayList<>());
    }



    @Override
    public void start(Stage primaryStage) throws Exception {


        ListView<GridPane> eventsList = new ListView<>(eventItems);
        eventsList.setMaxHeight(Double.MAX_VALUE);
        eventsList.setOnMouseClicked(event -> {
            if(eventItems.size()>0){
                int i = eventItems.indexOf(eventsList.getSelectionModel().getSelectedItem());

                showEvent(events.get(i));
            }
        });

        Label eventColumnHeader = new Label("События");
        eventColumnHeader.setFont(Font.font("Arial", FontWeight.BOLD,30));
        eventColumnHeader.setMaxWidth(Double.MAX_VALUE);
        eventColumnHeader.setAlignment(Pos.CENTER);

        VBox eventColumn = new VBox(20);
        eventColumn.setMaxWidth(WIDTH/3);

        VBox.setVgrow(eventsList, Priority.ALWAYS);
        VBox.setVgrow(eventColumnHeader, Priority.ALWAYS);

        eventColumn.getChildren().addAll(eventColumnHeader,eventsList);


        Label eventBuilderHeader = new Label("Регистрация нового события");
        eventBuilderHeader.setFont(Font.font("Arial", FontWeight.BOLD,40));
        eventBuilderHeader.setAlignment(Pos.CENTER);

        Label eventNameLabel = new Label("Название события:");
        eventNameLabel.setFont(Font.font("Arial",20));

        TextField eventNameInput = new TextField();

        Label eventDescriptionLabel = new Label("Описание события:");
        eventDescriptionLabel.setFont(Font.font("Arial",20));
        eventDescriptionLabel.setMaxWidth(Double.MAX_VALUE);
        eventDescriptionLabel.setAlignment(Pos.CENTER);

        TextArea eventDescriptionInput = new TextArea();
        eventDescriptionInput.setMaxHeight(Double.MAX_VALUE);

        Button pushButton  = new Button("Создать событие");
        pushButton.setOnAction((event -> {

            String name = eventNameInput.getText();
            if(name==null||name.equals("")){
                name = "событие";
            }

            String description  = eventDescriptionInput.getText();
            if(description==null||description.equals("")){
                description = "";
            }

            String time = new Date().toString();

            EventItem item = new EventItem(name,description,time);
            events.add(item);
          //  eventLayouts.add(item.getLayout());
            eventItems.add(item.getLayout());

        }));

        GridPane eventBuilderLayout = new GridPane();
        eventBuilderLayout.getColumnConstraints().add(0,new ColumnConstraints(WIDTH*2/9));
        eventBuilderLayout.getColumnConstraints().add(1,new ColumnConstraints(WIDTH*2/9));
        eventBuilderLayout.getColumnConstraints().add(2,new ColumnConstraints(WIDTH*2/9));
        eventBuilderLayout.getRowConstraints().add(0,new RowConstraints(HEIGHT/15));
        eventBuilderLayout.getRowConstraints().add(1,new RowConstraints(HEIGHT/15));
        eventBuilderLayout.getRowConstraints().add(2,new RowConstraints(HEIGHT/15));
        eventBuilderLayout.getRowConstraints().add(3,new RowConstraints(HEIGHT/15));
        eventBuilderLayout.getRowConstraints().add(4,new RowConstraints(HEIGHT*9/15));
        eventBuilderLayout.getRowConstraints().add(5,new RowConstraints(HEIGHT/15));
        eventBuilderLayout.getRowConstraints().add(6,new RowConstraints(HEIGHT/15));
        eventBuilderLayout.setAlignment(Pos.TOP_CENTER);
        eventBuilderLayout.setMaxWidth(Double.MAX_VALUE);

        eventBuilderLayout.add(eventBuilderHeader,0,0,3,1);
        eventBuilderLayout.add(eventNameLabel,0,2);
        eventBuilderLayout.add(eventNameInput,1,2,2,1);
        eventBuilderLayout.add(eventDescriptionLabel,0,3,3,1);
        eventBuilderLayout.add(eventDescriptionInput,0,4,3,1);
        eventBuilderLayout.add(pushButton,1,6);



        HBox mainLayout = new HBox(5);
        HBox.setHgrow(eventColumn,Priority.ALWAYS);
        HBox.setHgrow(eventBuilderLayout,Priority.ALWAYS);
        mainLayout.getChildren().addAll(eventColumn,eventBuilderLayout);

        Scene scene = new Scene(mainLayout,WIDTH,HEIGHT);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Окно регистрации событий");
        primaryStage.show();
    }


    private void showEvent(EventItem item){
        Stage mainStage = new Stage();

        Label name = new Label(item.getName());
        name.setFont(Font.font("Arial", FontWeight.BOLD,40));
        name.setAlignment(Pos.CENTER);
        name.setMaxWidth(Double.MAX_VALUE);

        Label description = new Label(item.getDescription());
        description.setFont(Font.font("Arial",20));
        description.setAlignment(Pos.CENTER);
        description.setMaxWidth(Double.MAX_VALUE);
        description.setMaxHeight(Double.MAX_VALUE);

        Label time = new Label("Время создания: "+item.getTime());
        time.setFont(Font.font("Arial",20));
        time.setAlignment(Pos.CENTER);
        time.setMaxWidth(Double.MAX_VALUE);

        Button cancelButton = new Button("Ok");
        cancelButton.setOnAction(event -> mainStage.close());

        VBox layout = new VBox(30);
        layout.setAlignment(Pos.CENTER);
        VBox.setVgrow(name,Priority.ALWAYS);
        layout.getChildren().add(name);
        VBox.setVgrow(description,Priority.ALWAYS);
        layout.getChildren().add(description);
        VBox.setVgrow(time,Priority.ALWAYS);
        layout.getChildren().add(time);
        layout.getChildren().add(cancelButton);

        Scene scene = new Scene(layout,WIDTH,HEIGHT);
        mainStage.setTitle(item.getName());
        mainStage.setScene(scene);
        mainStage.initModality(Modality.NONE);
        mainStage.show();
    }
}

class EventItem {
    private String name;
    private String description;
    private String time;

    private Label nameLabel;
    private Label descriptionLabel;
    private Label timeLabel;
    private GridPane layout;

    public EventItem(String name, String description, String time) {
        this.name = name;
        this.description = description;
        this.time = time;

        nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD,20));

        String descriptionShort;
        if(description.length()>15){
            descriptionShort = description.substring(0,15);
        }else{
            descriptionShort = description;
        }
        descriptionLabel = new Label(descriptionShort);

        timeLabel = new Label(time);
        layout = new GridPane();
        layout.setMaxWidth(Double.MAX_VALUE);
        layout.add(nameLabel,0,0,2,1);
        layout.add(descriptionLabel,0,1);
        layout.add(timeLabel,1,1);


    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public GridPane getLayout() {
        return layout;
    }
}

