package sample;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.Random;

public class ColorSquare extends Rectangle implements Runnable {
  private double probability;
  private long delay;
  private Random random;
  private ColorSquare[] neighbours;
  private boolean alive;
  private int consideredNeighbours = 4;
  
  /**
   * Constructor for ColorSquare.
   *
   * @param probability Probability of {@link #setRandomColor()}.
   * @param delay Delay of {@link #run()}.
   * @param size Size of a ColorSquare object.
   * @param random {@link Random} object for generaating random values.
   *
   * @throws IllegalArgumentException if parameters are invalid.
   */
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
    this.setRandomColor();
    this.probability = probability;
    this.delay = delay;
    
    alive = true;
  }
  
  @Override
  public synchronized void run() {
    while (alive) {
      try {
        Thread.sleep((long) (0.5 * delay + ((long) (random.nextDouble() * 1.5 * delay))));
        
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
   * Method kills {@link ColorSquare} object by setting {@link #alive} to false.
   */
  void kill() {
    alive = false;
  }
  
  /**
   * Method sets {@link #neighbours}.
   *
   * @param neighbours
   */
  void setNeighbours(ColorSquare[] neighbours) {
    this.neighbours = neighbours;
  }
  
  void considerEightNeighbours() {
    consideredNeighbours = 8;
  }
  
  void considerFourNeightbours() {
    consideredNeighbours = 4;
  }
  
  void explode() {
    for (int i = 0; i < consideredNeighbours; i++) {
      neighbours[i].setRandomColor();
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
    
    for (int i = 0; i < consideredNeighbours; i++) {
      red += ((Color) neighbours[i].getFill()).getRed();
      green += ((Color) neighbours[i].getFill()).getGreen();
      blue += ((Color) neighbours[i].getFill()).getBlue();
    }
    
    Color color = Color.color(red/consideredNeighbours, green/consideredNeighbours, blue/consideredNeighbours);
    Platform.runLater(() -> this.setFill(color));
  }

  
}
