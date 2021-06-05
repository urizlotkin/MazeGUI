package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import com.sun.javafx.stage.EmbeddedWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController extends AView implements  IView , Observer {

    public MyViewModel viewModel;
    public Button generateMaze;
    public Button solveMaze;

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addObserver( this);
        this.solveMaze.setDisable(true);
    }
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public void generateMaze(ActionEvent actionEvent) throws UnknownHostException, FileNotFoundException {
        String rowText = textField_mazeRows.getText();
        String columnText = textField_mazeColumns.getText();
        if(!(isNumeric(rowText)) ||!(isNumeric(columnText))) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("can't generate maze one of parameters is not a number");
            alert.show();
        }
        else {
            int rows = Integer.valueOf(rowText);
            int columns = Integer.valueOf(columnText);
            if (rows <= 1 || columns <= 1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("can't generate maze one of parameters zero or below");
                alert.show();
            } else {
                viewModel.generateMaze(rows, columns);
                setUpdatePlayerRow(0);
                setUpdatePlayerCol(0);
                setPlayerPosition(0, 0);
                this.solveMaze.setDisable(false);
            }
        }
    }

    public void solveMaze(ActionEvent actionEvent) throws UnknownHostException {
        viewModel.solveMaze();
    }
    public void keyPress (KeyEvent keyEvent){
        viewModel.movePlayer(keyEvent);
        keyEvent.consume();
    }

    public void setPlayerPosition(int row, int col) throws FileNotFoundException {
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }

    private void setUpdatePlayerCol(int col) {
        this.updatePlayerCol.set(col+"");
    }

    private void setUpdatePlayerRow(int row) {
        this.updatePlayerRow.set(row+"");
    }


    public void mouseClick(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    @Override
    public void update(Observable o, Object arg) {
        String change = (String) arg;
        switch (change){
            case "Maze generated" -> {
                try {
                    mazeGenerated();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            case "player moved" -> {
                try {
                    playerMoved();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            case "maze solved" -> {
                try {
                    mazeSolved();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            default -> System.out.println("Not implemented change: " + change);
        }
    }

    private void mazeSolved() throws FileNotFoundException {
        mazeDisplayer.setSolution(viewModel.getSolution());
    }

    private void playerMoved() throws FileNotFoundException {
        setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
    }

    private void mazeGenerated() throws FileNotFoundException {
        mazeDisplayer.drawMaze(viewModel.getMaze());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
    }

    public void saveMaze(ActionEvent actionEvent) throws IOException {
        if(this.viewModel.getMaze() == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("There is no existing maze to save");
            alert.show();
        }
        else
        {
            switchSence("Save.fxml");
        }
    }
}
