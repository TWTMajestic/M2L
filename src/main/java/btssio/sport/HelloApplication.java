package btssio.sport;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    // Def la fenêtre de l'application
    private static Stage primaryStage;
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    public static void main(String[] args) {
        launch();
    }

    // lancement de l'application
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Accueil.fxml"));
        Scene sc = new Scene(fxmlLoader.load(), 800, 600);
        sc.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.getIcons().add(new Image("file:src/main/resources/img_1.png"));
        stage.setScene(sc);
        stage.show();
    }

    @Override
    public void init() throws Exception{
        System.out.println("init");
    }

    public void stop() throws Exception{
        System.out.println("stop");
    }

}