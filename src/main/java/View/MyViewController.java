package View;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
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
    public MenuItem close;
    public MenuItem about;
    public ScrollPane mainScrollPane;
    public CheckBox muteButton2;
    public ChoiceBox character;
    public Pane characterPane;
    private boolean isSolved = false;
    private boolean isShowSolution =false;
    private boolean zoom= false;
    private static int counter = 0;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    private void setUpdatePlayerCol(int col) {
        this.updatePlayerCol.set(col+"");
    }
    private void setUpdatePlayerRow(int row) {
        this.updatePlayerRow.set(row+"");
    }

    /** Set viewModel for this controller.
     * @param viewModel middle connection with model.
     */
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
        mainScrollPane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                try {
                    scrollMouse(event);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                event.consume();
            }});



    }

    /** function get called when the window size change, the function call function that will change the mazeDisplayer size.
     * @throws FileNotFoundException
     */
    private void changeSize() throws FileNotFoundException {
        mazeDisplayer.setZoomNeededReset(true);
        mazeDisplayer.draw((int) Main.getPrimaryStage().getHeight()-100,(int) Main.getPrimaryStage().getWidth()-150);
    }


    /** the function called when player press on generate maze button, the function call viewModel to generate it.
     * @param actionEvent press on generate maze button.
     * @throws UnknownHostException
     * @throws FileNotFoundException
     */
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
            if((muteButton2.isSelected()))
                Main.getMedia().setMute(true);
        }
    }

    /** solve the current maze
     * @param actionEvent press on solve maze button.
     * @throws UnknownHostException
     */
    public void solveMaze(ActionEvent actionEvent) throws UnknownHostException {
        viewModel.solveMaze();
    }

    /** check if the player press control on the keyborad  maybe he want to zoom in/out, else maybe want to move the player in the maze.
     * @param keyEvent some key in the keyboard
     */
    public void keyPress (KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.CONTROL) {
            zoom = true;
        }
        else if(!(isSolved)) {
            viewModel.movePlayer(keyEvent);
            keyEvent.consume();
        }
    }

    /** set new player position
     * @param row current player row
     * @param col current player column
     * @throws FileNotFoundException
     */
    public void setPlayerPosition(int row, int col) throws FileNotFoundException {
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }

    /** make focus on the maze
     * @param mouseEvent mouse click on mazeDisplayer
     */
    public void mouseClick(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    /** this controller get info from model throw view model and this function make the needed changes in the sence.
     * @param o
     * @param arg what happen.
     */
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
                if(Main.getMedia().isMute())
                    muteButton2.setSelected(true);
            }
            case "finish maze" -> {
                try {
                    if(counter == 0)
                        finishMaze();
                    counter++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case "exit" -> {
                exitProgram();
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

    private void exitProgram() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * fnuction called when the player succeed to finish the maze.
     */
    private void finishMaze() throws IOException {
        Main.getMedia().setMute(true);
        Media song = new Media(new File("./resources/music/we are cut.mp3").toURI().toString());
        Main.setMedia(new MediaPlayer(song));
        Main.getMedia().setAutoPlay(true);
        Main.getMedia().setCycleCount(MediaPlayer.INDEFINITE);
        Main.getMedia().play();
        Main.getMedia().setMute(true);
        if(!(muteButton2.isSelected()))
            Main.getMedia().setMute(false);
        isSolved = true;
        solveMaze.setDisable(true);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Finish.fxml"));
        Parent root = fxmlLoader.load();
        Stage proStage = new Stage();
        proStage.setScene(new Scene(root, 550, 400));
        proStage.show();
    }

    /** function called when player ask for show the solution of gthe current maze. this function show the solution on the screen.
     * @throws FileNotFoundException
     */
    private void mazeSolved() throws FileNotFoundException {
        if(isShowSolution == false) {
            mazeDisplayer.canDrawSolution();
            mazeDisplayer.setSolution(viewModel.getSolution());
            isShowSolution = true;
            solveMaze.setText("Unsolved");

        }
        else {
            mazeDisplayer.clearSolution();
            isShowSolution = false;
            solveMaze.setText("Solve Maze");
        }
    }

    /** show the player movements.
     * @throws FileNotFoundException
     */
    private void playerMoved() throws FileNotFoundException {
        setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
    }

    /** this function called when the player want to generate new maze.
     * @throws FileNotFoundException
     */
    private void mazeGenerated() throws FileNotFoundException {
        mazeDisplayer.drawMaze(viewModel.getMaze().getMaze());
        counter = 0;
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


    /** function that called everytime there is some change in the scene.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
        ObservableList<String> choiceForGenerate = FXCollections.observableArrayList("Ash Ketchum","Mistey");
        character.setItems(choiceForGenerate);
        if(Main.getMedia().isMute())
            muteButton2.setSelected(true);
    }

    /** open new sence for saving the current maze.
     * @param actionEvent player press on save maze.
     * @throws IOException
     */
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

    /** load maze from the memory of your PC.
     * @param actionEvent player press on load .
     * @throws IOException
     * @throws ClassNotFoundException
     */
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

    /** open properties sence.
     * @param actionEvent press on properties.
     * @throws IOException
     */
    public void openProperties(ActionEvent actionEvent) throws IOException {
        switchSence("Properties.fxml");
    }

    /** safe exit
     * @param actionEvent press on exit
     * @throws InterruptedException
     */
    public void exit(ActionEvent actionEvent) throws InterruptedException {
        viewModel.stopServers();
    }

    /** change to about sence.
     * @param actionEvent  press on about
     * @throws IOException
     */
    public void about(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("About.fxml"));
        Parent root = fxmlLoader.load();
        Stage proStage = new Stage();
        proStage.setScene(new Scene(root, 1000, 700));
        proStage.show();
    }

    /** open help sence.
     * @param actionEvent press help.
     * @throws IOException
     */
    public void help(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Help.fxml"));
        Parent root = fxmlLoader.load();
        Stage proStage = new Stage();
        proStage.setScene(new Scene(root, 1200, 700));
        proStage.show();
    }

    /** called the function that responsible to move the player by the mouse dragging
     * @param mouseEvent when mouse drag on the screen
     */
    public void mouseDrag(MouseEvent mouseEvent) {
        viewModel.mouseDrag(mouseEvent, mazeDisplayer);
    }

    /** stop zoom in/out
     * @param keyEvent realse control
     */
    public void zoomOut(KeyEvent keyEvent) {
        if(zoom)
            zoom = false;
    }


    /** check if control press and may go to zoom in/out.
     * @param scrollEvent scroll mouse.
     * @throws FileNotFoundException
     */
    public void scrollMouse(ScrollEvent scrollEvent) throws FileNotFoundException {
        if(zoom) {
            mazeDisplayer.zoomInOut(scrollEvent);
            //scrollMaze(scrollEvent);
        }
    }

    /** mute the music in the game.
     * @param actionEvent press on mute checkBox.
     */
    public void mute(ActionEvent actionEvent) {
        if(muteButton2.isSelected()) {
            Main.getMedia().setMute(true);
        }
        else {
            Main.getMedia().setMute(false);
        }
    }

    /** change he player in the maze.
     * @param actionEvent choose diffrent player in the main sence.
     * @throws FileNotFoundException
     */
    public void changeCharacter(ActionEvent actionEvent) throws FileNotFoundException {
        if (character.getSelectionModel().getSelectedItem().equals("Ash Ketchum")) {
            Image image = null;
            try {
                image = new Image(new FileInputStream("resources/Images/ash.png"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true);
            Background back = new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size));
            characterPane.setBackground(back);
            mazeDisplayer.changePlayer("Ash Ketchum");
        }
        else{
            Image image = null;
            try {
                image = new Image(new FileInputStream("./resources/Images/mistty.jpg"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true);
            Background back = new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size));
            characterPane.setBackground(back);
            mazeDisplayer.changePlayer("Mistty");
        }
    }

}
