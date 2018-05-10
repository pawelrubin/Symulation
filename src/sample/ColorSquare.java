package sample;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class ColorSquare extends Rectangle implements Runnable {
  private double red, green, blue;
  private double p;
  private long ms;
  private Random random = Main.random;
  private ColorSquare[] somsiady;
  
  public ColorSquare(double p, long ms) throws IllegalArgumentException {
    super();
    
    if (p < 0 || p > 1) {
      Alert.display("Nieprawidłowe prawdopodobieśntwo","Prawdopodobieńśtwo musi być z przedziału od 0 do 1");
      throw new IllegalArgumentException("Wrong p");
    }

    if (ms <= 0) {
      Alert.display("Nieprawidłowy czas", "Czas nie może być mniejszy od zera");
      throw new IllegalArgumentException("Wrong time");
    }

    red = random.nextDouble();
    green = random.nextDouble();
    blue = random.nextDouble();
    Color color = Color.color(red,green,blue);
    
    this.setFill(color);
    this.p = p;
    this.ms = ms;
  }
  
  void setSomsiady(ColorSquare[] somsiady) {
    this.somsiady = somsiady;
  }
  
  @Override
  public synchronized void run() {
    while (Main.start) {
      try {
        Thread.sleep((long)(0.5*ms+((long) (random.nextDouble() * 1.5*ms))));
        if (random.nextDouble() < p) {
          System.out.println("changing color of this");
          red = random.nextDouble();
          green = random.nextDouble();
          blue = random.nextDouble();
          Color color = Color.color(red, green, blue);
          Platform.runLater(() -> this.setFill(color));
        } else if (random.nextDouble() < (1 - p)) {
          System.out.println("changing color accoring to neightours");
          red = 0;
          green = 0;
          blue = 0;
          for (int i = 0; i < 4; i++) {
            red += ((Color) somsiady[i].getFill()).getRed();
            green += ((Color) somsiady[i].getFill()).getGreen();
            blue += ((Color) somsiady[i].getFill()).getBlue();
          }
          red /= 4;
          green /= 4;
          blue /= 4;
          System.out.println("avg rgb: " + red + " " + green + " " + blue);
          Color color = Color.color(red, green, blue);
          Platform.runLater(() -> this.setFill(color));
        }
      } catch (InterruptedException ex) {
        System.out.println(ex.getMessage());
      }
    }
  }
}
