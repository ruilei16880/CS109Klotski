package view.login;

import User.User;
import controller.SoundController;
import controller.UserController;
import model.MapModel;
import view.FrameUtil;
import view.game.GameFrame;

import javax.swing.*;
import java.awt.*;


public class LoginFrame extends JFrame {
    private JTextField username;
    private JTextField password;
    private JButton submitBtn;
    private JButton resetBtn;
    private GameFrame gameFrame;


    public LoginFrame(int width, int height) {
        this.setTitle("Login Frame");
        this.setLayout(null);
        this.setSize(width, height);
        JLabel userLabel = FrameUtil.createJLabel(this, new Point(50, 20), 70, 40, "用户名:");
        JLabel passLabel = FrameUtil.createJLabel(this, new Point(50, 80), 70, 40, "密码:");
        username = FrameUtil.createJTextField(this, new Point(120, 20), 120, 40);
        password = FrameUtil.createJTextField(this, new Point(120, 80), 120, 40);

        submitBtn = FrameUtil.createButton(this, "登录", new Point(40, 140), 100, 40);
        JButton registerBtn = FrameUtil.createButton(this, "注册", new Point(160, 140), 100, 40); // 新增注册按钮
        resetBtn = FrameUtil.createButton(this, "重置", new Point(40, 180), 100, 40); // 调整按钮位置

        // 注册按钮逻辑
        registerBtn.addActionListener(e -> {
            SoundController.playButtonSound();
            new RegisterFrame().setVisible(true);
            this.dispose();
        });

        submitBtn.addActionListener(e -> {
            SoundController.playButtonSound();
            System.out.println("Username = " + username.getText());
            System.out.println("Password = " + password.getText());
            User user= new User(username.getText(), password.getText());
            if (UserController.validateUser(username.getText(),password.getText())){
                //todo: check login info
                MapModel mapModel = new MapModel(new int[][]{
                        {3, 4, 4, 3},
                        {3, 4, 4, 3},
                        {3, 2, 2, 3},
                        {3, 1, 1, 3},
                        {1, 0, 0, 1}
                });
                GameFrame gameFrame = new GameFrame(490, 440, mapModel, user);
                gameFrame.setVisible(true);
                this.setVisible(false);
            }else {
                JOptionPane.showMessageDialog(this,"Invalid Username or Password","Error",JOptionPane.ERROR_MESSAGE);
            }
        });
        resetBtn.addActionListener(e -> {
            SoundController.playButtonSound();
            username.setText("");
            password.setText("");
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


}
