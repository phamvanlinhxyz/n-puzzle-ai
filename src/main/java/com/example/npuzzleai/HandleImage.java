package com.example.npuzzleai;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HandleImage {
    private final int Size;
    private final int Length;
    protected int blank;
    protected int[] Value;
    private final Image img;
    private final double w;
    private final double h;
    private double cw;
    private double ch;
    private double width, height, cw1, ch1;
    private double align = 0;
    public boolean win = false;
    public HandleImage(Image img, int size, int [] val) {
        this.img = img;
        this.Size =size;
        Length = Size * Size;
        this.Value = val;
        if(img == null) {
            width = w = 400;
            height = h = 400;
        }
        else {
            width = w = img.getWidth();
            height = h = img.getHeight();
        }
        InitImage();
    }
    public void InitImage() {
        cw = w / Size;
        ch = h / Size;
        int kt = 400;
        if (w != kt || h != kt) {
            if (w > h) {
                width = kt;
                height = width * h / w;
            } else {
                height = kt;
                width = height * w / h;
                align = (kt - width) / 2;
            }
        }
        cw1 = width / Size;
        ch1 = height / Size;
        blank = posBlank(Value);
    }
    public int posBlank(int[] Value) {//Tìm vị trí phần tử blank
        int pos=0;
        for(int i = 0; i < Length; i++)
            if(Value[i] == 0) {
                pos = i;
                break;
            }
        return pos;
    }
    public void paint(GraphicsContext g) {
        // Tạo khung số
        if(img == null) {
            g.setFill(Color.BLACK);
            g.fillRect(0, 0, width, height);
            for(int i = 0; i < Length; i++) {
                double x = (i % Size) * cw1;
                double y = (double) (i / Size) * ch1;
                if(Value[i] != 0) {
                    g.setFill(Color.WHITE);
                    g.fillRect(x + 0.5, y + 0.5, cw1 - 1, ch1 - 1);
                    g.setFill(Color.BLACK);
                    g.setFont(Font.font("Roboto", FontWeight.BOLD, (double) 120 / Size));
                    if (Value[i] < 10) {
                        g.fillText(String.valueOf(Value[i]), x + 5*cw1/12, y + (3*ch1)/5);
                    } else {
                        g.fillText(String.valueOf(Value[i]), x + 1*cw1/3, y + (3*ch1)/5);
                    }
                } else {
                    g.setFill(Color.rgb(156, 127, 78));
                    g.fillRect(x + 0.5, y + 0.5, cw1 - 1, ch1 - 1);
                }
            }
        }
        // Tạo khung ảnh
        else {
            g.clearRect(0, 0, 400, 400);
            if (!win) {
                g.setFill(Color.GRAY);
                g.fillRect(align, 0, width, height);
                double dx, dy, sx, sy;
                for (int i = 0; i < Length; i++) {
                    if (Value[i] != 0) {
                        int c = Value[i] + 1 - State.goal;
                        sx = (c % Size) * cw;
                        sy = (double) (c / Size) * ch;
                        dx = (i % Size) * cw1;
                        dy = (double) (i / Size) * ch1;
                        g.drawImage(img, sx, sy, cw, ch, dx + align + 0.5, dy + 0.5, cw1 - 1, ch1 - 1);
                        g.setFill(Color.WHITE);
                        g.setFont(Font.font("Roboto", FontWeight.MEDIUM, (double) 90 / Size));
                        if (Value[i] < 10) {
                            g.fillText(String.valueOf(Value[i]), dx + cw1 + align - (double) 70/Size, dy + ch1 - (double) 30/Size);
                        } else {
                            g.fillText(String.valueOf(Value[i]), dx + cw1 + align - (double) 120/Size, dy + ch1 - (double) 30/Size);
                        }
                    }
                }
            } else {
                g.clearRect(0, 0, 400, 400);
                g.drawImage(img, align, 0, width, height);
            }
        }
    }
}
