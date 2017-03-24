/**
 * Created by Guillaume on 24/03/2017.
 */

import aimage.*;
import ij.ImagePlus;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

import java.io.File;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;

public class Main {
    private ArrayList<OCRImage> listImg;

    public void createListeImage(String path , ArrayList < OCRImage > listeImg)
    {
        File[] files = listFiles(path) ;
        if (files.length !=0)
        {
            for (int i=0; i < files.length ;i++) {
                ImagePlus tempImg = new ImagePlus (files[i].getAbsolutePath()) ;
                new ImageConverter(tempImg).convertToGray8();
                listeImg.add (new OCRImage (tempImg,
                        files[i].getName().substring(0 ,1).charAt(0),
                        files[i].getAbsolutePath()));
            }
        }
    }

    public File [] listFiles ( String directoryPath ) {
        File [] files = null ;
        File directoryToScan = new File ( directoryPath ) ;
        files = directoryToScan . listFiles () ;
        return files ;
    }

    public void logOCR(String pathOut) {

    }

    public void setFeatureNdgVect() {
        for(OCRImage img : listImg) {
            img.setFeatureNdg();
        }
    }

    public void resize ( ImagePlus img , int larg , int haut )
    {
        ImageProcessor ip2 = img.getProcessor();
        ip2.setInterpolate( true );
        ip2 = ip2.resize(larg , haut );
        img.setProcessor(null , ip2 );
    }

}
