package com.zhao.java.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SnakePanel extends JPanel {

    private int score;
    private int length;
    private String direction;
    // 存储所有蛇身上每个节点的坐标
    private int[][] position;
    // 定义判断游戏是否开始，是否结束的标志
    private boolean isStart;
    private boolean isEnd;

    private int food_x;
    private int food_y;

    private static final int SIMPLE = 0;
    private static final int COMMON = 1;
    private static final int DIFFICULT = 2;
    private static final int VERY_DIFFICULT = 3;
    private final Timer timer;
    private int delay = 200;
    private JLabel jLabel = new JLabel("游戏难度:");
    private final JComboBox jComboBox=new JComboBox();    //创建JComboBox
    //预定义资源

    ImageIcon body = new ImageIcon(getClass().getClassLoader().getResource("images/body.png"));
    ImageIcon down = new ImageIcon(getClass().getClassLoader().getResource("images/down.png"));
    ImageIcon food = new ImageIcon(getClass().getClassLoader().getResource("images/food.png"));
    ImageIcon left = new ImageIcon(getClass().getClassLoader().getResource("images/left.png"));
    ImageIcon right = new ImageIcon(getClass().getClassLoader().getResource("images/right.png"));
    ImageIcon title = new ImageIcon(getClass().getClassLoader().getResource("images/title.jpg"));
    ImageIcon up = new ImageIcon(getClass().getClassLoader().getResource("images/up.png"));

    public SnakePanel() {

        init();
        jComboBox.addItem("简单");
        jComboBox.addItem("一般");
        jComboBox.addItem("困难");
        jComboBox.addItem("极其难");
        this.setLayout(null);
        this.add(jLabel);
        this.add(jComboBox);
        jLabel.setFont(new Font("黑体", Font.BOLD, 16));
        jLabel.setForeground(Color.white);

        jLabel.setBounds(200, 20,80,30 );
        jComboBox.setBounds(280,20,70,30);
        jComboBox.setBorder(BorderFactory.createLineBorder(new Color(35,83, 129)));
        jComboBox.setFont(new Font("黑体", Font.BOLD, 12));
        jComboBox.setForeground(Color.WHITE);
        jComboBox.setBackground(new Color(35,83, 129));
        jComboBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jComboBox.setFocusable(false); // 当选择后让其自动失去焦点
            }
        });

        //让当前面板可以获取焦点
        this.setFocusable(true);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                setFocusable(true);
                // 判断游戏是否结束。若结束，按下任意键 即重新开始回到初始化页面
                if (isEnd) {
                    init();
                    isEnd = false;
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (isEnd) {
                        init();
                    } else {
                        isStart = !isStart;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP  && !"D".equals(direction)) {
                    direction = "U";
                } else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN && !"U".equals(direction)) {
                    direction = "D";
                } else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT && !"R".equals(direction)) {
                    direction = "L";
                } else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT && !"L".equals(direction)) {
                    direction = "R";
                }
            }
        });

        timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // 判断用户选择的级别
                int selectedIndex = jComboBox.getSelectedIndex();
                if (selectedIndex == SIMPLE) {
                    delay = 200;
                } else if (selectedIndex == COMMON) {
                    delay = 100;
                } else if (selectedIndex == DIFFICULT) {
                    delay = 50;
                } else if (selectedIndex == VERY_DIFFICULT) {
                    delay = 20;
                }
//                System.out.println("selectedIndex: " + delay);
                timer.setDelay(delay);
                //1.判断游戏的状态是暂停，结束还是开始
                if (isStart && !isEnd) {
                    //如果蛇超越界限，直接失败
                    if (position[0][0] > 850 || position[0][0] < 25
                            || position[0][1]< 75 || position[0][1] > 550) {
                        isEnd = true;
                        return;
                    }
                    for (int i = length; i > 0; i--) {
                        position[i][0] = position[i-1][0];
                        position[i][1] = position[i-1][1];
                    }
                    //2.移动蛇头,蛇身
                    // 蛇身和蛇头是有先后顺序的，必须要先画蛇身，因为蛇身的第一节蛇身是对应蛇头更新前的位置，所以要先赋值蛇身后，再更新蛇头位置
                    //蛇身
                    //蛇头
                    if ("R".equals(direction)) {
                        position[0][0] += 25;
//                        if (position[0][0] > 850) {
//                            position[0][0] = 25;
//                        }
                    } else if("L".equals(direction)) {
                        position[0][0] -= 25;
//                        if (position[0][0] < 25) {
//                            position[0][0] = 850;
//                        }
                    } else if("U".equals(direction)) {
                        position[0][1] -= 25;
//                        if (position[0][1] < 75) {
//                            position[0][1] = 550;
//                        }
                    } else if("D".equals(direction)) {
                        position[0][1] += 25;
//                        if (position[0][1] > 550) {
//                            position[0][1] = 75;
//                        }
                    }
                    //3.吃食物
                    if (position[0][0] == food_x && position[0][1] == food_y) {
                        RandomFood();
                        score += 1;
                        length += 1;
                        position[length][0] = position[length-1][0];
                        position[length][1] = position[length-1][1];
                    }
                    //4.游戏失败的处理
                    for (int i = 1; i < length; i++) {
                        if (position[0][0] == position[i][0] && position[0][1] == position[i][1]) {
                            isEnd = true;
                            break;
                        }
                    }
                }
                //重画
                repaint();
            }
        });
        timer.start();
    }
    public void init() {
        //初始化蛇的长度以及所得分数
        this.score = 0;
        this.length = 3;
        //设置蛇的默认方向 为向右
        this.direction = "R";

        position = new int[700][700];
        //初始化头部的位置以及两个蛇身的位置
        position[0][0] = 125;
        position[0][1] = 100;
        //蛇身位置
        position[1][0] = 100;
        position[1][1] = 100;
        position[2][0] = 75;
        position[2][1] = 100;
        //设置初始的状态
        isStart = false;
        isEnd = false;
        //随机化食物位置
        this.RandomFood();
    }

    protected void RandomFood() {
        //定义随机的食物坐标
        Random random = new Random();
        this.food_x = random.nextInt(34)*25 + 25;
        this.food_y = random.nextInt(20)*25 + 75;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // 对JPanel类中painComponent的继承，并继续使用该函数对面板进行及时的刷新
        super.paintComponent(g);
        // 将标题画出来
        title.paintIcon(this,g, 25,10);
        //设置画笔颜色
        g.setColor(Color.white);
        //画出背景
        g.fillRect(25,75,850,500);
        //画网格
        //将画笔颜色设置为黑色
        g.setColor(Color.black);
        //画线
        //画横线
        for (int i = 0; i <= 20; i++) {
            g.drawLine(25,75 + i * 25,875,75+i*25);
        }
        //画竖线
        for (int i = 0; i < 35; i++) {
            g.drawLine(25 + i * 25 , 75, 25+i*25, 575);
        }

        //画分数以及长度
        g.setColor(Color.white);
        //设置字体为Times Roman 加粗，大小为15像素
        g.setFont(new Font("Times New Roman" , Font.PLAIN,14));
        //在面板上画出对应的字符除按，指定对应的位置
        g.drawString("Score:  " + this.score, 700, 30);
        g.drawString("Length: " + this.length, 700, 50);
        //画出对应的日期
        SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = date.format(new Date());
        g.drawString(str, 50, 40);

        //画出对应的蛇头,蛇身
        if ("R".equals(direction)) {
            right.paintIcon(this, g, position[0][0], position[0][1]);
        } else if ("L".equals(direction)) {
            left.paintIcon(this, g, position[0][0], position[0][1]);
        } else if ("U".equals(direction)) {
            up.paintIcon(this, g, position[0][0], position[0][1]);
        } else if ("D".equals(direction)) {
            down.paintIcon(this, g, position[0][0], position[0][1]);
        }
        // 画蛇身
        for (int i = 1; i < length; i++) {
            body.paintIcon(this, g, position[i][0], position[i][1]);
        }

        //画随机的食物
        food.paintIcon(this, g, this.food_x, this.food_y);

        // 判断是否处于开始状态
        if (!isStart) {
            g.setFont(new Font("Times New Roman", Font.BOLD, 25));
            g.setColor(Color.black);
            g.drawString("Please press the space bar to start/pause the game!", 200, 300);
        }

        if (isEnd) {
            g.setFont(new Font("Times New Roman", Font.BOLD, 25));
            g.setColor(Color.red);
            g.drawString("Game over! Please enter any key to restart game!", 200, 300);
        }

    }
}
