import aimage.OCRModel;

public class Main {

    public static void main (String[] args) {
        OCRModel ocr = new OCRModel();
        ocr.createListeImage("resources", ocr.getListImg());
        ocr.logOCR("matriceConfusion.txt");
    }
}
