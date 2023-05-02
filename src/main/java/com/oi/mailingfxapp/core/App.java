package com.oi.mailingfxapp.core;

import com.oi.mailingfxapp.config.Config;
import com.oi.mailingfxapp.MailingFXAppShutdownHook;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Thread mailingFXAppShutdownThread = null;
    
    @Override
    public void start(Stage stage) throws IOException {
        MailingFXAppShutdownHook mailingFXAppShutdownHook = new MailingFXAppShutdownHook();
        mailingFXAppShutdownThread = new Thread(mailingFXAppShutdownHook, "MailingFXAppShutdownHook");
        Runtime.getRuntime().addShutdownHook(mailingFXAppShutdownThread);

        new Config().init();

        scene = new Scene(loadFXML("mailing_v7"), 640, 336);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        MailingFXApp.shutdown();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
