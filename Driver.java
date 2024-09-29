import java.io.IOException;

public class Driver {
    public static void main(String[] args) {
        Polynomial p = new Polynomial();
        System.out.println("p(3) = " + p.evaluate(3));

        double[] c1 = {6, 0, 5};
        int[] e1 = {0, 1, 2};
        Polynomial p1 = new Polynomial(c1, e1);

        double[] c2 = {-2, -9};
        int[] e2 = {1, 4};
        Polynomial p2 = new Polynomial(c2, e2);

        Polynomial s = p1.add(p2);
        s = s.multiply(p2);
        
        System.out.println("s(0.1) = " + s.evaluate(0.1));

        if (s.hasRoot(1)) {
            System.out.println("1 is a root of s");
        } 
        else {
            System.out.println("1 is not a root of s");
        }

        try {
            s.saveToFile("output_polynomial.txt");
            System.out.println("Polynomial saved as output_polynomial.txt");
        } 
        catch (IOException e) {
            System.out.println("Couldn't save file");
        }
    }
}