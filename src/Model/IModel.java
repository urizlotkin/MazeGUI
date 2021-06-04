package Model;

import algorithms.search.Solution;

import java.net.UnknownHostException;
import java.util.Observer;

public interface IModel {

    void generateMaze(int rows, int cols) throws UnknownHostException;
    int[][] getMaze();
    void updatePlayerLocation(MovementDirection direction);
    int getPlayerRow();
    int getPlayerCol();
    void assignObserver(Observer o);
    void solveMaze();
    Solution getSolution();



}
