/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lambda;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.ZoomService;
import view.ImageViewController;

/**
 *
 * @author swiri
 */
public class Main extends Application {

    static ImageViewController imageViewController;
    static ZoomService zoomService;
    private static final int MIN_PIXELS = 10;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ImageView.fxml"));
        Parent root = loader.load();
//        imageViewController = (ImageViewController) loader.getController();
//        ImageView imageView = imageViewController.getImageSource();
//        if(imageView.getImage() != null){
//            System.out.println("in main.java");
//            listenEvents(imageView);
//        }
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Lambda Editor");
        stage.setResizable(false);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

//    public void listenEvents(ImageView imageView){
//        ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();
//         double width = imageView.getImage().getWidth();
//        double height = imageView.getImage().getHeight();
//        imageView.setOnMousePressed(e -> {
//            
//            Point2D mousePress = zoomService.imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));
//            mouseDown.set(mousePress);
//        });
//
//        imageView.setOnMouseDragged(e -> {
//            Point2D dragPoint = zoomService.imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));
//            zoomService.shift(imageView, dragPoint.subtract(mouseDown.get()));
//            mouseDown.set(zoomService.imageViewToImage(imageView, new Point2D(e.getX(), e.getY())));
//        });
//
//        imageView.setOnScroll(e -> {
//            double delta = e.getDeltaY();
//            Rectangle2D viewport = imageView.getViewport();
//
//            double scale = zoomService.clamp(Math.pow(1.01, delta),
//
//                // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
//                Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),
//
//                // don't scale so that we're bigger than image dimensions:
//                Math.max(width / viewport.getWidth(), height / viewport.getHeight())
//
//            );
//
//            Point2D mouse = zoomService.imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));
//
//            double newWidth = viewport.getWidth() * scale;
//            double newHeight = viewport.getHeight() * scale;
//
//            // To keep the visual point under the mouse from moving, we need
//            // (x - newViewportMinX) / (x - currentViewportMinX) = scale
//            // where x is the mouse X coordinate in the image
//
//            // solving this for newViewportMinX gives
//
//            // newViewportMinX = x - (x - currentViewportMinX) * scale 
//
//            // we then clamp this value so the image never scrolls out
//            // of the imageview:
//
//            double newMinX = zoomService.clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale, 
//                    0, width - newWidth);
//            double newMinY = zoomService.clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale, 
//                    0, height - newHeight);
//
//            imageView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
//        });
//
//        imageView.setOnMouseClicked(e -> {
//            if (e.getClickCount() == 2) {
//            zoomService.reset(imageView, width, height);
//        }});
//
//    }
}
