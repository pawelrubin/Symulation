package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller {
  public BorderPane borderPane;
  public VBox vBox;
  public TextField mInput;
  public TextField nInput;
  public TextField pInput;
  public TextField msInput;
  public TextField sizeInput;
  public ScrollPane scrollPane;
  public GridPane gridPane;
  public Button stopButton;

  private Random random = Main.random;
  private List<List<Thread>> threads = new ArrayList<>();
  private ColorSquare[][] colorSquares = new ColorSquare[0][0];
  private boolean check;
  private int height;
  private int width;
  private double probability;
  private long delay;
  private double size;

  public void initialize() {
    stopButton.setVisible(false);
  }

  private void simulate() {
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        ColorSquare colorSquare = new ColorSquare(probability, delay, size, random);
        colorSquares[i+1][j+1] = colorSquare;
        gridPane.add(colorSquare, j, i);
      }
    }
  
    // Setting somsiady
    for (int i = 1; i < height + 1; i++) {
      colorSquares[i][0] = colorSquares[i][width];
      colorSquares[i][width +1] = colorSquares[i][1];
    }
  
    for (int i = 1; i < width + 1; i++) {
      colorSquares[0][i] = colorSquares[height][i];
      colorSquares[height +1][i] = colorSquares[1][i];
    }
    
    colorSquares[0][0] = colorSquares[height][width];
    colorSquares[0][width +1 ] = colorSquares[height][1];
    colorSquares[height +1][0] = colorSquares[1][width];
    colorSquares[height +1][width +1] = colorSquares[1][1];
  
    for (int i = 1; i < height + 1; i++) {
      List<Thread> threadsRow = new ArrayList<>();
      for (int j = 1; j < width + 1; j++) {
        ColorSquare[] somsiady = new ColorSquare[8];
      
        somsiady[0] = colorSquares[i][j - 1];
        somsiady[1] = colorSquares[i][j + 1];
        somsiady[2] = colorSquares[i - 1][j];
        somsiady[3] = colorSquares[i + 1][j];
        somsiady[4] = colorSquares[i - 1][j - 1];
        somsiady[5] = colorSquares[i - 1][j + 1];
        somsiady[6] = colorSquares[i + 1][j -1 ];
        somsiady[7] = colorSquares[i + 1][j + 1];
        
        colorSquares[i][j].setSomsiady(somsiady);
        Thread thread = new Thread(colorSquares[i][j]);
        threadsRow.add(thread);
      }
      threads.add(threadsRow);
    }
    // end of setting somsiady xD
    
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        threads.get(i).get(j).start();
      }
    }

    switchStopButtonVisibility(true);
  }

  private void switchStopButtonVisibility(boolean running) {
    stopButton.setVisible(running);
  }

  @FXML public void startSimulation() {
    try {
      stopSimulation();

      height = Integer.parseInt(mInput.getText());
      width = Integer.parseInt(nInput.getText());
      probability = Double.parseDouble(pInput.getText());
      delay = Long.parseLong(msInput.getText());
      size = Double.parseDouble(sizeInput.getText());
      
      if (height < 1) {
        throw new IllegalArgumentException("Wrong height value. Should be >= 1");
      }

      if (width < 1) {
        throw new IllegalArgumentException("Wrong width value. Should be >= 1");
      }

      if (delay < 0) {
        throw new IllegalArgumentException("Wrong delay value. Should be >= 0");
      }

      if (probability > 1 || probability < 0) {
        throw new IllegalArgumentException("Wrong probability value.");
      }

      if (size < 1) {
        throw new IllegalArgumentException("Wrong size.");
      }

      colorSquares = new ColorSquare[height + 2][width + 2];
  

      
      simulate();
      
    } catch (IllegalArgumentException e) {
      Alert.display(e.getClass().getSimpleName(), e.getMessage());
    }
  }

  private void stopSimulation() {
    for (int i = 0; i < colorSquares.length; i++) {
      for (int j = 0; j < colorSquares[i].length; j++) {
        colorSquares[i][j].kill();
      }
    }

    threads.clear();
    gridPane.getChildren().clear();

    switchStopButtonVisibility(false);
  }

  @FXML public void pauseSimulation() {
    stopSimulation();
  }

  /**
   * Method executes {@link ColorSquare#setRandomColor() mouseEvent target}
   *
   * @param mouseEvent OnMouseMoved
   */
  private void interact(MouseEvent mouseEvent) {
      ColorSquare colorSquare = (ColorSquare) mouseEvent.getTarget();
      colorSquare.setRandomColor();
  }
  
  /**
   * Method switches {@link #gridPane}.{@link Node#setOnMouseMoved(javafx.event.EventHandler)} to
   * {@link #interact} or null
   */
  public void interactButton() {
    check ^= true;
    if (check) {
      gridPane.setOnMouseMoved(this::interact);
      gridPane.setOnMouseClicked(this::explode);
      gridPane.setOnMousePressed(this::explode);
    } else {
      gridPane.setOnMouseMoved(null);
      gridPane.setOnMouseClicked(null);
    }
  }

  private void explode(MouseEvent mouseEvent) {
    ColorSquare colorSquare = (ColorSquare) mouseEvent.getTarget();
    colorSquare.explode();
  }
}
