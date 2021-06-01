package Model;

import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

public class MyModel implements  IModel{
    private Server generateMaze;
    private Server solveSearchProblem;
    public MyModel(){
        this.generateMaze = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        this.solveSearchProblem = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        this.generateMaze.start();
        this.solveSearchProblem.start();
    }
    public Maze generateMazeFromServer() {
        return null;
    }

    public Solution serverSolveMaze() {
        return null;
    }

    public void saveCurrentMaze() {

    }

    public Maze loadMaze(String name) {
        return null;
    }
}