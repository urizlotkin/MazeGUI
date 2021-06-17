package Model;
import Client.Client;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import View.MazeDisplayer;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.AState;
import algorithms.search.Solution;
import Client.IClientStrategy;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import Server.Configurations;

public class MyModel extends Observable implements IModel {
    private Maze maze;
    private int playerRow = 0;
    private int playerCol = 0;
    private Solution solution;
    private Server generateMaze;
    private Server solveSearchProblem;
    private int x;
    private final Logger LOG = LogManager.getLogger();

    /**
     * Constructor of MyModel class
     */
    public MyModel() {
        this.generateMaze = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        this.solveSearchProblem = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        this.generateMaze.start();
        LOG.info("Generator maze server start to work with port "+ 5400);
        this.solveSearchProblem.start();
        LOG.info("Solving maze server start to work with port "+ 5401);
    }


    /** This function generate a maze by the server, according client's request
     * @param row represents number of rows in maze
     * @param col represents number of columns in maze
     * @throws UnknownHostException
     */
    @Override
    public void generateMaze(int row, int col) throws UnknownHostException {
        Client client = new Client( InetAddress.getLocalHost()/*InetAddress.getByName("127.0.0.1")*/, 5400, new IClientStrategy() {
            public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                try {
                    LOG.info("client number: " +InetAddress.getLocalHost() +  " ask from server to generate new maze" );
                    ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                    ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                    toServer.flush();
                    int[] mazeDimensions = new int[]{row, col};
                    toServer.writeObject(mazeDimensions);
                    toServer.flush();
                    byte[] compressedMaze = (byte[]) fromServer.readObject();
                    InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                    byte[] decompressedMaze = new byte[24 + mazeDimensions[0] * mazeDimensions[1]];
                    is.read(decompressedMaze);
                    maze = new Maze(decompressedMaze);
                    LOG.info("Maze sizes: rows-"+maze.getRows()+" Maze sizes: colums-"+maze.getColumns());
                    LOG.info("Generator maze server finish serve client");
                } catch (Exception var10) {
                    var10.printStackTrace();
                }

            }
        });
        client.communicateWithServer();
        playerRow = 0;
        playerCol = 0;
        setChanged();
        notifyObservers("Maze generated");
    }

    /** This function solve a maze by the server, according client's request
     * @throws UnknownHostException
     */
    public void solveMaze() throws UnknownHostException {
        Client client = new Client(InetAddress.getLocalHost()/*InetAddress.getByName("127.0.0.1")*/, 5401, new IClientStrategy() {
            public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                try {
                    LOG.info("client number: " +InetAddress.getLocalHost() +  " ask from server to solve maze" );
                    ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                    ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                    toServer.flush();
                    toServer.writeObject(maze);
                    toServer.flush();
                    solution = (Solution) fromServer.readObject();
                } catch (Exception var10) {
                    var10.printStackTrace();
                }

            }
        });
        LOG.info("server finish solve maze and serve client");
        client.communicateWithServer();
        setChanged();
        notifyObservers("maze solved");
    }


    /**
     * @return the maze
     */
    @Override
    public Maze getMaze() {
        return maze;
    }

    /** This function update the player's location according his steps
     * @param direction represents the direction of the player
     */
    @Override
    public void updatePlayerLocation(MovementDirection direction) {
        switch (direction) {
            case UP -> {
                if (playerRow > 0 && maze.getMaze()[playerRow - 1][playerCol] == 0)
                    movePlayer(playerRow - 1, playerCol);
            }
            case DOWN -> {
                if (playerRow < maze.getMaze().length - 1 && maze.getMaze()[playerRow + 1][playerCol] == 0)
                    movePlayer(playerRow + 1, playerCol);
            }
            case LEFT -> {
                if (playerCol > 0 && maze.getMaze()[playerRow][playerCol - 1] == 0)
                    movePlayer(playerRow, playerCol - 1);
            }
            case RIGHT -> {
                if (playerCol < maze.getMaze()[0].length - 1 && maze.getMaze()[playerRow][playerCol + 1] == 0)
                    movePlayer(playerRow, playerCol + 1);
            }
            case RIGHTDOWN -> {
                if (playerCol < maze.getMaze()[0].length - 1 && playerRow < maze.getMaze().length - 1 && maze.getMaze()[playerRow + 1][playerCol + 1] == 0)
                    movePlayer(playerRow + 1, playerCol + 1);
            }
            case RIGHTUP -> {
                if (playerCol < maze.getMaze()[0].length - 1 && playerRow > 0 && maze.getMaze()[playerRow - 1][playerCol + 1] == 0)
                    movePlayer(playerRow - 1, playerCol + 1);
            }
            case LEFTDOWN -> {
                if (playerCol > 0 && playerRow < maze.getMaze().length - 1 && maze.getMaze()[playerRow + 1][playerCol - 1] == 0)
                    movePlayer(playerRow + 1, playerCol - 1);
            }
            case LEFTUP -> {
                if (playerCol > 0 && playerRow > 0 && maze.getMaze()[playerRow - 1][playerCol - 1] == 0)
                    movePlayer(playerRow - 1, playerCol - 1);
            }
        }


    }

    /** This function set the row number position of the player
     * @param playerRow represents the row number that the player is on
     */
    public void setPlayerRow(int playerRow) {
        this.playerRow = playerRow;
    }

    /** This function set the column number position of the player
     * @param playerCol represents the column number that the player is on
     */
    public void setPlayerCol(int playerCol) {
        this.playerCol = playerCol;
    }

    /** This function move the player to specific position
     * @param row represents the row number to which we want to move the player
     * @param col represents the column number to which we want to move the player
     */
    private void movePlayer(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        setChanged();
        notifyObservers("player moved");
        if (row == maze.getGoalPosition().getRowIndex() && col == maze.getGoalPosition().getColumnIndex()) {
            setChanged();
            notifyObservers("finish maze");
        }

    }

    /** This function checks if the player can move in given a specific location
     * @param row represents the row number
     * @param col represents the column number
     * @return True if there is no wall, else return False
     */
    public boolean canMove(int row, int col) {
        if (row < 0 || col < 0 || row > maze.getRows() - 1 || col > maze.getColumns() - 1)
            return false;
        if (maze.getMaze()[row][col] == 0)
            return true;
        else
            return false;
    }


    /** This function return the row number that the player is on
     * @return the row number that the player is on
     */
    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    /** This function return the column number that the player is on
     * @return the column number that the player is on
     */
    @Override
    public int getPlayerCol() {
        return playerCol;
    }


    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }


    /**
     * @return solution of a specific mane
     */
    @Override
    public Solution getSolution() {
        return solution;
    }


    /** This function set a new maze to the param maze
     * @param maze represent a new maze
     */
    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    /** This function save a maze as a file
     * @param name represents the name of the maze that the user wants to save
     * @throws IOException
     */
    public void saveMaze(String name) throws IOException {
        File dir = new File("./resources/savedMazes");
        File[] fileListing = dir.listFiles();
        for (int i = 0; i < fileListing.length; i++) {
            if (fileListing[i].getName().equals(name)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("this name already exist");
                alert.show();
                return;
            }
            else{
                if( i == fileListing.length-1){
                    FileOutputStream fileMaze = new FileOutputStream("./resources" + "/savedMazes/" + name);
                    ObjectOutputStream createMazeFile = new ObjectOutputStream(fileMaze);
                    createMazeFile.writeObject(getMaze());
                    createMazeFile.flush();
                    createMazeFile.close();
                    setChanged();
                    notifyObservers("maze saved");
                    break;
                }
            }

        }
    }

    /** With this function you can move the player by drag the mouse
     * @param mouseEvent represents the mouse
     * @param mazeDisplayer represents the maze in our application
     */
    @Override
    public void mouseDrag(MouseEvent mouseEvent, MazeDisplayer mazeDisplayer) {
        if (mazeDisplayer == null || (playerRow == maze.getGoalPosition().getRowIndex() && playerCol == maze.getGoalPosition().getColumnIndex()))
            return;
        double[] pos = getCellSize(mazeDisplayer);
        double mouseRow = (mouseEvent.getY());
        double mouseCol = (mouseEvent.getX());
        double cellHeight = pos[0];
        double cellWidth = pos[1];
        if (playerRow + 1 < mouseRow / cellHeight && mouseRow / cellHeight <= playerRow + 2 && playerCol + 1 >= mouseCol / cellWidth && playerCol <= mouseCol / cellWidth) {// down
            if (canMove(playerRow + 1, playerCol))
                movePlayer(playerRow + 1, playerCol);
        }
        if (playerRow  > mouseRow / cellHeight && mouseRow / cellHeight + 1 >= playerRow && playerCol + 1 >= mouseCol / cellWidth && playerCol <= mouseCol / cellWidth) {// up
            if (canMove(playerRow - 1, playerCol))
                movePlayer(playerRow - 1, playerCol);
        }
        if (playerCol  > mouseCol / cellWidth && mouseCol / cellWidth + 1 >= playerCol && playerRow + 1 >= mouseRow / cellHeight && playerRow <= mouseRow / cellHeight) {// left
            if (canMove(playerRow, playerCol - 1))
                movePlayer(playerRow, playerCol - 1);
        }
        if (playerCol + 1 < mouseCol / cellWidth && mouseCol / cellWidth <= playerCol + 2 && playerRow + 1 >= mouseRow / cellHeight && playerRow <= mouseRow / cellHeight) {// right
            if (canMove(playerRow, playerCol + 1))
                movePlayer(playerRow, playerCol + 1);
        }
    }


    /** This function calculates cell size
     * @param mazeDisplayer represents the maze in our application
     * @return cell height and cell width
     */
    private double[] getCellSize(MazeDisplayer mazeDisplayer) {
        double canvasHeight = mazeDisplayer.getHeight();
        double canvasWidth = mazeDisplayer.getWidth();
        int rows = maze.getMaze().length;
        int cols = maze.getMaze()[0].length;
        double cellHeight = canvasHeight / rows;
        double cellWidth = canvasWidth / cols;
        double[] a = new double[2];
        a[0] = cellHeight;
        a[1] = cellWidth;
        return a;
    }

    /** This function set to properties of the maze by user's request
     * @param num represents the number of thread pool
     * @param mazeAlgo represents the generate algorithm the user wants to use
     * @param solveAlgo represent the solve algorithm the user wants to use
     * @throws IOException
     * @throws InterruptedException
     */
    public void setProperties(String num, String mazeAlgo, String solveAlgo) throws IOException, InterruptedException {
        generateMaze.stop();
        solveSearchProblem.stop();
        OutputStream file = new FileOutputStream(new File("resources/config.properties"));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(file));
        bw.flush();
        bw.write("threadPoolSize="+num.toString());
        bw.newLine();
        bw.write("mazeGeneratingAlgorithm="+mazeAlgo.replaceAll("\\s+",""));
        bw.newLine();
        bw.write("mazeSearchingAlgorithm="+solveAlgo.replaceAll("\\s+",""));
        bw.flush();
        bw.close();
        Configurations.getInstance().updateConfig(num, mazeAlgo, solveAlgo);
        Thread.sleep(1000);
        this.generateMaze = new Server(5400 , 1000, new ServerStrategyGenerateMaze());
        this.solveSearchProblem = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        this.generateMaze.start();
        this.solveSearchProblem.start();
        setChanged();
        notifyObservers("properties changed");
    }

    /** This function stop the all servers
     * @throws InterruptedException
     */
    public void stopServers() throws InterruptedException {
        this.generateMaze.stop();
        this.solveSearchProblem.stop();
        Thread.sleep(1000);
        setChanged();
        notifyObservers("exit");
    }
}


