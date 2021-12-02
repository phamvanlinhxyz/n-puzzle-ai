package com.example.npuzzleai;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;

import java.awt.*;
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
    private Vector<Node> result;
    private String algorithm = "A*";
    private int countStep = 0;
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
                System.err.println(e);
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
            countStep = 0;
            image = new Image(file.toURI().toString());
            // Thêm ảnh nhỏ
            if (image.getHeight() > image.getWidth()) {
                double width = image.getWidth() * 180 / image.getHeight();
                imgView.setX((180 - width) / 2);
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
        countStep = 0;
        Thread runResult = new Thread(this);
        runResult.start();
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
    public void onKeyPressed(KeyEvent ke) {
        switch (ke.getCode()) {
            case W -> state.UP();
            case A -> state.LEFT();
            case S -> state.DOWN();
            case D -> state.RIGHT();
        }
        countStep++;
        stepField.setText(String.valueOf(countStep));
        displayImage(image);
    }
    public void onMouseClicked(MouseEvent me) {
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
            Toolkit.getDefaultToolkit().beep();
            countStep--;
        }
        stepField.setText(String.valueOf(countStep));
        displayImage(image);
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
