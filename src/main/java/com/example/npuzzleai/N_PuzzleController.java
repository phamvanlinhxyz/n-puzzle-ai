package com.example.npuzzleai;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.*;
import javafx.scene.control.*;

import java.io.File;
import java.net.URL;
import java.util.*;

import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

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
    private String algorithm = "A*";
    private State state = new State(size);
    private int[] value = state.createStartArray();
    private Vector<Node> result;
    //private boolean isSolve = false;

    @Override
    // Trạng thái khởi tạo ban đầu
    public void initialize(URL url, ResourceBundle resourceBundle) {
        State.heuristic = 1;
        displayImage(null);
    }

    @Override
    public void run() {
        int step = 0;
        for (Node node : result) {
            stepField.setText(step + "/" + (result.size() - 1));
            value = node.state.value;
            state.value = value;
            displayImage(image);
            step++;
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
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
            image = new Image(file.toURI().toString());
            // Thêm ảnh nhỏ
            if (image.getHeight() > image.getWidth()) {
                double width = image.getWidth() * 180 / image.getHeight();
                imgView.setX((180 - width) / 2);
            }
            imgView.setImage(image);
            value = state.createStartArray();
            displayImage(image);
        }
    }
    // Button thêm bảng số
    public void onAddNumberBtnClick() {
        image = null;
        imgView.setImage(null);
        value = state.createStartArray();
        displayImage(image);
    }
    // Button trộn ảnh
    public void onJumbleBtnClick() {
        stepField.setText("0/0");
        value = state.createRandomArray();
        displayImage(image);
    }
    // Button tìm kết quả
    public void onSolveBtnClick() {
        if (Objects.equals(algorithm, "BFS")) {
            bFS = new BFS();
            bFS.startNode = new Node(state, 0);
            State endState = new State(size);
            endState.Init();
            bFS.endNode = new Node(endState, 0);
            bFS.solve();
            result = bFS.RESULT;
        } else {
            aStar = new AStar();
            aStar.startNode = new Node(state, 0);
            aStar.solve();
            result = aStar.RESULT;
        }
        Thread test = new Thread(this);
        test.start();
        /*if (!isSolve) {
            isSolve = true;
            solveBtn.setText("Stop");
            sizeMenu.setDisable(true);
            algorithmMenu.setDisable(true);
            jumbleBtn.setDisable(true);
            addImage.setDisable(true);
            addNumber.setDisable(true);
            progressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        } else {
            isSolve = false;
            solveBtn.setText("Solve");
            sizeMenu.setDisable(false);
            algorithmMenu.setDisable(false);
            jumbleBtn.setDisable(false);
            addImage.setDisable(false);
            addNumber.setDisable(false);
            progressBar.setProgress(0);
        }*/
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
