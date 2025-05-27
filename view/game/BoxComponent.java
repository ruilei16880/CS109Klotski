package view.game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BoxComponent extends JComponent {
    private Image image;
    private int row;
    private int col;
    private boolean isSelected;


    public BoxComponent(int blockId, int row, int col) {
        loadImage(blockId);
        this.row = row;
        this.col = col;
        isSelected = false;
    }
    private void loadImage(int blockId) {
        try {
            String path = "images/block" + blockId + ".png";
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            image = null;
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }

        Border border ;
        if(isSelected){
            border = BorderFactory.createLineBorder(Color.red,3);
        }else {
            border = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
        }
        this.setBorder(border);
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        this.repaint();
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
