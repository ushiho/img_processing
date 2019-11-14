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
    
    public BufferedImage duplicateSrcBufferedImage(BufferedImage input){
        int width = input.getWidth() + 2;
        int height = input.getHeight() + 2;
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        copyPixelsToCorners(output, input, width, height);
        copyPixelsToLastColumnLastRow(width, output, input, height);
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                output.setRGB(i, j, input.getRGB(i - 1, j -1));
            }
        }
        return output;
    }

    public void copyPixelsToLastColumnLastRow(int width, BufferedImage output, BufferedImage input, int height) {
        for (int i = 1; i < width - 1; i++) {
            output.setRGB(i, 0, input.getRGB(i - 1, 0));
            output.setRGB(i, height - 1, input.getRGB(i - 1, height - 3));
        }
        for (int j = 1; j < height - 1; j++) {
            output.setRGB(0, j, input.getRGB(0, j - 1));
            output.setRGB(width - 1, j, input.getRGB(width - 3, j - 1));
        }
    }

    public void copyPixelsToCorners(BufferedImage output, BufferedImage input, int width, int height) {
        output.setRGB(0, 0, input.getRGB(0, 0));
        output.setRGB(width - 1, 0, input.getRGB(width - 3, 0));
        output.setRGB(0, height - 1, input.getRGB(0, height - 3));
        output.setRGB(width -1, height - 1, input.getRGB(width - 3, height - 3));
    }
    
    public BufferedImage matrixToBufferedImage(int width, int height, double[][] edgeColors, BufferedImage image) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int edgeColor = (int)edgeColors[i][j];
                edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;

                image.setRGB(i, j, edgeColor);
            }
        }
        return image;
    }
    
    public double[][] copyGrayScaleFromImage(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        double[][] copy = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                copy[i][j] = getGrayScale(image.getRGB(i, j));
            }
        }
        return copy;
    }
    
    public boolean saveResImageToFile(double[][] edgeColors, BufferedImage image)
            throws IOException {
        
        image = matrixToBufferedImage(image.getWidth(), image.getHeight(), edgeColors, image);

        File outputfile = new File("output/result.png");
        return ImageIO.write(image, "png", outputfile);
    }
    
}
