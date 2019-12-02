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
public class MalikPerona {
    ImageService imageService = new ImageService();
    CountourService countourService = new CountourService();
    
    private final double dt = 0.5;
    private final int lambda = 1;
    
    public boolean run(SelectedImage selectedImage, int iteration, double seuil) throws IOException{
        BufferedImage inputImage = selectedImage.getBufferedImage();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        double result[][] = imageService.copyGrayScaleFromImage(inputImage);
        double res = 0.0;
        
        if (iteration<=0) {
            return imageService.saveResImageToFile(imageService.copyGrayScaleFromImage(inputImage), inputImage);
        }
        System.out.println("in malik function");
        for (int iter = 0; iter < iteration; iter++) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    res = result[i][j] + dt*compute(inputImage, i, j);
//                    if(res >= 255){
//                        res = compensePixel(inputImage, i, j);
//                    }else if(res <= 0){
//                        System.out.println("Avvant i = "+i+" & j = "+j+" et res = "+res);
//                        res = compensePixel(inputImage, i, j);
//                        System.out.println("Apres i = "+i+" & j = "+j+" et res = "+res);
//                    }
                    result[i][j] = res;
//                    result[i][j] = Math.abs(res);
                    res = 0.0;
                }
            }
        }
        System.out.println("finishing the process of malik");
        return countourService.saveResImage(seuil, result, inputImage);
    }
    
    public double compute(BufferedImage image, int x, int y){
        float current = imageService.getGrayScale(image.getRGB(x, y));
        int width = image.getWidth();
        int height = image.getHeight();


        int px = x-1;
        int nx = x+1;
        int py = y-1;
        int ny = y+1;
        if (px<0)
          px=0;
        if (nx>=width)
          nx=width-1;
        if (py<0)
          py=0;
        if (ny>=height)
          ny=height-1;

        double ixp = imageService.getGrayScale(image.getRGB(px, y));
        double ixn = imageService.getGrayScale(image.getRGB(nx, y));
        double iyp = imageService.getGrayScale(image.getRGB(x, ny));
        double iyn = imageService.getGrayScale(image.getRGB(x, py));

        double diffxn = computeDiffusion(current - ixn);
        double diffxp = computeDiffusion(current - ixp);
        double diffyn = computeDiffusion(current - iyn);
        double diffyp = computeDiffusion(current - ixp);


        double delta = (diffxn * (ixn - current))
                      + (diffxp * (ixp - current))
                      + (diffyp * (iyp - current))
                      + (diffyn * (iyn - current));


        return delta;
    }
    
    public double computeDiffusion(double v){
        return (lambda)/((lambda) + Math.pow(Math.abs(v), 2));
    }
    
    public double compensePixel(BufferedImage image, int i, int j){
        double[][] neighborPixel = {
                    {imageService.getGrayScale(image.getRGB(i - 1, j - 1)), imageService.getGrayScale(image.getRGB(i - 1, j)), imageService.getGrayScale(image.getRGB(i - 1, j + 1))},
                    {imageService.getGrayScale(image.getRGB(i, j - 1)), imageService.getGrayScale(image.getRGB(i, j)), imageService.getGrayScale(image.getRGB(i, j + 1))},
                    {imageService.getGrayScale(image.getRGB(i + 1, j - 1)), imageService.getGrayScale(image.getRGB(i + 1, j)), imageService.getGrayScale(image.getRGB(i + 1, j + 1))}
                };
        double res = (neighborPixel[0][0] + neighborPixel[0][1] + neighborPixel[0][2] +
                neighborPixel[1][0] + neighborPixel[1][1] + neighborPixel[1][2] +
                neighborPixel[2][0] + neighborPixel[2][1] + neighborPixel[2][2] ) / 9;
        return res;
    }
}
