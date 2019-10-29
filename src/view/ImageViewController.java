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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import service.HeatEquationService;
import service.ImageService;
import service.PrewittService;
import service.SobelService;

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
    private String sliderValueFormat;
    private SelectedImage selectedImage;
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
    private ProgressIndicator progressIndicator;
    @FXML
    private Slider slider;
    @FXML
    private Label sliderLevelLabel;
    @FXML
    private Label sliderTitle;
    @FXML
    private Button sliderButton;
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hideShowSliderInfos(false);
        progressIndicator.setVisible(false);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            sliderLevelLabel.setText(String.format(sliderValueFormat, newValue));
            resetSourceImage();
        });
    }

    public String getSliderValueFormat() {
        if(sliderValueFormat == null || sliderValueFormat.equals("")){
            sliderValueFormat = "%.2f";
        }
        return sliderValueFormat;
    }

    public void setSliderValueFormat(String sliderValueFormat) {
        this.sliderValueFormat = sliderValueFormat;
    }
    
    public void hideShowSliderInfos(boolean state) {
        slider.setVisible(state);
        sliderLevelLabel.setVisible(state);
        sliderButton.setVisible(state);
        sliderTitle.setVisible(state);
    }

    public ImageView getImageSource() {
        return imageSource;
    }

    public SelectedImage getSelectedImage() {
        if(selectedImage == null){
            selectedImage = new SelectedImage();
        }
        return selectedImage;
    }

    public void setSelectedImage(SelectedImage selectedImage) {
        this.selectedImage = selectedImage;
    }
   
    public void setImageSource(){
        setSelectedImage(imageService.openFile(getSelectedImage(), (Stage) previewLabel.getScene().getWindow()));
        imageSrcToImageView();
    }

    public void imageSrcToImageView() {
        if(getSelectedImage().getFile() != null){
            resetApp();
            imageSource.setImage(new Image(getSelectedImage().getFile().toURI().toString(), 360, 290, false, false));
        }
    }
    
    public void resetApp(){
        if(getSelectedImage().getFile() != null){
            imageResult.setImage(null);
            previewLabel.setText("");
            imageService.getResultFile().delete();
            imageSource.setImage(null);
            hideShowSliderInfos(false);
            warningLabel.setText("");
        }
    }
    
    public void exitApp(){
        Platform.exit();
        System.exit(0);
        resetApp();
    }
    
    public void sobel() throws IOException{
        try {
            if(imageSourceIsSetted()){
                resetSourceImage();
                setSliderValueFormat("%.2f");
                sliderTitle.setText("Seuil");
                slider.setMin(0.0f);
                slider.setMax(1.0f);
                hideShowSliderInfos(true);
                warningLabel.setText("Doctrine classique: Sobel");
                sliderButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            setImageResult(sobelService.sobel(selectedImage, slider.getValue()), "Sobel");
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

    public void resetSourceImage() {
        selectedImage.setBufferedImage(SwingFXUtils.fromFXImage(imageSource.getImage(), null));
    }
    
    public void  sobelIy() throws IOException{
        try {
            if(imageSourceIsSetted()){
                warningLabel.setText("Doctrine classique: derivée Sobel suivant Y");
                hideShowSliderInfos(false);
                resetSourceImage();
                setImageResult(sobelService.sobelIY(selectedImage), "Sobel (Iy)");
            }
        } catch (Exception ex) {
            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sobelIx() throws IOException{
        try {
            if(imageSourceIsSetted()){
                warningLabel.setText("Doctrine classique: derivée Sobel suivant X");
                hideShowSliderInfos(false);
                resetSourceImage();
                setImageResult(sobelService.sobelIX(selectedImage), "Sobel (Ix)");
            }
        } catch (Exception ex) {
            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void prewitt() throws IOException{
        try {
            if(imageSourceIsSetted()){
                resetSourceImage();
                setSliderValueFormat("%.2f");
                sliderTitle.setText("Seuil");
                slider.setMin(0.0f);
                slider.setMax(1.0f);
                hideShowSliderInfos(true);
                warningLabel.setText("Doctrine classique: Prewitt");
                sliderButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            setImageResult(prewittService.prewitt(selectedImage, slider.getValue()), "Prewitt");
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
    
    public void  prewittIy() throws IOException{
        try {
            if(imageSourceIsSetted()){
                warningLabel.setText("Doctrine classique: derivée Prewitt suivant Y");
                hideShowSliderInfos(false);
                resetSourceImage();
                setImageResult(prewittService.prewittIY(selectedImage), "Prewitt (Iy)");
            }
        } catch (Exception ex) {
            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void prewittIx() throws IOException{
        try {
            warningLabel.setText("Doctrine classique: derivée Prewitt suivant X");
            if(imageSourceIsSetted()){
                hideShowSliderInfos(false);
                resetSourceImage();
                setImageResult(prewittService.prewittIX(selectedImage), "Prewitt (Ix)");
            }
        } catch (Exception ex) {
            Logger.getLogger(ImageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void heatFilter() throws IOException{
        try {
            if(imageSourceIsSetted()){
                setSliderValueFormat("%.0f");
                sliderTitle.setText("Iteration");
                slider.setMin(0);
                slider.setMax(100);
                hideShowSliderInfos(true);
                warningLabel.setText("Analyse Multi-échelle: Fonction de la chaleur");
                sliderLevelLabel.setText("0");
                sliderButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            resetSourceImage();
                            setImageResult(heatEquationService.run(selectedImage, (int)slider.getValue()), "Fonction de la chaleur avec "+sliderLevelLabel.getText()+" itération");
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
    
    // Set Image To privew
    public void setImageResult(boolean res, String processName) {
        if (res) {
            imageResult.setImage(new Image(imageService.getResultFile().toURI().toString(), 360, 290, false, false));
            previewLabel.setText("Aperçu: "+processName);
            previewLabel.setStyle("-fx-font-weight: bold");
        }else{
            System.out.println("Some Error is occured!");
        }
    }
    
    public boolean imageSourceIsSetted() throws Exception{
        if(imageSource.getImage() == null){
            warningLabel.setText("Aucune image n'est sélectionnée");
            return false;
        }
        return true;
    }
    
    
    public void handleOnDragOver(final DragEvent event){
        mouseDragOver(event);
    }
    
    public void handleOnDragDropped(final DragEvent event) {
        mouseDragDropped(event);
    }
    
    public void handleOnDragExited(final DragEvent event) {
        sourceHBox.setStyle("-fx-border-color: #C6C6C6;" +
                            "-fx-background-color:  #d1e0d5;");
    }
    
    private void mouseDragDropped(final DragEvent e) {
        final Dragboard db = e.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            // Only get the first file from the list
            final File file = db.getFiles().get(0);
            Platform.runLater(() -> {
                System.out.println(file.getAbsolutePath());
                setSelectedImage(imageService.setSelectedImgInfo(file));
                imageSrcToImageView();
            });
        }
        e.setDropCompleted(success);
        e.consume();
    }
    
    private  void mouseDragOver(final DragEvent e) {
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
    
    public void saveResImage() throws IOException{
        if(imageResult.getImage() == null){
            warningLabel.setText("Aucun filtre n'est appliqué pour enregistrer vos tests.");
        }else{
            imageService.saveImage(SwingFXUtils.fromFXImage(imageResult.getImage(), null));
        }
    }
   
}
