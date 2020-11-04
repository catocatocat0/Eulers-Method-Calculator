package catocato.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.InputMismatchException;
import java.util.Scanner;

public class EulersMethod {
    private Scanner scanner = new Scanner(System.in);
    private File file;

    public static void main(String[] args){
        EulersMethod euler = new EulersMethod();
        euler.eulersMethod();
    }

    public double equation(double x, double y){
        //what equation you want to run
        return 0.01*y*(1-y/100);
    }

    public void eulersMethod(){
        double iterations;
        double stepSize;
        double y;
        double x;

        //how many iterations of euler's method to do
        System.out.println("Enter the number of iterations");
        while(true) {
            try {
                iterations = scanner.nextDouble();
                break;
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("Invalid input! Enter the number of iterations");
            }
        }

        //the step size of each iteration
        System.out.println("Enter the step size");
        while(true) {
            try {
                stepSize = scanner.nextDouble();
                break;
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("Invalid input! Enter the step size");
            }
        }

        //initial y value
        System.out.println("Enter the initial y value");
        while(true) {
            try {
                y = scanner.nextDouble();
                break;
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("Invalid input! Enter the initial y value");
            }
        }

        //initial x value
        System.out.println("Enter the initial x value");
        while(true) {
            try {
                x = scanner.nextDouble();
                break;
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("Invalid input! Enter the initial x value");
            }
        }


        String result = "";

        for(int i = 0; i <iterations; i++) {
            result = result + "("+x+","+y+")\n";
            y = y + stepSize*(equation(x,y));
            x = x + stepSize;
        }

        if(outputResults(result)){
            System.out.println("Results successfully written to results.txt!");
        }
    }

    public void createFile(String results){
        try{
            try {
                String path = EulersMethod.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                path = URLDecoder.decode(path,"UTF-8");
                file = new File(path);
                file = new File(file.getParentFile().getPath(), "results.txt");
            }catch (NullPointerException e){
                e.printStackTrace();
                System.out.println("Was not able to access the file location!");
            }

            if(!file.exists()){
                file.createNewFile();
            }

        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Was not able to create file");
        }
    }

    public boolean outputResults(String results){
        boolean enable = false;

        createFile(results);

        if(file.exists()){
            try {
                FileWriter write = new FileWriter(file);
                write.write(results);
                write.flush();
                enable = true;
            }catch (IOException e){
                e.printStackTrace();
                System.out.println("Error writing to file!");
            }

        }else{
            System.out.println("Was not able to write to file!");
        }
        return enable;
    }

}
