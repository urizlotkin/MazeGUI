package View;
import View.Main;
import Server.Configurations;
import ViewModel.MyViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    @Override
    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> choiceForGenerate = FXCollections.observableArrayList("MyMazeGenerator","EmptyMazeGenerator","SimpleMazeGenerator");
        generateMaze.setItems(choiceForGenerate);
        ObservableList<String> choiceForSolve = FXCollections.observableArrayList("BestFirstSearch","BreadthFirstSearch","DepthFirstSearch");
        solveMaze.setItems(choiceForSolve);



    }

    public void setProperties(ActionEvent actionEvent) throws IOException, InterruptedException {
        String num = numOfThreads.getText();
        if((!(isNumeric(num))) || Integer.valueOf(num) < 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("num of thread must be larger then 0");
            alert.show();
            return;
        }

        viewModel.setProperties(num,(String)generateMaze.getSelectionModel().getSelectedItem(),(String)solveMaze.getSelectionModel().getSelectedItem());
        Stage stage = (Stage) setProperties.getScene().getWindow();
        stage.close();
    }
}
