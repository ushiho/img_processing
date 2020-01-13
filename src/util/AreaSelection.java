/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author swiri
 */
public class AreaSelection {

    private HBox hBox;
    private ImageView imageView;
    private Image mainImage;
    private boolean isAreaSelected;
    private ResizableRectangle selectionRectangle = null;
    private double rectangleStartX;
    private double rectangleStartY;
    private Paint darkAreaColor = Color.color(0, 0, 0, 0.5);


    public ResizableRectangle selectArea(HBox group) {
        System.out.println("In areaSelect class");
        this.hBox = group;

        // group.getChildren().get(0) == imageView. We assume image view as base container layer
        if (imageView != null && mainImage != null) {
            this.hBox.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            this.hBox.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            this.hBox.getChildren().get(0).addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
        }

        return selectionRectangle;
    }

    EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
        if (event.isSecondaryButtonDown()) {
            return;
        }

        rectangleStartX = event.getX();
        rectangleStartY = event.getY();

        clearSelection(hBox);

        selectionRectangle = new ResizableRectangle(rectangleStartX, rectangleStartY, 0, 0, hBox);

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
            isAreaSelected = true;
        }
    };

    public void darkenOutsideRectangle(Rectangle rectangle) {
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
        hBox.getChildren().add(1, darkAreaTop);
        hBox.getChildren().add(1, darkAreaLeft);
        hBox.getChildren().add(1, darkAreaBottom);
        hBox.getChildren().add(1, darkAreaRight);

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

    public void clearSelection(HBox group) {
        //deletes everything except for base container layer
        isAreaSelected = false;
        group.getChildren().remove(1, group.getChildren().size());

    }

}
