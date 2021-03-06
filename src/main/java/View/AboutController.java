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

public class AboutController extends AView implements IView, Observer {
    public AnchorPane mainPane;

    /** Set viewModel for this controller.
     * @param viewModel middle connection with model.
     */
    @Override
    public void setViewModel(MyViewModel viewModel) {this.viewModel = Main.getViewModel(); }


    /** function that called everytime there is some change in the scene.
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // create a input stream
        Image image = null;
        try {
            image = new Image(new FileInputStream("resources/Images/about.PNG"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true);
        Background back = new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size));
        mainPane.setBackground(back);

    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
