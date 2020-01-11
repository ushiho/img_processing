package service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



public class TraitImage {
		
    public static double img[][];
    static public int width;
    static public int height;
    static double ccc=0.146446609406726237799577818947575;
    static double a=1.414213562373095048801688724209698;
	
    public   BufferedImage loadImg(File file) {

             BufferedImage image=null;

            try {
                     image= ImageIO.read(file);
            }catch (IOException e) {
                    e.printStackTrace();
            }

            width = image.getWidth();
            height = image.getHeight();
            img=new double[width][height];
             for (int x = 0; x < width; x++) {
                for (int y = 0; y < height ; y++) {
              img[x][y] = new Color(image.getRGB(x,y)).getRed();
             }}

            return image;	
    }
	
	

    public double [][]  Grad_soble_prewitt(double c) {

        double gx[][] ;
        double gy[][] ;
        double[][] gradient;

        double[][] Masque_X =  {{1.0,0.0,-1.0},{c,0.0,-c},{1.0,0.0,-1.0}};
        double[][] Masque_Y =  {{1.0,c,1.0},{0.0,0.0,0.0},{-1.0,-c,-1.0}};
        gradient=gx=gy=new double[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height ; y++) {
            if (x==0 || x==width-1 || y==0 || y==height-1)
               {
                   gradient[x][y]=0;gx[x][y]=0;gy[x][y]=0;
               }
            else{
                gx[x][y] =  (Masque_X[0][0] * img[x-1][y-1]) + (Masque_X[0][1] * img[x-1][y]) 
                + (Masque_X[0][2] * img[x-1][y+1]) + (Masque_X[1][0] * img[x][y-1]) + (Masque_X[1][1] * img[x][y]) + (Masque_X[1][2] * img[x][y+1]) 
                + (Masque_X[2][0] * img[x+1][y-1])+ (Masque_X[2][1] * img[x+1][y]) + (Masque_X[2][2] * img[x+1][y+1]);

                gy[x][y] =  (Masque_Y[0][0] * img[x-1][y-1]) + (Masque_Y[0][1] * img[x-1][y]) 
                + (Masque_Y[0][2] * img[x-1][y+1]) + (Masque_Y[1][0] * img[x][y-1]) + (Masque_Y[1][1] * img[x][y]) + (Masque_Y[1][2] * img[x][y+1]) 
                + (Masque_Y[2][0] * img[x+1][y-1])+ (Masque_Y[2][1] * img[x+1][y]) + (Masque_Y[2][2] * img[x+1][y+1]);


                gradient[x][y]= Math.hypot(gx[x][y],gy[x][y]);
            }
            }
        }
        return gradient;	
    }
   public  double[][] ImageX_Prewit_Sobel(double c) {
		
		
		 double gx[][] ;
		 double gy[][] ;
		double[][] gradient;
		
		double[][] Masque_X =  {{1.0,0.0,-1.0},{c,0.0,-c},{1.0,0.0,-1.0}};
		gradient=gx=gy=new double[width][height];
		 
 for (int x = 0; x < width; x++) {
    for (int y = 0; y < height ; y++) {
	if (x==0 || x==width-1 || y==0 || y==height-1)
	    {gradient[x][y]=0;gx[x][y]=0;gy[x][y]=0;}
	else{
            gx[x][y] =  (Masque_X[0][0] * img[x-1][y-1]) + (Masque_X[0][1] * img[x-1][y]) 
	    + (Masque_X[0][2] * img[x-1][y+1]) + (Masque_X[1][0] * img[x][y-1]) + (Masque_X[1][1] * img[x][y]) + (Masque_X[1][2] * img[x][y+1]) 
	    + (Masque_X[2][0] * img[x+1][y-1])+ (Masque_X[2][1] * img[x+1][y]) + (Masque_X[2][2] * img[x+1][y+1]);
			         }   	 
			    }}
		return gx;
	}
   public  double[][] ImageY_Prewit_Sobel(double c) {
		
		
		 double gx[][] ;
		 double gy[][] ;
		double[][] gradient;
		
		double[][] Masque_Y =  {{1.0,c,1.0},{0.0,0.0,0.0},{-1.0,-c,-1.0}};
		gradient=gx=gy=new double[width][height];
		 
 for (int x = 0; x < width; x++) {
    for (int y = 0; y < height ; y++) {
	if (x==0 || x==width-1 || y==0 || y==height-1)
	    {gradient[x][y]=0;gx[x][y]=0;gy[x][y]=0;}
	else{
            gy[x][y] =  (Masque_Y[0][0] * img[x-1][y-1]) + (Masque_Y[0][1] * img[x-1][y]) 
	    + (Masque_Y[0][2] * img[x-1][y+1]) + (Masque_Y[1][0] * img[x][y-1]) + (Masque_Y[1][1] * img[x][y]) + (Masque_Y[1][2] * img[x][y+1]) 
            + (Masque_Y[2][0] * img[x+1][y-1])+ (Masque_Y[2][1] * img[x+1][y]) + (Masque_Y[2][2] * img[x+1][y+1]);
			         }   	 
			    }}
		return gy;
	}
         
public double [][]  Gr3() {
		double gx[][] ;
		double gy[][] ;
		double[][] gradient;
		gradient=gx=gy=new double[width][height];
		 
		double[][] Masque_X =  {{0.25,-0.5,0.25},{0.5,-1.0,0.5},{0.25,-0.5,0.25}};
		double[][] Masque_Y =  {{0.25,0.5,0.25},{-0.5,-1.0,-0.5},{0.25,0.5,0.25}};
		

for (int x = 0; x < width; x++) {
   for (int y = 0; y < height ; y++) {
	if (x==0 || x==width-1 || y==0 || y==height-1)
	 {gradient[x][y]=0;gx[x][y]=0;gy[x][y]=0;}
	else{
	 gx[x][y] =  (Masque_X[0][0] * img[x-1][y-1]) + (Masque_X[0][1] * img[x-1][y]) 
	 + (Masque_X[0][2] * img[x-1][y+1]) + (Masque_X[1][0] * img[x][y-1]) + (Masque_X[1][1] * img[x][y]) + (Masque_X[1][2] * img[x][y+1]) 
	 + (Masque_X[2][0] * img[x+1][y-1])+ (Masque_X[2][1] * img[x+1][y]) + (Masque_X[2][2] * img[x+1][y+1]);
			    	 
	 gy[x][y] =  (Masque_Y[0][0] * img[x-1][y-1]) + (Masque_Y[0][1] * img[x-1][y]) 
	 + (Masque_Y[0][2] * img[x-1][y+1]) + (Masque_Y[1][0] * img[x][y-1]) + (Masque_Y[1][1] * img[x][y]) + (Masque_Y[1][2] * img[x][y+1]) 
	 + (Masque_Y[2][0] * img[x+1][y-1]) + (Masque_Y[2][1] * img[x+1][y]) + (Masque_Y[2][2] * img[x+1][y+1]);
			     	 
			     	gradient[x][y]= gx[x][y]+gy[x][y];
			         } 	 
			    }}
		return gradient;
}	 
	 
	 
/*
public static void save( BufferedImage resultat, String c) {
	  try {
		   ImageIO.write(resultat, "JPG",new File("./output/out"+c+".jpg"));
	      } 
		  catch (IOException e) {
		   e.printStackTrace();
		  }
}
*/
	public  double[][] ImageX() {
		
		
		double Ix[][] = new double[width][height];
		 double[][] Masque_X = {{ccc,0.0,-ccc},
				                {a*ccc,0.0,-a*ccc},
				                {ccc,0.0,-ccc}};
		
		/* double[][] Masque_X =   {{0.1464466,0.0,-0.1464466},
				 {0.2071067,0.0,-0.2071067},
				 {0.1464466,0.0,-0.1464466}};*/
		 
		 
		 
		 for (int x = 0; x < width; x++) {
			    for (int y = 0; y < height ; y++) {
                            
                                
			    	if (x==0 || x==width-1 || y==0 || y==height-1) {Ix[x][y]= 0; }
			         else{
			    	 Ix[x][y] =  (Masque_X[0][0] * img[x-1][y-1]) 
	  	      					+ (Masque_X[0][1] * img[x-1][y]) 
	  	      					+ (Masque_X[0][2] * img[x-1][y+1]) 
	  	      					+ (Masque_X[1][0] * img[x][y-1]) 
	  	      					+ (Masque_X[1][1] * img[x][y]) 
	  	      					+ (Masque_X[1][2] * img[x][y+1]) 
	  	      					+ (Masque_X[2][0] * img[x+1][y-1])
	  	      					+ (Masque_X[2][1] * img[x+1][y]) 
	  	      					+ (Masque_X[2][2] * img[x+1][y+1]);
			         }
			    	 
			    }}
		return Ix;
		 
		
		 	
	}
	
	
	public  double[][] ImageY() {
		
		double Iy[][] = new double[width][height];
		
		 double[][] Masque_Y = {{ccc,a*ccc,ccc},
				                 {0.0,0.0,0.0},
				                {-ccc,-a*ccc,-ccc}};
		/*	double[][] Masque_Y =  {{0.1464466,0.2071067,0.1464466},
				{0.0,0.0,0.0},
				{-0.1464466,-0.2071067,-0.1464466}};*/
		 
		 
		 
		 for (int x = 0; x < width; x++) {
			    for (int y = 0; y < height ; y++) {
			    	
			    	if (x==0 || x==width-1 || y==0 || y==height-1)
			         {Iy[x][y]= 0; }
			         else{
			    	
			    	 Iy[x][y] =  (Masque_Y[0][0] * img[x-1][y-1]) 
	  	      					+ (Masque_Y[0][1] * img[x-1][y]) 
	  	      					+ (Masque_Y[0][2] * img[x-1][y+1]) 
	  	      					+ (Masque_Y[1][0] * img[x][y-1]) 
	  	      					+ (Masque_Y[1][1] * img[x][y]) 
	  	      					+ (Masque_Y[1][2] * img[x][y+1]) 
	  	      					+ (Masque_Y[2][0] * img[x+1][y-1])
	  	      					+ (Masque_Y[2][1] * img[x+1][y]) 
	  	      					+ (Masque_Y[2][2] * img[x+1][y+1]);
			         }
			    }}
		return Iy;
		 
		
		 	
	}
	
	
	public  double[][] ImageXX() {
		
		double Ixx[][] =new double[width][height];
		
		
			double[][] Masque_XX =  {{0.25,-0.5,0.25},
									 {0.5,-1.0,0.5},
									 {0.25,-0.5,0.25}};
		
			
		 
		 
		 
		 for (int x = 0; x < width; x++) {
			    for (int y = 0; y < height ; y++) {
			    	
			    	 if (x==0 || x==width-1 || y==0 || y==height-1)
			         {Ixx[x][y]= 0; }
			         else{
			    	
			    	 Ixx[x][y] =  (Masque_XX[0][0] * img[x-1][y-1]) 
	  	      					+ (Masque_XX[0][1] * img[x-1][y]) 
	  	      					+ (Masque_XX[0][2] * img[x-1][y+1]) 
	  	      					+ (Masque_XX[1][0] * img[x][y-1]) 
	  	      					+ (Masque_XX[1][1] * img[x][y]) 
	  	      					+ (Masque_XX[1][2] * img[x][y+1]) 
	  	      					+ (Masque_XX[2][0] * img[x+1][y-1])
	  	      					+ (Masque_XX[2][1] * img[x+1][y]) 
	  	      					+ (Masque_XX[2][2] * img[x+1][y+1]);
			    	// System.out.println(Ixx[x][y]);
			    	
			         }
			    }}
		return Ixx;
		 
		
		 	
	}
	
	
public  double[][] ImageYY() {
		
	
	double Iyy[][] =new double[width][height];
	
		double[][] Masque_YY =  {{0.25,0.5,0.25},
			 {-0.5,-1.0,-0.5},
			 {0.25,0.5,0.25}};
		 
		 
		 for (int x = 0; x < width; x++) {
			    for (int y = 0; y < height ; y++) {
			    	
			    	 if (x==0 || x==width-1 || y==0 || y==height-1)
			         {Iyy[x][y]= 0; }
			         else{
			    	
			    	 Iyy[x][y] =  (Masque_YY[0][0] * img[x-1][y-1]) 
	  	      					+ (Masque_YY[0][1] * img[x-1][y]) 
	  	      					+ (Masque_YY[0][2] * img[x-1][y+1]) 
	  	      					+ (Masque_YY[1][0] * img[x][y-1]) 
	  	      					+ (Masque_YY[1][1] * img[x][y]) 
	  	      					+ (Masque_YY[1][2] * img[x][y+1]) 
	  	      					+ (Masque_YY[2][0] * img[x+1][y-1])
	  	      					+ (Masque_YY[2][1] * img[x+1][y]) 
	  	      					+ (Masque_YY[2][2] * img[x+1][y+1]);
			         }
			    }}
		return Iyy;
		 
		
		 	
	}


public double[][] ImageXY() {
	
	double Ixy[][] = new double[width][height];
	
  	double[][] Masque_XY =  {{0.25,0.0,-0.25},
			                 {0.0,0.0,0.0},
			                {-0.25,0.0,0.25}};
	 
	 
	 for (int x = 0; x < width; x++) {
		    for (int y = 0; y < height ; y++) {
		    	
		    	 if (x==0 || x==width-1 || y==0 || y==height-1)
		         {Ixy[x][y]= 0; }
		         else{
		    	 Ixy[x][y] =  (Masque_XY[0][0] * img[x-1][y-1]) 
  	      					+ (Masque_XY[0][1] * img[x-1][y]) 
  	      					+ (Masque_XY[0][2] * img[x-1][y+1]) 
  	      					+ (Masque_XY[1][0] * img[x][y-1]) 
  	      					+ (Masque_XY[1][1] * img[x][y]) 
  	      					+ (Masque_XY[1][2] * img[x][y+1]) 
  	      					+ (Masque_XY[2][0] * img[x+1][y-1])
  	      					+ (Masque_XY[2][1] * img[x+1][y]) 
  	      					+ (Masque_XY[2][2] * img[x+1][y+1]);
		         }
		    }}
	return Ixy;
	 

	 	
}



	 public double [][]  Gr(double c) {
		 
		 double gx[][] ;
		 double gy[][] ;
		double[][] gradient;
		gradient=gx=gy=new double[width][height];
		 
		/* double[][] Masque_X =  {{1.0,0.0,-1.0},
				 {c,0.0,-c},
				 {1.0,0.0,-1.0}};*/
		
		 double[][] Masque_X = {{ccc,0.0,-ccc},
                 {a*ccc,0.0,-a*ccc},
                {ccc,0.0,-ccc}};

		 

		 /*double[][] Masque_Y =  {{1.0,c,1.0},
				 {0.0,0.0,0.0},
				 {-1.0,-c,-1.0}};*/
		 
		 double[][] Masque_Y = {{ccc,a*ccc,ccc},
                 {0.0,0.0,0.0},
                {-ccc,-a*ccc,-ccc}};

		 for (int x = 0; x < width; x++) {
			    for (int y = 0; y < height ; y++) {
			    	
			    	if (x==0 || x==width-1 || y==0 || y==height-1)
			         {gradient[x][y]=0;gx[x][y]=0;gy[x][y]=0;
			         }
			    	
			         else{
			    	
			    	 gx[x][y] =  (Masque_X[0][0] * img[x-1][y-1]) 
	  	      					+ (Masque_X[0][1] * img[x-1][y]) 
	  	      					+ (Masque_X[0][2] * img[x-1][y+1]) 
	  	      					+ (Masque_X[1][0] * img[x][y-1]) 
	  	      					+ (Masque_X[1][1] * img[x][y]) 
	  	      					+ (Masque_X[1][2] * img[x][y+1]) 
	  	      					+ (Masque_X[2][0] * img[x+1][y-1])
	  	      					+ (Masque_X[2][1] * img[x+1][y]) 
	  	      					+ (Masque_X[2][2] * img[x+1][y+1]);
			    	 
			    	 
			     	 gy[x][y] =  (Masque_Y[0][0] * img[x-1][y-1]) 
	  	      					+ (Masque_Y[0][1] * img[x-1][y]) 
	  	      					+ (Masque_Y[0][2] * img[x-1][y+1]) 
	  	      					+ (Masque_Y[1][0] * img[x][y-1]) 
	  	      					+ (Masque_Y[1][1] * img[x][y]) 
	  	      					+ (Masque_Y[1][2] * img[x][y+1]) 
	  	      					+ (Masque_Y[2][0] * img[x+1][y-1])
	  	      					+ (Masque_Y[2][1] * img[x+1][y]) 
	  	      					+ (Masque_Y[2][2] * img[x+1][y+1]);
			     	 
			     	gradient[x][y]= Math.sqrt(Math.pow(gx[x][y],2.0)+Math.pow(gy[x][y],2.0));
			     	
			     	//System.out.println(gradient[x][y]);
			     	
			         }
			    	 
			    }}
		return gradient;

			    	
			    	
			  
	
}
	 
	 public double [][]  Gr2(double c) {
		 
		 double gx[][] ;
		 double gy[][] ;
		double[][] gradient;
		gradient=gx=gy=new double[width][height];
		 
		double[][] Masque_X =  {{1.0,0.0,-1.0},
				 {c,0.0,-c},
				 {1.0,0.0,-1.0}};
		
		
		 

		 double[][] Masque_Y =  {{1.0,c,1.0},
				 {0.0,0.0,0.0},
				 {-1.0,-c,-1.0}};
		

		 for (int x = 0; x < width; x++) {
			    for (int y = 0; y < height ; y++) {
			    	
			    	if (x==0 || x==width-1 || y==0 || y==height-1)
			         {gradient[x][y]=0;gx[x][y]=0;gy[x][y]=0;
			         }
			    	
			         else{
			    	
			    	 gx[x][y] =  (Masque_X[0][0] * img[x-1][y-1]) 
	  	      					+ (Masque_X[0][1] * img[x-1][y]) 
	  	      					+ (Masque_X[0][2] * img[x-1][y+1]) 
	  	      					+ (Masque_X[1][0] * img[x][y-1]) 
	  	      					+ (Masque_X[1][1] * img[x][y]) 
	  	      					+ (Masque_X[1][2] * img[x][y+1]) 
	  	      					+ (Masque_X[2][0] * img[x+1][y-1])
	  	      					+ (Masque_X[2][1] * img[x+1][y]) 
	  	      					+ (Masque_X[2][2] * img[x+1][y+1]);
			    	 
			    	 
			     	 gy[x][y] =  (Masque_Y[0][0] * img[x-1][y-1]) 
	  	      					+ (Masque_Y[0][1] * img[x-1][y]) 
	  	      					+ (Masque_Y[0][2] * img[x-1][y+1]) 
	  	      					+ (Masque_Y[1][0] * img[x][y-1]) 
	  	      					+ (Masque_Y[1][1] * img[x][y]) 
	  	      					+ (Masque_Y[1][2] * img[x][y+1]) 
	  	      					+ (Masque_Y[2][0] * img[x+1][y-1])
	  	      					+ (Masque_Y[2][1] * img[x+1][y]) 
	  	      					+ (Masque_Y[2][2] * img[x+1][y+1]);
			     	 
			     	gradient[x][y]= Math.hypot(gx[x][y],gy[x][y]);
			     	
			     	
			     	
			         }
			    	 
			    }}
		return gradient;

}


         
    public static double[][] convolution(double[][] pixels, int height , int width ,double[][] mask_x) {
        
        double pixel_x[][] = new double[width][height];        
        double px=0;
   

        for(int i=1;i<width-1; i++) {

            for(int j=1;j<height-1;j++) {

		for(int y=-1;y<2;y++) { 
                    for(int x=-1;x<2;x++){
                    if(mask_x!=null)  
                        px = px + pixels[i+y][j+x] * mask_x[y+1][x+1];
                    }    
                }
                
              //  px=(px>255)?255:px;px=(px<0)?0:px;
               // py=(py>255)?255:py;py=(py<0)?0:py;                
                pixel_x[i][j]=px;                
                px=0;
            }
        }
        
        return pixel_x;


        
    }

         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
}
