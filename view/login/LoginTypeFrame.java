package view.login;

import controller.SoundController;
import view.FrameUtil;
import view.game.GameFrame;
import model.MapModel;
import javax.swing.*;
import java.awt.*;

public class LoginTypeFrame extends JFrame {
    public LoginTypeFrame() {
        SoundController.init();
        this.setTitle("选择登录方式");
        this.setSize(300, 200);
        this.setLayout(null);

        JButton guestBtn = FrameUtil.createButton(this, "游客登录", new Point(80, 30), 140, 40);
        JButton userBtn = FrameUtil.createButton(this, "注册用户登录", new Point(80, 100), 140, 40);

        // LoginTypeFrame.java 修改游客登录代码
        guestBtn.addActionListener(e -> {
            SoundController.playButtonSound();
            // 使用有效的初始地图数据
            int[][] initialMap = {
                    {3, 4, 4, 3},
                    {3, 4, 4, 3},
                    {3, 2, 2, 3},
                    {3, 1, 1, 3},
                    {1, 0, 0, 1}
            };
            MapModel mapModel = new MapModel(initialMap);
            GameFrame gameFrame = new GameFrame(490, 440, mapModel, null); // 增大窗口尺寸
            gameFrame.setVisible(true);
            this.dispose();
        });

        userBtn.addActionListener(e -> {
            // 跳转到用户登录界面
            SoundController.playButtonSound();
            new LoginFrame(280, 280).setVisible(true);
            this.dispose();
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}