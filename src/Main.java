import aimage.CalculMath;
import aimage.OCRModel;
import ij.IJ;

import java.util.ArrayList;

public class Main {

    public static void main (String[] args) {

        ArrayList <Double > tab0 = new ArrayList < Double >() ; tab0 . add (1.0) ; tab0 .add (1.0) ;
        ArrayList <Double > tab1 = new ArrayList < Double >() ; tab1 . add (5.0) ; tab1 .add (-1.0) ;
        ArrayList <Double > tab2 = new ArrayList < Double >() ; tab2 . add (2.0) ; tab2 .add (1.0) ;
        ArrayList <Double > tab3 = new ArrayList < Double >() ; tab3 . add (-1.0) ; tab3 .add (0.0) ;
        IJ.showMessage(Double.toString(CalculMath.distEucli(tab0,tab1)));
        IJ.showMessage(Double.toString(CalculMath.distEucli(tab0,tab2)));
        IJ.showMessage(Double.toString(CalculMath.distEucli(tab0,tab3)));
        ArrayList < ArrayList <Double > > myList = new ArrayList < ArrayList < Double > >() ;
        myList.add(tab1);myList.add(tab2);myList.add(tab3);
        IJ. showMessage (" dist = "+ CalculMath.PPV(tab0 , myList, -1) ) ;

        OCRModel ocr = new OCRModel();
        ocr.createListeImage("D:\\Guillaume\\Documents\\Cours\\DUT_2nd_annee\\S4\\Image\\Projet OCR\\resources", ocr.getListImg());
        ocr.logOCR("D:\\Guillaume\\Documents\\Cours\\DUT_2nd_annee\\S4\\Image\\Projet OCR\\result.txt");

    }

}
