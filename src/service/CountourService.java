/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import bean.SelectedImage;

/**
 *
 * @author swiri
 */
public class CountourService {
    
    ImageService imageService = new ImageService();
    private double[][] deriveIx;
    private double[][] deriveIy;

    public CountourService() {
    }

    public CountourService(double[][] deriveIx, double[][] deriveIy) {
        this.deriveIx = deriveIx;
        this.deriveIy = deriveIy;
    }

    public double[][] getDeriveIx() {
        return deriveIx;
    }

    public void setDeriveIx(double[][] deriveIx) {
        this.deriveIx = deriveIx;
    }

    public double[][] getDeriveIy() {
        return deriveIy;
    }

    public void setDeriveIy(double[][] deriveIy) {
        this.deriveIy = deriveIy;
    }
   
    public boolean deriveResult(SelectedImage selectedImage, double scale) throws IOException{
        BufferedImage copy = imageService.duplicateSrcBufferedImage(selectedImage.getBufferedImage());
        int width = copy.getWidth();
        int height = copy.getHeight();
        double[][] edgeColors = new double[width - 2][height - 2];

        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                double[][] neighborPixel = {
                {imageService.getGrayScale(copy.getRGB(i - 1, j - 1)), imageService.getGrayScale(copy.getRGB(i - 1, j)), imageService.getGrayScale(copy.getRGB(i - 1, j + 1))},
                {imageService.getGrayScale(copy.getRGB(i, j - 1)), imageService.getGrayScale(copy.getRGB(i, j)), imageService.getGrayScale(copy.getRGB(i, j + 1))},
                {imageService.getGrayScale(copy.getRGB(i + 1, j - 1)), imageService.getGrayScale(copy.getRGB(i + 1, j)), imageService.getGrayScale(copy.getRGB(i + 1, j + 1))}
            };

                double gx =  filterDeriveIx(neighborPixel);

                double gy =  filterDeriveIy(neighborPixel);

                int g = (int) Math.sqrt((gx * gx) + (gy * gy));

                edgeColors[i -1][j - 1] = g;
            }
        }

        return saveResImage(scale, edgeColors, selectedImage.getBufferedImage());
    }
    
    public boolean deriveIX(SelectedImage selectedImage) throws IOException{
        double[][] edgeColors = calculateIx(selectedImage.getBufferedImage());

        return saveResImage(1.0d, edgeColors, selectedImage.getBufferedImage());
    }
    
    public boolean deriveIY(SelectedImage selectedImage) throws IOException{
        double[][] edgeColors = calculateIy(selectedImage.getBufferedImage());

        return saveResImage(1.0d, edgeColors, selectedImage.getBufferedImage());
    }

    public double[][] calculateIy(BufferedImage imageSource) {
        BufferedImage copy = imageService.duplicateSrcBufferedImage(imageSource);
        int width = copy.getWidth();
        int height = copy.getHeight();
        double[][] edgeColors = new double[width - 2][height - 2];
        System.out.println("In calculate Iy");
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                double[][] neighborPixel = {
                    {imageService.getGrayScale(copy.getRGB(i - 1, j - 1)), imageService.getGrayScale(copy.getRGB(i - 1, j)), imageService.getGrayScale(copy.getRGB(i - 1, j + 1))},
                    {imageService.getGrayScale(copy.getRGB(i, j - 1)), imageService.getGrayScale(copy.getRGB(i, j)), imageService.getGrayScale(copy.getRGB(i, j + 1))},
                    {imageService.getGrayScale(copy.getRGB(i + 1, j - 1)), imageService.getGrayScale(copy.getRGB(i + 1, j)), imageService.getGrayScale(copy.getRGB(i + 1, j + 1))}
                };
                
                double gy =  filterDeriveIy(neighborPixel);
                edgeColors[i - 1][j - 1] = gy;
                System.out.println("Iy = "+gy);
            }
        }
        return edgeColors;
    }

    public double[][] calculateIx(BufferedImage imageSource) {
        BufferedImage copy = imageService.duplicateSrcBufferedImage(imageSource);
        int width = copy.getWidth();
        int height = copy.getHeight();
        double[][] edgeColors = new double[width - 2][height - 2];
        System.out.println("In calculate Iy");
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                double[][] neighborPixel = {
                    {imageService.getGrayScale(copy.getRGB(i - 1, j - 1)), imageService.getGrayScale(copy.getRGB(i - 1, j)), imageService.getGrayScale(copy.getRGB(i - 1, j + 1))},
                    {imageService.getGrayScale(copy.getRGB(i, j - 1)), imageService.getGrayScale(copy.getRGB(i, j)), imageService.getGrayScale(copy.getRGB(i, j + 1))},
                    {imageService.getGrayScale(copy.getRGB(i + 1, j - 1)), imageService.getGrayScale(copy.getRGB(i + 1, j)), imageService.getGrayScale(copy.getRGB(i + 1, j + 1))}
                };
                double gx = filterDeriveIx(neighborPixel);
                edgeColors[i - 1][j - 1] = gx;
                System.out.println("Ix = "+gx);
            }
        }
        return edgeColors;
    }
    
    public double filterDeriveIy(double[][] neighborPixel) {
        double gy = (((getDeriveIy()[0][0] * neighborPixel[0][0]) + (getDeriveIy()[0][1] * neighborPixel[0][1]) + (getDeriveIy()[0][2] * neighborPixel[0][2]))
                + ((getDeriveIy()[1][0] * neighborPixel[1][0]) + (getDeriveIy()[1][1] * neighborPixel[1][1]) + (getDeriveIy()[1][2] * neighborPixel[1][2]))
                + ((getDeriveIy()[2][0] * neighborPixel[2][0]) + (getDeriveIy()[2][1] * neighborPixel[2][1]) + (getDeriveIy()[2][2] * neighborPixel[2][2])));
        return gy;
    }

    public double filterDeriveIx(double[][] neighborPixel) {
        double gx = (((getDeriveIx()[0][0] * neighborPixel[0][0]) + (getDeriveIx()[0][1] * neighborPixel[0][1]) + (getDeriveIx()[0][2] * neighborPixel[0][2]))
                + ((getDeriveIx()[1][0] * neighborPixel[1][0]) + (getDeriveIx()[1][1] * neighborPixel[1][1]) + (getDeriveIx()[1][2] * neighborPixel[1][2]))
                + ((getDeriveIx()[2][0] * neighborPixel[2][0]) + (getDeriveIx()[2][1] * neighborPixel[2][1]) + (getDeriveIx()[2][2] * neighborPixel[2][2])));
        return gx;
    }
    
    public boolean saveResImage(double scale, double[][] edgeColors, BufferedImage image)
            throws IOException {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int edgeColor = (int) edgeColors[i][j];
                edgeColor = (int)(edgeColor * scale);
                edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;

                image.setRGB(i, j, edgeColor);
            }
        }
        File outputfile = new File("output/result.png");
        return ImageIO.write(image, "png", outputfile);
    }
}
