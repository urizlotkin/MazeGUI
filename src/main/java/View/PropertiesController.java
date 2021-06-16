package View;
import Server.Configurations;
import ViewModel.MyViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class PropertiesController extends AView implements IView, Observer {
    public ChoiceBox generateMaze;
    public ChoiceBox solveMaze;
    public Button setProperties;
    public TextField numOfThreads;
    public AnchorPane mainPane;

    @Override
    public void setViewModel(MyViewModel viewModel) { this.viewModel = viewModel; }

    @Override
    public void update(Observable o, Object arg) {

    }
    /** function that called everytime there is some change in the scene.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // create a input stream
        Image image = null;
        try{
            image = new Image(new FileInputStream("resources/Images/settings.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,true,true,true,true);
        Background back = new Background(new BackgroundImage(image,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,size));
        mainPane.setBackground(back);
        Object [] con = Configurations.getInstance().loadConfigToServer();
        numOfThreads.setText((String) con[0].toString());
        generateMaze.setValue(con[1].getClass().toString().substring(32));
        solveMaze.setValue(con[2].getClass().toString().substring(24));
        ObservableList<String> choiceForGenerate = FXCollections.observableArrayList("MyMazeGenerator","SimpleMazeGenerator", "EmptyMazeGenerator");
        generateMaze.setItems(choiceForGenerate);
        ObservableList<String> choiceForSolve = FXCollections.observableArrayList("BestFirstSearch","BreadthFirstSearch","DepthFirstSearch");
        solveMaze.setItems(choiceForSolve);



    }

    /** start changing properties.
     * @param actionEvent press on set properties.
     * @throws IOException
     * @throws InterruptedException
     */
    public void setProperties(ActionEvent actionEvent) throws IOException, InterruptedException {
        String num = numOfThreads.getText();
        if((!(isNumeric(num))) || Integer.valueOf(num) < 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("num of thread must be larger then 0");
            alert.show();
            return;
        }

        viewModel.setProperties(num,(String)generateMaze.getSelectionModel().getSelectedItem(),(String)solveMaze.getSelectionModel().getSelectedItem());
        switchSence("MyView.fxml");
    }

    /** back to main screen.
     * @param actionEvent press back to main screen.
     * @throws IOException
     */
    public void newMaze(ActionEvent actionEvent) throws IOException {
        viewModel.setMaze(null);
        switchSence("MyView.fxml");
    }
}
