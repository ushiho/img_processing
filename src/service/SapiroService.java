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
public class SapiroService {

    double[][] Ix = {{0.1464466, 0.0, -0.1464466}, {0.2071067, 0.0, -0.2071067}, {0.1464466, 0.0, -0.1464466}};
    double[][] Iy = {{0.1464466, 0.2071067, 0.1464466}, {0.0, 0.0, 0.0}, {-0.1464466, -0.2071067, -0.1464466}};
    double[][] Ixx = {{0.25, -0.5, 0.25}, {0.5, -1.0, 0.5}, {0.25, -0.5, 0.25}};
    double[][] Iyy = {{0.25, 0.5, 0.25}, {-0.5, -1.0, -0.5}, {0.25, 0.5, 0.25}};
    double[][] Ixy = {{0.25, 0.0, -0.25}, {0.0, 0.0, 0.0}, {-0.25, 0.0, 0.25}};

    public boolean run(SelectedImage selectedImage, int iteration) throws IOException {
        BufferedImage image = null;
        try {
            image = ImageIO.read(selectedImage.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = image.getWidth();
        int height = image.getHeight();
        double[][] newMat = new double[width + 4][height + 4];
        double[][] saprio = new double[width + 4][height + 4];
        double dt = 0.25;
        int i, x, y;
        int j, k;

        //Getting RGB Pix
        for (j = 0; j < width; j++) {
            for (k = 0; k < height; k++) {
                int pixel = image.getRGB(j, k);
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                int moyen_pixel = (r + g + b) / 3;
                newMat[j + 2][k + 2] = moyen_pixel;
            }
        }

        double rouge[][] = new double[width + 4][height + 4];
        double vert[][] = new double[width + 4][height + 4];
        double bleu[][] = new double[width + 4][height + 4];
        double sRouge[][] = new double[width + 4][height + 4];
        double sVert[][] = new double[width + 4][height + 4];
        double sBleu[][] = new double[width + 4][height + 4];

        for (j = 0; j < width; j++) {
            for (k = 0; k < height; k++) {
                int pixel = image.getRGB(j, k);
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                rouge[j + 2][k + 2] = r;
                vert[j + 2][k + 2] = g;
                bleu[j + 2][k + 2] = b;
            }
        }

        for (i = 0; i < iteration; i++) {
            System.out.println(((int) (((i + 1) * 100) / iteration)) + "%");
            // Red Sacale
            for (x = 1; x < width + 3; x++) {
                for (y = 1; y < height + 3; y++) {
                    double rI_x = (Ix[0][0] * rouge[x - 1][y - 1]) + (Ix[0][1] * rouge[x - 1][y])
                            + (Ix[0][2] * rouge[x - 1][y + 1]) + (Ix[1][0] * rouge[x][y - 1])
                            + (Ix[1][1] * rouge[x][y]) + (Ix[1][2] * rouge[x][y + 1])
                            + (Ix[2][0] * rouge[x + 1][y - 1]) + (Ix[2][1] * rouge[x + 1][y]) + (Ix[2][2] * rouge[x + 1][y + 1]);
                    double rI_y = (Iy[0][0] * rouge[x - 1][y - 1]) + (Iy[0][1] * rouge[x - 1][y])
                            + (Iy[0][2] * rouge[x - 1][y + 1]) + (Iy[1][0] * rouge[x][y - 1])
                            + (Iy[1][1] * rouge[x][y]) + (Iy[1][2] * rouge[x][y + 1])
                            + (Iy[2][0] * rouge[x + 1][y - 1]) + (Iy[2][1] * rouge[x + 1][y]) + (Iy[2][2] * rouge[x + 1][y + 1]);

                    double rI_xx = (Ixx[0][0] * rouge[x - 1][y - 1]) + (Ixx[0][1] * rouge[x - 1][y])
                            + (Ixx[0][2] * rouge[x - 1][y + 1]) + (Ixx[1][0] * rouge[x][y - 1])
                            + (Ixx[1][1] * rouge[x][y]) + (Ixx[1][2] * rouge[x][y + 1])
                            + (Ixx[2][0] * rouge[x + 1][y - 1]) + (Ixx[2][1] * rouge[x + 1][y]) + (Ixx[2][2] * rouge[x + 1][y + 1]);

                    double rI_yy = (Iyy[0][0] * rouge[x - 1][y - 1]) + (Iyy[0][1] * rouge[x - 1][y])
                            + (Iyy[0][2] * rouge[x - 1][y + 1]) + (Iyy[1][0] * rouge[x][y - 1])
                            + (Iyy[1][1] * rouge[x][y]) + (Iyy[1][2] * rouge[x][y + 1])
                            + (Iyy[2][0] * rouge[x + 1][y - 1]) + (Iyy[2][1] * rouge[x + 1][y]) + (Iyy[2][2] * rouge[x + 1][y + 1]);

                    double rI_xy = (Ixy[0][0] * rouge[x - 1][y - 1]) + (Ixy[0][1] * rouge[x - 1][y])
                            + (Ixy[0][2] * rouge[x - 1][y + 1]) + (Ixy[1][0] * rouge[x][y - 1])
                            + (Ixy[1][1] * rouge[x][y]) + (Ixy[1][2] * rouge[x][y + 1])
                            + (Ixy[2][0] * rouge[x + 1][y - 1]) + (Ixy[2][1] * rouge[x + 1][y]) + (Ixy[2][2] * rouge[x + 1][y + 1]);

                    double rG11 = rI_x * rI_x;
                    double rG12 = rI_x * rI_y;
                    double rG22 = rI_y * rI_y;
                    double landaPLUS = (rG11 + rG22 + Math.sqrt(((rG11 - rG22) * (rG11 - rG22)) + 4 * rG12 * rG12)) / 2;
                    double landaMOINS = (rG11 + rG22 - Math.sqrt(((rG11 - rG22) * (rG11 - rG22)) + 4 * rG12 * rG12)) / 2;
                    double redE = (rI_xx * rI_y * rI_y - 2 * rI_xy * rI_x * rI_y + rI_yy * rI_x * rI_x);
                    double redE2 = (rI_x * rI_x + rI_y * rI_y);
                    if(redE2 == 0){
                        redE2 = 1;
                    }
                    saprio[x][y] = Math.sqrt(landaPLUS - landaMOINS);
                    sRouge[x][y] = (redE / redE2);
                    sRouge[x][y] *= fctSeuille(saprio[x][y]);
                }
            }
            // Traitement  R 			
            for (x = 0; x < width + 4; x++) {
                for (y = 0; y < height + 4; y++) {
                    rouge[x][y] += dt * sRouge[x][y];
                }
            }

            // GREEN SCALE
            for (x = 1; x < width + 3; x++) {
                for (y = 1; y < height + 3; y++) {
                    double I_x = (Ix[0][0] * vert[x - 1][y - 1]) + (Ix[0][1] * vert[x - 1][y]) + (Ix[0][2] * vert[x - 1][y + 1])
                            + (Ix[1][0] * vert[x][y - 1]) + (Ix[1][1] * vert[x][y]) + (Ix[1][2] * vert[x][y + 1])
                            + (Ix[2][0] * vert[x + 1][y - 1]) + (Ix[2][1] * vert[x + 1][y]) + (Ix[2][2] * vert[x + 1][y + 1]);

                    double I_y = (Iy[0][0] * vert[x - 1][y - 1]) + (Iy[0][1] * vert[x - 1][y]) + (Iy[0][2] * vert[x - 1][y + 1])
                            + (Iy[1][0] * vert[x][y - 1]) + (Iy[1][1] * vert[x][y]) + (Iy[1][2] * vert[x][y + 1]) + (Iy[2][0] * vert[x + 1][y - 1])
                            + (Iy[2][1] * vert[x + 1][y])
                            + (Iy[2][2] * vert[x + 1][y + 1]);

                    double I_xx = (Ixx[0][0] * vert[x - 1][y - 1])
                            + (Ixx[0][1] * vert[x - 1][y])
                            + (Ixx[0][2] * vert[x - 1][y + 1])
                            + (Ixx[1][0] * vert[x][y - 1])
                            + (Ixx[1][1] * vert[x][y])
                            + (Ixx[1][2] * vert[x][y + 1])
                            + (Ixx[2][0] * vert[x + 1][y - 1])
                            + (Ixx[2][1] * vert[x + 1][y])
                            + (Ixx[2][2] * vert[x + 1][y + 1]);

                    double I_yy = (Iyy[0][0] * vert[x - 1][y - 1])
                            + (Iyy[0][1] * vert[x - 1][y])
                            + (Iyy[0][2] * vert[x - 1][y + 1])
                            + (Iyy[1][0] * vert[x][y - 1])
                            + (Iyy[1][1] * vert[x][y])
                            + (Iyy[1][2] * vert[x][y + 1])
                            + (Iyy[2][0] * vert[x + 1][y - 1])
                            + (Iyy[2][1] * vert[x + 1][y])
                            + (Iyy[2][2] * vert[x + 1][y + 1]);

                    double I_xy = (Ixy[0][0] * vert[x - 1][y - 1])
                            + (Ixy[0][1] * vert[x - 1][y])
                            + (Ixy[0][2] * vert[x - 1][y + 1])
                            + (Ixy[1][0] * vert[x][y - 1])
                            + (Ixy[1][1] * vert[x][y])
                            + (Ixy[1][2] * vert[x][y + 1])
                            + (Ixy[2][0] * vert[x + 1][y - 1])
                            + (Ixy[2][1] * vert[x + 1][y])
                            + (Ixy[2][2] * vert[x + 1][y + 1]);

                    double G11 = I_x * I_x;
                    double G12 = I_x * I_y;
                    double G22 = I_y * I_y;
                    double landaPLUS = (G11 + G22 + Math.sqrt(((G11 - G22) * (G11 - G22)) + 4 * G12 * G12)) / 2;
                    double landaMOINS = (G11 + G22 - Math.sqrt(((G11 - G22) * (G11 - G22)) + 4 * G12 * G12)) / 2;
                    double greenE = (I_xx * I_y * I_y - 2 * I_xy * I_x * I_y + I_yy * I_x * I_x);
                    double greenE2 = (I_x * I_x + I_y * I_y);
                    if(greenE2 == 0){
                        greenE2 = 1;
                    }
                    saprio[x][y] = Math.sqrt(landaPLUS - landaMOINS);
                    sVert[x][y] = greenE / greenE2;
                    sVert[x][y] *= fctSeuille(saprio[x][y]);

                }
            }
            // Traitement V
            for (x = 0; x < width + 4; x++) {
                for (y = 0; y < height + 4; y++) {
                    vert[x][y] += dt * sVert[x][y];
                }
            }
//---------------------------------------------------------------------------------------------------------------------

// SCALE BLUE --------------------------------------------------------------------------------------------------------
            for (x = 1; x < width + 3; x++) {
                for (y = 1; y < height + 3; y++) {

                    double I_x = (Ix[0][0] * bleu[x - 1][y - 1])
                            + (Ix[0][1] * bleu[x - 1][y])
                            + (Ix[0][2] * bleu[x - 1][y + 1])
                            + (Ix[1][0] * bleu[x][y - 1])
                            + (Ix[1][1] * bleu[x][y])
                            + (Ix[1][2] * bleu[x][y + 1])
                            + (Ix[2][0] * bleu[x + 1][y - 1])
                            + (Ix[2][1] * bleu[x + 1][y])
                            + (Ix[2][2] * bleu[x + 1][y + 1]);

                    double I_y = (Iy[0][0] * bleu[x - 1][y - 1])
                            + (Iy[0][1] * bleu[x - 1][y])
                            + (Iy[0][2] * bleu[x - 1][y + 1])
                            + (Iy[1][0] * bleu[x][y - 1])
                            + (Iy[1][1] * bleu[x][y])
                            + (Iy[1][2] * bleu[x][y + 1])
                            + (Iy[2][0] * bleu[x + 1][y - 1])
                            + (Iy[2][1] * bleu[x + 1][y])
                            + (Iy[2][2] * bleu[x + 1][y + 1]);

                    double I_xx = (Ixx[0][0] * bleu[x - 1][y - 1])
                            + (Ixx[0][1] * bleu[x - 1][y])
                            + (Ixx[0][2] * bleu[x - 1][y + 1])
                            + (Ixx[1][0] * bleu[x][y - 1])
                            + (Ixx[1][1] * bleu[x][y])
                            + (Ixx[1][2] * bleu[x][y + 1])
                            + (Ixx[2][0] * bleu[x + 1][y - 1])
                            + (Ixx[2][1] * bleu[x + 1][y])
                            + (Ixx[2][2] * bleu[x + 1][y + 1]);

                    double I_yy = (Iyy[0][0] * bleu[x - 1][y - 1])
                            + (Iyy[0][1] * bleu[x - 1][y])
                            + (Iyy[0][2] * bleu[x - 1][y + 1])
                            + (Iyy[1][0] * bleu[x][y - 1])
                            + (Iyy[1][1] * bleu[x][y])
                            + (Iyy[1][2] * bleu[x][y + 1])
                            + (Iyy[2][0] * bleu[x + 1][y - 1])
                            + (Iyy[2][1] * bleu[x + 1][y])
                            + (Iyy[2][2] * bleu[x + 1][y + 1]);

                    double I_xy = (Ixy[0][0] * bleu[x - 1][y - 1])
                            + (Ixy[0][1] * bleu[x - 1][y])
                            + (Ixy[0][2] * bleu[x - 1][y + 1])
                            + (Ixy[1][0] * bleu[x][y - 1])
                            + (Ixy[1][1] * bleu[x][y])
                            + (Ixy[1][2] * bleu[x][y + 1])
                            + (Ixy[2][0] * bleu[x + 1][y - 1])
                            + (Ixy[2][1] * bleu[x + 1][y])
                            + (Ixy[2][2] * bleu[x + 1][y + 1]);

                    double G11 = I_x * I_x;
                    double G12 = I_x * I_y;
                    double G22 = I_y * I_y;
                    double landaPLUS = (G11 + G22 + Math.sqrt(((G11 - G22) * (G11 - G22)) + 4 * G12 * G12)) / 2;
                    double landaMOINS = (G11 + G22 - Math.sqrt(((G11 - G22) * (G11 - G22)) + 4 * G12 * G12)) / 2;
                    double blueE = (I_xx * I_y * I_y - 2 * I_xy * I_x * I_y + I_yy * I_x * I_x);
                    double blueE2 = (I_x * I_x + I_y * I_y);
                    if(blueE2 == 0){
                        blueE2 = 1;
                    }
                    saprio[x][y] = Math.sqrt(landaPLUS - landaMOINS);

                    sBleu[x][y] = (blueE / blueE2);
                    sBleu[x][y] *= fctSeuille(saprio[x][y]);

                }
            }
            // TRAITEMENT B	
            for (x = 0; x < width + 4; x++) {
                for (y = 0; y < height + 4; y++) {
                    bleu[x][y] += dt * sBleu[x][y];
                }
            }
//---------------------------------------------------------------------------------------------------------------------

            // CREATING IMAGE , NEW Pxi to IMG
            for (x = 0; x < width; x++) {
                for (y = 0; y < height; y++) {
                    int r = (int) rouge[x + 2][y + 2];
                    int g = (int) vert[x + 2][y + 2];
                    int b = (int) bleu[x + 2][y + 2];
                    if (r > 255) {
                        r = 255;
                    }
                    if (g > 255) {
                        g = 255;
                    }
                    if (b > 255) {
                        b = 255;
                    }
                    if (r < 0) {
                        r = 0;
                    }
                    if (g < 0) {
                        g = 0;
                    }
                    if (b < 0) {
                        b = 0;
                    }
                    Color c = new Color(r, g, b);
                    image.setRGB(x, y, c.getRGB());
                }
            }

        }

        File outputfile = new File("output/result.png");
        return ImageIO.write(image, "png", outputfile);
    }

    // To elimin� flou
    private double fctSeuille(double g) {
        return 1 / (1 + Math.pow(g, 2));
    }
}
