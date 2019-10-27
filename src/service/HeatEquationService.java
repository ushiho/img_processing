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
public class HeatEquationService {
    ImageService imageService = new ImageService();
    
    int[][] laplacien = {{1, 1, 1}, {1, -8, 1}, {1, 1, 1}};
    double[][] Ixx = {{1/4, -1/2, 1/4}, {1/2, -1, 1/2}, {1/4, -1/2, 1/4}};
    double[][] Iyy = {{1/4, 1/2, 1/4}, {-1/2, -1, -1/2}, {1/4, 1/2, 1/4}};
    double dt = 0.5;
    
    public boolean applyFilter(SelectedImage selectedImage, int iteration) throws IOException{
        BufferedImage image = selectedImage.getBufferedImage();
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] edgeColors = new int[width][height];
        for (int iter = 0; iter < iteration; iter++) {
            for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                int[][] neighborPixel = {
                {imageService.getGrayScale(image.getRGB(i - 1, j - 1)), imageService.getGrayScale(image.getRGB(i - 1, j)), imageService.getGrayScale(image.getRGB(i - 1, j + 1))},
                {imageService.getGrayScale(image.getRGB(i, j - 1)), imageService.getGrayScale(image.getRGB(i, j)), imageService.getGrayScale(image.getRGB(i, j + 1))},
                {imageService.getGrayScale(image.getRGB(i + 1, j - 1)), imageService.getGrayScale(image.getRGB(i + 1, j)), imageService.getGrayScale(image.getRGB(i + 1, j + 1))}
                };

                

                edgeColors[i][j] += (int) (laplacien(neighborPixel)*dt);
                }
            }
        }
        return saveResImage(width, height, edgeColors, image);
    }
    
    public int laplacien(int[][] neighborPixel) {
        int result =  ((laplacien[0][0] * neighborPixel[0][0]) + (laplacien[0][1] * neighborPixel[0][1]) + (laplacien[0][2] * neighborPixel[0][2]))
                + ((laplacien[1][0] * neighborPixel[1][0]) + (laplacien[1][1] * neighborPixel[1][1]) + (laplacien[1][2] * neighborPixel[1][2]))
                + ((laplacien[2][0] * neighborPixel[2][0]) + (laplacien[2][1] * neighborPixel[2][1]) + (laplacien[2][2] * neighborPixel[2][2]));
        return result;
    }
    
    public boolean saveResImage(int width, int height, int[][] edgeColors, BufferedImage image)
            throws IOException {

        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                int edgeColor = edgeColors[i][j];
                edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;

                image.setRGB(i, j, edgeColor);
            }
        }

        File outputfile = new File("output/result.png");
        return ImageIO.write(image, "png", outputfile);
    }
    
    public double calculateIxx(int[][] neighborPixel){
        double result =  ((Ixx[0][0] * neighborPixel[0][0]) + (Ixx[0][1] * neighborPixel[0][1]) + (Ixx[0][2] * neighborPixel[0][2]))
                + ((Ixx[1][0] * neighborPixel[1][0]) + (Ixx[1][1] * neighborPixel[1][1]) + (Ixx[1][2] * neighborPixel[1][2]))
                + ((Ixx[2][0] * neighborPixel[2][0]) + (Ixx[2][1] * neighborPixel[2][1]) + (Ixx[2][2] * neighborPixel[2][2]));
        return result;
    }
    
    public double calculateIyy(int[][] neighborPixel){
        double result =  ((Iyy[0][0] * neighborPixel[0][0]) + (Iyy[0][1] * neighborPixel[0][1]) + (Iyy[0][2] * neighborPixel[0][2]))
                + ((Iyy[1][0] * neighborPixel[1][0]) + (Iyy[1][1] * neighborPixel[1][1]) + (Iyy[1][2] * neighborPixel[1][2]))
                + ((Iyy[2][0] * neighborPixel[2][0]) + (Iyy[2][1] * neighborPixel[2][1]) + (Iyy[2][2] * neighborPixel[2][2]));
        return result;
    }
}
