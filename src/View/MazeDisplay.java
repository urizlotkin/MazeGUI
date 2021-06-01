package View;

import algorithms.mazeGenerators.Maze;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class MazeDisplay extends Canvas {
    private Maze maze;

    public void drawMaze(Maze maze) {
        this.maze = maze;
        draw();
    }

    private void draw() {
        if (maze != null){
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.getRows();
            int cols = maze.getColumns();
            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            // clear the canvas:
            graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);
            graphicsContext.setFill(Color.BLACK);

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (maze.getMaze()[i][j] == 1){
                        //it is a wall
                        double x = i * cellHeight;
                        double y = j * cellWidth;
                        graphicsContext.fillRect(x,y,cellHeight,cellWidth);
                    }
                }

            }
        }
    }
}