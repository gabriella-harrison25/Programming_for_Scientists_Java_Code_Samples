/*
 * PROJECT III: TriMatrix.java
 *
 * This file contains a template for the class TriMatrix. Not all methods are
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

public class TriMatrix extends Matrix {
    /**
     * An array holding the diagonal elements of the matrix.
     */
    private double[] diagonal;

    /**
     * An array holding the upper-diagonal elements of the matrix.
     */
    private double[] upperDiagonal;

    /**
     * An array holding the lower-diagonal elements of the matrix.
     */
    private double[] lowerDiagonal;
    
    /**
     * Constructor function: should initialise iDim and jDim through the Matrix
     * constructor and set up the values array.
     *
     * @param dimension  The dimension of the array.
     */
    public TriMatrix(int dimension) {
        // You need to fill in this method.
        super(dimension,dimension);

        if(dimension<1){ //dimension check
            throw new MatrixException("Dimension must be at least 1");
        }

        diagonal = new double[dimension]; //initialize the three arrays that store the values
        upperDiagonal = new double[dimension-1];
        lowerDiagonal = new double[dimension-1];
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
        //CHECK indices

        if(i<0||i>=iDim||j<0||j>=jDim){ //check indices
            throw new MatrixException("Invalid matrix index");}
        
        if(i==j){ //if on main diagonal
            return diagonal[i];
        }
        if(i+1==j){ //if on upper diagonal
            return upperDiagonal[i];
        }
        if(i-1==j){ //if on lower diagonal
            return lowerDiagonal[i-1];
        }
        return 0.0; //all other entries are 0, but not able to access
    }
    
    /**
     * Setter function: set the (i,j)'th entry of the data array.
     *
     * @param i      The location in the first co-ordinate.
     * @param j      The location in the second co-ordinate.
     * @param value  The value to set the (i,j)'th entry to.
     */
    public void setIJ(int i, int j, double value) {
        // You need to fill in this method.
        if(i<0||i>=iDim||j<0||j>=jDim){ //check indices
            throw new MatrixException("Invalid matrix index");}
        
        if (i==j){ //if on main diagonal
            diagonal[i]=value;
        }
        else if (i+1==j){ //if on upper diagonal
            upperDiagonal[i]=value;
        }
        else if(i-1==j){//if on lower diagonal
            lowerDiagonal[i-1]=value;
        }
        else{
            throw new MatrixException("Cannot change non-diagonal elements");
        }
    }
    
    /**
     * Return the determinant of this matrix.
     *
     * @return The determinant of the matrix.
     */
    public double determinant() {
        // You need to fill in this method.

        /* The lower matrix has 1's on diagonal, so det =1
        The upper matrix is triangular, so multiply entries of diagonal matrix */

        //do LU decomp
        TriMatrix LU = this.LUdecomp();

        double det = 1.0;

        //multiply diagonal entries
        for(int i=0; i<iDim; i++){
            det *= LU.diagonal[i];
        }
        return det;
    }
    
    /**
     * Returns the LU decomposition of this matrix. See the formulation for a
     * more detailed description.
     * 
     * @return The LU decomposition of this matrix.
     */
    public TriMatrix LUdecomp() {
        // You need to fill in this method.

        //need to also check for matrix singularity
        //need to check for square matrix

        int n = this.iDim; //sets dimension as a variable here

        if(jDim != iDim){ //check for square-ness
            throw new MatrixException("Matrix is not square");
        }

        TriMatrix LU = new TriMatrix(n); //create new matrix for storage

        double[] a = this.diagonal; //initalize the diagonals arrays
        double[] b = this.upperDiagonal;
        double[] c = this.lowerDiagonal;

        double[] dstar = new double[n]; //make new arrays for main diagonal, upper/lower
        double[] ustar = new double[n-1];
        double[] lstar = new double[n-1]; 

        //perform the decomp

        dstar[0]=a[0]; //a1=d1

        if(Math.abs(dstar[0])<1e-14){
            throw new MatrixException("Matrix is singular");
        }

        if(n>1){
            ustar[0]=b[0]; //first upper diagonal element is same too
        }

        //recursion formulas
        for(int i =1; i<n; i++){
            lstar[i-1]=c[i-1] / dstar[i-1]; //lower diagonal formula

            dstar[i]=a[i]-lstar[i-1]*ustar[i-1];//upper diagonal

            //check for singularity again
            if(Math.abs(dstar[i])<1e-14){
                throw new MatrixException("Matrix is singular");
            }

            if(i<n-1){
                ustar[i] = b[i]; //upper diagonal (except last term) (all terms are same)
            }
        }
        for(int i=0; i<n; i++){
            LU.diagonal[i] = dstar[i]; //update the diagonal of LU matrix
        }
        for(int i=0; i<n-1; i++){
            LU.upperDiagonal[i]=ustar[i]; //update upper/lower diagonals here
            LU.lowerDiagonal[i]=lstar[i];
        }

       return LU;
        }

    /**
     * Add the matrix to another second matrix.
     *
     * @param second  The Matrix to add to this matrix.
     * @return        The sum of this matrix with the second matrix.
     */
    public Matrix add(Matrix second){
        // You need to fill in this method.

        if(this.iDim != second.iDim || this.jDim != second.jDim){//check for dimension matching
            throw new MatrixException("Dimensions do not match for addition");
        }
        if(second ==null){
            throw new MatrixException("Cannot add null matrix");
        }
    
        //Both matrices are tri-matrices:
        if (second instanceof TriMatrix){
            TriMatrix second2 = (TriMatrix) second; //puts second matrix in correct form
            TriMatrix result = new TriMatrix(iDim); //create final matrix

            for (int i =0; i<iDim; i++){ //main diagonal sums
                result.diagonal[i] = this.diagonal[i]+second2.diagonal[i];
            }
            for(int i=0; i<iDim-1; i++){ //upper and lower diagonal sums
                result.upperDiagonal[i] = this.upperDiagonal[i]+second2.upperDiagonal[i];
                result.lowerDiagonal[i] = this.lowerDiagonal[i]+second2.lowerDiagonal[i];
            }
            return result;
        }

        //First matrix is tri-matrix, second is generalmatrix:
        GeneralMatrix result = new GeneralMatrix(iDim,jDim);
        for (int i=0; i<iDim; i++){ //add corresponding elements
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
        if(this.jDim != A.iDim){ //check for dimension alignment
            throw new MatrixException("The dimensions do not align for matrix multiplication");
        }

        if(A == null){
            throw new MatrixException("Cannot multiply null matrix");
        }

        if(A.jDim<=0){
            throw new MatrixException("Invalid matrix dimensions");
        }

        //result has to be generalmatrix here
        GeneralMatrix result = new GeneralMatrix(this.iDim, A.jDim);
        for (int i=0; i<this.iDim; i++){
            for(int j=0; j<A.jDim; j++){

                double sum = 0.0;

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

        TriMatrix result = new TriMatrix(this.iDim);
        for(int i=0; i<iDim; i++){
            result.diagonal[i]=scalar*diagonal[i];
            }
        for(int i=0; i<iDim-1; i++){
            result.upperDiagonal[i]=scalar*upperDiagonal[i];
            result.lowerDiagonal[i]=scalar*lowerDiagonal[i];
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
            diagonal[i]=Math.random();}
        for(int i=0; i<iDim-1; i++){
            upperDiagonal[i]=Math.random();
            lowerDiagonal[i]=Math.random();
        }
    }

    
    /*
     * Your tester function should go here.
     */
    public static void main(String[] args) {
        // Test your class implementation using this method.

        //test constructors
        TriMatrix mat1 = new TriMatrix(2); //2x2 matrix
        TriMatrix mat2 = new TriMatrix(3); //3x3 matrix
        
        //setters
        mat1.setIJ(0,0,1);
        mat1.setIJ(1,1,2); //makes it a 1,0,2,0 matrix

        System.out.println("The matrix 2x2 is:\n" +mat1);
        //getters
        System.out.println("The top left entry of the 2x2 is: \n"+mat1.getIJ(0,0));
        
        mat2.setIJ(0,0,1); //set main diagonal to be 1,2,3
        mat2.setIJ(1,1,2);
        mat2.setIJ(2,2,3);

        mat2.setIJ(0,1,4); //set upper to be 4,5
        mat2.setIJ(1,2,5);

        mat2.setIJ(1,0,6); //set lower diag to be 6,7
        mat2.setIJ(2,1,7);

        System.out.println("The 3x3 matrix is now:\n"+ mat2);

        //test illegal setIJ
        //System.out.println("Testing out of bounds error:\n" +mat2.getIJ(3,3));
        //System.out.println("Testing invalid setting index error:\n");
        //mat2.setIJ(2,0,2);

        TriMatrix mat3 = new TriMatrix(3); //3x3 matrix
        mat3.random();
        System.out.println("Testing random function:\n"+mat3);

        //adding
        //test trimatrix + trimatrix
        System.out.println("Addition of the 3x3 tri matrices \n"+mat2.add(mat3));

        //test trimatrix + generalmatrix
        GeneralMatrix mat4 = new GeneralMatrix(3,3);
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
            mat4.setIJ(i,j,1);//make it all 1's
        }
    }

        System.out.println("Addition of 3x3 tri + gen matrix \n"+mat2.add(mat4));

        //multiply by scalar
        System.out.println("The random matrix multiplied by 0.5 (all entries still less than 1)\n"+mat3.multiply(0.5));

        //multiply by matrix
        TriMatrix mat5 = new TriMatrix(3);
        mat5.setIJ(0,0,1);
        mat5.setIJ(1,1,1);
        mat5.setIJ(2,2,1); //makes an identity matrix

        System.out.println("Matrix multiplication test \n"+mat2.multiply(mat5));

        //ludecomp

        TriMatrix mat6 = new TriMatrix(3); //making basic matrix we know LUDEcomp of
        mat6.setIJ(0,0,2);
        mat6.setIJ(0,1,1);
        mat6.setIJ(1,0,3);
        mat6.setIJ(1,1,4);
        mat6.setIJ(1,2,5);
        mat6.setIJ(2,1,6);
        mat6.setIJ(2,2,7);

        System.out.println("The matrix used for LU Decomp:\n"+mat6);
        System.out.println("Should return:\n diagonal = 2,2.5,-5 \n upper = 1,5 \n lower = 1.5,2.4 ");
        System.out.println("Output \n"+mat6.LUdecomp());

        //determinant
        System.out.println("The determinant of the matrix used for LU Decomp is (should be -25): \n" +mat6.determinant());
        
    }
}