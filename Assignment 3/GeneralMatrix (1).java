/*
 * PROJECT III: GeneralMatrix.java
 *
 * This file contains a template for the class GeneralMatrix. Not all methods
 * implemented and they do not have placeholder return statements. Make sure 
 * you have carefully read the project formulation before starting to work 
 * on this file. You will also need to have completed the Matrix class.
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

import java.util.Arrays;

public class GeneralMatrix extends Matrix {
    /**
     * This instance variable stores the elements of the matrix.
     */
    private double[][] values;

    /**
     * Constructor function: should initialise iDim and jDim through the Matrix
     * constructor and set up the data array.
     *
     * @param firstDim   The first dimension of the array.
     * @param secondDim  The second dimension of the array.
     */
    public GeneralMatrix(int firstDim, int secondDim) {
        // You need to fill in this method.
        super(firstDim,secondDim); //calls from Matrix superclass

        if(firstDim<=0||secondDim<=0){
            throw new MatrixException("Matrix dimensions must be positive");
        }
        values = new double[firstDim][secondDim]; //fills matrixwith 0's initially
    }

    /**
     * Constructor function. This is a copy constructor; it should create a
     * copy of the second matrix.
     *
     * @param second  The matrix to create a copy of.
     */
    public GeneralMatrix(GeneralMatrix second) {
        // You need to fill in this method.
        super(second.iDim, second.jDim); //copy the dimensions of the new matrix
        
        if(second==null){ //check for null matrix
            throw new MatrixException("Cannot copy null matrix");
        }

        values = new double[iDim][jDim]; //makes new matrix
        for (int i=0; i<iDim; i++){
            for(int j=0; j<jDim; j++){
                values[i][j]=second.getIJ(i,j); //copy the values individually in loop
            }
        }
    }
    
    /**
     * Getter function: return the (i,j)'th entry of the matrix.
     *
     * @param i  The location in the first co-ordinate.
     * @param j  The location in the second co-ordinate.
     * @return   The (i,j)'th entry of the matrix.
     */
    public double getIJ(int i, int j) {
        // You need to fill in this method.
        //NOTE: Check indices of func.
        if(i<0||i>=iDim||j<0||j>=jDim){ //check the indices are in the correct bounds
            throw new MatrixException("Invalid matrix index");
        }
        return values[i][j]; //return the value ifthe indices are fine

    }
    
    /**
     * Setter function: set the (i,j)'th entry of the values array.
     *
     * @param i      The location in the first co-ordinate.
     * @param j      The location in the second co-ordinate.
     * @param value  The value to set the (i,j)'th entry to.
     */
    public void setIJ(int i, int j, double value) {
        // You need to fill in this method.
        //NOTE: Check matrix indices again
        if(i<0||i>=iDim||j<0||j>=jDim){ //checking the indices entered
            throw new MatrixException("Invalid matrix index");
        }
        values[i][j]=value; //set that particular entry to the value entered
    }
    
    /**
     * Return the determinant of this matrix.
     *
     * @return The determinant of the matrix.
     */
    public double determinant() {
        // You need to fill in this method.

        /*NOTE: the det of this square matrix which has been decomposed is
        equal to the det of the lower matrix times det of upper matrix. But
        diagonals of lower matrix are all 1, so det(lower)=1. So only upper matrix
        matters. Hence we multiply the diagonal elements in "upper" matrix
        and thats the det of the whole thing */

        if (this.iDim !=this.jDim){ //check for squareness
            throw new MatrixException("Matrix must be square");
        }
        double[] sign = new double[1]; //create the sign parameter required for LUdecomp
        GeneralMatrix decomp = this.LUdecomp(sign); //do the decomposition
        double det = sign[0]; //take the appropriate value of sign for det

        for (int i=0; i<iDim; i++){
            det *=decomp.getIJ(i,i); //multiply diagonal elements
        }
        return det;
    }

    /**
     * Add the matrix to another second matrix.
     *
     * @param second  The Matrix to add to this matrix.
     * @return   The sum of this matrix with the second matrix.
     */
    public Matrix add(Matrix second) {
        // You need to fill in this method.
        //NOTE: check for dimension alignment
        GeneralMatrix result = new GeneralMatrix(this.iDim, this.jDim);

        if(this.iDim != second.iDim || this.jDim != second.jDim){
            throw new MatrixException("Dimensions do not match for addition");
        }
        if(second ==null){
            throw new MatrixException("Cannot add null matrix");
        }
    
        for (int i=0; i<iDim; i++){
                for (int j=0; j<jDim; j++){
                    double sum = this.getIJ(i,j)+second.getIJ(i,j);
                    result.setIJ(i,j,sum);
                }
            }
        return result;
    }
    
    /**
     * Multiply the matrix by another matrix A. This is a _left_ product,
     * i.e. if this matrix is called B then it calculates the product BA.
     *
     * @param A  The Matrix to multiply by.
     * @return   The product of this matrix with the matrix A.
     */
    public Matrix multiply(Matrix A) {
        // You need to fill in this method.
        //NOTE:
        //check for dimension alignment

        if(A==null){//check for null matrix
            throw new MatrixException("Cannot multiply by null matrix");
        }

        GeneralMatrix result = new GeneralMatrix(this.iDim, A.jDim); //make new matrix
        if(this.jDim != A.iDim){
            throw new MatrixException("The dimensions do not align for matrix multiplication");
        } //cannot multiply wrong dims

        if(this.iDim<=0 || A.jDim<=0){
            throw new MatrixException("Invalid matrix dimensions"); //check individual matrix dims are fine
        }

        for (int i=0; i<this.iDim; i++){
            for(int j=0; j<A.jDim; j++){//calculate matrix multiplication product

                double sum = 0;

                for(int k=0; k<A.iDim; k++){
                    sum += this.getIJ(i,k) * A.getIJ(k,j);
                }
                result.setIJ(i,j,sum);
            }
        }
        return result;
    }

    /**
     * Multiply the matrix by a scalar.
     *
     * @param scalar  The scalar to multiply the matrix by.
     * @return        The product of this matrix with the scalar.
     */
    public Matrix multiply(double scalar) {
        // You need to fill in this method.
        //no dimension checks needed because just scalar mult.

        GeneralMatrix result = new GeneralMatrix(this.iDim, this.jDim);
        for(int i=0; i<iDim; i++){
            for(int j=0; j<jDim; j++){
                double product = scalar * this.getIJ(i,j);
                result.setIJ(i,j,product);
            }
        }
        return result;
    }

    /**
     * Populates the matrix with random numbers which are uniformly
     * distributed between 0 and 1.
     */
    public void random() {
        // You need to fill in this method.

        for(int i=0; i<iDim; i++){
            for(int j=0; j<jDim; j++){
                this.setIJ(i,j,Math.random());
            }
        } 
    }

    /**
     * Returns the LU decomposition of this matrix; i.e. two matrices L and U
     * so that A = LU, where L is lower-diagonal and U is upper-diagonal.
     * 
     * On exit, decomp returns the two matrices in a single matrix by packing
     * both matrices as follows:
     *
     * [ u_11 u_12 u_13 u_14 ]
     * [ l_21 u_22 u_23 u_24 ]
     * [ l_31 l_32 u_33 u_34 ]
     * [ l_41 l_42 l_43 u_44 ]
     *
     * where u_ij are the elements of U and l_ij are the elements of l. When
     * calculating the determinant you will need to multiply by the value of
     * sign[0] calculated by the function.
     * 
     * If the matrix is singular, then the routine throws a MatrixException.
     * In this case the string from the exception's getMessage() will contain
     * "singular"
     *
     * This method is an adaptation of the one found in the book "Numerical
     * Recipies in C" (see online for more details).
     * 
     * @param sign  An array of length 1. On exit, the value contained in here
     *              will either be 1 or -1, which you can use to calculate the
     *              correct sign on the determinant.
     * @return      The LU decomposition of the matrix.
     */
    public GeneralMatrix LUdecomp(double[] sign) {
        // This method is complete. You should not even attempt to change it!!
        if (jDim != iDim)
            throw new MatrixException("Matrix is not square");
        if (sign.length != 1)
            throw new MatrixException("d should be of length 1");
        
        int           i, imax = -10, j, k; 
        double        big, dum, sum, temp;
        double[]      vv   = new double[jDim];
        GeneralMatrix a    = new GeneralMatrix(this);
        
        sign[0] = 1.0;
        
        for (i = 1; i <= jDim; i++) {
            big = 0.0;
            for (j = 1; j <= jDim; j++)
                if ((temp = Math.abs(a.values[i-1][j-1])) > big)
                    big = temp;
            if (big == 0.0)
                throw new MatrixException("Matrix is singular");
            vv[i-1] = 1.0/big;
        }
        
        for (j = 1; j <= jDim; j++) {
            for (i = 1; i < j; i++) {
                sum = a.values[i-1][j-1];
                for (k = 1; k < i; k++)
                    sum -= a.values[i-1][k-1]*a.values[k-1][j-1];
                a.values[i-1][j-1] = sum;
            }
            big = 0.0;
            for (i = j; i <= jDim; i++) {
                sum = a.values[i-1][j-1];
                for (k = 1; k < j; k++)
                    sum -= a.values[i-1][k-1]*a.values[k-1][j-1];
                a.values[i-1][j-1] = sum;
                if ((dum = vv[i-1]*Math.abs(sum)) >= big) {
                    big  = dum;
                    imax = i;
                }
            }
            if (j != imax) {
                for (k = 1; k <= jDim; k++) {
                    dum = a.values[imax-1][k-1];
                    a.values[imax-1][k-1] = a.values[j-1][k-1];
                    a.values[j-1][k-1] = dum;
                }
                sign[0] = -sign[0];
                vv[imax-1] = vv[j-1];
            }
            if (a.values[j-1][j-1] == 0.0)
                a.values[j-1][j-1] = 1.0e-20;
            if (j != jDim) {
                dum = 1.0/a.values[j-1][j-1];
                for (i = j+1; i <= jDim; i++)
                    a.values[i-1][j-1] *= dum;
            }
        }
        
        return a;
    }

    /*
     * Your tester function should go here.
     */
    public static void main(String[] args) {
        // Test your class implementation using this method.

        //constructors +copy
        GeneralMatrix mat1 = new GeneralMatrix(2,2); //2x2 matrix
        GeneralMatrix mat2 = new GeneralMatrix(3,3); //3x3 matrix
        GeneralMatrix copy = new GeneralMatrix(mat2); //tests copy function

        System.out.println("The first matrix is: \n"+ mat1);
        System.out.println("The second matrix is:\n"+ mat2);
        System.out.println("Copying the second matrix gives:\n" + copy);
        copy.setIJ(0,0,999); //show it is a deep copy by modifying local one;
        System.out.println("Modified second matrix:\n"+copy);
        System.out.println("Original matrix 2:\n"+mat2);


        //getter and setters
        //test index errors too
        mat1.setIJ(0,0,1);
        mat1.setIJ(0,1,2);
        mat1.setIJ(1,0,3);
        mat1.setIJ(1,1,4);
        System.out.println("After setting values of first matrix, it is:\n" + mat1);
        System.out.println("Then the bottom left entry is:" + mat1.getIJ(1,0));
        //System.out.println("Try to get a value that is out of bounds:" +mat1.getIJ(3,4));

        GeneralMatrix mat3 = new GeneralMatrix(2,2); //new matrix of just 1's
        mat3.setIJ(0,0,1);
        mat3.setIJ(0,1,1);
        mat3.setIJ(1,0,1);
        mat3.setIJ(1,1,1);

        GeneralMatrix mat4 = new GeneralMatrix(1,2); //new 1x2 matrix
        mat4.setIJ(0,0,1);
        mat4.setIJ(0,1,2);

        GeneralMatrix mat5 = new GeneralMatrix(2,1); //new 2x1 matrix, so product is 1x1
        mat5.setIJ(0,0,1);
        mat5.setIJ(1,0,2);


        //determinate
        System.out.println("Determinant of matrix 1 (should be -2):\n"+mat1.determinant());
        //System.out.println("Try finding det of non-square matrix:\n"+mat4.determinant());

        //add
        System.out.println("Adding mat 1 to mat 3 (should be 2,3,4,5):\n"+mat1.add(mat3));
        //System.out.println("Adding matrices of wrong dimensions:\n"+mat1.add(mat2));
        
        //multiply by matrix

        System.out.println("Multiplying two matrices of correct dimension:\n"+mat4.multiply(mat5));

        //System.out.println("Multiplying two matrices of wrong dimensions: \n"+mat4.multiply(mat2));
        
        //multiply by scalar
        System.out.println("Multiplying entries of mat3 by 2 (should be 2,2,2,2):\n"+mat3.multiply(2));
        
        //random filling entries
        mat2.random();
        System.out.println("Filling matrix 2 with random numbers from 0 to 1:\n"+mat2);
    }
}