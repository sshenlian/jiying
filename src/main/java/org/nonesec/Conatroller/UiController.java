package org.nonesec.Conatroller;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class UiController {

    //弱口令生成 UI组件
    public TextArea UsernameTextArea,PasswordTextArea;

    //去重文本 UI组件
    public TextArea DeduplicationResultTextArea,DeduplicationTextArea;
    //编码解码 UI组件

    public TextArea EncodeTextArea1,DecodeTextArea1;
   public ComboBox<String> DecodeComboBox;

    public  String encodeTextResults;
    public TextField fofaSyntax;
    public TableColumn num,link,title,ip;
    public TableView fofaTable;
    public Text statusText;
    public ComboBox toolslist;
    public Button addProgrammeBtn;
    public TextArea outText;
    public TextArea ProgrammelinkText;


    public void initialize() {
        // 设置
        this.initabComboBox();
        this.initProgramme();
    }

    private void initabComboBox() {
        ObservableList<String> encodeMethods = FXCollections.observableArrayList("Base64", "Unicode","URL编码");
        this.DecodeComboBox.setItems(encodeMethods);
    }

    public void initProgramme(){
        boolean b = this.DirectoryCheckAndCreate();
        if (b == false){
            return;
        }
        String[] programmes = getDirectoryFileNames("Programme");
        if (programmes != null) {
            ObservableList<String> itemList = FXCollections.observableArrayList(programmes);
            toolslist.setItems(itemList);
        }

    }

    private boolean DirectoryCheckAndCreate(){
        String directoryPath = "Programme";  // 目录路径

        // 创建文件对象
        File directory = new File(directoryPath);

        // 检查目录是否存在
        if (directory.exists() && directory.isDirectory()) {
            return true;
        } else {
            // 创建目录
            if (directory.mkdirs()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static String[] getDirectoryFileNames(String directoryPath) {
        // 创建文件对象
        File directory = new File(directoryPath);

        // 检查目录是否存在且是一个目录
        if (directory.exists() && directory.isDirectory()) {
            // 获取目录下的所有文件
            File[] files = directory.listFiles();

            // 提取文件名并存储到字符串数组中
            if (files != null) {
                String[] fileNames = new String[files.length];
                for (int i = 0; i < files.length; i++) {
                    fileNames[i] = files[i].getName();
                }
                return fileNames;
            }
        }

        return null;
    }
    public void initPasswordBtn(ActionEvent actionEvent){
        PasswordController passwordController = new PasswordController();
        String[] userName = this.GetUserName();
        String passwordString = Arrays.stream(userName)
                .filter(obj -> !obj.isEmpty())
                .map(passwordController::Template)
                .collect(Collectors.joining());
        String top100Passwords = passwordController.top100();
        String result = passwordString + top100Passwords;
        this.PasswordTextArea.setText(result);

    }


    public String[] GetUserName(){
        return UsernameTextArea.getText().split("\n");
    }

    public void DeduplicationTextBtn(ActionEvent actionEvent) {
        String[] DeduplicationText = GetDeduplicationText();
        DeduplicationController deduplicationController = new DeduplicationController();
        String deduplicationResultsText = deduplicationController.GetDeduplication(DeduplicationText);
        DeduplicationResultTextArea.setText(deduplicationResultsText);
    }

    public String[] GetDeduplicationText(){
        return DeduplicationTextArea.getText().split("\n");
    }


    public void DecodeBtn(ActionEvent actionEvent) throws UnsupportedEncodingException {
        String DecodeText = DecodeTextArea1.getText();
        String value = DecodeComboBox.getValue();
        DecodeController decodeController = new DecodeController();
        switch (value){
            case "Base64":
                encodeTextResults = decodeController.Base64ToSting(DecodeText);
                break;
            case "Unicode":
                encodeTextResults = decodeController.UnicodeToString(DecodeText);
                break;
            case "URL编码":
                encodeTextResults = decodeController.URLCodeToString(DecodeText);
                break;
            default:
                encodeTextResults = "";
        }
        EncodeTextArea1.setText(encodeTextResults);

    }

    public void EncodeBtn(ActionEvent actionEvent) throws UnsupportedEncodingException {
        String encodeText = EncodeTextArea1.getText();
        String value = DecodeComboBox.getValue();
        EncodeController encodeController = new EncodeController();
        switch (value){
            case "Base64":
                encodeTextResults = encodeController.StringToBase64(encodeText);
                break;
            case "Unicode":
                encodeTextResults = encodeController.StringToUnicode(encodeText);
                break;
            case "URL编码":
                encodeTextResults = encodeController.StringToURLCode(encodeText);
                break;
            default:
                encodeTextResults = "";
        }
        DecodeTextArea1.setText(encodeTextResults);
    }


    public void api_setting(ActionEvent actionEvent) throws IOException {
        Stage menuStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/Setting.fxml"));
        menuStage.setScene(new Scene(root));
        menuStage.setTitle("FOFA API配置");

        menuStage.show();
    }

    public void search(ActionEvent actionEvent){
        Task<Void> searchTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                statusText.setText("查询中……");
                System.out.println("Task call");
                fofaController fofaController = new fofaController();
                fofaController.getConfig();
                EncodeController encodeController = new EncodeController();
                String fofaSyntaxBase64 = encodeController.StringToBase64(fofaSyntax.getText());
                String Url = String.format("https://fofa.info/api/v1/search/all?&key=%s&size=10000&fields=link,title,ip&qbase64=%s", fofaController.fofaAPIKEY, fofaSyntaxBase64);
                String[][] searchResults = new String[0][];
                try {
                    searchResults = fofaController.getSearchResults(Url);
                    if (searchResults == null || searchResults.length == 0) {
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }


                ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

                TableColumn<ObservableList<String>, String> numColumn = new TableColumn<>("序号");
                TableColumn<ObservableList<String>, String> linkColumn = new TableColumn<>("链接");
                TableColumn<ObservableList<String>, String> titleColumn = new TableColumn<>("标题");
                TableColumn<ObservableList<String>, String> ipColumn = new TableColumn<>("IP");
                numColumn.setPrefWidth(46);
                linkColumn.setPrefWidth(329);
                titleColumn.setPrefWidth(259);
                ipColumn.setPrefWidth(219);
                Platform.runLater(() -> {
                    fofaTable.getColumns().clear();
                    fofaTable.getColumns().addAll(numColumn, linkColumn, titleColumn, ipColumn);
                });

                Arrays.stream(searchResults)
                        .filter(row -> null != row && row.length > 0)
                        .map(row -> {
                            ObservableList<String> rowData = FXCollections.observableArrayList();
                            rowData.add(String.valueOf(data.size() + 1));
                            rowData.addAll(Arrays.asList(row));
                            return rowData;
                        })
                        .forEach(data::add);

                numColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(0)));
                linkColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(1)));
                titleColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(2)));
                ipColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(3)));

                Platform.runLater(() -> fofaTable.setItems(data));

                //新增右键复制功能
                ContextMenu contextMenu = new ContextMenu();
                MenuItem copyMenuItem = new MenuItem("Copy Link");
                copyMenuItem.setOnAction(event -> {
                    StringBuilder clipboardString = new StringBuilder();
                    //clipboardString.append(data.get(1)); //复制所有数据
                    clipboardString.append(data.get(1).get(1)); //仅复制url
                    // 将复制的内容添加到剪贴板
                    ClipboardContent clipboardContent = new ClipboardContent();
                    clipboardContent.putString(clipboardString.toString());
                    Clipboard.getSystemClipboard().setContent(clipboardContent);
                });
                contextMenu.getItems().add(copyMenuItem);
                fofaTable.setContextMenu(contextMenu);
                fofaTable.setOnContextMenuRequested(event -> contextMenu.show(fofaTable, event.getScreenX(), event.getScreenY()));
                //右键复制功能结束

                statusText.setText("查询结束");
                return null;
            }
        };
        new Thread(searchTask).start();
    }


    public static void exportTableViewData(TableView<?> tableView, String filename) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(filename);
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                File filepath = new File(file, filename);
                FileWriter writer = new FileWriter(file);

                // 导出表头
                for (TableColumn<?, ?> column : tableView.getColumns()) {
                    writer.write(column.getText() + ",");
                }
                writer.write("\n");

                // 导出数据行
                for (Object item : tableView.getItems()) {
                    for (TableColumn<?, ?> column : tableView.getColumns()) {
                        TableColumn<Object, ?> typedColumn = (TableColumn<Object, ?>) column;
                        Object cellValue = typedColumn.getCellData(item);
                        if (cellValue != null) {
                            writer.write(cellValue.toString() + ",");
                        } else {
                            writer.write(",");
                        }
                    }
                    writer.write("\n");
                }

                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ExportBtn(ActionEvent actionEvent) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = now.format(formatter);
        exportTableViewData(fofaTable,"fofa导出结果"+timestamp+".csv");
        new Alert(Alert.AlertType.NONE,"导出资产到当前目录 -> "+"fofa导出结果"+timestamp+".csv",new ButtonType[]{ButtonType.CLOSE}).show();
    }


    public void addProgramme(Event actionEvent) throws IOException {
        Stage menuStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/Add-Programme.fxml")));
        menuStage.setScene(new Scene(root));
        menuStage.setTitle("配置方案");

        menuStage.show();
    }

    public void runtools(String path) {
        Thread thread = new Thread(() -> {
            try {
                // 创建 ProcessBuilder 对象
                ProcessBuilder processBuilder = new ProcessBuilder(path.split(" "));

                // 启动进程
                Process process = processBuilder.start();

                // 获取进程的输入流
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                // 读取输出
                StringBuilder outputBuilder = new StringBuilder();

                // 更新 UI，在 JavaFX 应用程序线程中执行更新操作
                // 读取输出
                String line;
                while ((line = reader.readLine()) != null) {
                    String finalLine = line;
                    Platform.runLater(() -> outText.appendText(finalLine + "\n"));
                }

                // 等待进程执行完成
                int exitCode = process.waitFor();

                // 打印进程退出码（可选）
                System.out.println("Exit Code: " + exitCode);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 启动线程
        thread.start();
    }

    public void runProgram(ActionEvent actionEvent) throws IOException {

        Object value = toolslist.getValue();
        String string = value.toString();
        File file = new File("./Programme/"+string);
        String[] split = ProgrammelinkText.getText().split("\n");
        String replace = null;
        if(file.exists()){
            BufferedReader reader = new BufferedReader(new FileReader(file));
            replace = reader.readLine().replace("\n", "");
        }
        for (String link:split){
            if (replace != null) {
                this.runtools(replace.replace("{link}",link));
            }

        }


    }
}