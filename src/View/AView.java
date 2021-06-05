package View;

import ViewModel.MyViewModel;
import View.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
public abstract class AView implements IView {

    protected MyViewModel viewModel;

    protected AView(){
        viewModel = Main.getViewModel();
    }

    protected void switchSence(String fxmlName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlName));
        Parent root = fxmlLoader.load();
        Main.getPrimaryStage().setScene(new Scene(root, 1000, 700));
        Main.getPrimaryStage().show();
        AView newView = fxmlLoader.getController();
       // newView.setViewModel(viewModel);
    }

    public abstract void setViewModel(MyViewModel viewModel);


}
