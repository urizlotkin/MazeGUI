package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class SaveContrroller extends AView implements IView, Observer {


    public Pane mainPane;
    public TextField mazeName;

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setViewModel(Main.getViewModel());
        Image image = null;
        try {
            image = new Image(new FileInputStream("resources/Images/save.jpeg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true);
        Background back = new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size));
        mainPane.setBackground(back);
    }
    
    @Override
    public void setViewModel(MyViewModel viewModel) {
            this.viewModel = viewModel;
            this.viewModel.addObserver( this);
    }

    public void saveMaze(ActionEvent actionEvent) throws IOException {
      // setViewModel(View.Main.getViewModel());
        String name = mazeName.getText();
        viewModel.saveMaze(name);
    }

    public void backToMain(ActionEvent actionEvent) throws IOException {
        viewModel.setMaze(null);
        switchSence("MyView.fxml");
    }
}
