/*
 * PROJECT II: Project2.java
 *
 * This file contains a template for the class Project2. Not all methods
 * are implemented. Make sure you have carefully read the project formulation
 * before starting to work on this file.
 *
 * There are a lot of functions in this class, as it deals with creating an
 * image using purely Java. I have already completed a lot of the technical
 * aspects for you, so there should not be a huge amount for you to do in this
 * class! 
 *
 * At the bottom of this class there is a section of functions which I have
 * already written and deal with the more complicated tasks. You should make
 * sure that you read through the function descriptions, but DO NOT ALTER
 * THEM! Also, remember to call the setupFractal() function from your
 * constructor!
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

// These next lines import the relevant classes needed to output an image and
// *SHOULD NOT* be changed. You do not need to worry about their definitions.
import java.io.*;
import java.awt.image.*;
import java.awt.*;
import javax.imageio.ImageIO;

import java.util.ArrayList;

public class Project2 {
    /**
     * A reference to the Secant iterator object.
     */
    private Secant iterator;
    
    /**
     * The top-left corner of the square in the complex plane to examine.
     */
    private Complex origin;
    
    /**
     * The width of the square in the complex plane to examine.
     */
    private double width;
    
    /**
     * A list of roots of the polynomial.
     */
    private ArrayList<Complex> roots;
    
    /**
     * A two dimensional array holding the colours of the plot.
     */
    private Color[][] colours;

    /**
     * A flag indicating the type of plot to generate. If true, colourPixel will
     * choose darker colours if a particular root takes longer to converge.
     */
    private boolean colourIterations;

    /**
     * A standard Java object which allows us to store a simple image in
     * memory. This will be set up by setupFractal -- you do not need to worry
     * about it!
     */
    private BufferedImage fractal;
    
    /**
     * This object is another standard Java object which allows us to perform
     * basic graphical operations (drawing lines, rectangles, pixels, etc) on
     * the BufferedImage. This will be set up by setupFractal -- you do not
     * need to worry about it!
     */
    private Graphics2D g2;

    /**
     * Defines the width (in pixels) of the BufferedImage and hence the
     * resulting image.
     */
    public static final int NUMPIXELS = 400;
    
    // ========================================================
    // Constructor function.
    // ========================================================
    
    /**
     * Constructor function which initialises the instance variables
     * above. IMPORTANT: Remember to call setupFractal at the end of this
     * function!!
     *
     * @param p       The polynomial to generate the fractal of.
     * @param origin  The top-left corner of the square to image.
     * @param width   The width of the square to image.
     */
    public Project2(Polynomial p, Complex origin, double width) {
        // You need to fill in this function with the correct code.
        if (p.degree()<3 || p.degree()>5)
            throw new RuntimeException("Degree of polynomial must be between 3 and 5");
        this.iterator = new Secant(p);
        this.origin = origin;
        this.width = width;
        this.roots = new ArrayList<Complex>(); //CHECK ALL I NEED IS TO MAKE A BLANK LIST
        setupFractal(); 
    } // CHECK I DON"T NEED TO SET UP ANY OTHER VARS FROM ABOVE
    
    // ========================================================
    // Basic operations.
    // ========================================================

    /**
     * Print out all of the roots found so far, which are contained in the
     * roots ArrayList.
     */
    public void printRoots() {
        // You need to fill in this function.
        for (Complex root: roots){
            System.out.println(root.toString());
        }
    }

    /**
     * 
     * @return an ArrayList of roots for the polynomial
     */
    public ArrayList<Complex> getRoots() {
        // This method is complete.
        return roots;
    }
    
    /**
     * Check to see if root is in the roots ArrayList (up to tolerance).
     *
     * @param root  Root to find in this.roots.
     * @return The index of root within roots (-1 if the root is not found)
     */
    public int index(Complex root) {
        // You need to fill in this function.
        for (int i=0; i<roots.size(); i++){
            Complex r = roots.get(i);
            Complex neg_root = root.negate();
            Double distance = (r.add(neg_root)).abs();
            if(distance<=Secant.TOL){
                return i;}
        }
        return -1;
    }
    
    /**
     * Convert from pixel indices (i,j) to the complex number (origin.real +
     * i*dz, origin.imag - j*dz).
     *
     * @param i  x-axis co-ordinate of the pixel located at (i,j)
     * @param j  y-axis co-ordinate of the pixel located at (i,j)
     * 
     */
    public Complex pixelToComplex(int j, int k) {
        // You need to fill in this function.
        double dz = width/(double)NUMPIXELS;
        double real = origin.getReal()+k*dz;
        double imag = origin.getImag()-j*dz;
        return new Complex(real, imag);
    }
    
    // ========================================================
    // Fractal generating function.
    // ========================================================

    /**
     * Generate the fractal image.  Use colourPixel() to add coloured pixels
     * to the image for this fractal.
     */
    public void createFractal(boolean colourIterations) {
        // You need to fill in this function.
        roots.clear();

        this.colourIterations = colourIterations;

        for (int j=0; j<NUMPIXELS;j++){
            for(int k=0; k<NUMPIXELS;k++){ //looping over j and k
                //convert pixel to a complex number
                Complex z5= pixelToComplex(j, k); 

                //find the roots using iterator from secant class
                iterator.iterate(new Complex(0.0,0.0), z5);

                if(iterator.getError() == Secant.Error.ZERO || iterator.getError()==Secant.Error.DNF){
                    g2.setColor(Color.BLACK);
                    g2.fillRect(k,j,1,1);
                    continue;
                }

                Complex root = iterator.getRoot();

                //find root index in existing roots (from index func earlier)
                int rootIndex = index(iterator.getRoot());

                //if root not in list
                if (rootIndex == -1){
                    if (roots.size()<5){
                        roots.add(root);
                        rootIndex=roots.size() - 1;}
                    else{
                        //System.out.println("Too many roots! This should not happen!");

                        //fix this with mapping new root to closest existing root
                        double minDistance = Double.MAX_VALUE;
                        int closestrootIndex = 0;
                        for (int i = 0; i< roots.size(); i++){
                            double distance = roots.get(i).add(root.negate()).abs();
                            if(distance<minDistance){
                                minDistance = distance;
                                closestrootIndex = i;
                            }
                        }
                        rootIndex = closestrootIndex;
                    }
                }

                int iterations_real = iterator.getNumIterations();
                if (iterations_real>=Secant.MAXITER){
                    iterations_real = Secant.MAXITER - 1;
                }
                colourPixel(j,k,rootIndex,iterations_real);
            }
        }
    }

    // ========================================================
    // Tester function.
    // ========================================================
    
    public static void main(String[] args) {
        // Here is some example code which generates the two images seen in
        // figure 1 of the formulation.
        Complex[] coeff = new Complex[] { new Complex(-1.0,0.0),  new Complex(), new Complex(), new Complex(1.0,0.0) };
        Polynomial p1    = new Polynomial(coeff);
        Project2 project = new Project2(p1, new Complex(-1.0,1.0), 2.0);
        
        // The following lines of code will raise Exceptions initially
        // because the createFractal and the constructor are incomplete
        project.createFractal(false);
        project.saveFractal("fractal-light.png");
        project.createFractal(true);
        project.saveFractal("fractal-dark.png");


        //my tests
        Complex[] coeff2 = new Complex[] { new Complex(-1.0,0.0),  new Complex(), new Complex(), new Complex(0.0,0.0), new Complex(1.0,0.0) };
        Polynomial p2    = new Polynomial(coeff2);
        Project2 project2 = new Project2(p2, new Complex(-2.0,2.0), 4.0);
        
        // The following lines of code will raise Exceptions initially
        // because the createFractal and the constructor are incomplete
        project2.createFractal(false);
        project2.saveFractal("fractal-light-2.png");
        project2.createFractal(true);
        project2.saveFractal("fractal-dark-2.png");
        
        
}

    
    // ====================================================================
    // OTHER FUNCTIONS
    //
    // The rest of the functions in this class are COMPLETE (with the
    // exception of the main function) since they involve quite complex Java
    // code to deal with the graphics. This means they *do not* and *should
    // not* need to be altered! But you should read their descriptions so you
    // know how to use them.
    // ====================================================================
    
    /**
     * Sets up all the fractal image. Make sure that your constructor calls
     * this function!
     */
    private void setupFractal()
    {
        // This function is complete!
        int i, j;

        if (iterator.getF().degree() < 3 || iterator.getF().degree() > 5)
            throw new RuntimeException("Degree of polynomial must be between 3 and 5 inclusive!");

        this.colours       = new Color[5][Secant.MAXITER];
        this.colours[0][0] = Color.RED;
        this.colours[1][0] = Color.GREEN;
        this.colours[2][0] = Color.BLUE;
        this.colours[3][0] = Color.CYAN;
        this.colours[4][0] = Color.MAGENTA;

        
        for (i = 0; i < 5; i++) {
            float[] components = colours[i][0].getRGBComponents(null);
            float[] delta      = new float[3];
            
            for (j = 0; j < 3; j++)
                delta[j] = 0.8f*components[j]/Secant.MAXITER;
            
            for (j = 1; j < Secant.MAXITER; j++) {
                float[] tmp  = colours[i][j-1].getRGBComponents(null);
                colours[i][j] = new Color(tmp[0]-delta[0], tmp[1]-delta[1], 
                                         tmp[2]-delta[2]);
            }
        }
        
        fractal = new BufferedImage(NUMPIXELS, NUMPIXELS, BufferedImage.TYPE_INT_RGB);
        g2      = fractal.createGraphics();
    }
    
    /**
     * Colours a pixel in the image.
     *
     * @param i          x-axis co-ordinate of the pixel located at (i,j)
     * @param j          y-axis co-ordinate of the pixel located at (i,j)
     * @param rootColour  An integer between 0 and 4 inclusive indicating the
     *                   root number.
     * @param numIter    Number of iterations at this root.
     */
    private void colourPixel(int i, int j, int rootColour, int numIter) 
    {
        // This function is complete!
        if (colourIterations)
            g2.setColor(colours[rootColour][numIter-1]);
        else
            g2.setColor(colours[rootColour][0]);
        g2.fillRect(i,j,1,1);
    }

    /**
     * Saves the fractal image to a file.
     *
     * @param fileName  The filename to save the image as. Should end in .png.
     */
    public void saveFractal(String fileName) {
        // This function is complete!
        try {
            File outputfile = new File(fileName);
            ImageIO.write(fractal, "png", outputfile);
        } catch (IOException e) {
            System.out.println("I got an error trying to save! Maybe you're out of space?");
        }
    }
}
