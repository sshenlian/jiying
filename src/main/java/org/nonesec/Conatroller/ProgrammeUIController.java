package org.nonesec.Conatroller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.nonesec.FOFA.FOFAUI;

import java.io.IOException;

public class ProgrammeUIController {
    public TextField pargraText;
    public TextField pargraNameText;
    public Button savebtn;

    public void savefile(ActionEvent actionEvent) throws IOException {
        String text = pargraText.getText();
        String text1 = pargraNameText.getText();
        FOFAUI fofaui = new FOFAUI();
        boolean b = fofaui.writeToFile(text, "./Programme/"+text1);
        Stage stage = (Stage)savebtn.getScene().getWindow();
        stage.close();
        if (b){
            new Alert(Alert.AlertType.NONE,"配置保存成功",new ButtonType[]{ButtonType.CLOSE}).show();
        }
    }
}
