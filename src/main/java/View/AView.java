package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
public abstract class AView implements IView {

    protected MyViewModel viewModel;


    /**
     * abstract class that represent some view controller
     * in the constractur the new view controller set main viewModel
     * as a field.
     */
    public AView(){this.viewModel = View.Main.getViewModel();}


    /** This function switch between sences.
     * @param fxmlName fxml name that we want to switch to.
     * @throws IOException
     */
    protected void switchSence(String fxmlName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlName));
        Parent root = fxmlLoader.load();
        View.Main.getPrimaryStage().setScene(new Scene(root, Main.getPrimaryStage().getWidth(), Main.getPrimaryStage().getHeight()));
        Main.getPrimaryStage().show();
        AView newView = fxmlLoader.getController();
        newView.setViewModel(View.Main.getViewModel());
    }

    /** check if string is numric.
     * @param str string we want to check
     * @return true if the string numric
     *         false if not numric.
     */
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
