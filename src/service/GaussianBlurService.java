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
import java.util.stream.IntStream;
import javax.imageio.ImageIO;

/**
 *
 * @author swiri
 */
public class GaussianBlurService {
    
    ImageService imageService = new ImageService();
    private final float sigma = 1;
    private double[][] kernel;

    public double[][] getKernel() {
        return kernel;
    }

    public void setKernel(double[][] kernel) {
        this.kernel = kernel;
    }
    
//    public double[][] createGaussienKernel(int k){
//        int width = 2*k + 1;
//        int height = 2*k + 1;
//        double kernelValue[][] = new double[width][height];
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                kernelValue[i][j] = (1/(2 * Math.PI * Math.pow(sigma, 2))) * 
//                        Math.exp(-((Math.pow((i - (k + 1)), 2) + Math.pow((j - (k + 1)), 2)) / (2 * Math.pow(sigma, 2))));
//            }
//        }
//        return kernelValue;
//    }
    
    
//    public boolean blur(SelectedImage selectedImage, int size) throws IOException{
//        BufferedImage copy = imageService.duplicateSrcBufferedImage(selectedImage.getBufferedImage());
//        int width = copy.getWidth();
//        int height = copy.getHeight();
//        setKernel(createGaussienKernel(size));
//        double[][] edgeColors = new double[width - 2][height - 2];
//        for (int i = 1; i < width - 1; i++) {
//            for (int j = 1; j < height - 1; j++) {
//                int[][] neighborPixel = {
//                {imageService.getGrayScale(copy.getRGB(i - 1, j - 1)), imageService.getGrayScale(copy.getRGB(i - 1, j)), imageService.getGrayScale(copy.getRGB(i - 1, j + 1))},
//                {imageService.getGrayScale(copy.getRGB(i, j - 1)), imageService.getGrayScale(copy.getRGB(i, j)), imageService.getGrayScale(copy.getRGB(i, j + 1))},
//                {imageService.getGrayScale(copy.getRGB(i + 1, j - 1)), imageService.getGrayScale(copy.getRGB(i + 1, j)), imageService.getGrayScale(copy.getRGB(i + 1, j + 1))}
//                };
//                edgeColors[i - 1][j - 1] = convolveWithKernel(neighborPixel);
//            }
//        }
//        
//        return saveResImage(width, height, edgeColors, selectedImage.getBufferedImage());
//    }

    public boolean saveResImage(int width, int height, double[][] edgeColors, BufferedImage image) throws IOException {
        for (int i = 0; i < width - 2; i++) {
            for (int j = 0; j < height - 2; j++) {
                int edgeColor = (int) edgeColors[i][j];
                edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;

                image.setRGB(i, j, edgeColor);
            }
        }
        File outputfile = new File("output/result.png");
        return ImageIO.write(image, "png", outputfile);
    }
    
    public double convolveWithKernel(int[][] neighborPixel) {
        double gy =  ((getKernel()[0][0] * neighborPixel[0][0]) + (getKernel()[0][1] * neighborPixel[0][1]) + (getKernel()[0][2] * neighborPixel[0][2]))
                + ((getKernel()[1][0] * neighborPixel[1][0]) + (getKernel()[1][1] * neighborPixel[1][1]) + (getKernel()[1][2] * neighborPixel[1][2]))
                + ((getKernel()[2][0] * neighborPixel[2][0]) + (getKernel()[2][1] * neighborPixel[2][1]) + (getKernel()[2][2] * neighborPixel[2][2]));
        return gy;
    }
    
    public BufferedImage blur(BufferedImage image, int[] filter, int filterWidth) {
        if (filter.length % filterWidth != 0) {
            throw new IllegalArgumentException("filter contains a incomplete row");
        }

        final int width = image.getWidth();
        final int height = image.getHeight();
        final int sum = IntStream.of(filter).sum();

        int[] input = image.getRGB(0, 0, width, height, null, 0, width);

        int[] output = new int[input.length];

        final int pixelIndexOffset = width - filterWidth;
        final int centerOffsetX = filterWidth / 2;
        final int centerOffsetY = filter.length / filterWidth / 2;

        // apply filter
        for (int h = height - filter.length / filterWidth + 1, w = width - filterWidth + 1, y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int r = 0;
                int g = 0;
                int b = 0;
                for (int filterIndex = 0, pixelIndex = y * width + x;
                        filterIndex < filter.length;
                        pixelIndex += pixelIndexOffset) {
                    for (int fx = 0; fx < filterWidth; fx++, pixelIndex++, filterIndex++) {
                        int col = input[pixelIndex];
                        int factor = filter[filterIndex];

                        // sum up color channels seperately
                        r += ((col >>> 16) & 0xFF) * factor;
                        g += ((col >>> 8) & 0xFF) * factor;
                        b += (col & 0xFF) * factor;
                    }
                }
                r /= sum;
                g /= sum;
                b /= sum;
                // combine channels with full opacity
                output[x + centerOffsetX + (y + centerOffsetY) * width] = (r << 16) | (g << 8) | b | 0xFF000000;
            }
        }

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        result.setRGB(0, 0, width, height, output, 0, width);
        System.out.println("Input width = "+image.getWidth()+" Height = "+image.getHeight());
        System.out.println("Result width = "+result.getWidth()+" Height = "+result.getHeight());
        return result;
    }
    
    public boolean applyBlur(SelectedImage selectedImage) throws IOException{
        int[] filter = {1, 2, 1, 2, 4, 2, 1, 2, 1};
        int filterWidth = 3;
        BufferedImage blurred = blur(selectedImage.getBufferedImage(), filter, filterWidth);
        File outputfile = new File("output/result.png");
        return ImageIO.write(blurred, "png", outputfile);
    }
}
