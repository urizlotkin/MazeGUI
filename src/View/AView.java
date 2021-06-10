package View;

import ViewModel.MyViewModel;
import View.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
public abstract class AView implements IView {

    protected MyViewModel viewModel;

    public AView(){this.viewModel = Main.getViewModel();}
    protected void switchSence(String fxmlName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlName));
        Parent root = fxmlLoader.load();
        Main.getPrimaryStage().setScene(new Scene(root, Main.getPrimaryStage().getWidth(), Main.getPrimaryStage().getHeight()));
        Main.getPrimaryStage().show();
        AView newView = fxmlLoader.getController();
        newView.setViewModel(Main.getViewModel());
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public abstract void setViewModel(MyViewModel viewModel);


}
