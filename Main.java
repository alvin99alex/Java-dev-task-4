import javax.swing.*;

public class Main {
    static int boardwidth = 600;
    static int boardHeight = 600;

    public static void main(String[] args) {

        JFrame j = new JFrame();
        j.setSize(boardwidth ,boardHeight );
        j.setTitle("Brick Game");
        j.setVisible(true);
        j.setResizable(false);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePlay gp = new GamePlay(boardwidth,boardHeight);
        j.add(gp);
        j.pack();
        gp.requestFocus();

    }
}