package Model;

import View.MazeDisplayer;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.MouseEvent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Observer;

public interface IModel {

    void generateMaze(int rows, int cols) throws UnknownHostException;
    Maze getMaze();
    void updatePlayerLocation(MovementDirection direction);
    int getPlayerRow();
    int getPlayerCol();
    void assignObserver(Observer o);
    void solveMaze() throws UnknownHostException;
    Solution getSolution();
    void setMaze(Maze maze);
    void stopServers();
    void setPlayerRow(int playerRow);
    void setPlayerCol(int playerCol);
    void saveMaze(String name) throws IOException;

    void mouseDrag(MouseEvent mouseEvent, MazeDisplayer mazeDisplayer);

}
