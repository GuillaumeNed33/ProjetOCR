package aimage;
import ij .*;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;

/**
 * Created by Guillaume on 23/03/2017.
 */

public class OCRImage {
    private ImagePlus img;// contenu de l’image
    private char label;// correspond au label de l’image ( donne par le nom du fichier --> 0 ,1 ,... ou 9)
    private String path;// path du fichier image
    private char decision;// affectation du label apres classification
    private ArrayList<Double> vect;// Vecteur de caracteristiques de l’image

    public ImagePlus getImg() {
        return img;
    }

    public char getLabel() {
        return label;
    }

    public ArrayList<Double> getVect() {
        return vect;
    }

    public char getDecision() {
        return decision;
    }

    public void setDecision(char decision) {
        this.decision = decision;
    }

    public OCRImage(ImagePlus img, char label, String path) {
        this.img = img;
        this.label = label;
        this.path = path;
        this.decision = '?';
        vect = new ArrayList<>();
    }

    public double AverageNdg() {
        ImageProcessor ip = img.getProcessor();
        byte[] pixels = (byte[]) ip.getPixels();

        int height = ip.getHeight();
        int width = ip.getWidth();

        double sum = 0;

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                sum += pixels[i * height + j] & 0xff;

        return sum / pixels.length;
    }

    public void resize(ImagePlus img, int larg, int haut) {
        ImageProcessor ip2 = img.getProcessor();
        ip2.setInterpolate(true);
        ip2 = ip2.resize(larg, haut);
        img.setProcessor(null, ip2);
    }


    public void setFeatureNdg() {
        vect.add(AverageNdg());
    }

    public void setFeatureProfilH() {
        new ImageConverter(img).convertToGray8();
        double valeur = 0;
        ImageProcessor ip = img.getProcessor();
        byte[] pixels = (byte[]) ip.getPixels();
        int width = ip.getWidth();
        int height = ip.getHeight();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                valeur += pixels[i * width + j] & 0xff;
                vect.add(valeur / pixels.length);
            }
        }
    }

    public void setFeatureProfilV() {
        new ImageConverter(img).convertToGray8();
        double valeur = 0;
        ImageProcessor ip = img.getProcessor();
        byte[] pixels = (byte[]) ip.getPixels();
        int width = ip.getWidth();
        int height = ip.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                valeur += pixels[i + width * j] & 0xff;
                vect.add(valeur / pixels.length);
            }
        }
    }

    public void setFeatureProfilHV() {
        setFeatureProfilH();
        setFeatureProfilV();
    }

    public void rapportIso() {
        double perimeter = 0;
        double surface = 0;

        ImageProcessor ip = this.img.getProcessor();
        byte[] pixels = (byte[]) ip.getPixels();
        for (int i = 0; i < ip.getHeight(); i++) {
            for (int j = 0; j < ip.getWidth(); j++) {
                int pix = pixels[i * ip.getWidth() + j] & 0xff;
                if (pix > 127) {
                    pixels[i * ip.getWidth() + j] = (byte) 0;
                } else {
                    pixels[i * ip.getWidth() + j] = (byte) 255;
                }
            }
        }
        for (int i = 1; i < ip.getWidth() - 1; i++) {
            for (int j = 1; j < ip.getHeight() - 1; j++) {
                int top = pixels[i + ip.getWidth() * j - ip.getWidth()] & 0xff;
                int right = pixels[i + ip.getWidth() * j + 1] & 0xff;
                int bottom = pixels[i + ip.getWidth() * j + ip.getWidth()] & 0xff;
                int left = pixels[i + ip.getWidth() * j - 1] & 0xff;
                int middle = pixels[i + ip.getWidth() * j] & 0xff;
                if (middle == 0) {
                    if (top == 255 || bottom == 255 || right == 255 || left == 255) {
                        perimeter++;
                    }
                    surface++;
                }
            }
        }
        this.vect.add(perimeter / (4 * Math.PI * surface));
    }


    public void zoning() {
        ImageProcessor imageProcessor = img.getProcessor();
        byte[] pixels = (byte[]) imageProcessor.getPixels();
        int height = imageProcessor.getHeight();
        int width = imageProcessor.getWidth();
        final int dim = 5;

        // liste qui va contenir notre liste de bytes pour calculer la moyenne
        ArrayList<ArrayList<Byte>> decoup = new ArrayList<>(dim * dim);

        // initialisation de la liste
        for (int i = 0; i < dim * dim; i++) {
            decoup.add(new ArrayList<>());
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int blocX = j / (width / dim);
                int blocY = i / (height / dim);

                // identifie ici le bloc dans lequel on se situe
                int id = blocX + blocY * dim;

                if (id < (dim * dim)) {
                    decoup.get(id).add(pixels[i * width + j]);
                }
            }
        }

        // calculer les niveaux de gris et ajouter les résultats au vecteur de l'image
        for (int i = 0; i < decoup.size(); i++) {
            double sum = 0;
            for (int j = 0; j < decoup.get(i).size(); j++) {
                sum += decoup.get(i).get(j) & 0xff;
            }
            vect.add(sum / decoup.get(i).size());
        }
    }
}