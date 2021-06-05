package View;

import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFinishPoint = new SimpleStringProperty();

    public void setImageSolvePath(String imageSolvePath) {
        this.imageSolvePath.set(imageSolvePath);
    }

    public String getImageSolvePath() {
        return imageSolvePath.get();
    }

    public StringProperty imageSolvePathProperty() {
        return imageSolvePath;
    }

    StringProperty imageSolvePath = new SimpleStringProperty();
    private int[][] maze;
    private int playerRow=0;
    private int playerCol=0;
    private Solution sol = null;

    public void setImageFinishPoint(String imageFinishPoint) {
        this.imageFinishPoint.set(imageFinishPoint);
    }

    public String getImageFinishPoint() {
        return imageFinishPoint.get();
    }

    public StringProperty imageFinishPointProperty() {
        return imageFinishPoint;
    }
    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }
    public void setPlayerPosition(int row, int col) throws FileNotFoundException {
        this.playerCol = col;
        this.playerRow = row;
        draw();
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }


    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }
    public MazeDisplayer(){
        super();
    }
    public void drawMaze(int[][] maze) throws FileNotFoundException {
        this.maze = maze;
        this.sol = null;
        draw();
    }

    private void draw() throws FileNotFoundException {
        if(maze != null){
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.length;
            int cols = maze[0].length;

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            drawMazeWalls(graphicsContext,cellHeight,cellWidth,rows,cols);
            drawPlayer(graphicsContext,cellHeight,cellWidth);
            drawEndPoint(graphicsContext,cellHeight,cellWidth);
            if(sol != null)
                drawSolution(graphicsContext,cellHeight,cellWidth);
        }
    }

    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth) throws FileNotFoundException {
        Image solvePath = new Image(new FileInputStream(getImageSolvePath()));
        for (int i = 0; i < sol.getSolutionPath().size()-1;i ++) {
            graphicsContext.drawImage(solvePath, cellWidth* ((MazeState)(sol.getSolutionPath().get(i))).getPos().getRowIndex(), cellHeight*((MazeState)(sol.getSolutionPath().get(i))).getPos().getColumnIndex(), cellWidth, cellHeight);
        }
    }

    private void drawEndPoint(GraphicsContext graphicsContext, double cellHeight, double cellWidth) throws FileNotFoundException {
        //graphicsContext.setFill(Color.GREEN);
        Image finishPoint = new Image(new FileInputStream(getImageFinishPoint()));
        graphicsContext.drawImage(finishPoint, cellWidth* (maze.length-1), cellHeight*(maze[0].length-1), cellWidth, cellHeight);
    }

    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.RED);

        Image wallImage = null;
        try{
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image file");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(maze[i][j] == 1){
                    //if it is a wall:
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    if(wallImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                }
            }
        }
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);

        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if(playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }

    public void setSolution(Solution solution) throws FileNotFoundException {
        this.sol = solution;
        draw();
    }
}
