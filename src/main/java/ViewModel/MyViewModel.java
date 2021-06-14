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

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this); //Observe the Model for it's changes
    }


    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }
    public void setMaze(Maze maze){
        model.setMaze(maze);
    }

    public Maze getMaze(){
        return model.getMaze();
    }

    public int getPlayerRow(){
        return model.getPlayerRow();
    }

    public int getPlayerCol(){
        return model.getPlayerCol();
    }

    public Solution getSolution(){
        return model.getSolution();
    }

    public void generateMaze(int rows, int cols) throws UnknownHostException {
        model.generateMaze(rows, cols);
    }

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
    public void setPlayerRow(int num){
        model.setPlayerRow(num);
    }
    public void setPlayerCol(int num){
        model.setPlayerCol(num);
    }

    public void solveMaze() throws UnknownHostException {
        model.solveMaze();
    }

    public void mouseDrag(MouseEvent mouseEvent, MazeDisplayer mazeDisplayer) {
        model.mouseDrag(mouseEvent,mazeDisplayer);
    }


    public void saveMaze(String name) throws IOException {
        model.saveMaze(name);
    }

    public void setProperties(String num, String mazeAlgo, String solveAlgo) throws IOException, InterruptedException {
        model.setProperties(num,mazeAlgo,solveAlgo);
    }

    public void stopServers() throws InterruptedException {
        model.stopServers();
    }
}
