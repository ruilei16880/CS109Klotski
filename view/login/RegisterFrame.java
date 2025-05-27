package view.login;

import controller.SoundController;
import controller.UserController;
import view.FrameUtil;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    public RegisterFrame() {
        this.setTitle("用户注册");
        this.setSize(300, 250);
        this.setLayout(null);

        JLabel userLabel = FrameUtil.createJLabel(this, new Point(30, 20), 80, 30, "用户名:");
        JLabel pwdLabel = FrameUtil.createJLabel(this, new Point(30, 70), 80, 30, "密码:");
        JLabel confirmLabel = FrameUtil.createJLabel(this, new Point(30, 120), 80, 30, "确认密码:");
//创建Username和password
        JTextField username = FrameUtil.createJTextField(this, new Point(120, 20), 150, 30);
        JPasswordField password = new JPasswordField();
        password.setBounds(120, 70, 150, 30);
        this.add(password);
        JPasswordField confirmPwd = new JPasswordField();
        confirmPwd.setBounds(120, 120, 150, 30);
        this.add(confirmPwd);





        JButton submitBtn = FrameUtil.createButton(this, "注册", new Point(100, 170), 100, 30);
        submitBtn.addActionListener(e -> {
            SoundController.playButtonSound();
            String user = username.getText();
            String pwd = new String(password.getPassword());
            String confirm = new String(confirmPwd.getPassword());
//二次确认密码
            if (!pwd.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "两次密码不一致！");
            }
            //如果用户名不存在创建新用户
            else if (UserController.registerUser(user, pwd)) {
                JOptionPane.showMessageDialog(this, "注册成功！");
                new LoginFrame(280, 280).setVisible(true);
                this.dispose();
            }
            //如果存在提示信息
            else {
                JOptionPane.showMessageDialog(this, "用户名已存在！");
            }
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}