package javafxml_package;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application
{

    @Override
    public void start(Stage stage) throws Exception 
    {
        Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("JavaFXML Application");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[]  args)
    {
        launch(args);
    }
}
