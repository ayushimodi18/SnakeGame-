import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

import java.util.Random;
import javax.swing.*;


public class SnakeGame extends JPanel implements ActionListener , KeyListener{
    private class Tile{

        int x ;
        int y;

        Tile(int x, int y){
            this.x =x;
            this.y = y;
        }
    
        
    }
    int boardHeight;
    int boardWidth;

    int tilesize = 25;

    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    Tile food;

    Random random ;

    // game logic

    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;


    SnakeGame(int boardWidth, int  boardHeight){
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);

        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);


        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);

        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        // Grid
        g.setColor(Color.DARK_GRAY);
        for(int i =0; i < boardWidth/tilesize; i++){
            g.drawLine(i*tilesize, 0, i *tilesize, boardHeight);
            g.drawLine(0, i* tilesize, boardWidth, i * tilesize);
        }

        // food

        g.setColor(Color.RED);
        g.fillRect(tilesize * food.x, tilesize * food.y, tilesize, tilesize);

        g.setColor(Color.GREEN);
        g.fillRect(tilesize*snakeHead.x, tilesize*snakeHead.y, tilesize, tilesize);

        // snake body

        for(int i =0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x * tilesize, snakePart.y * tilesize, tilesize, tilesize); 
        }

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if(gameOver){
            g.setColor(Color.RED);
            g.drawString("Game Over"+ String.valueOf(snakeBody.size()), tilesize -16 , tilesize);
        }
        else{
            g.drawString("Scores" + String.valueOf(snakeBody.size()) , tilesize -16, tilesize);
        }
    }

    public void placeFood(){
        food.x = random.nextInt(boardWidth/tilesize);
        food.y = random.nextInt(boardHeight/tilesize);

    }

    public boolean collision(Tile t1, Tile t2){
        return t1.x == t2.x && t1.y == t2.y;
    }

    public void move(){
        if(collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        for(int i = snakeBody.size()-1 ; i >= 0 ; i--){
            Tile snakePart = snakeBody.get(i);

            if(i == 0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else{
                Tile prev = snakeBody.get(i-1);
                snakePart.x = prev.x;
                snakePart.y = prev.y;
            }
        }
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        for(int i =0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            if(collision(snakePart, snakeHead)){
                gameOver = true;
            }
        }

        if(snakeHead.x * tilesize < 0 || snakeHead.x * tilesize > boardWidth || 
            snakeHead.y * tilesize < 0 || snakeHead.y * tilesize > boardHeight){
                gameOver = true;
            }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() ==  KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        }
        else if( e.getKeyCode() ==  KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
        }
        else if( e.getKeyCode() ==  KeyEvent.VK_LEFT && velocityX != 1){
            velocityX =-1;
            velocityY =0;
        }
        else if( e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
        }
    }

    // do not need this

    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
}
