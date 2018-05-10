package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class Controller {
  public TextField mInput;
  public TextField nInput;
  public Button startButton;
  public TextField pInput;
  public TextField msInput;
  @FXML GridPane gridPane;
  @FXML ToolBar toolBar;
  @FXML VBox vBox;
  private List<List<Thread>> threads = new ArrayList<>();

  @FXML public void startSimulation() {
    threads.clear();
    gridPane.getChildren().clear();
    Main.start = false;
    int m = Integer.parseInt(mInput.getText());
    int n = Integer.parseInt(nInput.getText());
    double p = Double.parseDouble(pInput.getText());
    long ms = Long.parseLong(msInput.getText());
    // TODO szkalowanie
    ColorSquare[][] colorSquares = new ColorSquare[m + 2][n + 2];
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        ColorSquare colorSquare = new ColorSquare(p, ms);
        colorSquare.setHeight((gridPane.getHeight() - toolBar.getHeight())/m);
        colorSquare.setWidth(gridPane.getWidth()/n);
        colorSquares[i+1][j+1] = colorSquare;
        gridPane.add(colorSquare, i, j);
      }
    }
    for (int i = 1; i < m +1; i++) {
      colorSquares[i][0] = colorSquares[i][n];
      colorSquares[i][n +1] = colorSquares[i][1];
    }
    
    for (int i = 1; i < n +1; i++) {
      colorSquares[0][i] = colorSquares[m][i];
      colorSquares[m +1][i] = colorSquares[1][i];
    }
    colorSquares[0][0] = colorSquares[m][n];
    colorSquares[0][n +1] = colorSquares[m][1];
    colorSquares[m +1][0] = colorSquares[1][n];
    colorSquares[m +1][n +1] = colorSquares[1][1];
    
    for (int i = 1; i < m +1; i++) {
      List<Thread> threadsRow = new ArrayList<>();
      for (int j = 1; j < n +1; j++) {
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

    Main.start = true;

    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        threads.get(i).get(j).start();
      }
    }
    
  }
}
