/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author swiri
 */
public class MainSelectionAndCropService {

    private Group group;
    private ImageView mainImageView;
    private Image mainImage;
    private boolean areaSelected;
    private AreaSelection areaSelection = new AreaSelection();
//    private EventHandler<MouseEvent> onMousePressedEventHandler;
//    private EventHandler<MouseEvent> onMouseDraggedEventHandler;
//    private EventHandler<MouseEvent> onMouseReleasedEventHandler;

    public MainSelectionAndCropService() {
    }

    public MainSelectionAndCropService(Group group, ImageView mainImageView, Image mainImagen, boolean isAreaSelected) {
        this.group = group;
        this.mainImageView = mainImageView;
        this.mainImage = mainImage;
        this.areaSelected = isAreaSelected;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public ImageView getMainImageView() {
        return mainImageView;
    }

    public void setMainImageView(ImageView mainImageView) {
        this.mainImageView = mainImageView;
    }

    public Image getMainImage() {
        return mainImage;
    }

    public void setMainImage(Image mainImage) {
        this.mainImage = mainImage;
    }

    public boolean isAreaSelected() {
        return areaSelected;
    }

    public void setAreaSelected(boolean areaSelected) {
        this.areaSelected = areaSelected;
    }

    public Image cropImage(Bounds bounds, ImageView imageView) {

        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        parameters.setViewport(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), width, height));

        WritableImage wi = new WritableImage(width, height);
        Image croppedImage = imageView.snapshot(parameters, wi);

        //showCroppedImageNewStage(wi, croppedImage);
        return croppedImage;
    }

    public void showCroppedImageNewStage(WritableImage wi, Image croppedImage) {
        final Stage croppedImageStage = new Stage();
        croppedImageStage.setResizable(true);
        croppedImageStage.setTitle("Cropped Image");
        changeStageSizeImageDimensions(croppedImageStage, croppedImage);
        final BorderPane borderPane = new BorderPane();
        final MenuBar menuBar = new MenuBar();
        final Menu menu1 = new Menu("File");
        final MenuItem save = new MenuItem("Save");
        save.setOnAction(event -> saveCroppedImage(croppedImageStage, wi));
        menu1.getItems().add(save);
        menuBar.getMenus().add(menu1);
        borderPane.setTop(menuBar);
        borderPane.setCenter(new ImageView(croppedImage));
        final Scene scene = new Scene(borderPane);
        croppedImageStage.setScene(scene);
    }

    public void saveCroppedImage(Stage stage, WritableImage wi) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.setInitialFileName("cats.png");

        File file = fileChooser.showSaveDialog(stage);
        if (file == null) {
            return;
        }

        Runnable runnable = () -> {
            BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
            BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(),
                    bufImageARGB.getHeight(), BufferedImage.BITMASK);

            Graphics2D graphics = bufImageRGB.createGraphics();
            graphics.drawImage(bufImageARGB, 0, 0, null);

            try {
                ImageIO.write(bufImageRGB, "png", file);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                graphics.dispose();
                System.gc();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        stage.close();
    }

    public void clearSelection(Group group) {
        areaSelected = false;
        group.getChildren().remove(1, group.getChildren().size());
        if (group.getChildren().size() > 0) {
            areaSelection.removeEventsFromImageView();
        }
    }

    public void changeStageSizeImageDimensions(Stage stage, Image image) {
        if (image != null) {
            stage.setMinHeight(250);
            stage.setMinWidth(250);
            stage.setWidth(image.getWidth() + 4);
            stage.setHeight(image.getHeight() + 56);
        }
        stage.show();
    }

    public Image convertFileToImage(File imageFile) {
        Image image = null;
        try (FileInputStream fileInputStream = new FileInputStream(imageFile)) {
            image = new Image(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    private class AreaSelection {

        private Group group;

        private ResizableRectangle selectionRectangle = null;
        private double rectangleStartX;
        private double rectangleStartY;
        private Paint darkAreaColor = Color.color(0, 0, 0, 0.5);

//      group.getChildren().get(0) = imageView
        private void removeEventsFromImageView() {
            this.group = group;
            if (group != null) {
                ImageView imageView = (ImageView) this.group.getChildren().get(0);
                // group.getChildren().get(0) == mainImageView. We assume image view as base container layer
                if (mainImageView != null && mainImage != null) {
                    imageView.removeEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
                    imageView.removeEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
                    imageView.removeEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
                }
                selectionRectangle = null;
            }
        }

        private ResizableRectangle selectArea(Group group) {
            this.group = group;

            // group.getChildren().get(0) == mainImageView. We assume image view as base container layer
            if (mainImageView != null && mainImage != null) {
                this.group.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
                this.group.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
                this.group.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
            }

            return selectionRectangle;
        }

        EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
            if (event.isSecondaryButtonDown()) {
                return;
            }

            rectangleStartX = event.getX();
            rectangleStartY = event.getY();
            clearSelection(group);

            selectionRectangle = new ResizableRectangle(rectangleStartX, rectangleStartY, 0, 0, group, mainImage.getWidth(), mainImage.getHeight());

            darkenOutsideRectangle(selectionRectangle);
        };

        EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
            if (event.isSecondaryButtonDown()) {
                return;
            }

            double offsetX = event.getX() - rectangleStartX;
            double offsetY = event.getY() - rectangleStartY;

            if (offsetX > 0) {
                if (event.getX() > mainImage.getWidth()) {
                    selectionRectangle.setWidth(mainImage.getWidth() - rectangleStartX);
                } else {
                    selectionRectangle.setWidth(offsetX);
                }
            } else {
                if (event.getX() < 0) {
                    selectionRectangle.setX(0);
                } else {
                    selectionRectangle.setX(event.getX());
                }
                selectionRectangle.setWidth(rectangleStartX - selectionRectangle.getX());
            }

            if (offsetY > 0) {
                if (event.getY() > mainImage.getHeight()) {
                    selectionRectangle.setHeight(mainImage.getHeight() - rectangleStartY);
                } else {
                    selectionRectangle.setHeight(offsetY);
                }
            } else {
                if (event.getY() < 0) {
                    selectionRectangle.setY(0);
                } else {
                    selectionRectangle.setY(event.getY());
                }
                selectionRectangle.setHeight(rectangleStartY - selectionRectangle.getY());
            }

        };

        EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
            if (selectionRectangle != null) {
                areaSelected = true;
            }
        };

        private void darkenOutsideRectangle(Rectangle rectangle) {
            Rectangle darkAreaTop = new Rectangle(0, 0, darkAreaColor);
            Rectangle darkAreaLeft = new Rectangle(0, 0, darkAreaColor);
            Rectangle darkAreaRight = new Rectangle(0, 0, darkAreaColor);
            Rectangle darkAreaBottom = new Rectangle(0, 0, darkAreaColor);

            darkAreaTop.widthProperty().bind(mainImage.widthProperty());
            darkAreaTop.heightProperty().bind(rectangle.yProperty());

            darkAreaLeft.yProperty().bind(rectangle.yProperty());
            darkAreaLeft.widthProperty().bind(rectangle.xProperty());
            darkAreaLeft.heightProperty().bind(rectangle.heightProperty());

            darkAreaRight.xProperty().bind(rectangle.xProperty().add(rectangle.widthProperty()));
            darkAreaRight.yProperty().bind(rectangle.yProperty());
            darkAreaRight.widthProperty().bind(mainImage.widthProperty().subtract(
                    rectangle.xProperty().add(rectangle.widthProperty())));
            darkAreaRight.heightProperty().bind(rectangle.heightProperty());

            darkAreaBottom.yProperty().bind(rectangle.yProperty().add(rectangle.heightProperty()));
            darkAreaBottom.widthProperty().bind(mainImage.widthProperty());
            darkAreaBottom.heightProperty().bind(mainImage.heightProperty().subtract(
                    rectangle.yProperty().add(rectangle.heightProperty())));

            // adding dark area rectangles before the selectionRectangle. So it can't overlap rectangle
            group.getChildren().add(1, darkAreaTop);
            group.getChildren().add(1, darkAreaLeft);
            group.getChildren().add(1, darkAreaBottom);
            group.getChildren().add(1, darkAreaRight);

            // make dark area container layer as well
            darkAreaTop.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            darkAreaTop.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            darkAreaTop.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

            darkAreaLeft.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            darkAreaLeft.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            darkAreaLeft.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

            darkAreaRight.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            darkAreaRight.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            darkAreaRight.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

            darkAreaBottom.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            darkAreaBottom.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            darkAreaBottom.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
        }
    }

    public void selectArea() {
//        clearSelection(group);
//        if(group.getChildren().size() > 1) group.getChildren().remove(1, group.getChildren().size());
//        
//        ResizableRectangle selectionRectangle = new ResizableRectangle(mainImage.getWidth()/2, mainImage.getHeight()/2
//                ,0, 0, group, mainImage.getWidth(), mainImage.getHeight());
//        areaSelection.group = this.group;
//        areaSelection.darkenOutsideRectangle(selectionRectangle);
        areaSelection.selectArea(this.group);
    }

    public Image cropSelected(Group selectionGroup) {
        if (areaSelected) {
            return cropImage(areaSelection.selectArea(selectionGroup).getBoundsInParent(), mainImageView);
        }
        return null;
    }

    public void centerImage(ImageView imageView) {
        Image img = imageView.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if (ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;
//            imageView.setX((imageView.getFitWidth() - w) / 2);
//            imageView.setY((imageView.getFitHeight() - h) / 2);
//            imageView.setX(200);
//            imageView.setY(175);
        }
    }

}
