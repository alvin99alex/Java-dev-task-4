import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class GamePlay extends JPanel implements ActionListener, KeyListener {

    private boolean leftKeyPressed;
    private boolean rightKeyPressed;
    private boolean gameOver;

    class Bricks{
        static int width = 60;
        static int height = 25;
        int x;
        int y;

        Bricks(int x,int y){
            this.x = x;
            this.y = y;
        }
    }

    class Ball{
        int x;
        int y;
        static int radius = 15;

        Ball(int x,int y){
            this.x = x;
            this.y = y;
        }
    }
    class Paddle{
        int Paddle_width;
        int Paddle_height;
        int Paddle_X;
        int Paddle_Y;

        Paddle(int x,int y,int width,int height){
            Paddle_X = x;
            Paddle_Y = y;
            Paddle_height = height;
            Paddle_width = width;
        }

    }
    int boardwidth ;
    int boardheight;

    Paddle paddle;
    ArrayList<Bricks> bricksList;
    Ball ball;
    Timer gameLoop;

    int score;
    int ballvelocityX;
    int ballvelocityY;
    GamePlay(int width,int height){
        boardwidth = width;
        boardheight = height;
        score = 0;
        setPreferredSize(new Dimension(boardwidth,boardheight));
        setBackground(Color.black);


        paddle = new Paddle(300,550,60,10);
        ball = new Ball(320,500);

        bricksList = new ArrayList<>();
        fillBrick();

        ballvelocityX = -10;
        ballvelocityY = -10;
        addKeyListener(this);
        setFocusable(true);

        gameLoop = new Timer(100,this);
        gameLoop.start();

    }

    private void fillBrick() {
        int fy = 100;
        for(int j=0;j<3;j++){
            int fx = 100;

            for(int i=0;i<6;i++){
                bricksList.add(new Bricks(fx,fy));
                fx += Bricks.width + 5;
            }
            fy += Bricks.height + 5;
        }
//        Random rd = new Random();
//        for(int i=0;i<18;i++){
//            int fx = rd.nextInt(400);
//            int fy = rd.nextInt(400);
//            bricksList.add(new Bricks(fx,fy));
//        }


    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){

        //paddle
        g.setColor(Color.green);
        g.fillRect(paddle.Paddle_X, paddle.Paddle_Y, paddle.Paddle_width, paddle.Paddle_height);

        g.setColor(Color.WHITE);
        g.fillOval(ball.x - ball.radius, ball.y - ball.radius, ball.radius, ball.radius);

        getDrawBrick(g);

        g.setFont(new Font("Arial",Font.PLAIN,16));
        if(gameOver && bricksList.isEmpty()){
            gameLoop.stop();
            g.setColor(Color.green);
            g.drawString(" You WON !!!! Score :" + String.valueOf(score) ,  9,25);
        }
        else if(gameOver){
            gameLoop.stop();
            g.setColor(Color.red);
            g.drawString("Game Over :" + String.valueOf(score) , 9,25);

        }
        else{
            g.setColor(Color.green);
            g.drawString("Score :" + String.valueOf(score) ,  9,25);
        }


    }

    private void getDrawBrick(Graphics g) {
        g.setColor(Color.BLUE);
        for(int i=0;i<bricksList.size();i++){
            Bricks b = bricksList.get(i);
            g.fillRect(b.x,b.y,b.width,b.height);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    private void move() {

        if (leftKeyPressed && paddle.Paddle_X + WIDTH > 0) {
            paddle.Paddle_X -= 20;
        }
        if (rightKeyPressed && paddle.Paddle_X + paddle.Paddle_width < boardwidth) {
            paddle.Paddle_X += 20;
        }

        //Ball moving
        movingBall();

    }

    private void movingBall() {
        if(bricksList.isEmpty()) gameOver = true;
        // When stucks with brick
        for (int i = 0; i < bricksList.size(); i++) {
            Bricks bk = bricksList.get(i);
            if ((ball.y + ball.radius >= bk.y && ball.y - ball.radius <= bk.y + Bricks.height) &&
                    (ball.x + ball.radius >= bk.x && ball.x - ball.radius <= bk.x + Bricks.width)) {
                bricksList.remove(i);
                score += 10;
                ballvelocityY *= -1;
                break;
            }
        }

        if(ball.x == 10 || ball.x == boardwidth){
            ballvelocityX *= -1;
        }
        if(ball.y ==    10){
            ballvelocityY *= -1;
        }
        if ((ball.y >= paddle.Paddle_Y - ball.radius && ball.y <= paddle.Paddle_Y + paddle.Paddle_height) &&
                (ball.x + ball.radius >= paddle.Paddle_X && ball.x - ball.radius <= paddle.Paddle_X + paddle.Paddle_width)) {
            ballvelocityY *= -1; // Change Y direction to make the ball bounce back
        }
        if(ball.y == boardheight) {
            gameOver = true;
        }
        ball.x += ballvelocityX;
        ball.y += ballvelocityY;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        switch (keycode){
            case KeyEvent.VK_LEFT:
                leftKeyPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                rightKeyPressed = true;
                break;
            default:


        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keycode = e.getKeyCode();
        switch (keycode){
            case KeyEvent.VK_LEFT:
                leftKeyPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
                rightKeyPressed = false;
                break;
            case KeyEvent.VK_ENTER:
                if(gameOver){
                    restartGame();
                }
                break;
            default:
        }
    }

    private void restartGame() {
        gameOver = false;
        score = 0;

        paddle.Paddle_X = 300;
        paddle.Paddle_Y = 550;
        ball.x = 320;
        ball.y = 500;
       ballvelocityX = -10;
       ballvelocityY = -10;
        bricksList.clear();
        fillBrick();

        gameLoop.restart();
    }

}
