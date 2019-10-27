/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import bean.SelectedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author swiri
 */
public class ImageService {
    
    public SelectedImage openFile(SelectedImage selectedImage, Stage primaryStage){
        FileChooser fileChooser = new FileChooser();
        if(selectedImage != null && selectedImage.getFile() != null){
            File existDirectory = selectedImage.getFile().getParentFile();
            fileChooser.setInitialDirectory(existDirectory);
        }
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", ".jpeg"));
        File file = fileChooser.showOpenDialog(primaryStage);
        return setSelectedImgInfo(file);
    }
    
    public SelectedImage setSelectedImgInfo(File file){
        try {
            if (file != null) {
            SelectedImage selectedImage = new SelectedImage();
            BufferedImage image = ImageIO.read(file);
            selectedImage.setFile(file);
            selectedImage.setBufferedImage(image);
            selectedImage.setHeight(image.getHeight());
            selectedImage.setWidth(image.getWidth());
            return selectedImage;
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }
    
    // calculating luminance
    public int  getGrayScale(int rgb) {
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = (rgb) & 0xff;
        return (r + g + b) / 3;
    }
    
    public File getResultFile(){
        return new File("output/result.png");
    }
    
    public boolean saveImage(BufferedImage image) throws IOException{
        File outputFile = new File("save/"+(new SimpleDateFormat("yyyyMMddHHmm'.png'").format(new Date())));
        return ImageIO.write(image, "png", outputFile);
    }
    
}
