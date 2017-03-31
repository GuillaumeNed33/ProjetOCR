package aimage;

import java.util.ArrayList;

public class CalculMath {

    public static double distEucli( ArrayList<Double>vect1,ArrayList <Double>vect2) {
        double res = 0;
        for(int i = 0; i < vect1.size(); i++) {
            res += Math.pow(vect1.get(i) - vect2.get(i),2);
        }
        return Math.sqrt(res);
    }

    public static int PPV(ArrayList<Double>vect,ArrayList<ArrayList<Double> >tabVect,int except) throws ArrayIndexOutOfBoundsException {
        int pos = 0;
        for(int i = 0; i<tabVect.size();i++) {
            if(tabVect.get(i).size() != vect.size()) {
                throw new ArrayIndexOutOfBoundsException();
            }
            if(i != except && distEucli(vect,tabVect.get(i)) < distEucli(vect,tabVect.get(pos))) {
                pos = i;
            }
        }
        return pos;
    }
}
