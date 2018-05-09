package sample;

import javafx.scene.paint.Color;

import java.util.Random;

public class ColorMethods {
  private static Random random = Main.random;
  
  public static synchronized void colorChange (Color color) {
    color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
  }
}
