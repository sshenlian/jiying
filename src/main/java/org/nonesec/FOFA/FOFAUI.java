package org.nonesec.FOFA;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.nonesec.Conatroller.fofaController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FOFAUI {
    public TextField fofaUser;
    public TextField fofaAPI;
    public Button fofasavebtn;

    public void initialize() throws IOException {
        // 设置
        this.initfofaConfig();
    }
    public void initfofaConfig() throws IOException {
        fofaController fofaController = new fofaController();
        fofaController.getConfig();
        String fofaUser1 = fofaController.fofaUser;
        String fofaAPIKEY = fofaController.fofaAPIKEY;
        fofaUser.setText(fofaUser1);
        fofaAPI.setText(fofaAPIKEY);
    }
    public void fofaSave(ActionEvent actionEvent) throws IOException {
        String text = fofaUser.getText();
        String text1 = fofaAPI.getText();
        String config = String.format("fofaUser = %s\n",text);
        config += String.format("fofaAPIKEY = %s",text1);
        boolean b = writeToFile(config,"config.txt");
        Stage stage = (Stage)fofasavebtn.getScene().getWindow();
        stage.close();
        if (b){
            new Alert(Alert.AlertType.NONE,"配置保存成功",new ButtonType[]{ButtonType.CLOSE}).show();
        }
    }

    public boolean writeToFile(String content,String filename) throws IOException {
        File file = new File(filename); // 指定输出文件名为"output.txt"

        if (!file.exists()) {
            file.createNewFile(); // 若文件不存在则创建新文件
        }

        FileWriter writer = new FileWriter(file); // 创建FileWriter对象来进行写操作

        writer.write(content); // 将文本内容写入文件

        writer.close(); // 关闭流

        return true;
    }
}
