package controller;

import User.User;
import model.Direction;
import model.MapModel;
import view.game.BoxComponent;
import view.game.GamePanel;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * It is a bridge to combine GamePanel(view) and MapMatrix(model) in one game.
 * You can design several methods about the game logic in this class.
 */
public class GameController {
    private final GamePanel view;
    private final MapModel model;

    public GameController(GamePanel view, MapModel model) {
        this.view = view;
        this.model = model;
        view.setController(this);
    }

    public void restartGame() {
        System.out.println("Do restart game here");
        this.model.resetOriginalMatrix();
        this.view.clearAllBoxFromPanel();
        this.view.initialGame(model.getMatrix());
        view.resetSteps();
    }
    private boolean checkVictory() {
        // 检测4x4方块是否在右上角（假设地图为4x4）
        int targetRow = 0;
        int targetCol = 2; // 根据地图布局调整

        for (int r = 0; r < model.getHeight(); r++) {
            for (int c = 0; c < model.getWidth(); c++) {
                if (model.getId(r, c) == 4) {
                    // 检查是否占据目标区域
                    return (r == targetRow && c == targetCol) &&
                            model.getId(r, c+1) == 4 &&
                            model.getId(r+1, c) == 4 &&
                            model.getId(r+1, c+1) == 4;
                }
            }
        }
        return false;
    }


    public boolean doMove(int row, int col, Direction direction) {
        int blockId = model.getId(row, col);
        if (blockId == 0) return false; // empty space

        // Determine block size based on ID
        int width = 1, height = 1;
        if (blockId == 2) { // Horizontal block (1×2)
            width = 2;
            height = 1;
        } else if (blockId == 3) { // Vertical block (2×1)
            width = 1;
            height = 2;
        } else if (blockId == 4) { // Big block (2×2)
            width = 2;
            height = 2;
        }

        // Check if movement is possible
        if (canMove(row, col, width, height, direction)) {
            // Clear old positions
            clearBlock(row, col, width, height);

            // Calculate new position
            int newRow = row + direction.getRow();
            int newCol = col + direction.getCol();

            // Set new positions
            placeBlock(newRow, newCol, width, height, blockId);

            // Update view
            updateBoxPosition(row, col, newRow, newCol);
            if (checkVictory()) {
                showVictoryDialog();
            }
            return true;
        }
        return false;
    }

    private void showVictoryDialog() {

            SoundController.playVictorySound();
            String message = "恭喜！曹操逃脱成功！\n步数: " + view.getSteps();
            JOptionPane.showMessageDialog(view, message, "胜利", JOptionPane.INFORMATION_MESSAGE);
            // 播放胜利音效


    }
    private void clearBlock(int row, int col, int width, int height) {
        for (int r = row; r < row + height; r++) {
            for (int c = col; c < col + width; c++) {
                model.getMatrix()[r][c] = 0;
            }
        }
    }

    private void placeBlock(int row, int col, int width, int height, int blockId) {
        for (int r = row; r < row + height; r++) {
            for (int c = col; c < col + width; c++) {
                model.getMatrix()[r][c] = blockId;
            }
        }
    }

    private void updateBoxPosition(int oldRow, int oldCol, int newRow, int newCol) {
        BoxComponent box = view.getSelectedBox();
        box.setRow(newRow);
        box.setCol(newCol);
        box.setLocation(box.getCol() * view.getGRID_SIZE() + 2,
                box.getRow() * view.getGRID_SIZE() + 2);
        box.repaint();
    }

    private boolean canMove(int row, int col, int width, int height, Direction direction) {
        int newRow = row + direction.getRow();
        int newCol = col + direction.getCol();

        // Check boundaries
        if (newRow < 0 || newRow + height > model.getHeight() ||
                newCol < 0 || newCol + width > model.getWidth()) {
            return false;
        }

        // Check if all new positions are empty
        for (int r = newRow; r < newRow + height; r++) {
            for (int c = newCol; c < newCol + width; c++) {
                if (model.getId(r, c) != 0) {
                    // Allow movement if the occupied space is part of the same block
                    if (r < row || r >= row + height || c < col || c >= col + width) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    //todo: add other methods such as loadGame, saveGame...



    // GameController.java 修改加载逻辑
    // GameController.java - 修改 saveGame 方法
    // GameController.java - 修改 saveGame 方法
    public void saveGame(User user) {
        int[][] map = model.getMatrix();
        int steps = view.getSteps();
        List<String> gameData = new ArrayList<>();
        gameData.add("Step:" + steps);

        for (int[] row : map) {
            StringBuilder sb = new StringBuilder();
            for (int value : row) {
                sb.append(value).append(" ");
            }
            gameData.add(sb.toString().trim());
        }
        String dirPath = "save/" + user.getUsername();
        File dir = new File(dirPath);
        if (!dir.exists() && !dir.mkdirs()) {
            JOptionPane.showMessageDialog(view, "无法创建存档目录！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String filePath = dirPath + "/data.txt";
        try {
            Files.write(Path.of(filePath), gameData, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            JOptionPane.showMessageDialog(view, "存档保存成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view, "存档保存失败！错误信息：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    public void loadGame(String path) {
        try {
            Path filePath = Path.of(path);
            if (!Files.exists(filePath)) {
                throw new IOException("存档文件不存在: " + path);
            }
            List<String> lines = Files.readAllLines(filePath);
            if (lines.isEmpty()) {
                throw new IOException("存档文件为空");
            }
            // 解析步数（首行必须为 Step:X）
            int steps = 0;
            String firstLine = lines.get(0);
            try {
                steps = Integer.parseInt(firstLine.substring(5));
            } catch (NumberFormatException e) {
                throw new IOException("步数格式错误: " + firstLine);
            }
            lines = lines.subList(1, lines.size()); // 移除步数行
            // 解析地图数据
            int rows = lines.size();
            int cols = lines.get(0).trim().split("\\s+").length;
            int[][] map = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) {
                    throw new IOException("第 " + (i + 1) + " 行为空");
                }
                String[] values = line.split("\\s+");
                if (values.length != cols) {
                    throw new IOException("第 " + (i + 1) + " 行列数不一致");
                }
                for (int j = 0; j < cols; j++) {
                    try {
                        map[i][j] = Integer.parseInt(values[j]);
                    } catch (NumberFormatException e) {
                        throw new IOException("第 " + (i + 1) + " 行第 " + (j + 1) + " 列包含非数字字符: " + values[j]);
                    }
                }
            }
            // 更新模型和视图
            model.setMatrix(map);
            view.clearAllBoxFromPanel(); // 强制清空旧组件
            view.initialGame(map);       // 重新初始化游戏
            view.setSteps(steps);        // 更新步数显示
            view.revalidate();           // 刷新布局
            view.repaint();              // 强制重绘

            JOptionPane.showMessageDialog(view, "存档加载成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            String errorMsg = "无法加载存档:\n" + e.getMessage();
            JOptionPane.showMessageDialog(view, errorMsg, "加载失败", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    //todo: add other methods such as loadGame, saveGame...

}
