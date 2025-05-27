package view.game;

import User.User;
import controller.GameController;
import controller.SoundController;
import model.MapModel;
import view.FrameUtil;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private GameController controller;
    private JButton restartBtn;
    private JButton loadBtn;
    private JButton saveBtn;

    private JLabel stepLabel;
    private JLabel userLabel;
    private GamePanel gamePanel;
    private User user;

    public GameFrame(int width, int height, MapModel mapModel, User user) {
        this.setTitle("数字华容道");
        this.setLayout(null);
        this.setSize(width, height);
        gamePanel = new GamePanel(mapModel);
        gamePanel.setLocation(0, 0);
        this.add(gamePanel);
        this.user = user;

        // 初始化 controller
        this.controller = new GameController(gamePanel, mapModel);

        // 初始化按钮和标签
        this.restartBtn = FrameUtil.createButton(this, "Restart", new Point(gamePanel.getWidth() , 120), 80, 50);
        this.loadBtn = FrameUtil.createButton(this, "Load", new Point(gamePanel.getWidth() , 210), 80, 50);
        this.saveBtn = FrameUtil.createButton(this, "Save", new Point(gamePanel.getWidth() , 300), 80, 50);
        this.stepLabel = FrameUtil.createJLabel(this, "Start", new Font("serif", Font.ITALIC, 22), new Point(gamePanel.getWidth() , 70), 180, 50);

       /* JLabel background = new JLabel(new ImageIcon("images/bg.png"));
        background.setLocation(0, 0);
        background.setSize(500, 430);
        this.add(background);
        background.add(gamePanel);*/

        // 处理用户标签和按钮状态
        if (user == null) {
            saveBtn.setEnabled(false);
            loadBtn.setEnabled(false);
            this.userLabel = FrameUtil.createJLabel(
                    this,
                    "当前用户: 游客",
                    new Font("serif", Font.ITALIC, 18),
                    new Point(gamePanel.getWidth() , 15),
                    180,
                    50
            );
        } else {
            this.userLabel = FrameUtil.createJLabel(
                    this,
                    "当前用户: " + user.getUsername(),
                    new Font("serif", Font.ITALIC, 18),
                    new Point(gamePanel.getWidth() , 15),
                    180,
                    50
            );
        }

        gamePanel.setStepLabel(stepLabel);
        JLabel background = new JLabel(new ImageIcon("images/bg.png"));
        background.setLocation(-10, -10);
        background.setSize(500, 440);
        this.add(background);
        background.add(gamePanel);
        // 按钮事件监听器
        this.restartBtn.addActionListener(e -> {
            SoundController.playButtonSound();
            controller.restartGame();
            gamePanel.requestFocusInWindow();
        });

        this.loadBtn.addActionListener(e -> {
            if (user != null) {
                SoundController.playButtonSound();
                String path = String.format("save/%s/data.txt", user.getUsername());
                controller.loadGame(path);
                gamePanel.requestFocusInWindow();
            }
        });

        this.saveBtn.addActionListener(e -> {
            if (user != null) {
                SoundController.playButtonSound();
                controller.saveGame(user); // 提示由 controller 内部处理
                gamePanel.requestFocusInWindow();
            }
        });
        this.add(gamePanel);
        this.revalidate(); // 强制刷新布局
        this.repaint();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
