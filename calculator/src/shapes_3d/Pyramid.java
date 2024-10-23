package shapes_3d;

import abstract_classes.Figure;
import interfaces.Printable;
import shapes.*;

public class Pyramid implements Printable{
    private Figure base;
    private double height;

    public Pyramid(Circle base, double height) throws IllegalArgumentException {
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be greater than 0");
        }
        
        this.base = base;
        this.height = height;
    }

    public Pyramid(Rectangle base, double height) throws IllegalArgumentException {
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be greater than 0");
        }
        
        this.base = base;
        this.height = height;
    }

    public Pyramid(Triangle base, double height) throws IllegalArgumentException {
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be greater than 0");
        }
        
        this.base = base;
        this.height = height;
    }

    public double calculateVolume() {
        return base.calculateArea() * height / 3;
    }

    public double calculateSurfaceArea() {
        double baseArea = base.calculateArea();
        
        if (base instanceof Circle) {
            Circle circleBase = (Circle) base;

            return baseArea + Math.PI * circleBase.getRadius() * Math.sqrt(Math.pow(circleBase.getRadius(), 2) + Math.pow(height, 2));
        } else if (base instanceof Rectangle) {
            Rectangle rectangleBase = (Rectangle) base;
            
            double lsw = Math.sqrt(Math.pow(rectangleBase.getWidth() / 2,2) + Math.pow(height, 2));
            double lsh = Math.sqrt(Math.pow(rectangleBase.getHeight() / 2,2) + Math.pow(height, 2));
            return baseArea + rectangleBase.getWidth() * lsh + rectangleBase.getHeight() * lsw;
        } if (base instanceof Triangle) {
            Triangle triangleBase = (Triangle) base;
            
            double ha = 2 * baseArea / triangleBase.getSideA();
            double hb = 2 * baseArea / triangleBase.getSideB();
            double hc = 2 * baseArea / triangleBase.getSideC();
        
            double la = Math.sqrt(Math.pow(ha, 2) + Math.pow(height, 2));
            double lb = Math.sqrt(Math.pow(hb, 2) + Math.pow(height, 2));
            double lc = Math.sqrt(Math.pow(hc, 2) + Math.pow(height, 2));
        
            return baseArea + 0.5 * triangleBase.getSideA() * la 
                            + 0.5 * triangleBase.getSideB() * lb 
                            + 0.5 * triangleBase.getSideC() * lc;
        } else {
            return 0;
        } 
    }

    public void print() {
        System.out.println("Pyramid with base: ");
        base.print();
        System.out.println(" and height: " + height);
    }
}
