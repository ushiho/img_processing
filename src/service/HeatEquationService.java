/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.SelectedImage;
import java.awt.Color;
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
    
    double[][] deriveIxx = {{0.25, -0.5, 0.25}, {0.5, -1, 0.5}, {0.25, -0.5, 0.25}};
    double[][] deriveIyy = {{0.25, 0.5, 0.25}, {-0.5, -1, -0.5}, {0.25, 0.5, 0.25}};
    double dt = 0.5;
    
    public boolean run(SelectedImage selectedImage, int iteration) throws IOException{
        BufferedImage inputImage = readImage(selectedImage.getBufferedImage());
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        double result[][] = copyGrayScaleFromImage(inputImage);
        if (iteration<=0) {
            return saveResImage(width, height, copyGrayScaleFromImage(inputImage), inputImage);
        }
        for (int iter = 0; iter < iteration; iter++) {
            double Ixx[][] = calculateIxx(matrixToBufferedImage(width, height, result, inputImage));
            double Iyy[][] = calculateIyy(matrixToBufferedImage(width, height, result, inputImage));
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    result[i][j] += dt*(Ixx[i][j] + Iyy[i][j]);
                }
            }
        }
        return saveResImage(width, height, result, inputImage);
    }
    
    public double[][] calculateIxx(BufferedImage imageSource){
        BufferedImage copy = imageService.duplicateSrcBufferedImage(imageSource);
        int width = imageSource.getWidth();
        int height = imageSource.getHeight();
        double[][] Ixx = new double[width][height];
        for (int i = 1; i < width + 1; i++) {
            for (int j = 1; j < height + 1; j++) {
                int[][] neighborPixel = {
                {imageService.getGrayScale(copy.getRGB(i - 1, j - 1)), imageService.getGrayScale(copy.getRGB(i - 1, j)), imageService.getGrayScale(copy.getRGB(i - 1, j + 1))},
                {imageService.getGrayScale(copy.getRGB(i, j - 1)), imageService.getGrayScale(copy.getRGB(i, j)), imageService.getGrayScale(copy.getRGB(i, j + 1))},
                {imageService.getGrayScale(copy.getRGB(i + 1, j - 1)), imageService.getGrayScale(copy.getRGB(i + 1, j)), imageService.getGrayScale(copy.getRGB(i + 1, j + 1))}
                };
                double ixx = filterDeriveIxx(neighborPixel);
                Ixx[i - 1][j - 1] = ixx;
            }
        }
        return Ixx;
    }
    
    public double[][] calculateIyy(BufferedImage imageSource){
        BufferedImage copy = imageService.duplicateSrcBufferedImage(imageSource);
        int width = imageSource.getWidth();
        int height = imageSource.getHeight();
        double[][] Iyy = new double[width][height];
        for (int i = 1; i < width + 1; i++) {
            for (int j = 1; j < height + 1; j++) {
                int[][] neighborPixel = {
                {imageService.getGrayScale(copy.getRGB(i - 1, j - 1)), imageService.getGrayScale(copy.getRGB(i - 1, j)), imageService.getGrayScale(copy.getRGB(i - 1, j + 1))},
                {imageService.getGrayScale(copy.getRGB(i, j - 1)), imageService.getGrayScale(copy.getRGB(i, j)), imageService.getGrayScale(copy.getRGB(i, j + 1))},
                {imageService.getGrayScale(copy.getRGB(i + 1, j - 1)), imageService.getGrayScale(copy.getRGB(i + 1, j)), imageService.getGrayScale(copy.getRGB(i + 1, j + 1))}
                };
                double iyy = filterDeriveIyy(neighborPixel);
                Iyy[i - 1][j - 1] = iyy;
            }
        }
        return Iyy;
    }
    
    public boolean saveResImage(int width, int height, double[][] edgeColors, BufferedImage image)
            throws IOException {

        image = matrixToBufferedImage(width, height, edgeColors, image);

        File outputfile = new File("output/result.png");
        return ImageIO.write(image, "png", outputfile);
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
                copy[i][j] = imageService.getGrayScale(image.getRGB(i, j));
            }
        }
        return copy;
    }
    
    public double filterDeriveIyy(int[][] neighborPixel) {
        double iyy =  ((deriveIyy[0][0] * neighborPixel[0][0]) + (deriveIyy[0][1] * neighborPixel[0][1]) + (deriveIyy[0][2] * neighborPixel[0][2]))
                + ((deriveIyy[1][0] * neighborPixel[1][0]) + (deriveIyy[1][1] * neighborPixel[1][1]) + (deriveIyy[1][2] * neighborPixel[1][2]))
                + ((deriveIyy[2][0] * neighborPixel[2][0]) + (deriveIyy[2][1] * neighborPixel[2][1]) + (deriveIyy[2][2] * neighborPixel[2][2]));
        return iyy;
    }

    public double filterDeriveIxx(int[][] neighborPixel) {
        double ixx =  ((deriveIxx[0][0] * neighborPixel[0][0]) + (deriveIxx[0][1] * neighborPixel[0][1]) + (deriveIxx[0][2] * neighborPixel[0][2]))
                + ((deriveIxx[1][0] * neighborPixel[1][0]) + (deriveIxx[1][1] * neighborPixel[1][1]) + (deriveIxx[1][2] * neighborPixel[1][2]))
                + ((deriveIxx[2][0] * neighborPixel[2][0]) + (deriveIxx[2][1] * neighborPixel[2][1]) + (deriveIxx[2][2] * neighborPixel[2][2]));
        return ixx;
    }
    
    public BufferedImage readImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        double img[][] = new double[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height ; j++) {
                img[i][j] = new Color(image.getRGB(i,j)).getRed();
            }
        }
        return image;
    }
}
