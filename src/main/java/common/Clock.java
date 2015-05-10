package common;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Clock extends Application implements Initializable {
    private static int CLOCK_RENDERING_DELAY = 500; // msec
    private static String CLOCK_TITLE = "Digital clock";
    private DateTimeFormatter formatter;

    @FXML private Label clockLabel;

    public Clock() {
        formatter = DateTimeFormatter.ofPattern("HH:mm ss");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
            loader = new FXMLLoader(getClass().getResource("layout.fxml"));
            loader.setController(new Clock());
            root = (Parent) loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        stage.setTitle(CLOCK_TITLE);
        stage.setScene(new Scene(root, 600, 800));
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
