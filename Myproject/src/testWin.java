import javax.swing.*;
import java.awt.*;

public class testWin extends JFrame {
    public testWin(){
        add(new NewPanel());
    }

    public static void main(String[] args) {
        testWin frame=new testWin();
        frame.setTitle("TestPaintComponent");
        frame.setSize(1000,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class NewPanel extends JPanel{
    protected void paintComponent(Graphics g){
        this.setLayout(null);
        JButton jButton = new JButton("1234");
        JButton t = new JButton("2342");
        this.add(jButton);
        this.add(t);
        jButton.setBounds(50, 100, 60, 20);
        t.setBounds(150, 100, 60, 20);

        super.paintComponent(g);
        g.drawLine(0,0,50,50);
        g.drawRect(100, 100, 20, 20);
        g.drawString("1",100,100+20);
    }
}

