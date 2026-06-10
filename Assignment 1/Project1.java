/*
 * PROJECT I: Project1.java
 *
 * As in project 0, this file - and the others you downloaded - form a
 * template which should be modified to be fully functional.
 *
 * This file is the *last* file you should implement, as it depends on both
 * Point and Circle. Thus your tasks are to:
 *
 * 1) Make sure you have carefully read the project formulation. It contains
 *    the descriptions of all of the functions and variables below.
 * 2) Write the class Point.
 * 3) Write the class Circle
 * 4) Write this class, Project1. The results() method will perform the tasks
 *    laid out in the project formulation.
 */

import java.util.*;
import java.io.*;

public class Project1 {
    // -----------------------------------------------------------------------
    // Do not modify the names or types of the instance variables below! This 
    // is where you will store the results generated in the Results() function.
    // -----------------------------------------------------------------------
    public int      circleCounter; // Number of non-singular circles in the file.
    public double[] aabb;          // The bounding rectangle for the 10th and 
                                   // 20th circles
    public double   Smax;          // Area of the largest circle (by area).
    public double   Smin;          // Area of the smallest circle (by area).
    public double   areaAverage;   // Average area of the circles.
    public double   areaSD;        // Standard deviation of area of the circles.
    public double   areaMedian;    // Median of the area.
    public int      stamp = 220209;
    // -----------------------------------------------------------------------
    // You should implement - but *not* change the types, names or parameters of
    // the variables and functions below.
    // -----------------------------------------------------------------------

    /**
     * Default constructor for Project1. You should leave it empty.
     */
    public Project1() {
        // This method is complete.
    }

    /**
     * Results function. It should open the file called fileName (using
     * Scanner), and from it generate the statistics outlined in the project
     * formulation. These are then placed in the instance variables above.
     *
     * @param fileName  The name of the file containing the circle data.
     */

    //ADDED MYSELF, new vars to use in each of them
    private double[] areas;
    private int validCount;

    public void results(String fileName){
        // You need to fill in this method.
        //start by counting total lines in file
        int totalLines = 0 ;
        try(Scanner sc = new Scanner(new BufferedReader(new FileReader("student.data")))){
            while(sc.hasNextLine()){
                sc.nextLine();
                totalLines++;
            }
        } catch(Exception e){
            System.out.println("Error reading file");
            e.printStackTrace();
        }

        Circle[]circles = new Circle[totalLines]; //creates circle array
        areas = new double[totalLines]; //creates area array
        
        validCount = 0; //number of nonsingular circles

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader("student.data")))){
            while(scanner.hasNext()){ //method of reading the file and extracting x,y,r
                double x = scanner.nextDouble();
                double y = scanner.nextDouble();
                double rad = scanner.nextDouble();

                if(Math.abs(rad)<=Point.GEOMTOL){
                    continue; //skips singular circles
                }

                Point center = new Point(x,y); //create center point
                Circle circle = new Circle(center, rad); //create circle object

                circles[validCount] = circle; //add circle to array
                areas[validCount] = circle.area(); //add area to array
                validCount++; //increment valid circle count
            }
        } catch(Exception e){
            System.out.println("Error reading file");
            e.printStackTrace();
            return;
        }
        circleCounter = validCount; //circle counter is number of valid circles

        //calculate Smax, Smin
        Smax = areas[0];
        Smin = areas[0]; //initialize values to first area
        for(int i=1; i<validCount; i++){
            if(areas[i]>Smax){
                Smax = areas[i];
            }
            if(areas[i]<Smin){
                Smin = areas[i];
            }
        }

        //calculate average area
        areaAverage = averageCircleArea(circles);

        //standard deviation of area
        areaSD = areaStandardDeviation(circles);

        //median of area
        Arrays.sort(areas,0,validCount); //sorts area array to be in order

        if(validCount%2 ==1){ //if odd circles, median is middle value
            areaMedian = areas[validCount/2];}
        else{//if even # of circles, median is average of middle
            int mid1 = validCount/2;
            int mid2 = mid1-1;
            areaMedian = (areas[mid2]+areas[mid1])/2;
        }

        //calculate aabb box:
        Circle[] twocircles = new Circle[]{circles[9], circles[19]}; //10th and 20th circles only
        aabb = calculateAABB(twocircles);
        
    }


    /**
     * A function to calculate the avarage area of circles in the array provided. 
     * This array may contain 0 or more circles.
     *
     * @param circles  An array of Circles
     */
    public double averageCircleArea(Circle[] circles) {
      // You need to fill in this method
      double sum = 0.0; //initialize sum
        for (int i=0; i<validCount;i++){ //summing all the areas up
            sum +=areas[i];
        }
        areaAverage = sum/validCount;
      return areaAverage;
    }
    
    /**
     * A function to calculate the standard deviation of areas in the circles in the array provided. 
     * This array may contain 0 or more circles.
     * 
     * @param circles  An array of Circles
     */
    public double areaStandardDeviation(Circle[] circles) {
      //You need to complete this method.
      double varsum = 0;
        for(int i=0; i<validCount; i++){
            varsum += Math.pow((areas[i]-areaAverage),2);
        }
        areaSD = Math.sqrt(varsum/validCount);
      return areaSD;
    }

    /**
     * Returns 4 values in an array [X1,Y1,X2,Y2] that define the rectangle
     * that surrounds the array of circles given, as set out in the 
     * project formulation.
     *******************************************************
     * IMPORTANT REMARK
     * *******************************************************
     * This method can take any number of circles, and not just 2 circles.
     * As indicated below, it must return an array of doubles that define the 
     * bottom left and top right of a rectangle that surrounds ALL the given circles.
     * In case no circle is provided to calculateAABB() or in case all the circles are singular,
     * then the (default) bouding rectangle should be the largest possible we can define
     * using Double.MAX_VALUE and Double.MIN_VALUE
     *
     * @param circles  An array of Circles
     * @return An array of doubles [X1,Y1,X2,Y2] that define the bounding rectangle with
     *         the origin (bottom left) at [X1,Y1] and opposite corner (top right)
     *         at [X2,Y2]
     */
    public double[] calculateAABB(Circle[] circles)
    {
         // You need to fill in this method.
        //make default bounding box
        double x1 = Double.MAX_VALUE;
        double y1 = Double.MAX_VALUE;
        double x2 = Double.MIN_VALUE;
        double y2 = Double.MIN_VALUE;

        //set boolean so it determines if there are valid boxes
        boolean validBox = false;

        for(int i= 0; i<circles.length; i++){
            Circle c = circles[i];
            if(c == null){continue;} //skip null circles
            if(Math.abs(c.getRadius())<=Point.GEOMTOL){continue;} //skip singular
            validBox = true; 

            double c_x = c.getCentre().getX(); //retrieve x,y coord and radius
            double c_y = c.getCentre().getY();
            double rad = c.getRadius();

            double left = c_x-rad; //new bounds for box
            double right = c_x+rad;
            double bottom = c_y-rad;
            double top = c_y+rad;

            if(left<x1){x1 = left;} //if bounds are bigger/smaller then adjust
            if(right>x2){x2=right;}
            if(bottom<y1){y1=bottom;}
            if(top>y2){y2=top;}
        } 
        if(!validBox){ //no valid boxes found
            return new double[]{Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_VALUE};
        };

        return new double[]{x1,y1,x2,y2};

    }
    

  
    // =======================================================
    // Tester - tests methods defined in this class
    // =======================================================

    /**
     * Your tester function should go here (see week 14 lecture notes if
     * you're confused). It is not tested by BOSS, but you should still
     * implement it in a sensible fashion.
     */
    public static void main(String args[]){
        // You can use this method for testing.
        Project1 p = new Project1();
        p.results("student.data");

        System.out.println("Valid circles:"+p.circleCounter);
        System.out.println("\n Smax:" + p.Smax);
        System.out.println("\n Smin:" + p.Smin);
        System.out.println("\n Average area:"+p.areaAverage);
        System.out.println("\n Standard deviation:"+p.areaSD);
        System.out.println("\n Median area:" +p.areaMedian);
        System.out.println("\n AABB:" + Arrays.toString(p.aabb));
    }
}
