package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class SaveContrroller extends AView implements IView, Observer {
    public Button save;
    public TextField mazeName;

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setViewModel(Main.getViewModel());
    }

    @Override
    public void setViewModel(MyViewModel viewModel) {
            this.viewModel = viewModel;
            this.viewModel.addObserver( this);
    }

    public void saveMaze(ActionEvent actionEvent) throws IOException {
      // setViewModel(Main.getViewModel());
        String name = mazeName.getText();
        viewModel.saveMaze(name);
    }

    public void newMaze(ActionEvent actionEvent) throws IOException {
        viewModel.setMaze(null);
        switchSence("MyView.fxml");

    }

}
