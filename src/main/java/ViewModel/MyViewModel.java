package ViewModel;

import Model.IModel;

import Model.MovementDirection;
import Server.Configurations;
import View.MazeDisplayer;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import Model.MyModel;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel model;

    /** The ViewModel layer is responsible for connecting the View layer to the Model layer.
     * Constructor of MyViewModel class
     * @param model represents IModel
     */
    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this); //Observe the Model for it's changes
    }


    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    /** This function set a new maze to the param maze
     * @param maze represent a new maze
     */
    public void setMaze(Maze maze){
        model.setMaze(maze);
    }

    /** This function return a maze
     * @return a maze by model class
     */
    public Maze getMaze(){
        return model.getMaze();
    }

    /** This function return the row number that the player is on
     * @return the row number that the player is on
     */
    public int getPlayerRow(){
        return model.getPlayerRow();
    }

    /** This function return the column number that the player is on
     * @return the column number that the player is on
     */
    public int getPlayerCol(){
        return model.getPlayerCol();
    }

    /**
     * @return solution of a specific mane
     */
    public Solution getSolution(){
        return model.getSolution();
    }

    /** This function generate a maze by the server, according client's request
     * @param rows represents number of rows in maze
     * @param cols represents number of columns in maze
     * @throws UnknownHostException
     */
    public void generateMaze(int rows, int cols) throws UnknownHostException {
        model.generateMaze(rows, cols);
    }

    /** This function update the player's location according the keyEvent that represent step
     * @param keyEvent represents the direction of the player
     */
    public void movePlayer(KeyEvent keyEvent){
        MovementDirection direction;
        switch (keyEvent.getCode()){
            case NUMPAD8 -> direction = MovementDirection.UP;
            case NUMPAD2 -> direction = MovementDirection.DOWN;
            case NUMPAD4 -> direction = MovementDirection.LEFT;
            case NUMPAD6 -> direction = MovementDirection.RIGHT;
            case NUMPAD3 -> direction = MovementDirection.RIGHTDOWN;
            case NUMPAD1 -> direction = MovementDirection.LEFTDOWN;
            case NUMPAD7 -> direction = MovementDirection.LEFTUP;
            case NUMPAD9 -> direction = MovementDirection.RIGHTUP;
            default -> {
                // no need to move the player...
                return;
            }
        }
        model.updatePlayerLocation(direction);
    }

    /** This function set the row number position of the player
     * @param num represents the row number that the player is on
     */
    public void setPlayerRow(int num){
        model.setPlayerRow(num);
    }

    /** This function set the column number position of the player
     * @param num represents the column number that the player is on
     */
    public void setPlayerCol(int num){
        model.setPlayerCol(num);
    }

    /** This function solve a maze by the server, according client's request
     * @throws UnknownHostException
     */
    public void solveMaze() throws UnknownHostException {
        model.solveMaze();
    }

    /** With this function you can move the player by drag the mouse
     * @param mouseEvent represents the mouse
     * @param mazeDisplayer represents the maze in our application
     */
    public void mouseDrag(MouseEvent mouseEvent, MazeDisplayer mazeDisplayer) {
        model.mouseDrag(mouseEvent,mazeDisplayer);
    }


    /** This function save a maze as a file
     * @param name represents the name of the maze that the user wants to save
     * @throws IOException
     */
    public void saveMaze(String name) throws IOException {
        model.saveMaze(name);
    }

    /** This function set to properties of the maze by user's request
     * @param num represents the number of thread pool
     * @param mazeAlgo represents the generate algorithm the user wants to use
     * @param solveAlgo represent the solve algorithm the user wants to use
     * @throws IOException
     * @throws InterruptedException
     */
    public void setProperties(String num, String mazeAlgo, String solveAlgo) throws IOException, InterruptedException {
        model.setProperties(num,mazeAlgo,solveAlgo);
    }

    /** This function stop the all servers
     * @throws InterruptedException
     */
    public void stopServers() throws InterruptedException {
        model.stopServers();
    }
}
