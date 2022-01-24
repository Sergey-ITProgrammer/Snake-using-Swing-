package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.Random;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = SCREEN_HEIGHT * SCREEN_WIDTH / UNIT_SIZE;
    static final int DELAY = 75;

    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];

    int bodyParts = 5;
    int applesEaten = 0;
    int appleX = 0;
    int appleY = 0;

    char direction = 'R';

    boolean running = false;

    Timer timer;
    Random random;

    JButton newGameButton;

    GamePanel(GameFrame frame) {
        random = new Random();

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());


        newGameButton = new JButton("New Game");
        newGameButton.setVisible(false);
        newGameButton.setBackground(Color.RED);
        newGameButton.setMargin(new Insets(20,50,20,50));
        newGameButton.setFont(new Font("", Font.BOLD, 40));
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();

                new GameFrame();
            }
        });

        this.add(newGameButton);

        start();
    }

    public void start() {
        createApple();

        running = true;

        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics) {
        if (running) {
//            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//                graphics.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//                graphics.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//            }

            graphics.setColor(Color.RED);

            graphics.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    graphics.setColor(Color.GREEN);
                    graphics.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    graphics.setColor(new Color(94, 130, 60));
//                    graphics.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    graphics.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            graphics.setColor(Color.RED);
            graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));

            FontMetrics metrics = getFontMetrics(graphics.getFont());

            graphics.drawString("Score: " + applesEaten,
                    (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                    graphics.getFont().getSize());
        } else {
            gameOver(graphics);
        }
    }

    public void createApple() {
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;

        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];

            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            createApple();
        }
    }

    public void checkCollision() {
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        if (x[0] < 0) {
            x[0] = SCREEN_WIDTH;
        }

        if (x[0] > SCREEN_WIDTH) {
            x[0] = 0;
        }

        if (y[0] < 0) {
            y[0] = SCREEN_HEIGHT;
        }

        if (y[0] > SCREEN_HEIGHT) {
            y[0] = 0;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 75));

        FontMetrics metrics1 = getFontMetrics(graphics.getFont());

        graphics.drawString("Game Over!",
                (SCREEN_WIDTH - metrics1.stringWidth("Game Over!")) / 2,
                SCREEN_HEIGHT / 2);

        graphics.setColor(Color.RED);
        graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));

        FontMetrics metrics2 = getFontMetrics(graphics.getFont());

        graphics.drawString("Score: " + applesEaten,
                (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2,
                SCREEN_HEIGHT - graphics.getFont().getSize());

        Graphics2D g2 = (Graphics2D) graphics;

        newGameButton.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollision();

        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
