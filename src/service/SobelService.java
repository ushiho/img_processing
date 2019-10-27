/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.io.IOException;
import bean.SelectedImage;

/**
 *
 * @author swiri
 */
public class SobelService {    
    int[][] sobelIx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
    int[][] sobelIy = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
    
    CountourService countourService = new CountourService(sobelIx, sobelIy);
   
    public boolean sobel(SelectedImage selectedImage, double scale) throws IOException{
        return countourService.deriveResult(selectedImage, scale);
    }
    
    public boolean sobelIY(SelectedImage selectedImage) throws IOException{
        return countourService.deriveIY(selectedImage);
    }
    
    public boolean sobelIX(SelectedImage selectedImage) throws IOException{
        return countourService.deriveIX(selectedImage);
    }
    
}
