package common;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Clock extends Application implements Initializable {
    private static final int CLOCK_RENDERING_DELAY = 500; // msec
    private static final String CLOCK_TITLE = "Digital clock";
    private static final String LAYOUT_FILE = "layout.fxml";
    private static final String STYLESHEET_FILE = "style.css";

    @FXML private TextField query;
    @FXML private Label clockLabel;
    @FXML private ListView<String> searchResult;

    private DateTimeFormatter formatter;
    private ObservableList<String> masterData = FXCollections.observableArrayList("item1", "item2", "item3");
    private FilteredList<String> filteredResult;

    public Clock() {
        formatter = DateTimeFormatter.ofPattern("HH:mm ss");
    }

    private ChangeListener<String> onQueryChanged() {
        /**
         * See: http://code.makery.ch/blog/javafx-2-event-handlers-and-change-listeners/
         */
        return (property, oldValue, newValue) -> {
            filteredResult.setPredicate(item -> {
                return item.contains(newValue);
            });
        };
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filteredResult = new FilteredList<>(masterData, p -> true);
        query.textProperty().addListener(onQueryChanged());

        searchResult.setItems(filteredResult);
        clockLabel.setText(formattedDate());
        Timeline timer = new Timeline(new KeyFrame(Duration.millis(CLOCK_RENDERING_DELAY), event -> {
            clockLabel.setText(formattedDate());
        }));

        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    };

    @Override
    public void start(Stage stage) {
        FXMLLoader loader = null;
        Parent root = null;
        try {
            loader = new FXMLLoader(getClass().getResource(LAYOUT_FILE));
            loader.setController(new Clock());
            root = (Parent) loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        Scene scene = new Scene(root);
        String style = getClass().getResource(STYLESHEET_FILE).toExternalForm();
        scene.getStylesheets().add(style);
        stage.setScene(scene);
        stage.setTitle(CLOCK_TITLE);
        stage.setResizable(false);
        stage.show();
    }

    public String formattedDate() {
        return formatter.format((LocalDateTime.now()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
