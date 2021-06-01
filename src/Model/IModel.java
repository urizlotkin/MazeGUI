package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

public interface IModel {

    public Maze generateMazeFromServer();
    public Solution serverSolveMaze();
    public void saveCurrentMaze();
    public Maze loadMaze(String name);


}
