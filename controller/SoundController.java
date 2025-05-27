package controller;
import javax.sound.sampled.*;
import java.io.File;

public class SoundController {
    private static Clip backgroundMusic;

    // 初始化音效系统（在游戏启动时调用）
    public static void init() {
        try {
            // 加载背景音乐
            AudioInputStream bgAis = AudioSystem.getAudioInputStream(
                    new File("sounds/background.wav"));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(bgAis);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println("背景音乐加载失败: " + e.getMessage());
        }
    }

    // 播放移动音效（水滴声）
    public static void playMoveSound() {
        playSound("sounds/move.wav");
    }

    // 播放按钮点击音效
    public static void playButtonSound() {
        playSound("sounds/button.wav");
    }

    // 播放胜利音效（新增）
    public static void playVictorySound() {
        playSound("sounds/victory.wav");
    }

    // 通用音效播放方法
    private static void playSound(String path) {
        new Thread(() -> {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path));
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
            } catch (Exception e) {
                System.err.println("音效播放失败: " + path + " - " + e.getMessage());
            }
        }).start();
    }
}