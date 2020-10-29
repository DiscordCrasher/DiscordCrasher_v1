package me.dc.automaticdiscordcrasher;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static AppManager appManager;

    @Override
    public void start(Stage primaryStage) {
        appManager = new AppManager();
        appManager.showFrame();



    }
}
