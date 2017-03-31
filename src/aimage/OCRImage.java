package aimage;
import ij .*;
import ij.process.ImageProcessor;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;

/**
 * Created by Guillaume on 23/03/2017.
 */

public class OCRImage {
    private ImagePlus img ;// contenu de l’image
    private char label ;// correspond au label de l’image ( donne par le nom du fichier --> 0 ,1 ,... ou 9)
    private String path ;// path du fichier image

    public char getLabel() {
        return label;
    }

    public void setLabel(char label) {
        this.label = label;
    }

    public char getDecision() {
        return decision;
    }

    public void setDecision(char decision) {
        this.decision = decision;
    }

    private char decision ;// affectation du label apres classification
    private ArrayList<Double > vect ;// Vecteur de caracteristiques de l’image
    public OCRImage ( ImagePlus img , char label , String path )
    {
        resize(img,20,20);
        this.label = label ;
        this.path = path ;
        this.decision = '?';
        vect = new ArrayList<Double>();
    }
    public void setImg( ImagePlus img){ this.img=img ;}
    public ImagePlus getImg(){ return img ;}
    public void setVect( int i, double val){ this.vect.set(i,val );}
    public Double getVect( int i){ return vect.get(i);}

    public double AverageNdg() {
        ImageProcessor ip = img.getProcessor();
        byte[] pixels = (byte[]) ip.getPixels();

        int height = ip.getHeight();
        int width = ip.getWidth();

        double sum = 0;

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                sum += pixels[i*height + j] & 0xff;

        return sum/(height*width);
    }

    public void resize ( ImagePlus img , int larg , int haut ) {
        ImageProcessor ip2 = img.getProcessor () ;
        ip2.setInterpolate(true) ;
        ip2 = ip2.resize(larg,haut) ;
        img.setProcessor(null ,ip2) ;
    }


    public void setFeatureNdg() {
            vect.clear();
            vect.add(AverageNdg());
        }
}