package sample;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.Random;

public class ColorSquare extends Rectangle implements Runnable {
  private double probability;
  private long delay;
  private Random random;
  private ColorSquare[] somsiady;
  private boolean alive;
  
  ColorSquare(double probability, long delay, double size, Random random) throws IllegalArgumentException {
    super(size, size);
    
    if (probability < 0 || probability > 1) {
      throw new IllegalArgumentException("Invalid probability value.");
    } else if (delay < 0) {
      throw new IllegalArgumentException("Invalid delay value.");
    } else if (size < 0) {
      throw new IllegalArgumentException("Invalid size value.");
    }
    
    this.random = random;
    this.setFill(Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble()));
    this.probability = probability;
    this.delay = delay;
    
    alive = true;
  }
  
  void kill() {
    alive = false;
  }
  
  void setSomsiady(ColorSquare[] somsiady) {
    this.somsiady = somsiady;
  }
  
  @Override
  public synchronized void run() {
    while (alive) {
      try {
        Thread.sleep((long)(0.5* delay +((long) (random.nextDouble() * 1.5* delay))));
        
        if (random.nextDouble() < probability) {
          setRandomColor();
        }
        
        if (random.nextDouble() < (1 - probability)) {
          setAverageNeighboursColor();
        }
      } catch (InterruptedException ex) {
        System.out.println(ex.getMessage());
      }
    }
  }
  
  /**
   * Method sets random color of this.
   */
  void setRandomColor() {
    System.out.println("changing color of this");
    Platform.runLater(() -> this.setFill(Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble())));
  }
  
  /**
   * Method sets color of this to average of neighbours.
   */
  private void setAverageNeighboursColor() {
    System.out.println("changing color according to neighbours");
    double red = 0;
    double green = 0;
    double blue = 0;
    
    for (int i = 0; i < 8; i++) {
      red += ((Color) somsiady[i].getFill()).getRed();
      green += ((Color) somsiady[i].getFill()).getGreen();
      blue += ((Color) somsiady[i].getFill()).getBlue();
    }
    
    Color color = Color.color(red/8, green/8, blue/8);
    Platform.runLater(() -> this.setFill(color));
  }
}
