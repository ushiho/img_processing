/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author swiri
 */
public class JFXProgressIndicator {
    /**
     * Set a display time of long toast
     */
    public static final int LONG = 3500;
    /**
     * Set a display time of short toast
     */
    public static final int SHORT = 500;
    /**
     * Set the toast in the central high position of the reference stage
     */
    public static final int TOP = 1;
    /**
     * Set the toast in the low central position of the reference stage
     */
    public static final int BUTTON = 0;
    /**
     * Set the toast in the low central position of the reference stage
     */
    public static final int CENTTER = -1;

    private JFXProgressIndicator(){}

    /**
     * Create toast with maximum customization
     * @param ownerStage
     * @param toastMsg
     * @param toastDelay
     * @param position
     */
    public static void makeProgessIndicator(Stage ownerStage, double progress, int position) {

        int fadeInDelay = 500;
        int fadeOutDelay = 500;

        Stage progressStage = new Stage();
        progressStage.initOwner(ownerStage);
        progressStage.setResizable(false);
        progressStage.initStyle(StageStyle.TRANSPARENT);
        final ProgressIndicator pin = new ProgressIndicator(-1.0f);


//        Text text = new Text(toastMsg);
//        text.setFont(Font.font("Roboto", 15));
//        text.setFill(Color.LIGHTGRAY);

        final HBox hb = new HBox();
        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(pin);

        Scene scene = new Scene(hb);
        scene.setFill(Color.TRANSPARENT);
        progressStage.setScene(scene);
        progressStage.setWidth(20);
        progressStage.setHeight(20);

        //set position toast
        if (position == BUTTON) {
            progressStage.setY(ownerStage.getY() + (ownerStage.getHeight()) - 20);
            progressStage.setX(ownerStage.getX() + ((ownerStage.getWidth() / 2) - (progressStage.getWidth() / 2)));
        } else if(position == TOP) {
            progressStage.setY(ownerStage.getY() + 10);
            progressStage.setX(ownerStage.getX() + ((ownerStage.getWidth() / 2) - (progressStage.getWidth() / 2)));
        }else{
            progressStage.setY(ownerStage.getY() + ((ownerStage.getHeight()/ 2) - (progressStage.getHeight()/ 2)));
            progressStage.setX(ownerStage.getX() + ((ownerStage.getWidth() / 2) - (progressStage.getWidth() / 2)));
        }

        progressStage.show();
    }

    /**
     * Create a Toast at the bottom of the reference stage and with the following editable parameters
     * @param ownerStage
     * @param toastMsg
     * @param toastDelay
     */
    public static void makeText(Stage ownerStage, String toastMsg, int toastDelay){
        makeProgessIndicator(ownerStage, toastDelay, BUTTON);
    }

    /**
     * Create a Toast at the bottom of the reference stage, with maximum display time and with the following editable parameter
     * @param ownerStage
     * @param toastMsg
     */
    public static void makeText(Stage ownerStage, String toastMsg){
        makeProgessIndicator(ownerStage, LONG, BUTTON);
    }
}
