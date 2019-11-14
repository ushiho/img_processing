/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.SelectedImage;
import java.io.IOException;

/**
 *
 * @author swiri
 */
public class PrewittService {
    double[][] prewittIx = {{-1, 0, 1}, {-1, 0, 1}, {-1, 0, 1}};
    double[][] prewittIy = {{-1, -1, -1}, {0, 0, 0}, {1, 1, 1}};
     CountourService countourService = new CountourService(prewittIx, prewittIy);
   
    public boolean prewitt(SelectedImage selectedImage, double scale) throws IOException{
        return countourService.deriveResult(selectedImage, scale);
    }
    
    public boolean prewittIY(SelectedImage selectedImage) throws IOException{
        return countourService.deriveIY(selectedImage);
    }
    
    public boolean prewittIX(SelectedImage selectedImage) throws IOException{
        return countourService.deriveIX(selectedImage);
    } 
}
