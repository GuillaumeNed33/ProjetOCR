package aimage;

import ij.ImagePlus;
import ij.process.ImageConverter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

public class OCRModel {

    private ArrayList<OCRImage> listImg = new ArrayList<>();

    public void createListeImage(String path , ArrayList < OCRImage > listeImg) {
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

    public void compare() {
        ArrayList<ArrayList<Double>> vectors = new ArrayList<>();
        for(OCRImage img : listImg)
            vectors.add(img.getVect());

        for (int i = 0; i < listImg.size(); i++) {
            int res = CalculMath.PPV(listImg.get(i).getVect(), vectors, i);
            if (listImg.get(res).getLabel() != '+' && listImg.get(res).getLabel() != '-') {
                listImg.get(i).setDecision(listImg.get(res).getLabel());
            }
        }
    }

    public void logOCR(String pathOut) {
        int[][] confusion = new int[10][10];

        //initialisation de la matrice
        for(int i=0; i<10; i++) {
            for(int j=0; j<10; j++) {
                confusion[i][j] = 0;
            }
        }

        for(OCRImage img : listImg) {
            if (img.getLabel() != '+' && img.getLabel() != '-' && img.getDecision() != '?') {
                int l = Integer.parseInt(String.valueOf(img.getLabel()));
                int c = Integer.parseInt(String.valueOf(img.getDecision()));
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
            outFile.println ("--- Matrice de confusion ---\n");

            outFile.print("    ");
            for(int i=0; i<10 ; i++) {
                outFile.print(Integer.toString(i) + "   ");
            }
            outFile.print("\n------------------------------------------\n");

            double total = 0, value = 0;
            for(int i=0; i<10; i++) {
                outFile.print(Integer.toString(i) + " | ");
                for (int j=0; j<10; j++) {
                    outFile.print(Integer.toString(confusion[i][j]) + "   ");
                    if (i == j) {
                        value += confusion[i][j];
                    }
                    total += confusion[i][j];
                }
                outFile.print("\n");
            }
            outFile.print("------------------------------------------\n");

            value /= total;
            outFile.print("Le taux de reconnaissance est de : " + Math.floor(value * 100) + "%.");
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

    public void setProfilHV() {
        for(OCRImage img : listImg) {
            img.setFeatureProfilHV();
        }
    }

    public void setRapportIso() {
        for(OCRImage img : listImg) {
            img.rapportIso();
        }
    }

    public void setZoning() {
        for(OCRImage img : listImg) {
            img.zoning();
        }
    }

    public void resizeAll(int larg , int haut ) {
        for(OCRImage i : listImg) {
            i.resize(i.getImg(), larg, haut);
        }
    }

    public ArrayList<OCRImage> getListImg() {
        return listImg;
    }
}
