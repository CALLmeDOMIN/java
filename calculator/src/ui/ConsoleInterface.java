package ui;

import java.util.Scanner;
import shapes.Circle;
import shapes.Rectangle;
import shapes.Triangle;
import shapes_3d.Pyramid;

public class ConsoleInterface {
    private Scanner scanner;

    public ConsoleInterface(Scanner scanner) {
        this.scanner = scanner;
    }

    public void run() {
        while (true) {
            System.out.println("Select an option:");
            System.out.println("1. Circle");
            System.out.println("2. Rectangle");
            System.out.println("3. Triangle");
            System.out.println("4. Pyramid");
            System.out.println("5. Exit");

            int option = scanner.nextInt();

            switch (option) {
                case 1 -> handleCircle();
                case 2 -> handleRectangle();
                case 3 -> handleTriangle();
                case 4 -> handlePyramid();
                case 5 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void handleCircle() {
        double radius = getDoubleInput("Enter the radius of the circle: ");
        Circle circle = new Circle(radius);
        displayFigure(circle);
    }

    private void handleRectangle() {
        double width = getDoubleInput("Enter the width of the rectangle: ");
        double height = getDoubleInput("Enter the height of the rectangle: ");
        Rectangle rectangle = new Rectangle(width, height);
        displayFigure(rectangle);
    }

    private void handleTriangle() {
        double sideA = getDoubleInput("Enter the first side of the triangle: ");
        double sideB = getDoubleInput("Enter the second side of the triangle: ");
        double sideC = getDoubleInput("Enter the third side of the triangle: ");
        Triangle triangle = new Triangle(sideA, sideB, sideC);
        displayFigure(triangle);
    }

    private void handlePyramid() {
        System.out.println("Which figure do you want to use as the base of the pyramid?");
        System.out.println("1. Circle");
        System.out.println("2. Rectangle");
        System.out.println("3. Triangle");

        int baseOption = getIntInput("Choose a base option: ");
        double heightPyramid = getDoubleInput("Enter the height of the pyramid: ");

        Pyramid pyramid = null;
        switch (baseOption) {
            case 1 -> pyramid = createPyramidWithCircleBase(heightPyramid);
            case 2 -> pyramid = createPyramidWithRectangleBase(heightPyramid);
            case 3 -> pyramid = createPyramidWithTriangleBase(heightPyramid);
            default -> System.out.println("Invalid option");
        }

        if (pyramid != null) {
            displayPyramid(pyramid);
        }
    }

    private Pyramid createPyramidWithCircleBase(double height) {
        double radius = getDoubleInput("Enter the radius of the circle: ");
        Circle circle = new Circle(radius);
        return new Pyramid(circle, height);
    }

    private Pyramid createPyramidWithRectangleBase(double height) {
        double width = getDoubleInput("Enter the width of the rectangle: ");
        double rectHeight = getDoubleInput("Enter the height of the rectangle: ");
        Rectangle rectangle = new Rectangle(width, rectHeight);
        return new Pyramid(rectangle, height);
    }

    private Pyramid createPyramidWithTriangleBase(double height) {
        double sideA = getDoubleInput("Enter the first side of the triangle: ");
        double sideB = getDoubleInput("Enter the second side of the triangle: ");
        double sideC = getDoubleInput("Enter the third side of the triangle: ");
        Triangle triangle = new Triangle(sideA, sideB, sideC);
        return new Pyramid(triangle, height);
    }

    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. " + prompt);
            scanner.next();
        }
        return scanner.nextInt();
    }

    private double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input. " + prompt);
            scanner.next();
        }
        return scanner.nextDouble();
    }

    private void displayFigure(Object figure) {
        if (figure instanceof Circle circle) {
            circle.print();
            System.out.println("Area: " + circle.calculateArea());
            System.out.println("Perimeter: " + circle.calculatePerimeter() + "\n");
        } else if (figure instanceof Rectangle rectangle) {
            rectangle.print();
            System.out.println("Area: " + rectangle.calculateArea());
            System.out.println("Perimeter: " + rectangle.calculatePerimeter() + "\n");
        } else if (figure instanceof Triangle triangle) {
            triangle.print();
            System.out.println("Area: " + triangle.calculateArea());
            System.out.println("Perimeter: " + triangle.calculatePerimeter() + "\n");
        }
    }

    private void displayPyramid(Pyramid pyramid) {
        pyramid.print();
        System.out.println("Volume: " + pyramid.calculateVolume());
        System.out.println("Surface area: " + pyramid.calculateSurfaceArea() + "\n");
    }
}
