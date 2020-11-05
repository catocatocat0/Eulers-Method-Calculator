package catocato.test;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.InputMismatchException;
import java.util.Scanner;

public class EulersMethod {
    private final Scanner scanner = new Scanner(System.in);
    private File file;
    private String expression;
    int iterations;
    double stepSize;
    double y;
    double x;

    EulersMethod(){
        eulersMethod();
        file = null;
        expression = null;
    }

    public static void main(String[] args){
        new EulersMethod();
    }

    public double equation(double x, double y){
        //what equation you want to run
        Expression e = new ExpressionBuilder(expression)
                .variables("x","y")
                .build()
                .setVariable("x",x)
                .setVariable("y",y);
        return e.evaluate();
    }

    public void eulersMethod(){

        //the equation to evaluate
        System.out.println("Enter the equation");
        while(true) {
            try {
                expression = scanner.next();
                break;
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("Invalid input! Enter the equation");
            }
        }

        //how many iterations of euler's method to do
        System.out.println("Enter the number of iterations");
        while(true) {
            try {
                iterations = scanner.nextInt();
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
        XYSeries series = new XYSeries("dy/dx = "+expression+"; y("+x+") = "+y);
        for(int i = 0; i <iterations; i++) {
            series.add(x,y);
            result = result + x+" "+y+"\n";
            y = y + stepSize*equation(x,y);
            x = x + stepSize;
        }

        XYSeriesCollection plotData = new XYSeriesCollection();

        plotData.addSeries(series);

        if(outputResults(result)&&plot(plotData)){
            System.out.println("Results successfully written to results.txt & plot.png!");
            System.exit(0);
        }else{
            System.out.println("There was an error writing results... Exiting");
            System.exit(-1);
        }
    }

    public void createFile(){
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

    public boolean plot(XYDataset plotData){
        boolean status = false;
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Euler's method graph: "+iterations+" iterations, "+stepSize+" step size.",
                "x",
                "y",
                plotData,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle("Euler's method graph: "+iterations+" iterations, "+stepSize+" step size.",
                        new Font("Serif", java.awt.Font.BOLD, 18)
                )
        );

        try {
            ChartUtils.saveChartAsPNG(new File("plot.png"), chart, 1500, 1000);
            status = true;
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("There was an error saving the chart image.");
        }

        return status;
    }

    public boolean outputResults(String results){
        boolean enable = false;

        createFile();

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
