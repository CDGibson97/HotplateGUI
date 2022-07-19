package Question_1;

import java.awt.*;
import java.util.ArrayList;

public class Element implements Runnable {
    private final ArrayList<Element> neighbours = new ArrayList<>();
    private double currentTemp;
    private static double heatConstant = 0.1;
    public static int x = 0;
    public static int y = 0;
    public boolean stopRequested = false;

    public Element(double startTemp) {
        currentTemp = startTemp;
    }

    public static int getX() {
        return x;
    }

    public static int getY() {
        return y;
    }


    public double getTemperature() {
        return this.currentTemp;
    }

    public void requestStop() {
        stopRequested = true;
    }

    public void addNeighbour(Element e) {
        neighbours.add(e);
    }

    public void applyTempToElement(double appliedTemp, double heatConstant) {
        currentTemp += (appliedTemp - currentTemp) * heatConstant;
    }

    public void run() {

        while (!stopRequested) {
            getTemperature();

            double average = 0;

            for (Element neighbour : neighbours) {
                average += neighbour.currentTemp;
            }
            average = average / neighbours.size();

            currentTemp += (average - currentTemp) * heatConstant;

            try {
                Thread.sleep(20);
            } catch (Exception e) {
            }
        }
    }

    public void draw(Graphics g) {

        float red = ((float) this.currentTemp / 1000);
        float blue = ((float) this.currentTemp / 1000);
        float green = 0.0f;

        blue = 1 - blue;
        g.setColor(new Color(red, green, blue));
        g.fillRect(x, y, 39, 39);
    }

    public static void main(String[] args) {
        Element element1 = new Element(100);
        Element element2 = new Element(0);
        element1.addNeighbour(element2);
        element2.addNeighbour(element1);

        Thread thread1 = new Thread(element1);
        Thread thread2 = new Thread(element2);
        thread1.start();
        thread2.start();

        for (int i = 0; i < 20; i++) {
            System.out.println(element1.currentTemp + " " + element2.currentTemp);
            element1.currentTemp = element1.getTemperature();
            element2.currentTemp = element2.getTemperature();
        }
    }
}
