package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller {
  public TextField mInput;
  public TextField nInput;
  public Button startButton;
  public TextField pInput;
  public TextField msInput;
  @FXML GridPane gridPane;
  
  private List<List<Thread>> threads = new ArrayList<>();
  private ColorSquare[][] colorSquares;
  
  private int m, n;
  private double p;
  private long ms;
  private Random random = Main.random;
  
  @FXML public void startSimulation() {
    
    gridPane.getChildren().clear();
    m = Integer.parseInt(mInput.getText());
    n = Integer.parseInt(nInput.getText());
    p = Double.parseDouble(pInput.getText());
    ms = Long.parseLong(msInput.getText());
    
    colorSquares = new ColorSquare[m+2][n+2];
    
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        ColorSquare colorSquare = new ColorSquare(p, ms);
        colorSquare.setHeight(50);
        colorSquare.setWidth(50);
        colorSquares[i+1][j+1] = colorSquare;
        gridPane.add(colorSquare, i, j);
      }
    }
    for (int i = 1; i < m+1; i++) {
      colorSquares[i][0] = colorSquares[i][n];
      colorSquares[i][n+1] = colorSquares[i][1];
    }
    
    for (int i = 1; i < n+1; i++) {
      colorSquares[0][i] = colorSquares[m][i];
      colorSquares[m+1][i] = colorSquares[1][i];
    }
    colorSquares[0][0] = colorSquares[m][n];
    colorSquares[0][n+1] = colorSquares[m][1];
    colorSquares[m+1][0] = colorSquares[1][n];
    colorSquares[m+1][n+1] = colorSquares[1][1];
    
    for (int i = 1; i < m+1; i++) {
      List<Thread> threadsRow = new ArrayList<>();
      for (int j = 1; j < n+1; j++) {
        ColorSquare[] somsiady = new ColorSquare[4];
        for (int k = 0; k < 4; k++) {
          somsiady[k] = colorSquares[i][j - 1];
          somsiady[k] = colorSquares[i][j + 1];
          somsiady[k] = colorSquares[i - 1][j];
          somsiady[k] = colorSquares[i + 1][j];
        }
        colorSquares[i][j].setSomsiady(somsiady);
        Thread thread = new Thread(colorSquares[i][j]);
        threadsRow.add(thread);
      }
      threads.add(threadsRow);
    }
  
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        threads.get(i).get(j).start();
      }
    }
    
  }
}
