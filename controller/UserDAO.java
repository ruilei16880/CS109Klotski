package controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final String USER_FILE = "users.txt";  // 用户数据文件
    private static final List<String[]> users = new ArrayList<>();  // 内存缓存：[用户名, 密码]

    // 初始化时加载用户数据到内存
    static {
        loadUsers();
    }

    /**
     * 从文本文件加载用户数据（格式：username:password）
     */
    private static void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    users.add(new String[]{parts[0], parts[1]});
                }
            }
        } catch (IOException e) {
            // 文件不存在时忽略，首次运行会自动创建
        }
    }

    /**
     * 保存用户数据到文本文件（覆盖写入）
     */
    private static void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (String[] user : users) {
                bw.write(user[0] + ":" + user[1]);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证用户是否存在
     */
    public static boolean validateUser(String username, String password) {
        for (String[] user : users) {
            if (user[0].equals(username) && user[1].equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 注册用户（检查唯一性）
     */
    public static synchronized boolean registerUser(String username, String password) {
        // 检查用户名是否已存在
        for (String[] user : users) {
            if (user[0].equals(username)) {
                return false;
            }
        }

        // 添加到内存并保存到文件
        users.add(new String[]{username, password});
        saveUsers();
        return true;
    }
}