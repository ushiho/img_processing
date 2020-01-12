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
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author swiri
 */
public class OsherRudinService {
//    ProgressBar progressBar = new ProgressBar(-0.1f);
//    ProgressIndicator progressIndicator = new ProgressIndicator(-1.0f);
    public boolean run(SelectedImage selectedImage, int iteration, Stage stage)throws IOException, InterruptedException{
//        Scene scene = stage.getScene();
//        Pane ancParent = (Pane) scene.getRoot();
        BufferedImage image = null;
        double Image_T[][] ;
        double osherrudin[][] ;
        double I_x,I_y,I_xx,I_yy,I_xy;
        int i,x,y;
        try {
            image= ImageIO.read(selectedImage.getFile());
        }catch (IOException e) {
            e.printStackTrace();
        }
        int width = image.getWidth();
        int height = image.getHeight();
        Image_T=new double[width+4][height+4];
        osherrudin=new double[width+4][height+4];
        
        double dt=0.25;
        int j,k;
        for(j=0; j<width+4; j++)
            for(k=0; k<height+4; k++)
                Image_T[j][k] = 100+Math.random()*100;
        
        for(j=0; j<width; j++)
            for(k=0; k<height; k++){
                int pixel = image.getRGB(j, k);
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                int moyen_pixel = (r+g+b)/3;
                Image_T[j+2][k+2] = moyen_pixel;
                pixel = (moyen_pixel << 24) | (moyen_pixel << 16) | (moyen_pixel << 8) | moyen_pixel;
                image.setRGB(j, k, pixel);
            }
        
        for(i=0;i<iteration;i++){
//            JFXToast.makeText(stage, "Traitement En cours..."+((int)(((i+1)*100)/iteration))+"%", 1, JFXToast.CENTTER);
//            JFXProgressIndicator.makeProgessIndicator(stage, ((i+1)*100)/iteration ,JFXProgressIndicator.CENTTER);
            System.out.println(((int)(((i+1)*100)/iteration))+"%");
            for (x = 1; x <width+3; x++) {
                for (y = 1; y <height+3; y++) {
                    double delta_x_plus = Image_T[x+1][y]-Image_T[x][y];
                    double delta_x_min = Image_T[x][y]-Image_T[x-1][y];
                    double delta_y_plus = Image_T[x][y+1]-Image_T[x][y];
                    double delta_y_min = Image_T[x][y]-Image_T[x][y-1];
                    I_x = minimumModule(delta_x_plus, delta_x_min);
                    I_y = minimumModule(delta_y_plus, delta_y_min); 
                    I_xx = Image_T[x+1][y] - 2*Image_T[x][y] + Image_T[x-1][y];
                    I_yy = Image_T[x][y+1] - 2*Image_T[x][y] + Image_T[x][y-1];
                    I_xy = 0.5*(Image_T[x+1][y+1]-Image_T[x+1][y]-Image_T[x][y+1]
                                    +2*Image_T[x][y]-Image_T[x][y-1]
                                    -Image_T[x-1][y]+Image_T[x-1][y-1]);
                    double root = dt*Math.sqrt(Math.pow(I_x,2) + Math.pow(I_y,2));
                    double L = I_xx*Math.pow(I_x,2) + 2*I_xy*I_x*I_y + I_yy*Math.pow(I_y,2);
                    root = root * sign(L);
                    osherrudin[x][y] = Image_T[x][y] - (int)root;
                }
            }
            
            for (x = 1; x <width+3; x++) {
                for (y = 1; y <height+3; y++) {
                    Image_T[x][y] = osherrudin[x][y];
                }
            }
            
            for (x = 0; x <width; x++) {
                for (y = 0; y < height; y++) {
                    int conv =(int)Image_T[x+2][y+2];
                    image.setRGB(x,y,(conv<<16 | conv<<8 | conv));
                }
            }
        }
        
        File outputfile = new File("output/result.png");
        return ImageIO.write(image, "png", outputfile);
    }
    
    
    private double minimum(double x, double y){
        if (x<y) return x;
	else return y;
    }
    
    private double sign(double x){
        double signe;
        if (x<0) signe = -1;
        else if(x>0) signe = 1;
        else signe = 0;
        return signe;
    }
    
    private double minimumModule(double x, double y){
        double minimumModule;
        if( (x*y) <= 0) minimumModule=0;
        else
            minimumModule = minimum(Math.abs(x), Math.abs(y))*sign(x);
        return minimumModule;
    }
}
