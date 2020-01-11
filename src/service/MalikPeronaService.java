package service;

import bean.SelectedImage;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static service.EMSS.output;


public class MalikPeronaService {
    public CountourService countourService = new CountourService();
    
    public BufferedImage source;
    public BufferedImage intermediaire;
    public BufferedImage output;
    double K_VALUE = 1.0;
    double img[][] ;
    double img_xx[][];
    double img_yy[][] ;
    double sum[][];
    double temp[][];

    public int i,x,y;


    public boolean run(SelectedImage selectedImage, int iteration) throws IOException {
        double lambda=0.25;

        double GRAD_S[][];
        double GRAD_N[][];
        double GRAD_W[][];
        double GRAD_E[][];

        double C_S[][];
        double C_N[][];
        double C_W[][];
        double C_E[][];
        try {
            source= ImageIO.read(selectedImage.getFile());
            intermediaire=source;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        GRAD_S=new double[source.getWidth()][source.getHeight()];
        GRAD_N=new double[source.getWidth()][source.getHeight()];
        GRAD_W=new double[source.getWidth()][source.getHeight()];
        GRAD_E=new double[source.getWidth()][source.getHeight()];

        C_S=new double[source.getWidth()][source.getHeight()];
        C_N=new double[source.getWidth()][source.getHeight()];
        C_W=new double[source.getWidth()][source.getHeight()];
        C_E=new double[source.getWidth()][source.getHeight()];

        img=new double[source.getWidth()][source.getHeight()];
        img_xx=new double[source.getWidth()][source.getHeight()];
        img_yy=new double[source.getWidth()][source.getHeight()];
        sum=new double[source.getWidth()][source.getHeight()];


        double delta_t=0.5;
        int j,k;

        for(j=0; j<source.getWidth(); j++)
            for(k=0; k<source.getHeight(); k++){
                {
                    img[j][k] = new Color(source.getRGB(j,k)).getRed();
                }
            }

        for(i=0;i<iteration;i++){
            for (x = 0; x < intermediaire.getWidth(); x++) {
                for (y = 0; y < intermediaire.getHeight() ; y++) {
                    if (x==0 || x==intermediaire.getWidth()-1 || y==0 || y==intermediaire.getHeight()-1){
                        img_xx[x][y] = img_yy[x][y] = img[x][y] = 0;
                    }
                    else{
                        GRAD_N[x][y]=img[x-1][y] - img[x][y];
                        GRAD_S[x][y]=img[x+1][y] - img[x][y];
                        GRAD_W[x][y]=img[x][y-1] - img[x][y];
                        GRAD_E[x][y]=img[x][y+1] - img[x][y];

                        C_N[x][y]=exp(Math.abs(GRAD_N[x][y]));
                        C_S[x][y]=exp(Math.abs(GRAD_S[x][y]));
                        C_W[x][y]=exp(Math.abs(GRAD_W[x][y]));
                        C_E[x][y]=exp(Math.abs(GRAD_E[x][y]));
                        sum[x][y]=GRAD_N[x][y]*C_N[x][y]+GRAD_S[x][y]*C_S[x][y]+GRAD_W[x][y]*C_W[x][y]+GRAD_E[x][y]*C_E[x][y];
                    }
                }
            }

            for (x = 0; x < intermediaire.getWidth(); x++) {
                for (y = 0; y < intermediaire.getHeight() ; y++) {
                    if (x==0 || x==intermediaire.getWidth()-1 || y==0 || y==intermediaire.getHeight()-1){
                        sum[x][y] = img[x][y] = 0;
                    }
                    else {
                        img[x][y]+=lambda*sum[x][y];
                    }
                    int conv =(int)img[x][y];
                    intermediaire.setRGB(x,y,(conv<<16 | conv<<8 | conv));
                }
            }
        }
        output=intermediaire;

        File outputfile = new File("output/result.png");
        return ImageIO.write(output, "png", outputfile);
    }

    public double exp(double gradient){
        return (1/(1+Math.pow(Math.abs(gradient)/K_VALUE, 2)));
    }

    public double seuillage_exp(double gradient,double K){
        return (Math.exp(-(Math.pow(Math.abs(gradient)/K,2))));
    }

}