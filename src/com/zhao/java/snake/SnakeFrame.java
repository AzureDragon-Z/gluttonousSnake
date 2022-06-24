package com.zhao.java.snake;

import javax.swing.*;
import java.awt.*;

/**
 * @author 赵同学
 */
public class SnakeFrame extends JFrame {
    public SnakeFrame(){
        //将画板添加到窗体中

        add(new SnakePanel());
        //设置窗体的标题
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/logo.png"));
        this.setIconImage(image);
        setTitle("贪吃的小青龙");
        //设置窗体大小
        int width = 910;
        int height = 650;
        setSize(width,height);

        int screen_with = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
        //设置窗体的位置
        setLocation((screen_with-width)/2, (screen_height-height)/2);
        //设置关闭方式
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //设置窗体大小不可以改变
        this.setResizable(false);
        setVisible(true);

    }

    public static void main(String[] args) {
        new SnakeFrame();
    }
}
