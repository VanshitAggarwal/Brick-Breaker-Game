import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
//
public class BrickBreakerGame2 extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 1550; // Increased display width
    private static final int HEIGHT = 800;
    private static final int PADDLE_WIDTH = 120;
    private static final int PADDLE_HEIGHT = 20;
    private static final int BALL_DIAMETER = 20;
    private static final int BRICK_WIDTH = 90; // Increased brick width
    private static final int BRICK_HEIGHT = 30;
    private static final int PADDLE_SPEED = 25;
    private static final int BALL_SPEED = 5;

    private int paddleX;
    private int ballX;
    private int ballY;
    private int ballXDir;
    private int ballYDir;
    private int score;
    private int level;
    private ArrayList<Brick> bricks;
    private Timer timer;
    private boolean gameOver;

    public BrickBreakerGame2() {
        initializeGame();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(5, this);
        timer.start();
    }

    private void initializeGame() {
        paddleX = (WIDTH - PADDLE_WIDTH) / 2;
        ballX = WIDTH / 2;
        ballY = HEIGHT - PADDLE_HEIGHT - BALL_DIAMETER;
        ballXDir = BALL_SPEED;
        ballYDir = -BALL_SPEED;
        score = 0;
        level = 1;
        bricks = new ArrayList<>();
        createBricks();
        gameOver = false;
    }

    private void createBricks() {
        int numRows = 6; // Increased number of rows
        int numBricksPerRow = 16; // Increased number of bricks per row
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numBricksPerRow; j++) {
                int brickX = j * (BRICK_WIDTH + 5);
                int brickY = i * (BRICK_HEIGHT + 5);
                bricks.add(new Brick(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT));
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        moveBall();
        checkCollisions();
        repaint();
    }

    private void moveBall() {
        ballX += ballXDir;
        ballY += ballYDir;

        if (ballX <= 0 || ballX >= WIDTH - BALL_DIAMETER) {
            ballXDir = -ballXDir;
        }

        if (ballY <= 0) {
            ballYDir = -ballYDir;
        }

        if (ballY >= HEIGHT - BALL_DIAMETER) {
            gameOver = true;
            timer.stop();
        }
    }

    private void checkCollisions() {
        Rectangle ballRect = new Rectangle(ballX, ballY, BALL_DIAMETER, BALL_DIAMETER);
        Rectangle paddleRect = new Rectangle(paddleX, HEIGHT - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);

        if (ballRect.intersects(paddleRect)) {
            ballYDir = -ballYDir;
        }

        for (Brick brick : bricks) {
            if (brick.isVisible() && ballRect.intersects(brick.getRect())) {
                brick.setVisible(false);
                score += 10;
                ballYDir = -ballYDir;
            }
        }

        if (bricks.stream().noneMatch(Brick::isVisible)) {
            level++;
            initializeGame();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        if (!gameOver) {
            g.setColor(Color.GREEN);
            g.fillRect(paddleX, HEIGHT - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);

            g.setColor(Color.WHITE);
            g.fillOval(ballX, ballY, BALL_DIAMETER, BALL_DIAMETER);

            for (Brick brick : bricks) {
                if (brick.isVisible()) {
                    g.setColor(Color.RED);
                    g.fillRect(brick.getX(), brick.getY(), BRICK_WIDTH, BRICK_HEIGHT);
                }
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Level: " + level, 30, 30);
            g.drawString("Score: " + score, 30, 60);
        } else {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over", 720, 300);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press Enter to Restart", 720, 350);
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT && paddleX > 0) {
            paddleX -= PADDLE_SPEED;
        }

        if (key == KeyEvent.VK_RIGHT && paddleX < WIDTH - PADDLE_WIDTH) {
            paddleX += PADDLE_SPEED;
        }

        if (key == KeyEvent.VK_ENTER) {
            if (gameOver) {
                initializeGame();
                timer.start();
            }
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Brick Breaker Game");
        BrickBreakerGame2 game = new BrickBreakerGame2();
        frame.add(game);
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private class Brick {
        private int x;
        private int y;
        private int width;
        private int height;
        private boolean visible;

        public Brick(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.visible = true;
        }

        public Rectangle getRect() {
            return new Rectangle(x, y, width, height);
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}