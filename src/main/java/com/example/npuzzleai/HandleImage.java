package com.example.npuzzleai;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HandleImage {
    private int Size;
    private int Length;
    protected int blank;
    protected int[] Value;
    private Image bi;
    private double w, h, cw, ch;  // kích thước thực của ảnh
    private double width, height, cw1, ch1;  // kích thước mới: cw1, ch1 là chiều dài và rộng của 1 ô số
    private double align = 0;
    private int type;
    private Color ColorEbox;
    private Color ColorBoxs;
    public boolean win = false;
    public HandleImage(Image img, int size, int [] val, int t, Color Eb, Color b) {
        this.bi = img;
        this.type = t;
        this.ColorEbox = Eb;
        this.ColorBoxs = b;
        this.Size =size;
        Length = Size * Size;
        this.Value = val;
        if(type == 0) {
            width = w = 400;
            height = h = 400;
        }
        else {
            width = w = bi.getWidth();
            height = h = bi.getHeight();
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
        if(type == 0) {
            g.setFill(ColorEbox);
            g.fillRect(0, 0, width, height);
            for(int i = 0; i < Length; i++) {
                double x = (i % Size) * cw1;
                double y = (i / Size) * ch1;
                if(Value[i] != 0) {
                    g.setFill(ColorBoxs);
                    g.fillRect(x + 0.5, y + 0.5, cw1 - 1, ch1 - 1);
                    g.setFill(Color.WHITE);
                    g.setFont(Font.font("Roboto", FontWeight.BOLD, 80 / Size));
                    g.fillText(Value[i]+"", x + cw1/2 - 3*Size/2, y + (2*ch1)/3 - 2*Size);
                }
            }
        }
        // Tạo khung ảnh
        else if(type == 1) {
            g.clearRect(0, 0, 400, 400);
            if(!win) {
                g.setFill(ColorEbox);
                g.fillRect(align, 0, width, height);
                double dx, dy, sx, sy;
                for(int i = 0; i < Length; i++) {
                    if(Value[i] != 0) {
                        sx = (Value[i] % Size) * cw;
                        sy = (Value[i] / Size) * ch;
                        dx = (i % Size) * cw1;
                        dy = (i / Size) * ch1;
                        g.drawImage(bi, sx, sy, cw, ch, dx + align + 0.5, dy + 0.5, cw1 - 1, ch1 - 1);
                    }
                }
            }
            else  //trạng thái lúc win
            {
                g.clearRect(0, 0, 400, 400);
                g.drawImage(bi, 0, 0, width, height);
            }
        } else if(type == 3) {
            g.drawImage(bi, 0, 0, width, height);
        }
    }
}
