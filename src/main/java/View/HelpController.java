package View;

import ViewModel.MyViewModel;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class HelpController extends AView implements IView, Observer {
    public AnchorPane mainPane;

    @Override
    public void setViewModel(MyViewModel viewModel) { this.viewModel = Main.getViewModel();}

    @Override
    public void update(Observable o, Object arg) {

    }
    /** function that called everytime there is some change in the scene.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = null;
        try {
            image = new Image(new FileInputStream("resources/Images/helpFinish.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true);
        Background back = new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size));
        mainPane.setBackground(back);
    }
}
