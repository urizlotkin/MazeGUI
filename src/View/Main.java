package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    private static Stage mainStage;
    private static MyViewModel viewModel;
    private static MediaPlayer media;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.mainStage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        mainStage.setTitle("Maze");
        mainStage.setScene(new Scene(root, 1000, 700));
        mainStage.show();
        IModel model = new MyModel();
        viewModel = new MyViewModel(model);
        MyViewController view = fxmlLoader.getController();
        view.setViewModel(viewModel);
        Media song = new Media(new File("resources/music/mainMusic.mp3").toURI().toString());
        media = new MediaPlayer(song);
        media.setAutoPlay(true);
        media.setCycleCount(MediaPlayer.INDEFINITE);
        media.play();
    }
    public static Stage getPrimaryStage(){
        return  mainStage;
    }
    public static MyViewModel getViewModel(){
        return  viewModel;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
