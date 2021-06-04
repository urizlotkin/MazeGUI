package Model;

import Client.Client;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.AState;
import algorithms.search.Solution;
import Client.IClientStrategy;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {
    private Maze maze;
    private int playerRow;
    private int playerCol;
    private Solution solution;
    private Server generateMaze;
    private Server solveSearchProblem;
    public MyModel(){
        this.generateMaze = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        this.solveSearchProblem = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        this.generateMaze.start();
        this.solveSearchProblem.start();
    }
    @Override
    public void generateMaze(int row, int col) throws UnknownHostException {
        Client client = new Client(InetAddress.getByName("1.0.0.127"), 5400, new IClientStrategy() {
            public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                try {
                    ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                    ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                    toServer.flush();
                    int[] mazeDimensions = new int[]{row, col};
                    toServer.writeObject(mazeDimensions);
                    toServer.flush();
                    byte[] compressedMaze = (byte[])fromServer.readObject();
                    InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                    byte[] decompressedMaze = new byte[24 + mazeDimensions[0] * mazeDimensions[1]];
                    is.read(decompressedMaze);
                    maze = new Maze(decompressedMaze);
                }
                catch (Exception var10) {
                    var10.printStackTrace();
                }

            }
        });
        setChanged();
        notifyObservers("new Maze generated");
    }

    public void solveMaze() {
            try {
                Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                    public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                        try {
                            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                            toServer.flush();
                            toServer.writeObject(maze);
                            toServer.flush();
                            solution = (Solution)fromServer.readObject();
                        } catch (Exception var10) {
                            var10.printStackTrace();
                        }

                    }
                });
            } catch (UnknownHostException var1) {
                var1.printStackTrace();
            }
        setChanged();
        notifyObservers("Maze solved");
        }

    public void saveCurrentMaze() {

    }

    public Maze loadMaze(String name) {
        return null;
    }


    @Override
    public int[][] getMaze() {
        return maze.getMaze();
    }

    @Override
    public void updatePlayerLocation(MovementDirection direction) {
        switch (direction) {
            case UP -> {
                if (playerRow > 0)
                    movePlayer(playerRow - 1, playerCol);
            }
            case DOWN -> {
                if (playerRow < maze.getMaze().length- 1)
                    movePlayer(playerRow + 1, playerCol);
            }
            case LEFT -> {
                if (playerCol > 0)
                    movePlayer(playerRow, playerCol - 1);
            }
            case RIGHT -> {
                if (playerCol < maze.getMaze()[0].length - 1)
                    movePlayer(playerRow, playerCol + 1);
            }
        }


    }

    private void movePlayer(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        setChanged();
        notifyObservers("player moved");
    }


    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    @Override
    public int getPlayerCol() {
        return playerCol;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }


    @Override
    public Solution getSolution() {
        return solution;
    }

}

