/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.SelectedImage;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
    double[][] Ixx = {{0.25, -0.5, 0.25}, {0.5, -1, 0.5}, {0.25, -0.5, 0.25}};
    double[][] Iyy = {{0.25, 0.5, 0.25}, {-0.5, -1, -0.5}, {0.25, 0.5, 0.25}};
    
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
            BufferedImage copy = imageService.duplicateSrcBufferedImage(imageService.matrixToBufferedImage(width, height, result, inputImage));
            double res = 0.0;
            double d_Ix = 0.0;
            double d_Iy = 0.0;
            double d_Ixy = 0.0;
            double d_Ixx = 0.0;
            double d_Iyy = 0.0;
            for (int i = 1; i < width + 1; i++) {
                for (int j = 1; j < height + 1; j++) {
                    int[][] neighborPixel = {
                    {imageService.getGrayScale(copy.getRGB(i - 1, j - 1)), imageService.getGrayScale(copy.getRGB(i - 1, j)), imageService.getGrayScale(copy.getRGB(i - 1, j + 1))},
                    {imageService.getGrayScale(copy.getRGB(i, j - 1)), imageService.getGrayScale(copy.getRGB(i, j)), imageService.getGrayScale(copy.getRGB(i, j + 1))},
                    {imageService.getGrayScale(copy.getRGB(i + 1, j - 1)), imageService.getGrayScale(copy.getRGB(i + 1, j)), imageService.getGrayScale(copy.getRGB(i + 1, j + 1))}
                    };
                    d_Ix = calculateDerive(Ix, neighborPixel);
                    d_Iy = calculateDerive(Iy, neighborPixel);
                    d_Ixy = calculateDerive(Ixy, neighborPixel);
                    d_Ixx = calculateDerive(Ixx, neighborPixel);
                    d_Iyy = calculateDerive(Iyy, neighborPixel);
                    res = (d_Iy * d_Iy * d_Ixx - 2 * d_Ix * d_Iy * d_Ixy + d_Ix * d_Ix * d_Iyy) / (d_Ix * d_Ix + d_Iy * d_Iy);
                    result[i - 1][j - 1] += dt * res;
                }
            }
        }
        return imageService.saveResImageToFile(result, inputImage);
    }
    
    public double calculateDerive(double[][] deriveToApply, int[][] neighborPixel) {
        return ((deriveToApply[0][0] * neighborPixel[0][0]) + (deriveToApply[0][1] * neighborPixel[0][1]) + (deriveToApply[0][2] * neighborPixel[0][2]))
                + ((deriveToApply[1][0] * neighborPixel[1][0]) + (deriveToApply[1][1] * neighborPixel[1][1]) + (deriveToApply[1][2] * neighborPixel[1][2]))
                + ((deriveToApply[2][0] * neighborPixel[2][0]) + (deriveToApply[2][1] * neighborPixel[2][1]) + (deriveToApply[2][2] * neighborPixel[2][2]));
    }
}
