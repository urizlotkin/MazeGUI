package View;

import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

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
    private boolean isShowSol = false;
    private boolean isZoomNeededReset = false;
    private double zoomHight = 0;
    private double zoomWidth = 0;

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
    public void setZoomNeededReset(boolean zoomNeededReset) { isZoomNeededReset = zoomNeededReset; }

    public void setPlayerPosition(int row, int col) throws FileNotFoundException {
        this.playerCol = col;
        this.playerRow = row;
        draw((int)Main.getPrimaryStage().getHeight()-100,(int)Main.getPrimaryStage().getWidth()-150);
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
    public void setImageFileNamePlayer(String imageFileNamePlayer) { this.imageFileNamePlayer.set(imageFileNamePlayer); }
    public MazeDisplayer(){
        super();
    }

    public void drawMaze(int[][] maze) throws FileNotFoundException {
        this.maze = maze;
        this.sol = null;
        zoomHight = 0;
        zoomWidth = 0;
        isZoomNeededReset = true;
        draw((int)Main.getPrimaryStage().getHeight()-100,(int)Main.getPrimaryStage().getWidth()-150);
    }

    public void draw(double height, double width) throws FileNotFoundException {
        if(maze != null){
            if(isZoomNeededReset) {
                this.setHeight(height-50 + zoomHight);
                this.setWidth(width - 50 + zoomWidth);
            }
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
            if(sol != null && isShowSol ==false)
                drawSolution(graphicsContext,cellHeight,cellWidth);
        }
    }

    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth) throws FileNotFoundException {
        Image solvePath = new Image(new FileInputStream(getImageSolvePath()));
        for (int i = 0; i < sol.getSolutionPath().size()-1;i ++) {

            graphicsContext.drawImage(solvePath,cellWidth*((MazeState)(sol.getSolutionPath().get(i))).getPos().getColumnIndex() , cellHeight* ((MazeState)(sol.getSolutionPath().get(i))).getPos().getRowIndex(), cellWidth, cellHeight);
        }
    }

    private void drawEndPoint(GraphicsContext graphicsContext, double cellHeight, double cellWidth) throws FileNotFoundException {
        //graphicsContext.setFill(Color.GREEN);
        Image finishPoint = new Image(new FileInputStream(getImageFinishPoint()));
        graphicsContext.drawImage(finishPoint, cellWidth* (maze[0].length-1), cellHeight*(maze.length-1), cellWidth, cellHeight);
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
        draw((int)Main.getPrimaryStage().getHeight()-100,(int)Main.getPrimaryStage().getWidth()-150);
    }


    public void clearSolution() throws FileNotFoundException {
        isShowSol=true;
        draw((int)Main.getPrimaryStage().getHeight()-100,(int)Main.getPrimaryStage().getWidth()-150);
    }

    public void canDrawSolution() {
        isShowSol=false;
    }

    public void zoomInOut(ScrollEvent scrollEvent) throws FileNotFoundException {
        if (scrollEvent.getDeltaY() < 0)
        {
            zoomHight += (getHeight()/1.1 - getHeight());
            zoomWidth += (getWidth()/1.1) -  getWidth();
            setHeight(getHeight()/1.1);
            setWidth(getWidth()/1.1);

        }
        else
        {
            if(getWidth()>3600 || getHeight()>3600)
            {
                return;
            }
            else
            {
                zoomHight += ( (getHeight()*1.1) - getHeight());
                zoomWidth += ((getWidth()*1.1 ) - getWidth());
                setHeight(getHeight() * 1.1);
                setWidth(getWidth() * 1.1);

            }
        }
        draw(Main.getPrimaryStage().getHeight() - 100 , Main.getPrimaryStage().getWidth() - 150);

    }

    public void dragMaze(MouseEvent mouseEvent, Pane mainPane, double x, double y) {
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();
        Bounds boundsInScene = mainPane.localToScene(mainPane.getBoundsInLocal());
        if(mouseX > boundsInScene.getMinX() - 175 && mouseY > boundsInScene.getMinY()-25) {
            this.setLayoutX(mouseX-x);
            this.setLayoutY(mouseY - y);
        }
    }

    public void changePlayer(String s) throws FileNotFoundException {
        if(s.equals("Ash Ketchum"))
            setImageFileNamePlayer("./resources/Images/ash.png");
        else{
            setImageFileNamePlayer("./resources/Images/mistty.jpg");
        }
        draw((int)Main.getPrimaryStage().getHeight()-100,(int)Main.getPrimaryStage().getWidth()-150);
    }
}

