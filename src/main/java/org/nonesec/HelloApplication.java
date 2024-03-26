package org.nonesec;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/fxml/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),  950,737);
        stage.setTitle("极影-渗透辅助工具 v1.5 by:沈炼 None安全团队");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}