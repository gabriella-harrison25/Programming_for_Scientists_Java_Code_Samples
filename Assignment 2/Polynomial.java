/*
 * PROJECT II: Polynomial.java
 *
 * This file contains a template for the class Polynomial. Not all methods are
 * implemented. Make sure you have carefully read the project formulation
 * before starting to work on this file.
 *
 * This class is designed to use Complex in order to represent polynomials
 * with complex co-efficients. It provides very basic functionality and there
 * are very few methods to implement! The project formulation contains a
 * complete description.
 *
 * Remember not to change the names, parameters or return types of any
 * variables in this file! You should also test this class using the main()
 * function.
 *
 * The function of the methods and instance variables are outlined in the
 * comments directly above them.
 * 
 * Tasks:
 *
 * 1) Complete this class with the indicated methods and instance variables.
 *
 * 2) Fill in the following fields:
 *
 * NAME: Gabriella Harrison
 * UNIVERSITY ID: 5683879
 * DEPARTMENT: Mathematics
 */

public class Polynomial {
    /**
     * An array storing the complex co-efficients of the polynomial.
     */
    Complex[] coeff;

    // ========================================================
    // Constructor functions.
    // ========================================================

    /**
     * General constructor: assigns this polynomial a given set of
     * co-efficients.
     *
     * @param coeff  The co-efficients to use for this polynomial.
     */
    public Polynomial(Complex[] coeff) {
        // You need to fill in this function.
        int n = coeff.length;
        while(n>1 && (coeff[n-1]).getReal()==0.0 && coeff[n-1].getImag()==0.0){
            n--;} //find last nonzero coeff (to det degree)
        this.coeff = new Complex[n];
        for (int i=0; i<n; i++){
            this.coeff[i]=coeff[i];
        }
        }
    
    /**
     * Default constructor: sets the Polynomial to the zero polynomial.
     */
    public Polynomial() {
        // You need to fill in this function.
        coeff = new Complex[]{new Complex(0,0) };}

    // ========================================================
    // Operations and functions with polynomials.
    // ========================================================

    /**
     * Return the coefficients array.
     *
     * @return  The coefficients array.
     */
    public Complex[] getCoeff() {
        // You need to fill in this method with the correct code.
        return coeff;
    }

    /**
     * Create a string representation of the polynomial.
     * Use z to represent the variable.  Include terms
     * with zero co-efficients up to the degree of the
     * polynomial.
     *
     * For example: (-5.000+5.000i) + (2.000-2.000i)z + (-1.000+0.000i)z^2
     */
    public String toString() {
        // You need to fill in this method with the correct code.
        String s = "";
        for(int i=0; i<coeff.length;i++){
            if(i>0){
                s=s+" + ";
            }
            if(i==0){
                s = s+coeff[i];
            }
            else if(i==1){
                s=s+coeff[i]+ "z";}
            else{
                s = s+coeff[i]+"z^"+i;
            }
            }
        return s;}
    

    /**
     * Returns the degree of this polynomial.
     */
    public int degree() {
        // You need to fill in this method with the correct code.
        return coeff.length-1;
    }

    /**
     * Evaluates the polynomial at a given point z.
     *
     * @param z  The point at which to evaluate the polynomial
     * @return   The complex number P(z).
     */
    public Complex evaluate(Complex z) {
        // You need to fill in this method with the correct code.
        Complex result = new Complex(0,0);
        for(int i=coeff.length-1; i>=0; i--){
            result = result.multiply(z).add(coeff[i]);
        }
        return result;
    }

    
    // ========================================================
    // Tester function.
    // ========================================================

    public static void main(String[] args) {
        // You can fill in this function with your own testing code.
        Polynomial p = new Polynomial();
        System.out.print("Default Polynomial \n" + p.toString()+ "\n");
        Complex c1 = new Complex(1,2);
        Complex c2 = new Complex(3,4);
        Complex[] coeff={c1,c2};
        Polynomial p2 = new Polynomial(coeff);
        System.out.print("New Polynomial \n" + p2.toString() + "\n");

        System.out.print("Coefficients of p2 (should be 1+2i,3+4i): \n"+p2.getCoeff()[0] + " " + p2.getCoeff()[1] + "\n");
        System.out.print("Degree of p2 (should be 1): \n" + p2.degree() + "\n");
        Complex z = new Complex(1,0);
        System.out.print("p2 evaluated at z (should be 4+6i) : \n" + p2.evaluate(z)+"\n");
    }
}
