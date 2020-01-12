package service;

import bean.SelectedImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class EmssService {
	
    public static BufferedImage source;
    public static BufferedImage output;
    public static int width,height;
    static double delta_t=0.5;
    
    public boolean run(SelectedImage selectedImage, int iter) throws IOException {
        double img[][];
        double emss[][];
        double Ix[][];
        double Iy[][];
        double Ixx[][];
        double Iyy[][];
        double Ixy[][];
        int i,x,y;

        TraitImage ic=new TraitImage();

        source=ic.loadImg(selectedImage.getFile());

        width=TraitImage.width;
        height=TraitImage.height;
        output=source;
        img=TraitImage.img;
        emss=Ixx=Iyy=Ix=Iy=Ixy=new double[width][height];
        
        for ( i = 0; i < iter; i++) {
            Ixx=ic.ImageXX();
            Iyy=ic.ImageYY();
            Ix=ic.ImageX();
            Iy=ic.ImageY();
            Ixy=ic.ImageXY();
            for ( x = 0; x < width; x++) {
                for ( y = 0; y < height ; y++) {
                    double a=(Ixx[x][y]*Iy[x][y]*Iy[x][y]-2*Ixy[x][y]
                                            *Ix[x][y]*Iy[x][y]+Iyy[x][y]*Ix[x][y]*Ix[x][y]);
                    double b=Ix[x][y]*Ix[x][y]+Iy[x][y]*Iy[x][y];
                    if(b==0){
                        b=0.0000001;
                    }
                    emss[x][y]=(a/b);
                    img[x][y]+=delta_t*emss[x][y];
                }
            }
            for ( x = 0; x < width; x++) {
                for ( y = 0; y < height ; y++) {
                    int conv =(int)img[x][y];
                    if(conv>255) conv=255;
                    output.setRGB(x,y,(conv<<16 | conv<<8 | conv));
                }
            }
    }
	//TraitImage.save(output,"Emss");

        File outputfile = new File("output/result.png");
        return ImageIO.write(output, "png", outputfile);
    }
}
