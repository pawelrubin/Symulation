package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class Controller {
  public BorderPane borderPane;
  public VBox vBox;
  public TextField mInput;
  public TextField nInput;
  public TextField pInput;
  public TextField msInput;
  public TextField sizeInput;
  public Button startButton;
  public ScrollPane scrollPane;
  public GridPane gridPane;
  
  private List<List<Thread>> threads = new ArrayList<>();
  private Random random = Main.random;
  private boolean check;
  
  // TODO zabijanie watkow
  @FXML public void startSimulation() {
    threads.clear();
    gridPane.getChildren().clear();
    Main.start = false;
    int height = Integer.parseInt(mInput.getText());
    int width = Integer.parseInt(nInput.getText());
    double probability = Double.parseDouble(pInput.getText());
    long delay = Long.parseLong(msInput.getText());
    double size = Double.parseDouble(sizeInput.getText());
  
    ColorSquare[][] colorSquares = new ColorSquare[height + 2][width + 2];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        ColorSquare colorSquare = new ColorSquare(probability, delay);
        colorSquare.setHeight(size);
        colorSquare.setWidth(size);
        colorSquares[i+1][j+1] = colorSquare;
        gridPane.add(colorSquare,j,i);
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
        ColorSquare[] somsiady = new ColorSquare[4];
        
          somsiady[0] = colorSquares[i][j - 1];
          somsiady[1] = colorSquares[i][j + 1];
          somsiady[2] = colorSquares[i - 1][j];
          somsiady[3] = colorSquares[i + 1][j];

        colorSquares[i][j].setSomsiady(somsiady);
        Thread thread = new Thread(colorSquares[i][j]);
        threadsRow.add(thread);
      }
      threads.add(threadsRow);
    }
    // end of setting somsiady xD
    
    Main.start = true;

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        threads.get(i).get(j).start();
      }
    }
    
  }
  
  private void interact(MouseEvent mouseEvent) {
      ColorSquare colorSquare = (ColorSquare) mouseEvent.getTarget();
      System.out.println("changing color of this");
      double red = random.nextDouble();
      double green = random.nextDouble();
      double blue = random.nextDouble();
      Color color = Color.color(red, green, blue);
      Platform.runLater(() -> colorSquare.setFill(color));
  }
  
  public void interactButton(ActionEvent actionEvent) {
    check ^= true;
    if (check) {
      gridPane.setOnMouseMoved(this::interact);
    } else {
      gridPane.setOnMouseMoved(null);
    }
  }
}
