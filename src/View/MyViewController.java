package View;
import Model.MyModel;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController extends AView implements  IView , Observer {

    public MyViewModel viewModel;
    public Button generateMaze;
    public Button solveMaze;
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    public BorderPane pane;
    public Menu exit;
    public MenuItem close;
    public MenuItem about;
    private boolean isSolved = false;
    private boolean isShowSolution =false;
    private boolean zoom= false;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addObserver( this);
        this.solveMaze.setDisable(true);
        Main.getPrimaryStage().widthProperty().addListener(observable -> {
            try {
                changeSize();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        Main.getPrimaryStage().heightProperty().addListener(observable -> {
            try {
                changeSize();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        //this.generateMaze.prefHeightProperty().bind(pane.heightProperty());
        //this.generateMaze.prefWidthProperty().bind(pane.widthProperty());

    }

    private void changeSize() throws FileNotFoundException {
        mazeDisplayer.setZoomNeededReset(true);
        mazeDisplayer.draw((int)Main.getPrimaryStage().getHeight()-100,(int)Main.getPrimaryStage().getWidth()-150);
    }



    public void generateMaze(ActionEvent actionEvent) throws UnknownHostException, FileNotFoundException {
        String rowText = textField_mazeRows.getText();
        String columnText = textField_mazeColumns.getText();
        if(!(isNumeric(rowText)) ||!(isNumeric(columnText))) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("can't generate maze one of parameters is not a number");
            alert.show();
        }
        else {
            int rows = Integer.valueOf(rowText);
            int columns = Integer.valueOf(columnText);
            if (rows <= 1 || columns <= 1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("can't generate maze one of parameters zero or below");
                alert.show();
            } else {
                viewModel.generateMaze(rows, columns);
                setUpdatePlayerRow(0);
                setUpdatePlayerCol(0);
                setPlayerPosition(0, 0);
                this.solveMaze.setDisable(false);
                //this.mazeDisplayer.scaleXProperty().bind(pane.widthProperty());
                //this.mazeDisplayer.scaleYProperty().bind(pane.heightProperty());
            }
        }
    }

    public void solveMaze(ActionEvent actionEvent) throws UnknownHostException {
        viewModel.solveMaze();
    }
    public void keyPress (KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.CONTROL)
            zoom = true;
        else if(!(isSolved)) {
            viewModel.movePlayer(keyEvent);
            keyEvent.consume();
        }
    }

    public void setPlayerPosition(int row, int col) throws FileNotFoundException {
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }

    private void setUpdatePlayerCol(int col) {
        this.updatePlayerCol.set(col+"");
    }

    private void setUpdatePlayerRow(int row) {
        this.updatePlayerRow.set(row+"");
    }


    public void mouseClick(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    @Override
    public void update(Observable o, Object arg) {
        String change = (String) arg;
        switch (change){
            case "Maze generated" -> {
                try {
                    mazeGenerated();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            case "player moved" -> {
                try {
                    playerMoved();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            case "maze solved" -> {
                try {
                    mazeSolved();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            case "properties changed" -> {
                    viewModel.stopServers();
                    viewModel = new MyViewModel(new MyModel());
            }
            case "finish maze" -> {
                finishMaze();
            }
            case "maze saved" -> {
                viewModel.setMaze(null);
                try {
                    switchSence("MyView.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            default -> System.out.println("Not implemented change: " + change);
        }
    }

    private void finishMaze() {
        Main.getMedia().setMute(true);
        Media song = new Media(new File("./resources/music/we are cut.mp3").toURI().toString());
        Main.setMedia(new MediaPlayer(song));
        Main.getMedia().setAutoPlay(true);
        Main.getMedia().setCycleCount(MediaPlayer.INDEFINITE);
        Main.getMedia().play();
        Main.getMedia().setMute(false);
        isSolved = true;
        solveMaze.setDisable(true);

    }

    private void mazeSolved() throws FileNotFoundException {
        if(isShowSolution == false) {
            mazeDisplayer.canDrawSolution();
            mazeDisplayer.setSolution(viewModel.getSolution());
            isShowSolution = true;
        }
        else {
            mazeDisplayer.clearSolution();
            isShowSolution = false;
        }
    }

    private void playerMoved() throws FileNotFoundException {
        setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
    }

    private void mazeGenerated() throws FileNotFoundException {
        mazeDisplayer.drawMaze(viewModel.getMaze().getMaze());
        if(isSolved){
            Main.getMedia().setMute(true);
            Media song = new Media(new File("./resources/music/Lugia's Song.mp3").toURI().toString());
            Main.setMedia(new MediaPlayer(song));
            Main.getMedia().setAutoPlay(true);
            Main.getMedia().setCycleCount(MediaPlayer.INDEFINITE);
            Main.getMedia().play();
            isSolved = false;
            Main.getMedia().setMute(false);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
    }

    public void saveMaze(ActionEvent actionEvent) throws IOException {
        if(this.viewModel.getMaze() == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("There is no existing maze to save");
            alert.show();
        }
        else
        {
            switchSence("Save.fxml");
            //viewModel.setMaze(null);

        }
    }

    public void loadFile(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Load maze");
        //fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("maze file (*.maze)"))
        fc.setInitialDirectory(new File("./resources/savedMazes"));
        File chooser = fc.showOpenDialog(null);
        if(chooser == null)
            return;
        if(chooser.getName() != null){
            FileInputStream loadedMaze = new FileInputStream(chooser);
            ObjectInputStream showMaze = new ObjectInputStream(loadedMaze);
            Maze maze = (Maze)showMaze.readObject();
            setUpdatePlayerRow(0);
            setUpdatePlayerCol(0);
            setPlayerPosition(0, 0);
            viewModel.setPlayerCol(0);
            viewModel.setPlayerRow(0);
            mazeDisplayer.drawMaze(maze.getMaze());
            viewModel.setMaze(maze);
            this.solveMaze.setDisable(false);
            if(isSolved){
                Main.getMedia().setMute(true);
                Media song = new Media(new File("./resources/music/Lugia's Song.mp3").toURI().toString());
                Main.setMedia(new MediaPlayer(song));
                Main.getMedia().setAutoPlay(true);
                Main.getMedia().setCycleCount(MediaPlayer.INDEFINITE);
                Main.getMedia().play();
                Main.getMedia().setMute(false);
                isSolved = false;
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Maze did not select");
            alert.show();
        }


    }

    public void openProperties(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Properties.fxml"));
        Parent root = fxmlLoader.load();
        Stage proStage = new Stage();
        proStage.setScene(new Scene(root, 200, 200));
        proStage.show();
    }

    public void exit(ActionEvent actionEvent) {
            System.exit(0);
    }

    public void about(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("About.fxml"));
        Parent root = fxmlLoader.load();
        Stage proStage = new Stage();
        proStage.setScene(new Scene(root, 200, 200));
        proStage.show();
    }

    public void help(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Help.fxml"));
        Parent root = fxmlLoader.load();
        Stage proStage = new Stage();
        proStage.setScene(new Scene(root, 200, 200));
        proStage.show();
    }

    public void mouseDrag(MouseEvent mouseEvent) {
        viewModel.mouseDrag(mouseEvent, mazeDisplayer);
    }

    public void zoomOut(KeyEvent keyEvent) {
        zoom = false;
    }

    public void scrollMouse(ScrollEvent scrollEvent) throws FileNotFoundException {
        if(zoom)
            mazeDisplayer.zoomInOut(scrollEvent);
    }
}
