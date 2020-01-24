/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import bean.SelectedImage;
import com.qoppa.pdf.PDFException;
import com.qoppa.pdf.dom.IPDFDocument;
import com.qoppa.pdfViewerFX.PDFViewer;
import java.io.FileNotFoundException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import service.AlvarezMorelService;
import service.EmssService;
import service.EmssServiceDiverge;
import service.GaussianBlurService;
import service.HeatEquationService;
import service.ImageService;
import service.PrewittService;
import service.SobelService;
import service.ZoomService;
import service.JFXToast;
import service.MalikPeronaService;
import service.OsherRudinService;
import service.SapiroService;
import util.MainSelectionAndCropService;

/*import javafx.stage.Stage;
*
 * FXML Controller class
 *
 * @author swiri
 */
public class ImageViewController implements Initializable {

    SobelService sobelService = new SobelService();
    PrewittService prewittService = new PrewittService();
    ImageService imageService = new ImageService();
    HeatEquationService heatEquationService = new HeatEquationService();
    GaussianBlurService gaussianBlurService = new GaussianBlurService();
    EmssServiceDiverge emssService = new EmssServiceDiverge();
    MalikPeronaService malikPeronaService = new MalikPeronaService();
    ZoomService zoomService = new ZoomService();
    EmssService emss = new EmssService();
    AlvarezMorelService alvarezMorelService = new AlvarezMorelService();
    OsherRudinService osherRudinService = new OsherRudinService();
    SapiroService sapiroService = new SapiroService();

    MainSelectionAndCropService mainSelectionAndCropService = new MainSelectionAndCropService();
    private String sliderValueFormat;
    private SelectedImage selectedImage;
    protected int width = 0;
    protected int height = 0;
    private static boolean isAreaSelected = false;
    ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();
    private static final int MIN_PIXELS = 10;
    @FXML
    private Pane parentPane;
    @FXML
    private ImageView imageSource;
    @FXML
    private ImageView imageResult;
    @FXML
    private Label previewLabel;
    @FXML
    private Label warningLabel;
    @FXML
    private HBox sourceHBox;
    @FXML
    private Slider seuilSlider;
    @FXML
    private Label seuilLevel;
    @FXML
    private Label seuilLabel;
    @FXML
    private TextField iteration;
    @FXML
    private Label iterationLabel;
    @FXML
    private Button applyFilterButton;
    @FXML
    private Pane sourceInfoPane;
    @FXML
    private Label srcName;
    @FXML
    private Label srcSize;
    @FXML
    private Label srcWidth;
    @FXML
    private Label srcHeight;
    @FXML
    private Group sourceGroup;
    @FXML
    private MenuItem cropItem;
    @FXML
    private MenuItem selectItem;
//    @FXML
//    private ProgressIndicator progressIndicator;
//    @FXML
//    private Label zoomSrcValue;
//    @FXML
//    private Slider zoomSrcSlider;
//    @FXML
//    private Pane zoomSrcPane;
//    @FXML
//    private Label zoomResValue;
//    @FXML
//    private Slider zoomResSlider;
//    @FXML
//    private Pane zoomResPane;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hideShowSeuilInfos(false);
        hideShowIterationInfos(false);
        applyFilterButton.setVisible(false);
        sourceInfoPane.setVisible(false);
        seuilSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            seuilLevel.setText(String.format(sliderValueFormat, newValue));
            applyFilterButton.setDisable(false);
            resetSourceImage();
        });
        iteration.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (newValue.equals("")) {
                    applyFilterButton.setDisable(true);
                }
                if (!newValue.equals("0") && !newValue.equals(oldValue)) {
                    applyFilterButton.setDisable(false);
                }
            }
        });
//        zoomSrcPane.setVisible(false);
//        zoomResPane.setVisible(false);
        imageViewEventsListener(imageSource);
        imageViewEventsListener(imageResult);

    }

    public void imageViewEventsListener(ImageView imageView) {
//        zoom.valueProperty().addListener(e->{
//            double zoomlvl = zoom.getValue();
//            double newValue = (double)((int)(zoomlvl*10))/10;
//            double offSetX = width/2;
//            double offSetY = height/2;
//            zoomLabel.setText("x"+newValue);
//            if(offSetX<(width/newValue)/2) {
//                offSetX = (width/newValue)/2;
//            }
//            if(offSetX>width-((width/newValue)/2)) {
//                offSetX = width-((width/newValue)/2);
//            }
//            if(offSetY<(height/newValue)/2) {
//                offSetY = (height/newValue)/2;
//            }
//            if(offSetY>height-((height/newValue)/2)) {
//                offSetY = height-((height/newValue)/2);
//            }
//            imageView.setViewport(new Rectangle2D(offSetX-((width/newValue)/2), offSetY-((height/newValue)/2), width/newValue, height/newValue));
//        });

        imageView.setOnMousePressed(e -> {
            if (imageView.getViewport() != null) {
                Point2D mousePress = zoomService.imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));
                mouseDown.set(mousePress);
                imageView.setCursor(Cursor.CLOSED_HAND);
            }
        });

        imageView.setOnMouseReleased(e -> {
            if (imageView.getViewport() != null) {
                imageView.setCursor(Cursor.OPEN_HAND);
            }
        });

        imageView.setOnMouseDragged(e -> {
            if (imageView.getViewport() != null) {
                Point2D dragPoint = zoomService.imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));
                zoomService.shift(imageView, dragPoint.subtract(mouseDown.get()));
                mouseDown.set(zoomService.imageViewToImage(imageView, new Point2D(e.getX(), e.getY())));
            }
        });

        imageView.setOnScroll(e -> {
            if (imageView.getImage() != null) {
                if (imageView.getViewport() == null) {
                    imageView.setViewport(new Rectangle2D(imageView.getImage().getWidth(), imageView.getImage().getHeight(),
                             imageView.getImage().getWidth(), imageView.getImage().getHeight()));
                }
                double delta = e.getDeltaY();
                Rectangle2D viewport = imageView.getViewport();
                double scale = zoomService.clamp(Math.pow(1.01, delta),
                        Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),
                        Math.max(width / viewport.getWidth(), height / viewport.getHeight())
                );

                Point2D mouse = zoomService.imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));

                double newWidth = viewport.getWidth() * scale;
                double newHeight = viewport.getHeight() * scale;
                double newMinX = zoomService.clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale,
                        0, width - newWidth);
                double newMinY = zoomService.clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale,
                        0, height - newHeight);

                imageView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
                //            zoomLabel.setText("x"+scale);
                System.out.println("scale = " + scale);
            }
        });

        imageView.setOnMouseClicked(e -> {
            if (imageView.getViewport() != null && e.getClickCount() == 2) {
                zoomService.reset(imageView, width, height);
//                zoomLabel.setText("x1");
//                zoom.setValue(1);
            }
        });
    }

    public String getSliderValueFormat() {
        if (sliderValueFormat == null || sliderValueFormat.equals("")) {
            sliderValueFormat = "%.2f";
        }
        return sliderValueFormat;
    }

    public void setSliderValueFormat(String sliderValueFormat) {
        this.sliderValueFormat = sliderValueFormat;
    }

    public void hideShowSeuilInfos(boolean state) {
        seuilSlider.setVisible(state);
        seuilLevel.setVisible(state);
        seuilLabel.setVisible(state);
    }

    public void hideShowIterationInfos(boolean state) {
        iteration.setVisible(state);
        iterationLabel.setVisible(state);
    }

    public ImageView getImageSource() {
        return imageSource;
    }

    public SelectedImage getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(SelectedImage selectedImage) {
        this.selectedImage = selectedImage;
    }

    public void setImageSource() throws FileNotFoundException {
        setSelectedImage(imageService.openFile(getSelectedImage(), (Stage) previewLabel.getScene().getWindow()));
        imageSrcToImageView();
    }

    public void imageSrcToImageView() throws FileNotFoundException {
        if (selectedImage != null && selectedImage.getFile() != null) {
            File file = selectedImage.getFile();
            resetApp();
            calculateWidthAndHeight(new Image(file.toURI().toString()));
            Image source = new Image(file.toURI().toString(), width, height, false, false);
            imageSource.setImage(source);
//            zoomSrcPane.setVisible(true);
            sourceInfoPane.setVisible(true);
            resetSrcInfo();
            srcHeight.setText(source.getHeight() + " px");
            srcWidth.setText(source.getWidth() + " px");
            srcName.setText(file.getName());
            srcSize.setText(file.length() / 1024 + " Kb");
            mainSelectionAndCropService.setGroup(sourceGroup);
            mainSelectionAndCropService.setAreaSelected(isAreaSelected);
            mainSelectionAndCropService.setMainImageView(imageSource);
            mainSelectionAndCropService.setMainImage(imageSource.getImage());
        }
    }

    public void calculateWidthAndHeight(Image source) {
        ImageView image = new ImageView(source);
        double ratio = source.getWidth() / source.getHeight();

        if (400 / ratio < 400) {
            width = 400;
            height = (int) (400 / ratio);
        } else if (350 * ratio < 350) {
            height = 350;
            width = (int) (350 * ratio);
        } else {
            height = 350;
            width = 400;
        }
    }

    public void resetApp() {
        if (getSelectedImage().getFile() != null) {
            clearSelection();
            imageResult.setImage(null);
            previewLabel.setText("");
            imageService.getResultFile().delete();
            imageSource.setImage(null);
            hideShowSeuilInfos(false);
            hideShowIterationInfos(false);
            applyFilterButton.setVisible(false);
            warningLabel.setText("");
            iteration.setText("");
            resetSrcInfo();
            sourceInfoPane.setVisible(false);
//            zoomSrcPane.setVisible(false);
//            zoomResPane.setVisible(false);
        }
    }

    public void resetSrcInfo() {
        srcHeight.setText("");
        srcName.setText("");
        srcSize.setText("");
        srcWidth.setText("");
    }

    public void exitApp() {
        Platform.exit();
        System.exit(0);
        resetApp();
    }

    // Set Image To privew
    public void setImageResult(boolean res, String processName) {
        if (res) {
            imageResult.setImage(new Image(imageService.getResultFile().toURI().toString(), width, height, false, false));
            previewLabel.setText("Aperçu: " + processName);
            previewLabel.setStyle("-fx-font-weight: bold");
//            zoomResPane.setVisible(true);
        } else {
            previewLabel.setText("Some Error is occured!");
        }
    }

    public boolean imageSourceIsSetted() throws Exception {
        if (imageSource.getImage() == null) {
            warningLabel.setText("Aucune image n'est sélectionnée");
            return false;
        }
        return true;
    }

    public void handleOnDragOver(final DragEvent event) {
        mouseDragOver(event);
    }

    public void handleOnDragDropped(final DragEvent event) {
        mouseDragDropped(event);
    }

    public void handleOnDragExited(final DragEvent event) {
        sourceHBox.setStyle("-fx-border-color: #C6C6C6;"
                + "-fx-background-color:  #d1e0d5;");
    }

    private void mouseDragDropped(final DragEvent e) {
        final Dragboard db = e.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            // Only get the first file from the list
            final File file = db.getFiles().get(0);
            Platform.runLater(() -> {
                setSelectedImage(imageService.setSelectedImgInfo(file));
                try {
                    imageSrcToImageView();
                    calculateWidthAndHeight(imageSource.getImage());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        e.setDropCompleted(success);
        e.consume();
    }

    private void mouseDragOver(final DragEvent e) {
        final Dragboard db = e.getDragboard();

        final boolean isAccepted = db.getFiles().get(0).getName().toLowerCase().endsWith(".png")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpeg")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpg");

        if (db.hasFiles()) {
            if (isAccepted) {
                sourceHBox.setStyle("-fx-border-color: red;"
                        + "-fx-border-width: 5;"
                        + "-fx-background-color: #C6C6C6;"
                        + "-fx-border-style: solid;");
                e.acceptTransferModes(TransferMode.COPY);
            }
        } else {
            e.consume();
        }
    }

    public void saveResImage() throws IOException {
        if (imageResult.getImage() == null) {
            warningLabel.setText("Aucun filtre n'est appliqué pour enregistrer vos tests.");
        } else {
            imageService.saveImage(imageResult, (Stage) previewLabel.getScene().getWindow());
        }
    }

    // Begin Filter Functions 
    public void sobel() throws IOException {
        try {
            if (imageSourceIsSetted()) {
                Stage stage = (Stage) imageSource.getScene().getWindow();
                resetSourceImage();
                setSliderValueFormat("%.2f");
                seuilSlider.setValue(0.0);
                hideShowSeuilInfos(true);
                hideShowIterationInfos(false);
                applyFilterButton.setVisible(true);
                applyFilterButton.setDisable(false);
                warningLabel.setText("Doctrine classique: Sobel");
                applyFilterButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            setImageResult(sobelService.sobel(selectedImage, seuilSlider.getValue()), "Sobel");
                            JFXToast.makeText(stage, "Traitement Terminé", JFXToast.LONG, JFXToast.CENTTER);
                        } catch (IOException ex) {
                            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        applyFilterButton.setDisable(true);
                    }
                });
            }
        } catch (Exception ex) {
            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void resetSourceImage() {
        if(selectedImage != null)
        selectedImage.setBufferedImage(SwingFXUtils.fromFXImage(imageSource.getImage(), null));
    }

    public void sobelIy() throws IOException {
        try {
            if (imageSourceIsSetted()) {
                Stage stage = (Stage) imageSource.getScene().getWindow();
                warningLabel.setText("Doctrine classique: derivée Sobel suivant Y");
                hideShowSeuilInfos(false);
                resetSourceImage();
                setImageResult(sobelService.sobelIY(selectedImage), "Sobel (Iy)");
                applyFilterButton.setVisible(false);
                JFXToast.makeText(stage, "Traitement Terminé", JFXToast.LONG, JFXToast.CENTTER);
            }
        } catch (Exception ex) {
            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sobelIx() throws IOException {
        try {
            if (imageSourceIsSetted()) {
                Stage stage = (Stage) imageSource.getScene().getWindow();
                warningLabel.setText("Doctrine classique: derivée Sobel suivant X");
                hideShowSeuilInfos(false);
                resetSourceImage();
                setImageResult(sobelService.sobelIX(selectedImage), "Sobel (Ix)");
                applyFilterButton.setVisible(false);
                JFXToast.makeText(stage, "Traitement Terminé", JFXToast.LONG, JFXToast.CENTTER);
            }
        } catch (Exception ex) {
            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void prewitt() throws IOException {
        try {
            if (imageSourceIsSetted()) {
                Stage stage = (Stage) imageSource.getScene().getWindow();
                resetSourceImage();
                setSliderValueFormat("%.2f");
                seuilSlider.setValue(0.0);
                hideShowSeuilInfos(true);
                hideShowIterationInfos(false);
                applyFilterButton.setVisible(true);
                applyFilterButton.setDisable(false);
                warningLabel.setText("Doctrine classique: Prewitt");
                applyFilterButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            setImageResult(prewittService.prewitt(selectedImage, seuilSlider.getValue()), "Prewitt");
                            applyFilterButton.setDisable(true);
                            JFXToast.makeText(stage, "Traitement Terminé", JFXToast.LONG, JFXToast.CENTTER);
                        } catch (IOException ex) {
                            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                });
            }
        } catch (Exception ex) {
            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void prewittIy() throws IOException {
        try {
            if (imageSourceIsSetted()) {
                Stage stage = (Stage) imageSource.getScene().getWindow();
                warningLabel.setText("Doctrine classique: derivée Prewitt suivant Y");
                hideShowSeuilInfos(false);
                resetSourceImage();
                setImageResult(prewittService.prewittIY(selectedImage), "Prewitt (Iy)");
                applyFilterButton.setVisible(false);
                JFXToast.makeText(stage, "Traitement Terminé", JFXToast.LONG, JFXToast.CENTTER);
            }
        } catch (Exception ex) {
            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void prewittIx() throws IOException {
        try {
            Stage stage = (Stage) imageSource.getScene().getWindow();
            warningLabel.setText("Doctrine classique: derivée Prewitt suivant X");
            if (imageSourceIsSetted()) {
                hideShowSeuilInfos(false);
                resetSourceImage();
                setImageResult(prewittService.prewittIX(selectedImage), "Prewitt (Ix)");
                applyFilterButton.setVisible(false);
                JFXToast.makeText(stage, "Traitement Terminé", JFXToast.LONG, JFXToast.CENTTER);
            }
        } catch (Exception ex) {
            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void heatFilter() throws IOException {
        try {
            if (imageSourceIsSetted()) {
                Stage stage = (Stage) imageSource.getScene().getWindow();
                hideShowSeuilInfos(false);
                hideShowIterationInfos(true);
                applyFilterButton.setVisible(true);
                applyFilterButton.setDisable(false);
                iteration.setText("");
                removeFocusOnTextField();
                warningLabel.setText("Analyse Multi-échelle: Fonction de la chaleur");
                applyFilterButton.setOnAction(handleHealthEvent(stage));
                iteration.setOnAction(handleHealthEvent(stage));
//                parentPane.setCursor(Cursor.DEFAULT);
            }
        } catch (Exception ex) {
            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EventHandler<ActionEvent> handleHealthEvent(Stage stage) {
        parentPane.setCursor(Cursor.WAIT);
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!iteration.getText().equals("")) {
                        resetSourceImage();
                        setImageResult(heatEquationService.run(selectedImage, Integer.parseInt(iteration.getText())), "Fonction de chaleur avec " + iteration.getText() + " itération");
                        applyFilterButton.setDisable(true);
                        JFXToast.makeText(stage, "Traitement Terminé", JFXToast.LONG, JFXToast.CENTTER);
                    } else {
                        JFXToast.makeText(stage, "Veuillez donner le nombre d'itération.", JFXToast.LONG, JFXToast.CENTTER);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
    }

    public void gaussianBlur() throws IOException {
        try {
            if (imageSourceIsSetted()) {
                Stage stage = (Stage) imageSource.getScene().getWindow();
                warningLabel.setText("Doctrine classique: Gaussian blur");
                hideShowSeuilInfos(false);
                resetSourceImage();
                setImageResult(gaussianBlurService.applyBlur(selectedImage), "Gaussian blur");
            }
        } catch (Exception ex) {
            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void emssFilter() throws IOException {
        try {
            if (imageSourceIsSetted()) {
                Stage stage = (Stage) imageSource.getScene().getWindow();
                hideShowSeuilInfos(false);
                hideShowIterationInfos(true);
                applyFilterButton.setVisible(true);
                iteration.setText("");
                removeFocusOnTextField();
                warningLabel.setText("Analyse Multi-échelle: EMSS");
                applyFilterButton.setOnAction(handleEMSSEvent(stage));
                iteration.setOnAction(handleEMSSEvent(stage));
            }
        } catch (Exception ex) {
            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EventHandler<ActionEvent> handleEMSSEvent(Stage stage) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!iteration.getText().equals("")) {
                        resetSourceImage();
                        setImageResult(emss.run(selectedImage, Integer.parseInt(iteration.getText())), "EMSS avec " + iteration.getText() + " itérations");
                        applyFilterButton.setDisable(true);
                        JFXToast.makeText(stage, "Traitement Terminé", JFXToast.LONG, JFXToast.CENTTER);
                    } else {
                        JFXToast.makeText(stage, "Veuillez donner le nombre d'itération.", JFXToast.LONG, JFXToast.CENTTER);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
    }

    public void removeFocusOnTextField() {
        Scene scene = iteration.getScene();
        scene.setOnMousePressed(event -> {
            if (!iteration.equals(event.getSource())) {
                iteration.getParent().requestFocus();
            }
        });
    }

    public void malikPeronaFilter() throws IOException {
        try {
            if (imageSourceIsSetted()) {
                Stage stage = (Stage) imageSource.getScene().getWindow();
                hideShowSeuilInfos(false);
                hideShowIterationInfos(true);
                applyFilterButton.setVisible(true);
                iteration.setText("");
                warningLabel.setText("Diffusion Anistrope: Malik & Perona");
                removeFocusOnTextField();
                applyFilterButton.setOnAction(handleMalikPeronaEvent(stage));
                iteration.setOnAction(handleMalikPeronaEvent(stage));
            }
        } catch (Exception ex) {
            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EventHandler<ActionEvent> handleMalikPeronaEvent(Stage stage) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!iteration.getText().equals("")) {
                        resetSourceImage();
                        setImageResult(malikPeronaService.run(selectedImage, Integer.parseInt(iteration.getText())), "Malik & Perona avec " + iteration.getText() + " itérations");
                        applyFilterButton.setDisable(true);
                        JFXToast.makeText(stage, "Traitement Terminé", JFXToast.LONG, JFXToast.CENTTER);
                    } else {
                        JFXToast.makeText(stage, "Veuillez donner le nombre d'itération.", JFXToast.LONG, JFXToast.CENTTER);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
    }

    public void alvarezMorelFilter() {
        try {
            if (imageSourceIsSetted()) {
                Stage stage = (Stage) imageSource.getScene().getWindow();
                hideShowSeuilInfos(false);
                hideShowIterationInfos(true);
                applyFilterButton.setVisible(true);
                iteration.setText("");
                removeFocusOnTextField();
                warningLabel.setText("Analyse Multi-échelle: Alvarez-Morel");
                applyFilterButton.setOnAction(handleAlvarezMorelEvent(stage));
                iteration.setOnAction(handleAlvarezMorelEvent(stage));
            }
        } catch (Exception ex) {
            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EventHandler<ActionEvent> handleAlvarezMorelEvent(Stage stage) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!iteration.getText().equals("")) {
                        resetSourceImage();
                        setImageResult(alvarezMorelService.run(selectedImage, Integer.parseInt(iteration.getText())), "Alvarez-Morel avec " + iteration.getText() + " itérations");
                        applyFilterButton.setDisable(true);
                        JFXToast.makeText(stage, "Traitement Terminé", JFXToast.LONG, JFXToast.CENTTER);
                    } else {
                        JFXToast.makeText(stage, "Veuillez donner le nombre d'itération.", JFXToast.LONG, JFXToast.CENTTER);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
    }

    public void osherRudinFilter() {
        try {
            if (imageSourceIsSetted()) {
                Stage stage = (Stage) imageSource.getScene().getWindow();
                hideShowSeuilInfos(false);
                hideShowIterationInfos(true);
                applyFilterButton.setVisible(true);
                iteration.setText("");
                removeFocusOnTextField();
                warningLabel.setText("Analyse Multi-échelle: Osher-Rudin");
                applyFilterButton.setOnAction(handleOsherRudinEvent(stage));
                iteration.setOnAction(handleOsherRudinEvent(stage));
            }
        } catch (Exception ex) {
            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EventHandler<ActionEvent> handleOsherRudinEvent(Stage stage) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!iteration.getText().equals("")) {
                        resetSourceImage();
                        setImageResult(osherRudinService.run(selectedImage, Integer.parseInt(iteration.getText()), stage), "Osher-Rudin avec " + iteration.getText() + " itérations");
                        applyFilterButton.setDisable(true);
                        JFXToast.makeText(stage, "Traitement Terminé", JFXToast.LONG, JFXToast.CENTTER);
//                        progressIndicator.setVisible(false);
                    } else {
                        JFXToast.makeText(stage, "Veuillez donner le nombre d'itération.", 500, JFXToast.CENTTER);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
    }

    public void sapiroFilter() {
        try {
            if (imageSourceIsSetted()) {
                Stage stage = (Stage) imageSource.getScene().getWindow();
                hideShowSeuilInfos(false);
                hideShowIterationInfos(true);
                applyFilterButton.setVisible(true);
                iteration.setText("");
                removeFocusOnTextField();
                warningLabel.setText("Cas Couleur: Sapiro");
                applyFilterButton.setOnAction(handleSapiroEvent(stage));
                iteration.setOnAction(handleSapiroEvent(stage));
            }
        } catch (Exception ex) {
            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EventHandler<ActionEvent> handleSapiroEvent(Stage stage) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!iteration.getText().equals("")) {
                        resetSourceImage();
                        setImageResult(sapiroService.run(selectedImage, Integer.parseInt(iteration.getText())), "Sapiro avec " + iteration.getText() + " itérations");
                        applyFilterButton.setDisable(true);
                        JFXToast.makeText(stage, "Traitement Terminé", JFXToast.LONG, JFXToast.CENTTER);
                    } else {
                        JFXToast.makeText(stage, "Veuillez donner le nombre d'itération.", 500, JFXToast.CENTTER);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
    }

    public void selectArea() {
        if (imageSource != null && imageSource.getImage() != null) {
            mainSelectionAndCropService.selectArea();
        }
    }

    public void crop() {
        Image croppedImage = mainSelectionAndCropService.cropSelected(sourceGroup);
        if (croppedImage != null) {
            imageSource.setImage(croppedImage);
            srcHeight.setText("" + croppedImage.getHeight());
            srcWidth.setText("" + croppedImage.getWidth());
//            mainSelectionAndCropService.centerImage(imageSource);
            clearSelection();
        }
    }

    public void clearSelection() {
        if (imageSource != null && imageSource.getImage() != null) {
            mainSelectionAndCropService.clearSelection(sourceGroup);
        }
    }

    public void restoreImage() {
        if (getSelectedImage() != null) {
            Image source = new Image(getSelectedImage().getFile().toURI().toString(), width, height, false, false);
            getImageSource().setImage(source);
        }
    }

    public void rotateToLeft() {
        if (imageService != null) {
            Image rotatedImage = imageService.rotateImage(imageSource.getImage(), -90, width, height);
            imageSource.setImage(rotatedImage);
        }
    }

    public void rotateToRight() {
        if (imageService != null) {
            Image rotatedImage = imageService.rotateImage(imageSource.getImage(), 90, width, height);
            imageSource.setImage(rotatedImage);
        }
    }

    public void aboutApp() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/about.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("About");
        stage.setResizable(false);
        stage.show();
    }

    public void showCours() throws PDFException {
        String url = "/home/swiri/NetBeansProjects/Lambda/support/resume.pdf";
        Stage stage = new Stage();
        PDFViewer m_PDFViewer = new PDFViewer();
        m_PDFViewer.loadPDF(url);
        BorderPane borderPane = new BorderPane(m_PDFViewer);
        Scene scene = new Scene(borderPane);
        stage.setTitle("Lambda - Cours");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

}
