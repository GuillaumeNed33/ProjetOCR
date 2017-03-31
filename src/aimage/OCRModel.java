package aimage;

import ij.ImagePlus;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

public class OCRModel {

    private ArrayList<OCRImage> listImg = new ArrayList<OCRImage>();

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
        int[][] confusion = new int[10][10];

        //initialisation de la matrice
        for(int i=0; i<10; i++) {
            for(int j=0; j<10; j++) {
                confusion[i][j] = 0;
            }
        }

        //debut
        int l,c;
        for(int i=0;i<10;i++)
        {
            for(int j=0;j<10;j++)
            {
                l = Character.getNumericValue(listImg.get(i*10+j).getLabel());
                c =l;
                confusion[l][c]++;
            }
        }

        //ecriture dans pathOut.txt
        try {
            FileWriter fw = new FileWriter (pathOut);
            BufferedWriter bw = new BufferedWriter (fw);
            PrintWriter outFile = new PrintWriter (bw);

            Date date = new Date();
            outFile.println ("Test OCR effectués le "+ date.toString() + " .\n");

            outFile.print("   ");
            for(int i=0; i<10 ; i++) {
                outFile.print(Integer.toString(i) + "   ");
            }
            outFile.print("\n---------------------------------------\n");

            for(int i=0; i<10; i++) {
                outFile.print(Integer.toString(i) + " | ");
                for (int j=0; j<10; j++) {
                    outFile.print(Integer.toString(confusion[i][j]) + "  ");
                }
                outFile.print("\n");
            }
            outFile.print("\n---------------------------------------\n");
            outFile.close();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
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

    public ArrayList<OCRImage> getListImg() {
        return listImg;
    }
}
