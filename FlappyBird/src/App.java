import javax.swing.*;


public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth=360;
        int boardHight=640;

        JFrame frame = new JFrame("Flappy Bird");
        // frame.setVisible
        frame.setSize(boardWidth,boardHight);                    //size of the window and x button
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyBird flappybird=new FlappyBird();
        frame.add(flappybird);
        frame.pack();                //bec if not then it will take into account the dimension of the title board
        flappybird.requestFocus();
         
        frame.setVisible(true);
    }
}
