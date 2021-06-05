package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

    }

    @Override
    public void setViewModel(MyViewModel viewModel) {
            this.viewModel = viewModel;
            this.viewModel.addObserver( this);
    }

    public void saveMaze(ActionEvent actionEvent) throws IOException {
        String name = mazeName.getText();
        FileOutputStream fileMaze = new FileOutputStream( "./resources"+ "/savedMazes/" + name);
        ObjectOutputStream createMazeFile = new ObjectOutputStream(fileMaze);
        createMazeFile.writeObject(viewModel.getMaze());
        createMazeFile.flush();
        createMazeFile.close();
        switchSence("MyView.fxml");
    }
}
