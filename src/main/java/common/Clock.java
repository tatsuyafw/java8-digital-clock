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
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.MethodInfo;
import model.MethodStore;

public class Clock extends Application implements Initializable {
    private static final int CLOCK_RENDERING_DELAY = 500; // msec
    private static final String CLOCK_TITLE = "Digital clock";
    private static final String LAYOUT_FILE = "layout.fxml";
    private static final String STYLESHEET_FILE = "style.css";

    private static final String LOCATED_ROOT_PACKAGE = "java";
    @FXML private TextField query;
    @FXML private Label clockLabel;
    @FXML private ListView<MethodInfo> searchResult;

    private DateTimeFormatter formatter;
    private ObservableList<MethodInfo> masterData = FXCollections.observableArrayList();
    private FilteredList<MethodInfo> filteredResult;

    public Clock() {
        formatter = DateTimeFormatter.ofPattern("HH:mm ss");
    }

    private ChangeListener<String> onQueryChanged() {
        /**
         * See: http://code.makery.ch/blog/javafx-2-event-handlers-and-change-listeners/
         */
        return (property, oldQeury, newQuery) -> {
            filteredResult.setPredicate(methodInfo -> {
                String methodName = methodInfo.getMethodName();
                // TODO: 大文字小文字を区別するかどうかを設定できるようにする
                return methodName.toLowerCase().contains(newQuery.toLowerCase());
            });
        };
    }

    private EventHandler<? super MouseEvent> onClockClicked() {
        return event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    System.out.println("Double clicked");
                    String clockColor = clockLabel.getStyle().replace("-fx-opacity: ", "");
                    if (clockColor.equals("") || clockColor.equals("0.0")) {
                        clockLabel.setStyle("-fx-opacity: 1.0");
                    } else {
                        clockLabel.setStyle("-fx-opacity: 0.0");
                    }
                    System.out.println(clockLabel.getStyle());
                }
            }
        };
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        MethodStore methodStore = new MethodStore();

        //masterData = FXCollections.observableArrayList(ClassFinder.getClasses(LOCATED_ROOT_PACKAGE));
        masterData = FXCollections.observableArrayList(methodStore.search(""));

        // 時計の設定
        clockLabel.setText(formattedDate());
        Timeline timer = new Timeline(new KeyFrame(Duration.millis(CLOCK_RENDERING_DELAY), event -> {
            clockLabel.setText(formattedDate());
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

        clockLabel.setOnMouseClicked(onClockClicked());

        // 検索
        filteredResult = new FilteredList<>(masterData, p -> true);
        query.textProperty().addListener(onQueryChanged());

        SortedList<MethodInfo> sortedResult = new SortedList<>(filteredResult, (methodInfo1, methodInfo2) -> {
            return methodInfo1.toString().compareTo(methodInfo2.toString());
        });
        searchResult.setItems(sortedResult);
    };

    @Override
    public void start(Stage stage) {
        FXMLLoader loader = null;
        Parent root = null;
        try {
            loader = new FXMLLoader(getClass().getResource("/" + LAYOUT_FILE));
            loader.setController(new Clock());
            root = (Parent) loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        Scene scene = new Scene(root);
        String style = getClass().getResource("/" + STYLESHEET_FILE).toExternalForm();
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
