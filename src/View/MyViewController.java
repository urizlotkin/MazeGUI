package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class MyViewController implements  IView {
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;



    public void generateMaze(ActionEvent actionEvent) {
        String rowText = textField_mazeRows.getText();
        int rows = Integer.valueOf(rowText);
        String columnText = textField_mazeColumns.getText();
        int columns = Integer.valueOf(columnText);
        // ask from server to generate maze
        // maze
        // draw maze
        MyMazeGenerator genearte = new MyMazeGenerator();
        Maze maze = genearte.generate(rows,columns);
        mazeDisplayer.drawMaze(maze.getMaze());



    }

    public void solveMaze(ActionEvent actionEvent) {
    }
    public void keyPress (ActionEvent actionEvent){

    }

    public void mouseClick(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }
}