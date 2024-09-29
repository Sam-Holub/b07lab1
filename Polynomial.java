import java.io.*;
import java.util.*;

public class Polynomial {
    private double[] coefficients;
    private int[] exponents;

    public Polynomial() {
        this.coefficients = new double[]{0.0};
        this.exponents = new int[]{0};
    }

    public Polynomial(double[] coefficients, int[] exponents) {
        this.coefficients = coefficients.clone();
        this.exponents = exponents.clone();
    }

    public Polynomial(File inputFile) throws IOException {
    BufferedReader fileReader = new BufferedReader(new FileReader(inputFile));
    String polynomialExpression = fileReader.readLine();
    fileReader.close();

    String[] polynomialTerms = polynomialExpression.split("(?=[+-])");
    
    int[] exponentArray = new int[polynomialTerms.length];
    double[] coefficientArray = new double[polynomialTerms.length];
    int i = 0;

    for (String term : polynomialTerms) {
        if (term.contains("x")) {
            String[] termChunks = term.split("x\\^?|x");

            double parsedCoefficient;
            int parsedExponent;

            if (termChunks[0].isEmpty() || termChunks[0].equals("+")) {
                parsedCoefficient = 1;
            } 
            else if (termChunks[0].equals("-")) {
                parsedCoefficient = -1;
            } 
            else {
                parsedCoefficient = Double.parseDouble(termChunks[0]);
            }

            if (termChunks.length == 1) {
                parsedExponent = 1;
            } 
            else {
                parsedExponent = Integer.parseInt(termChunks[1]);
            }

            coefficientArray[i] = parsedCoefficient;
            exponentArray[i] = parsedExponent;
        } 
        else {
            coefficientArray[i] = Double.parseDouble(term);
            exponentArray[i] = 0;
        }
        i++;
    }

    this.coefficients = new double[i];
    this.exponents = new int[i];

    System.arraycopy(coefficientArray, 0, this.coefficients, 0, i);
    System.arraycopy(exponentArray, 0, this.exponents, 0, i);
}


    // pain :(
    public Polynomial add(Polynomial other) {
        Map<Integer, Double> polyList = new TreeMap<>();
        int exponent;
        double coefficient;

        for (int i = 0; i < this.coefficients.length; i++) {
            coefficient = this.coefficients[i];
            exponent = this.exponents[i];
            polyList.put(exponent, coefficient);
        }

        for (int i = 0; i < other.coefficients.length; i++) {
            exponent = other.exponents[i];
            coefficient = other.coefficients[i];

            if (polyList.containsKey(exponent) && (coefficient == -(polyList.get(exponent)))) {
                polyList.remove(exponent); 
            } 
            else {
                polyList.put(exponent, (polyList.getOrDefault(exponent, 0.0) + coefficient));
            }
        }

        double[] resultCoefficients = new double[polyList.size()];
        int[] resultExponents = new int[polyList.size()];

        int i = 0;
        for (Map.Entry<Integer, Double> entry : polyList.entrySet()) {
            resultExponents[i] = entry.getKey();
            resultCoefficients[i] = entry.getValue();
            i++;
        }

        return new Polynomial(resultCoefficients, resultExponents);
    }

    // not that bad [|87
    public Polynomial multiply(Polynomial other) {
        Map<Integer, Double> polyList = new TreeMap<>();
        for (int i = 0; i < this.coefficients.length; i++) {
            for (int j = 0; j < other.coefficients.length; j++) {
                int tempExp = this.exponents[i] + other.exponents[j];
                double tempCoef = this.coefficients[i] * other.coefficients[j];
                polyList.put(tempExp, polyList.getOrDefault(tempExp, 0.0) + tempCoef);
            }
        }

        double[] resultCoeff = new double[polyList.size()];
        int[] resultExp = new int[polyList.size()];
        int i = 0;
        for (Map.Entry<Integer, Double> entry : polyList.entrySet()) {
            resultExp[i] = entry.getKey();
            resultCoeff[i] = entry.getValue();
            i++;
        }

        return new Polynomial(resultCoeff, resultExp);
    }

    public double evaluate(double x_val) {
        double result = 0.0;
        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x_val, exponents[i]);
        }
        return result;
    }

    public boolean hasRoot(double val) {
        return evaluate(val) == 0.0;
    }

    public void saveToFile(String name) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(name)))
        {
            String polynomialStr = ""; 
            int currentExponent;
            double currentCoefficient;


            for (int i = 0; i < coefficients.length; i++) 
            {
                currentCoefficient = coefficients[i];
                currentExponent = exponents[i];

                if (currentCoefficient == 0) {
                    continue;
                }

                if (i > 0 && currentCoefficient > 0) {
                    polynomialStr += "+";
                }
                polynomialStr += currentCoefficient;

                if (currentExponent > 0) {
                    polynomialStr += "x";
                    if (currentExponent > 1) {
                        polynomialStr += "^" + currentExponent;
                    }
                }
            }

            writer.write(polynomialStr);
        }
    }
}