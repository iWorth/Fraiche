package Source;


import Source.ComparadorNombres.ComparaNombres;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ComparadorNombres/ComparaNombresFXML.fxml"));
        primaryStage.setTitle("Comparar nombres");
        primaryStage.setScene(new Scene(root, 476, 199));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
