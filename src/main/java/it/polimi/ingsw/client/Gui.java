package it.polimi.ingsw.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Gui extends Application {


    static Stage stage;

    /**
     * The entry point for the JavaFX application. Sets up the initial stage (window) for the game.
     *
     * @param stage The primary stage for the application.
     * @throws Exception if an error occurs during the start of the application.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Gui.stage = stage;
        Font.loadFont(getClass().getResourceAsStream("Ink-Blossoms.ttf"), 12);

        // Load the FXML file with the FXMLLoader
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Welcome.fxml"));
        Parent root = fxmlLoader.load();

        // Construct the Scene using the loaded Parent and set it on the Stage
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);

        // Define the behavior when the application window is closed
        stage.setOnCloseRequest((WindowEvent t) -> {
            Platform.exit();
            System.exit(0);
        });

        // Show the stage
        stage.show();
    }


    //Launches the GUI
    public static void main() {
        launch();
    }

    /**
     * Retrieves the current stage.
     *
     * @return The Stage object representing the current stage.
     */
    public static Stage getStage(){
        return stage;
    }
}
