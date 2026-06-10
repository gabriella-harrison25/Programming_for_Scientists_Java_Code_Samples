/*
 * PROJECT II: Secant.java
 *
 * This file contains a template for the class Secant. Not all methods are
 * implemented. Make sure you have carefully read the project formulation
 * before starting to work on this file.
 *
 * In this class, you will create a basic Java object responsible for
 * performing the Secant root finding method on a given polynomial
 * f(z) with complex co-efficients. The formulation outlines the method, as
 * well as the basic class structure, details of the instance variables and
 * how the class should operate.
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

public class Secant {
    /**
     * The maximum number of iterations that should be used when applying
     * Secant. Ensure this is *small* (e.g. at most 50) otherwise your
     * program may appear to freeze.
     */
    public static final int MAXITER = 30;

    /**
     * The tolerance that should be used throughout this project. Note that
     * you should reference this variable and _not_ explicity write out
     * 1.0e-10 in your solution code. Other classes can access this tolerance
     * by using Secant.TOL.
     */
    public static final double TOL = 1.0e-10;

    /**
     * The polynomial we wish to apply the Secant method to.
     */
    private Polynomial f;


    /**
     * A root of the polynomial f corresponding to the root found by the
     * iterate() function below.
     */
    private Complex root;
    
    /**
     * The number of iterations required to reach within TOL of the root.
     */
    private int numIterations;

    /**
     * An enumeration that signifies errors that may occur in the root finding
     * process.
     *
     * Possible values are:
     *   OK: Nothing went wrong.
     *   ZERO: Difference went to zero during the algorithm.
     *   DNF: Reached MAXITER iterations (did not finish)
     */
    enum Error { OK, ZERO, DNF };
    private Error err = Error.OK;
    
    
    // ========================================================
    // Constructor functions.
    // ========================================================

    /**
     * Basic constructor.
     *
     * @param p  The polynomial used for Secant.
     */
    public Secant(Polynomial p) {
        // You need to fill in this method.
        this.f=p; //store polynomial
        this.root = new Complex (); //no roots found yet
        this.numIterations = 0; //no iterations done yet
        this.err = Error.OK; //no errors yet
    }

    // ========================================================
    // Accessor methods.
    // ========================================================
    
    /**
     * Returns the current value of the err instance variable.
     */
    public Error getError() {
        // You need to fill in this method with the correct code.
        return this.err;
    }

    /**
     * Returns the current value of the numIterations instance variable.
     */
    public int getNumIterations() { 
        // You need to fill in this method with the correct code.
        return this.numIterations;
    }
    
    /**
     * Returns the current value of the root instance variable.
     */
    public Complex getRoot() {
        // You need to fill in this method with the correct code.
        return this.root;
    }

    /**
     * Returns the polynomial associated with this object.
     */
    public Polynomial getF() {
        // You need to fill in this method with the correct code.
        return this.f;
    }

    // ========================================================
    // Secant method (check the comment)
    // ========================================================
    
    /**
     * Given two complex numbers z0 and z1, apply Secant to the polynomial f in
     * order to find a root within tolerance TOL.
     *
     * One of three things may occur:
     *
     *   - The root is found, in which case, set root to the end result of the
     *     algorithm, numIterations to the number of iterations required to
     *     reach it and err to OK.
     *   - At some point the absolute difference between f(zn) and f(zn-1) becomes zero. 
     *     In this case, set err to ZERO and return.
     *   - After MAXITER iterations the algorithm has not converged. In this 
     *     case set err to DNF and return.
     *
     * @param z0,z1  The initial starting points for the algorithm.
     */
    public void iterate(Complex z0, Complex z1) {
        // You need to fill in this method.
        this.err=Error.OK;
        this.numIterations = 0;
        this.root = new Complex();


        Complex f0 = f.evaluate(z0); //evaluate at z0 first
        Complex f1 = f.evaluate(z1); //evaluate at z1 first (as starting points)
        for(int i = 1; i<=MAXITER;i++){
            Complex denom = f1.add(f0.negate()); //denom of secant formula
            if(denom.abs()<TOL){ //tests if denom is 0
                err=Error.ZERO;
                numIterations=i;
                return;
            };

            Complex num = z1.add(z0.negate()); //num of secant formula
            Complex frac = num.divide(denom); //fraction in secant formula
            Complex z_next = z1.add(f1.negate().multiply(frac));
            
           if((z_next.add(z1.negate())).abs()<TOL && f.evaluate(z_next).abs()<TOL){ //check convergence
                this.root = z_next; //sets root to this value
                this.numIterations = i; //sets iters
                this.err = Error.OK; //no errors
                return;
           }

            //set up next round of iters
            z0=z1;
            f0=f1;
            z1 = z_next;
            f1 = f.evaluate(z1);
        }
        this.err = Error.DNF; //couldn't finish within max iters here
        this.root = z1; //root is last value
        this.numIterations = MAXITER; //sets iters to max
    }
      
    // ========================================================
    // Tester function.
    // ========================================================
    
    public static void main(String[] args) {
        // Basic tester: find a root of f(z) = z^3-1.
        Complex[] coeff = new Complex[] { new Complex(-1.0,0.0), new Complex(), new Complex(), new Complex(1.0,0.0) };
        Polynomial p    = new Polynomial(coeff);
        Secant     s    = new Secant(p);
                
        s.iterate(new Complex(), new Complex(1.0,1.0));
        System.out.println(s.getNumIterations());   // 12
        System.out.println(s.getError());           // OK

        System.out.println(s.getRoot()); //1.0+0.0i


    //MY additional tests:
        System.out.println("\n Additional Tests: \n");
        System.out.println("Test with complex starter points (should be similar to above):\n");
        s.iterate(new Complex(0.0,1.0), new Complex(0.5, 0.5)); //ensures it works with complex starter points
        System.out.println(s.getNumIterations());
        System.out.println(s.getError());
        System.out.println(s.getRoot()); //all should be same as above

        Complex[] coeff2 = new Complex[] {new Complex(0.0,0.0), new Complex(0.0,0.0), new Complex(1.0,0.0)
        }; //basic x^2 function
        Polynomial p2 = new Polynomial(coeff2);
        Secant s2 = new Secant(p2);

        System.out.println("\n Test to get zero error: \n");
        s2.iterate(new Complex(0.0,0.0), new Complex(0.0,0.0)); //should give zero error
        System.out.println(s2.getError()); //ZERO

        System.out.println("\n Test to get DNF error: \n");
        s2.iterate(new Complex(0,1000000.0), new Complex(0,-1000000000000.0)); //should give DNF error
        System.out.println(s2.getError()); //DNF

        Complex[] coeff3 = new Complex[] {new Complex(1.0,0.0), new Complex(-2.0,0.0), new Complex(1.0,0.0)
        }; //(x-1)^2 function to test repeated roots
        Polynomial p3 = new Polynomial(coeff3);
        Secant s3 = new Secant(p3);

        System.out.println("\n Test with repeated roots: \n");
        s3.iterate(new Complex(0,0), new Complex(3,0)); //should find root of 1
        System.out.println(s3.getError());
        System.out.println(s3.getRoot());
        System.out.println(s3.getNumIterations()); //either a zero or dnf error expected here
        //secant method can converge slowly or oscillate around repeated roots.
        //to fix this, you just have to pick better starting points



    }
}
