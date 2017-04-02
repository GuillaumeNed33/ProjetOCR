import aimage.OCRModel;

public class Main {

    public static void main (String[] args) {
        OCRModel ocr = new OCRModel();
        ocr.createListeImage("resources", ocr.getListImg());
        ocr.resizeAll(20,20);
        ocr.setFeatureNdgVect();
        ocr.setProfilHV();
        ocr.setRapportIso();
        ocr.setZoning();
        ocr.compare();
        ocr.logOCR("result.txt");
    }
}
