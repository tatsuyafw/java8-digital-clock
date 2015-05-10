package common;

import java.time.LocalDateTime;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    LocalDateTime datetime;

    public Main() {
        datetime = LocalDateTime.now();
    }

    @Override
    public void start(Stage stage) {
        Label label = new Label(datetime.toString());
        label.setFont(new Font(50));

        Timeline timer = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
            label.setText(LocalDateTime.now().toString());
        }));

        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

        stage.setScene(new Scene(label, 600, 100));
        stage.setTitle("Hello");
        stage.show();
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
