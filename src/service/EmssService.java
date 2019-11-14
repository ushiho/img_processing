/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.SelectedImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author swiri
 */
public class EmssService {
    ImageService imageService = new ImageService();
    HeatEquationService heatEquationService = new HeatEquationService();
    
    private final double dt = 0.25;
    double[][] Ixy = {{0.25, 0, 0.25}, {0, 0, 0}, {-0.25, 0, 0.25}};
    double[][] Ix =  {{0.1464466, 0.0, -0.1464466},{0.2071067, 0.0, -0.2071067},{0.1464466, 0.0, -0.1464466}};
    double[][] Iy =  {{0.1464466, 0.2071067, 0.1464466},{0.0, 0.0, 0.0},{-0.1464466, -0.2071067, -0.1464466}};
    
    CountourService countourService = new CountourService(Ix, Iy);
    public boolean run(SelectedImage selectedImage, int iteration) throws IOException{
        BufferedImage inputImage = selectedImage.getBufferedImage();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        double result[][] = imageService.copyGrayScaleFromImage(inputImage);
        if (iteration<=0) {
            return imageService.saveResImageToFile(imageService.copyGrayScaleFromImage(inputImage), inputImage);
        }
        for (int iter = 0; iter < iteration; iter++) {
            double res = 0.0;
            double deriveIxx[][] = heatEquationService.calculateIxx(imageService.matrixToBufferedImage(width, height, result, inputImage));
            double deriveIyy[][] = heatEquationService.calculateIyy(imageService.matrixToBufferedImage(width, height, result, inputImage));
            double deriveIxy[][] = calculateIxy(imageService.matrixToBufferedImage(width, height, result, inputImage));
            double deriveIx[][] = countourService.calculateIx(imageService.matrixToBufferedImage(width, height, result, inputImage));
            double deriveIy[][] = countourService.calculateIy(imageService.matrixToBufferedImage(width, height, result, inputImage));
            
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    res = (deriveIy[i][j] * deriveIy[i][j] * deriveIxx[i][j] - 2 * deriveIx[i][j] * deriveIy[i][j] * deriveIxy[i][j] + deriveIx[i][j] * deriveIx[i][j] * deriveIyy[i][j])
                            / (deriveIx[i][j] * deriveIx[i][j] + deriveIy[i][j] * deriveIy[i][j]);
                    //System.out.println("Ix = "+deriveIx[i][j]);
                    //System.out.println("Iy = "+deriveIy[i][j]);
                    //System.out.println("Ixx = "+deriveIxx[i][j]);
                    //System.out.println("Iyy = "+deriveIyy[i][j]);
                    //System.out.println("Ixy = "+deriveIxy[i][j]);
                    //System.out.println("Ancien result["+i+"]["+j+"] = "+result[i][j]);
                    result[i][j] += dt * res;
                    //System.out.println("Nouveau result["+i+"]["+j+"] = "+result[i][j]);
                }
            }
        }
        return imageService.saveResImageToFile(result, inputImage);
    }
    
    public double[][] calculateIxy(BufferedImage imageSource){
        BufferedImage copy = imageService.duplicateSrcBufferedImage(imageSource);
        int width = imageSource.getWidth();
        int height = imageSource.getHeight();
        double[][] Ixy = new double[width][height];
        for (int i = 1; i < width + 1; i++) {
            for (int j = 1; j < height + 1; j++) {
                int[][] neighborPixel = {
                {imageService.getGrayScale(copy.getRGB(i - 1, j - 1)), imageService.getGrayScale(copy.getRGB(i - 1, j)), imageService.getGrayScale(copy.getRGB(i - 1, j + 1))},
                {imageService.getGrayScale(copy.getRGB(i, j - 1)), imageService.getGrayScale(copy.getRGB(i, j)), imageService.getGrayScale(copy.getRGB(i, j + 1))},
                {imageService.getGrayScale(copy.getRGB(i + 1, j - 1)), imageService.getGrayScale(copy.getRGB(i + 1, j)), imageService.getGrayScale(copy.getRGB(i + 1, j + 1))}
                };
                double ixy = filterDeriveIxy(neighborPixel);
                Ixy[i - 1][j - 1] = ixy;
            }
        }
        return Ixy;
    }
    
    public double filterDeriveIxy(int[][] neighborPixel) {
        double ixy =  ((Ixy[0][0] * neighborPixel[0][0]) + (Ixy[0][1] * neighborPixel[0][1]) + (Ixy[0][2] * neighborPixel[0][2]))
                + ((Ixy[1][0] * neighborPixel[1][0]) + (Ixy[1][1] * neighborPixel[1][1]) + (Ixy[1][2] * neighborPixel[1][2]))
                + ((Ixy[2][0] * neighborPixel[2][0]) + (Ixy[2][1] * neighborPixel[2][1]) + (Ixy[2][2] * neighborPixel[2][2]));
        return ixy;
    }
}
