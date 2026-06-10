/*
 * PROJECT III: Project3.java
 *
 * This file contains a template for the class Project3. None of methods are
 * implemented and they do not have placeholder return statements. Make sure 
 * you have carefully read the project formulation before starting to work 
 * on this file. You will also need to have completed the Matrix class, as 
 * well as GeneralMatrix and TriMatrix.
 *
 * Remember not to change the names, parameters or return types of any
 * variables in this file!
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

public class Project3 {
    /**
     * Calculates the variance of the distribution defined by the determinant
     * of a random matrix. See the formulation for a detailed description.
     *
     * @param matrix      The matrix object that will be filled with random
     *                    samples.
     * @param nSamp       The number of samples to generate when calculating 
     *                    the variance. 
     * @return            The variance of the distribution.
     */
    public static double matVariance(Matrix matrix, int nSamp) {
        // You need to fill in this method.

        double sum1 = 0.0; //the first sum of squared dets
        double sum2 = 0.0; //second sum of just single dets

        for(int i=1; i<= nSamp; i++){
            matrix.random(); //fills each matrix randomly with values in U(0,1)
            double det = matrix.determinant(); //find determinant of all the matrices
            sum1 += det * det; //builds up sums as it loops
            sum2 += det;
        }
        
        double term1 = sum1/nSamp; //term 1 of formula
        double term2 = sum2/nSamp; //second term in expression
        double variance = term1 - (term2*term2);//variance 
        
    return variance;

    }
    
    /**
     * This function should calculate the variances of matrices for matrices
     * of size 2 <= n <= 50 and print the results to the output. See the 
     * formulation for more detail.
     */
    public static void main(String[] args) {
        // You need to fill in this method.

        for(int n=2; n<=50; n++){//change upper to 50 later
            GeneralMatrix genmatrix = new GeneralMatrix(n,n); //making the matrices
            TriMatrix trimatrix = new TriMatrix(n);
            double varGen = matVariance(genmatrix,20000); //change to 20,000 later //generating samples
            double varTri = matVariance(trimatrix,200000); //change to 200,000 later
        
            System.out.printf("%d %.15e %.15e%n", n, varGen, varTri); //printing out data
        }

    }
}
