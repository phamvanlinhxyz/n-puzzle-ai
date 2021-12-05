package com.example.npuzzleai;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;

import java.io.File;
import java.net.URL;
import java.util.*;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class N_PuzzleController implements Initializable, Runnable {
    @FXML
    ToggleGroup sizeToggle;
    @FXML
    ToggleGroup algorithmToggle;
    @FXML
    RadioMenuItem size3;
    @FXML
    RadioMenuItem size4;
    @FXML
    RadioMenuItem size5;
    @FXML
    Canvas imgCanvas;
    @FXML
    ImageView imgView;
    @FXML
    ProgressBar progressBar;
    @FXML
    Button solveBtn;
    @FXML
    Button jumbleBtn;
    @FXML
    Button addImage;
    @FXML
    Button addNumber;
    @FXML
    SplitMenuButton sizeMenu;
    @FXML
    SplitMenuButton algorithmMenu;
    @FXML
    TextField stepField;
    @FXML
    AnchorPane displayPane;

    public AStar aStar;
    public BFS bFS;
    public Image image;
    public HandleImage handledImage;
    private int size = 3;
    private State state = new State(size);
    private int[] value = state.createStartArray();
    private Vector<int[]> result;
    private String algorithm = "A*";
    private int countStep = 0;
    private boolean isSolve = false;
    private int approvedNodes;
    private int totalNodes;
    private long solveTime;
    private String error;

    @Override
    // Trạng thái khởi tạo ban đầu
    public void initialize(URL url, ResourceBundle resourceBundle) {
        State.heuristic = 1;
        progressBar.setVisible(false);
        displayImage(null);
    }
    // Luồng chạy lời giải
    public void run() {
        int totalStep = result.size() - 1;
        for (int i = 0; i <= totalStep; i++) {
            value = result.get(i);
            state.value = value;
            displayImage(image);
            String step = i + "/" + totalStep;
            Platform.runLater(() -> stepField.setText(step));
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Platform.runLater(this::notSolve);
    }

    @FXML
    // Chọn size bảng
    public void onChangeImageSize() {
        RadioMenuItem selectedSize = (RadioMenuItem) sizeToggle.getSelectedToggle();
        switch (selectedSize.getId()) {
            case "size4" -> size = 4;
            case "size5" -> size = 5;
            default -> size = 3;
        }
        sizeMenu.setText(selectedSize.getText());
        state = new State(size);
        value = state.createStartArray();
        displayImage(image);
    }
    // Chọn thuật toán
    public void onChangeAlgorithm() {
        RadioMenuItem selectedAlgorithm = (RadioMenuItem) algorithmToggle.getSelectedToggle();
        switch (selectedAlgorithm.getId()) {
            case "heuristic1" -> {
                State.heuristic = 1;
                algorithm = "A*";
            }
            case "heuristic2" -> {
                State.heuristic = 2;
                algorithm = "A*";
            }
            case "heuristic3" -> {
                State.heuristic = 3;
                algorithm = "A*";
            }
            case "heuristic4" -> {
                State.heuristic = 4;
                algorithm = "A*";
            }
            case "heuristic5" -> {
                State.heuristic = 5;
                algorithm = "A*";
            }
            default -> algorithm = "BFS";
        }
        algorithmMenu.setText(selectedAlgorithm.getText());
    }
    // Button thêm ảnh
    public void onAddImgBtnClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            countStep = 0;
            image = new Image(file.toURI().toString());
            // Thêm ảnh nhỏ
            if (image.getHeight() > image.getWidth()) {
                double width = image.getWidth() * 180 / image.getHeight();
                imgView.setX((180 - width) / 2);
            } else {
                imgView.setX(0);
            }
            stepField.setText("0");
            imgView.setImage(image);
            value = state.createStartArray();
            displayImage(image);
        }
    }
    // Button thêm bảng số
    public void onAddNumberBtnClick() {
        countStep = 0;
        image = null;
        stepField.setText("0");
        imgView.setImage(null);
        value = state.createStartArray();
        displayImage(image);
    }
    // Button trộn ảnh
    public void onJumbleBtnClick() {
        stepField.setText("0");
        countStep = 0;
        value = state.createRandomArray();
        displayImage(image);
    }
    // Button tìm kết quả
    public void onSolveBtnClick() {
        countStep = 0;
        if (!isSolve) {
            solveThread().start();
            solving();
        } else {
            if(Objects.equals(algorithm, "BFS")) {
                bFS.stop = true;
            } else {
                aStar.stop = true;
            }
            notSolve();
        }
    }
    // Sự kiện từ bàn phím
    public void onKeyPressed(KeyEvent ke) {
        if (!isSolve) {
            switch (ke.getCode()) {
                case W -> state.UP();
                case A -> state.LEFT();
                case S -> state.DOWN();
                case D -> state.RIGHT();
                default -> countStep--;
            }
            countStep++;
            stepField.setText(String.valueOf(countStep));
            displayImage(image);
        }
    }
    // Sự kiện click chuột
    public void onMouseClicked(MouseEvent me) {
        if (!isSolve) {
            int blank = state.posBlank(state.value);
            int x = blank % size;
            int y = blank / size;
            int mx = (int) (me.getX() / imgCanvas.getWidth() * size);
            int my = (int) (me.getY() / imgCanvas.getHeight() * size);
            countStep++;
            if (mx == x && my == y - 1) {
                state.UP();
            } else if (mx == x && my == y + 1) {
                state.DOWN();
            } else if (mx == x - 1 && my == y) {
                state.LEFT();
            } else if (mx == x + 1 && my == y) {
                state.RIGHT();
            } else {
                countStep--;
            }
            stepField.setText(String.valueOf(countStep));
            displayImage(image);
        }
    }
    // Luồng tìm kiếm lời giải
    public Thread solveThread() {
        return new Thread(() -> {
            if (Objects.equals(algorithm, "BFS")) {
                bFS = new BFS();
                bFS.startNode = new Nodes(state, 0);
                State endState = new State(size);
                endState.Init();
                bFS.endNode = new Nodes(endState, 0);
                bFS.solve();
                result = bFS.RESULT;
                approvedNodes = bFS.approvedNodes;
                totalNodes = bFS.totalNodes;
                solveTime = bFS.time;
                error = bFS.error;
            } else {
                aStar = new AStar();
                aStar.startNode = new Nodes(state, 0);
                aStar.solve();
                result = aStar.RESULT;
                approvedNodes = aStar.approvedNodes;
                totalNodes = aStar.totalNodes;
                solveTime = aStar.time;
                error = aStar.error;
            }
            // Nếu tìm được lời giải
            if (result.size() > 1) {
                Platform.runLater(this::showAlert);
            }
            // Nếu không tìm được lời giải
            else if(result.size() == 0 && error != null) {
                Platform.runLater(this::showWarning);
            }
            // Người chơi chọn dừng tìm kiếm hoặc trạng thái ban đầu là trạng thái đích
            else {
                Platform.runLater(this::notSolve);
            }
        });
    }
    // Trạng thái đang tìm kiếm
    public void solving() {
        isSolve = true;
        solveBtn.setText("Stop");
        jumbleBtn.setDisable(true);
        addImage.setDisable(true);
        addNumber.setDisable(true);
        sizeMenu.setDisable(true);
        algorithmMenu.setDisable(true);
        progressBar.setVisible(true);
    }
    // Trạng thái không tìm kiếm
    public void notSolve() {
        isSolve = false;
        solveBtn.setText("Solve");
        solveBtn.setDisable(false);
        jumbleBtn.setDisable(false);
        addImage.setDisable(false);
        addNumber.setDisable(false);
        sizeMenu.setDisable(false);
        algorithmMenu.setDisable(false);
        progressBar.setVisible(false);
    }
    // Bảng thông báo không tìm được lời giải
    public void showWarning() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        ButtonType closeTypeBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(closeTypeBtn);
        alert.setTitle("Notification");
        alert.setHeaderText("Không tìm được lời giải!");
        alert.setContentText("Nguyên nhân: \n" + error);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(N_PuzzleApplication.class.getResourceAsStream("img/logo.png"))));
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        alert.showAndWait().ifPresent(res -> notSolve());
    }
    // Bảng thông báo kết quả tìm kiếm
    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        ButtonType runTypeBtn = new ButtonType("Run", ButtonBar.ButtonData.OK_DONE);
        ButtonType closeTypeBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.setTitle("Notification");
        alert.getButtonTypes().setAll(runTypeBtn, closeTypeBtn);
        alert.setHeaderText("Lời giải: ");
        alert.setContentText("Thuật toán sử dụng: " + (Objects.equals(algorithm, "BFS") ? "BFS" : "A* với Heuristic " + State.heuristic)  + "\n"
            + "Số node đã duyệt: " + approvedNodes + "\n"
            + "Tổng số node trên cây: " + totalNodes + "\n"
            + "Tổng số bước: " + (result.size() - 1) + "\n"
            + "Thời gian tìm kiếm: " + solveTime + " ms" + "\n"
            + "Bạn có muốn chạy lời giải?"
        );
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(N_PuzzleApplication.class.getResourceAsStream("img/logo.png"))));
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        Node closeBtn = alert.getDialogPane().lookupButton(closeTypeBtn);
        closeBtn.setId("close-btn");
        // Show alert và đợi phản hồi
        alert.showAndWait().ifPresent(res -> {
            if (res == runTypeBtn) {
                solveBtn.setDisable(true);
                Thread runResult = new Thread(this);
                runResult.start();
            } else {
                notSolve();
            }
        });
    }
    // Hiển thị ra màn hình
    public void displayImage(Image img) {
        if (img == null) {
            displayPane.setStyle("-fx-background-radius: 20px; -fx-background-color: #703838");
        } else {
            displayPane.setStyle("");
        }
        handledImage = new HandleImage(img ,size, value);
        if (state.isGoal()) {
            handledImage.win = true;
        }
        GraphicsContext gc = imgCanvas.getGraphicsContext2D();
        handledImage.paint(gc);
    }
}
