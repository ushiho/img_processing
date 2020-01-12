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
public class AlvarezMorelService {
    double[][] Ix =  {{0.1464466,0.0,-0.1464466},{0.2071067,0.0,-0.2071067},{0.1464466,0.0,-0.1464466}};
    double[][] Iy =  {{0.1464466,0.2071067,0.1464466},{0.0,0.0,0.0},{-0.1464466,-0.2071067,-0.1464466}};
    double[][] Ixx = {{0.25,-0.5,0.25},{0.5,-1.0,0.5},{0.25,-0.5,0.25}};
    double[][] Iyy = {{0.25,0.5,0.25},{-0.5,-1.0,-0.5},{0.25,0.5,0.25}};
    double[][] Ixy = {{0.25,0.0,-0.25},{0.0,0.0,0.0},{-0.25,0.0,0.25}};
    
    public boolean run(SelectedImage selectedImage, int iteration)throws IOException{
        BufferedImage image = null;
        
        try {image= ImageIO.read(selectedImage.getFile());}catch (IOException e) {e.printStackTrace();}
        double ch[][] ;
        double Alvarez[][];
        int i,x,y;
        int width = image.getWidth();
        int height = image.getHeight();
  		
        ch=new double[width+4][height+4];
        Alvarez=new double[width+4][height+4];

        double dt=0.5;
        int j,k;

        for(j=0; j<width; j++)
            for(k=0; k<height; k++){
                int pixel = image.getRGB(j, k);
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                int moyen_pixel = (r+g+b)/3;
                ch[j+2][k+2] = moyen_pixel;
                pixel = (moyen_pixel << 24) | (moyen_pixel << 16) | (moyen_pixel << 8) | moyen_pixel;
                image.setRGB(j, k, pixel);
            }
        
        for(i=0;i<iteration;i++){
            System.out.println(((int)(((i+1)*100)/iteration))+"%");
            for (x = 1; x <width+3; x++) {
                for (y = 1; y <height+3; y++) {
                    double I_x =  (Ix[0][0] * ch[x-1][y-1]) 
  	      					+ (Ix[0][1] * ch[x-1][y]) 
  	      					+ (Ix[0][2] * ch[x-1][y+1]) 
  	      					+ (Ix[1][0] * ch[x][y-1]) 
  	      					+ (Ix[1][1] * ch[x][y]) 
  	      					+ (Ix[1][2] * ch[x][y+1]) 
  	      					+ (Ix[2][0] * ch[x+1][y-1])
  	      					+ (Ix[2][1] * ch[x+1][y]) 
  	      					+ (Ix[2][2] * ch[x+1][y+1]);
                    double I_y =  (Iy[0][0] * ch[x-1][y-1]) 
  	      					+ (Iy[0][1] * ch[x-1][y]) 
  	      					+ (Iy[0][2] * ch[x-1][y+1]) 
  	      					+ (Iy[1][0] * ch[x][y-1]) 
  	      					+ (Iy[1][1] * ch[x][y]) 
  	      					+ (Iy[1][2] * ch[x][y+1]) 
  	      					+ (Iy[2][0] * ch[x+1][y-1])
  	      					+ (Iy[2][1] * ch[x+1][y]) 
  	      					+ (Iy[2][2] * ch[x+1][y+1]);
                    double I_xx =  (Ixx[0][0] * ch[x-1][y-1]) 
  	      					+ (Ixx[0][1] * ch[x-1][y]) 
  	      					+ (Ixx[0][2] * ch[x-1][y+1]) 
  	      					+ (Ixx[1][0] * ch[x][y-1]) 
  	      					+ (Ixx[1][1] * ch[x][y]) 
  	      					+ (Ixx[1][2] * ch[x][y+1]) 
  	      					+ (Ixx[2][0] * ch[x+1][y-1])
  	      					+ (Ixx[2][1] * ch[x+1][y]) 
  	      					+ (Ixx[2][2] * ch[x+1][y+1]);
                    double I_yy =  (Iyy[0][0] * ch[x-1][y-1]) 
  	      					+ (Iyy[0][1] * ch[x-1][y]) 
  	      					+ (Iyy[0][2] * ch[x-1][y+1]) 
  	      					+ (Iyy[1][0] * ch[x][y-1]) 
  	      					+ (Iyy[1][1] * ch[x][y]) 
  	      					+ (Iyy[1][2] * ch[x][y+1]) 
  	      					+ (Iyy[2][0] * ch[x+1][y-1])
  	      					+ (Iyy[2][1] * ch[x+1][y]) 
  	      					+ (Iyy[2][2] * ch[x+1][y+1]);
                    double I_xy =  (Ixy[0][0] * ch[x-1][y-1]) 
   	      					+ (Ixy[0][1] * ch[x-1][y]) 
   	      					+ (Ixy[0][2] * ch[x-1][y+1]) 
   	      					+ (Ixy[1][0] * ch[x][y-1])
   	      					+ (Ixy[1][1] * ch[x][y]) 
   	      					+ (Ixy[1][2] * ch[x][y+1]) 
   	      					+ (Ixy[2][0] * ch[x+1][y-1])
   	      					+ (Ixy[2][1] * ch[x+1][y]) 
   	      					+ (Ixy[2][2] * ch[x+1][y+1]);
                    
                    double e =  (I_xx*I_y*I_y-2*I_xy*I_x*I_y+I_yy*I_x*I_x); 
                    double e2 = (I_x*I_x+I_y*I_y);
                    if(e2==0.0) {
                        e2=1;
                    }  
                    double heat = (double)  I_xx + I_yy;
                    double emss =(double)  e/e2;
                    double grad = (double)  Math.sqrt(I_x*I_x+I_y*I_y);
                    
                    double value =  fctseuille(grad)*heat +  (1-fctseuille(grad))* emss;
                        
                    Alvarez[x][y]=value;
                }
            }
            
            for (x = 0; x <width+4; x++) {
                for (y = 0; y < height+4; y++) {
                    ch[x][y]+=dt*Alvarez[x][y];
                }
            }
            
            for (x = 0; x <width; x++) {
                for (y = 0; y < height; y++) {
                    int conv =(int)ch[x+2][y+2];
                    image.setRGB(x,y,(conv<<16 | conv<<8 | conv));
                }
            }
        }
  	// Enregistrement de l'image
        File outputfile = new File("output/result.png");
        return ImageIO.write(image, "jpg", outputfile);
    }
    
    private double fctseuille(double gradient){
//        return 1/(1+Math.pow(g, 2));
    return (1/(1+Math.pow(Math.abs(gradient)/1, 2)));
    }
}
