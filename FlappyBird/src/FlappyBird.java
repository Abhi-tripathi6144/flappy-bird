import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;           //random pipr positions
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener{
    int boardWidth=360;
    int boardHight=640;

    //Images
    Image backgroundImag;
    Image birdImag;
    Image toppipeImag;
    Image bottompipeImag;

    //Bird
    int birdx=boardWidth/8;
    int birdy=boardHight/2;
    int birdWidth=34;
    int birdHight=24;

    class Bird{
        int x=birdx;
        int y=birdy;
        int width=birdWidth;
        int hight=birdHight;
        Image img;

        Bird(Image img){
           this.img=img;
        }
    }

    //Pipes
    int pipex=boardWidth;    //makes the pipe on the right side of our screen
    int pipey=0;
    int pipewidth=64;          //scaled by 1/6
    int pipeHeight=512;

    class Pipe{
        int x=pipex;
        int y=pipey;
        int width=pipewidth;
        int height=pipeHeight;
        Image img;
        boolean passed=false;

        Pipe(Image img){
            this.img=img;
        }
    }
    //game logic 
    Bird bird;
    int velocityx=-4;  //moves pipes to the left(simulates the bird moving right)
    int velocityy=0;  //bird only moves up and down
    int gravity=1;

    ArrayList<Pipe> pipes;     //stores many pipes
    Random random = new Random();

    Timer gameloop;
    Timer placePipesTimer;
    boolean gameOver = false;
    double score=0;

    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth,boardHight));
        // setBackground(Color.blue);
        setFocusable(true);  //key events 
        addKeyListener(this);

        //load images
        backgroundImag = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImag = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        toppipeImag = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottompipeImag = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        //bird
        bird = new Bird(birdImag);
        pipes = new ArrayList<Pipe>();

        //place pipes timer
        placePipesTimer = new Timer(1500, new ActionListener() {      //new pipe add every 1.5 sec
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();                                          
            }
        });
        placePipesTimer.start();

        //game timer
        gameloop = new Timer(1000/60, this);   // 60 frames per sec
        gameloop.start();       //starts the loop ,draws 60 frames in 1 sec


    }
    
    public void placePipes(){
        int randomPipey = (int) (pipey - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int opeaningSpace = boardHight/4;

        Pipe topPipe=new Pipe(toppipeImag);
        topPipe.y=randomPipey;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottompipeImag);
        bottomPipe.y=topPipe.y + pipeHeight + opeaningSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);    //super refers to japnel i.e parent class
        draw(g);
    }

    public void draw(Graphics g){
        //backgrond
        g.drawImage(backgroundImag, 0, 0, boardWidth,boardHight , null); //top left corner 00

        //bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.hight, null);

        //pipe
        for(int i=0;i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        //score
        g.setColor(Color.black);
        g.setFont( new Font("Areal",Font.PLAIN, 32));
        if(gameOver){
            g.drawString("Game Over : "+String.valueOf((int)score),10,35);
        }else{
            g.drawString(String.valueOf((int)score), 10, 35);
        }
    }

    public void move(){
        //bird
        velocityy+=gravity;
        bird.y+=velocityy;
        bird.y=Math.max(bird.y,0);

        //pipe
        for(int i=0;i<pipes.size();i++){
            Pipe pipe=pipes.get(i);
            pipe.x+=velocityx;        //each pipe moves -4 to the left per frame
            
            if(!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score +=0.5;      //0.5 for one pipe above & below
            }
            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }

        if(bird.y > boardHight){
            gameOver=true;
        }
        
    }

    public boolean collision(Bird a,Pipe b){
        return a.x<b.x+b.width &&
               a.x+a.width>b.x &&
               a.y<b.y+b.height &&
               a.y+a.hight>b.y;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placePipesTimer.stop();
            gameloop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()== KeyEvent.VK_SPACE){
            velocityy=-10;
        }
        if(gameOver){
            //restart the game by resetting the conditions
            bird.y= birdy;
            velocityy=0;
            pipes.clear();
            score = 0;
            gameOver=false;
            gameloop.start();
            placePipesTimer.start();

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}


    @Override
    public void keyReleased(KeyEvent e) {}
}
