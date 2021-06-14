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

    /** This function generate a maze by the server, according client's request
     * @param rows represents number of rows in maze
     * @param cols represents number of columns in maze
     * @throws UnknownHostException
     */
    void generateMaze(int rows, int cols) throws UnknownHostException;

    /**
     * @return the maze
     */
    Maze getMaze();

    /** This function update the player's location according his steps
     * @param direction represents the direction of the player
     */
    void updatePlayerLocation(MovementDirection direction);

    /** This function return the row number that the player is on
     * @return the row number that the player is on
     */
    int getPlayerRow();

    /** This function return the column number that the player is on
     * @return the column number that the player is on
     */
    int getPlayerCol();
    void assignObserver(Observer o);

    /** This function solve a maze by the server, according client's request
     * @throws UnknownHostException
     */
    void solveMaze() throws UnknownHostException;


    /**
     * @return solution of a specific mane
     */
    Solution getSolution();

    /** This function set a new maze to the param maze
     * @param maze represent a new maze
     */
    void setMaze(Maze maze);

    /** This function set the row number position of the player
     * @param playerRow represents the row number that the player is on
     */
    void setPlayerRow(int playerRow);

    /** This function set the column number position of the player
     * @param playerCol represents the column number that the player is on
     */
    void setPlayerCol(int playerCol);

    /** This function save a maze as a file
     * @param name represents the name of the maze that the user wants to save
     * @throws IOException
     */
    void saveMaze(String name) throws IOException;

    /** With this function you can move the player by drag the mouse
     * @param mouseEvent represents the mouse
     * @param mazeDisplayer represents the maze in our application
     */
    void mouseDrag(MouseEvent mouseEvent, MazeDisplayer mazeDisplayer);

    /** This function set to properties of the maze by user's request
     * @param num represents the number of thread pool
     * @param mazeAlgo represents the generate algorithm the user wants to use
     * @param solveAlgo represent the solve algorithm the user wants to use
     * @throws IOException
     * @throws InterruptedException
     */
    void setProperties(String num, String mazeAlgo, String solveAlgo) throws IOException, InterruptedException;

    /** This function stop the all servers
     * @throws InterruptedException
     */
    void stopServers() throws InterruptedException;
}
