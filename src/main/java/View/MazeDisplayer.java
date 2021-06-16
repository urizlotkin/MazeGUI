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

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFinishPoint = new SimpleStringProperty();
    StringProperty imageSolvePath = new SimpleStringProperty();
    private int[][] maze;
    private int playerRow=0;
    private int playerCol=0;
    private Solution sol = null;
    private boolean isShowSol = false;
    private boolean isZoomNeededReset = false;
    private double zoomHight = 0;
    private double zoomWidth = 0;

    public String getImageSolvePath() {
        return imageSolvePath.get();
    }
    public String getImageFinishPoint() {
        return imageFinishPoint.get();
    }
    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }
    public void setImageFileNameWall(String imageFileNameWall) { this.imageFileNameWall.set(imageFileNameWall); }
    public void setImageFinishPoint(String imageFinishPoint) { this.imageFinishPoint.set(imageFinishPoint); }
    public void setImageSolvePath(String imageSolvePath) { this.imageSolvePath.set(imageSolvePath); }
    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }
    public void setImageFileNamePlayer(String imageFileNamePlayer) { this.imageFileNamePlayer.set(imageFileNamePlayer); }
    public int getPlayerRow() {
        return playerRow;
    }
    public int getPlayerCol() {
        return playerCol;
    }

    /**
     * constructor of Canvas.javafx.
     */
    public MazeDisplayer(){
        super();
    }
    /** set boolean that check if mazeDisplayer need reset for the zoom, like when the player want to generate new maze.
     * @param zoomNeededReset true/false depence on the situation.
     */
    public void setZoomNeededReset(boolean zoomNeededReset) { isZoomNeededReset = zoomNeededReset; }

    /** set new position for the player in the maze.
     * @param row new player row.
     * @param col new player column.
     * @throws FileNotFoundException
     */
    public void setPlayerPosition(int row, int col) throws FileNotFoundException {
        this.playerCol = col;
        this.playerRow = row;
        draw((int) Main.getPrimaryStage().getHeight()-100,(int) Main.getPrimaryStage().getWidth()-150);
    }


    /** draw new maze on the mazeDisplayer.
     * @param maze new maze.
     * @throws FileNotFoundException
     */
    public void drawMaze(int[][] maze) throws FileNotFoundException {
        this.maze = maze;
        this.sol = null;
        zoomHight = 0;
        zoomWidth = 0;
        isZoomNeededReset = true;
        draw((int) Main.getPrimaryStage().getHeight()-100,(int) Main.getPrimaryStage().getWidth()-150);
    }

    /** draw maze when somthing change there, like every move of the player or resize the screen of the game.
     * @param height the height that the mazeDisplayer will draw himself on the screen.
     * @param width the width that the mazeDisplayer will draw himself on the screen.
     * @throws FileNotFoundException
     */
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

    /** draw solution path on the mazeDisplayer.
     * @param graphicsContext
     * @param cellHeight the height size of each cell in the maze.
     * @param cellWidth the width size of each cell in the maze.
     * @throws FileNotFoundException
     */
    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth) throws FileNotFoundException {
        Image solvePath = new Image(new FileInputStream(getImageSolvePath()));
        for (int i = 0; i < sol.getSolutionPath().size()-1;i ++) {

            graphicsContext.drawImage(solvePath,cellWidth*((MazeState)(sol.getSolutionPath().get(i))).getPos().getColumnIndex() , cellHeight* ((MazeState)(sol.getSolutionPath().get(i))).getPos().getRowIndex(), cellWidth, cellHeight);
        }
    }

    /** draw the end point image.
     * @param graphicsContext
     * @param cellHeight the height size of each cell in the maze.
     * @param cellWidth the width size of each cell in the maze.
     * @throws FileNotFoundException
     */
    private void drawEndPoint(GraphicsContext graphicsContext, double cellHeight, double cellWidth) throws FileNotFoundException {
        Image finishPoint = new Image(new FileInputStream(getImageFinishPoint()));
        graphicsContext.drawImage(finishPoint, cellWidth* (maze[0].length-1), cellHeight*(maze.length-1), cellWidth, cellHeight);
    }

    /** draw maze walls.
     * @param graphicsContext
     * @param cellHeight the height size of each cell in the maze.
     * @param cellWidth the width size of each cell in the maze.
     * @param rows the number of rows in the maze.
     * @param cols the number of columns in the maze.
     */
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

    /** draw the image of the player in the maze.
     * @param graphicsContext
     * @param cellHeight the height size of each cell in the maze.
     * @param cellWidth the width size of each cell in the maze.
     */
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

    /** set new solution for the current maze.
     * @param solution object that keep the solution information.
     * @throws FileNotFoundException
     */
    public void setSolution(Solution solution) throws FileNotFoundException {
        this.sol = solution;
        draw((int) Main.getPrimaryStage().getHeight()-100,(int) Main.getPrimaryStage().getWidth()-150);
    }


    /** clear the mazeDisplayer form the solution path.
     * @throws FileNotFoundException
     */
    public void clearSolution() throws FileNotFoundException {
        isShowSol=true;
        draw((int) Main.getPrimaryStage().getHeight()-100,(int) Main.getPrimaryStage().getWidth()-150);
    }

    public void canDrawSolution() {
        isShowSol=false;
    }

    /** handle zoom in and out on the mazeDisplayer.
     * @param scrollEvent get scroll mouse delta.
     * @throws FileNotFoundException
     */
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


    /** change the player in the maze.
     * @param s string that represent witch player the gamer choose.
     * @throws FileNotFoundException
     */
    public void changePlayer(String s) throws FileNotFoundException {
        if(s.equals("Ash Ketchum"))
            setImageFileNamePlayer("./resources/Images/ash.png");
        else{
            setImageFileNamePlayer("./resources/Images/mistty.jpg");
        }
        draw((int) Main.getPrimaryStage().getHeight()-100,(int) Main.getPrimaryStage().getWidth()-150);
    }
}

