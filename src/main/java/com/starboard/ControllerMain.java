package com.starboard;//package sample;

import com.starboard.util.ConsoleColors;
import com.starboard.util.Prompt;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ControllerMain implements EventHandler<ActionEvent> {
    @FXML
    private Label gameTextAREA;
    @FXML
    TextArea gameTextArea;

    @FXML
    Button btnUserInput;

    @FXML
    Button btnNewGame;

    public void onButtonClicked() {
        System.out.println("Right Panel ok btn");
    }

    public void handleUserInput() {
        System.out.println("User Input BTN");
        gameTextArea.setText("User Input BTN");
    }

    @Override
    public void handle(ActionEvent event) {
//        btnUserInput.setOnAction(e -> gameTextArea.setText("Lambda User Input BTN"));

        if (event.getSource() == btnUserInput) {
            btnUserInput.setOnAction(e -> gameTextArea.setText("Lambda User Input BTN"));
            gameTextArea.setText("User Input BTN");
        } else if(
            event.getSource() == btnNewGame) {
            System.out.println("else");

        }
    }

    public void guiGameIntro(){
//        String str ="Itachi";
//
//        Text text = new Text();
//        VBox root = new VBox(text);
//        root.setAlignment(Pos.CENTER);
//        Scene scene = new Scene(root, 330, 120, Color.WHITE);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
//        final IntegerProperty i = new SimpleIntegerProperty(0);
//        Timeline timeline = new Timeline();
//        KeyFrame keyFrame = new KeyFrame(
//                Duration.seconds(1),
//                event -> {
//                    if (i.get() > str.length()) {
//                        timeline.stop();
//                    } else {
//                        text.setText(str.substring(0, i.get()));
//                        i.set(i.get() + 1);
//                    }
//                });
//        timeline.getKeyFrames().add(keyFrame);
//        timeline.setCycleCount(Animation.INDEFINITE);
//        timeline.play();
//    }


}
}
