/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author baDcoder
 */


class event_handler implements MouseListener{
    Reversi myGame;
    public event_handler(Reversi game) {
        myGame=game;
    }
    
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
         int x=e.getX();
         int y=e.getY();
        // System.out.println(x+" "+y);

         x-=Gui.init_x;
         y-=Gui.init_y;
         //System.out.println(x+" "+y);
         if (myGame.row==-1 && x>=0 && x<=Gui.board_size && y>=0 && y<=Gui.board_size)
         {
             int col=x/50;
             int row=y/50;
            // System.out.println(myGame.row+" "+myGame.col);   
             if (myGame.board[row][col]==2)
             {
                myGame.row=row;
                myGame.col=col;
                myGame.moveLock.release();
             }
         }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
}

public class Gui extends JPanel{

    /**
     * @param args the command line arguments
     */
    
    int cell_size;
    int N;
    static int board_size;
    static int init_x;
    static int init_y;
    Image GreyBall,RedBall;
    Reversi game;
    JLabel label=new JLabel();
    JLabel label2=new JLabel();
    JLabel label3=new JLabel();
    JLabel label4=new JLabel();
    
    public Gui(int n,Reversi g)
    {
        setLayout(null);
        game=g;
        cell_size=50;
        N=n;
        board_size=cell_size*N;
        init_x=100;
        init_y=100;
        try {
            GreyBall=ImageIO.read(new File("resources\\grey.png"));
            RedBall=ImageIO.read(new File("resources\\image_1.png"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        label2.setBounds(10,10,200,50);
        //label2.setFont(label.getFont().deriveFont(30f));
        label2.setVisible(true);
        label2.setBackground(Color.red); 
        label2.setSize(200,50);
        
        label2.setText(game.agent[0].name+" is BLACK,\n"+game.agent[1].name+" is GREY");
        add(label2);
        label.setBounds(200,50, 200, 50);
        label.setFont(label.getFont().deriveFont(30f));
        label.setVisible(true);
        label.setBackground(Color.red);
        label.setSize(200,50);
        add(label);
        
        label3.setBounds(10,board_size+init_y*2,50,20);
        label3.setSize(200,50);
        label3.setForeground(Color.BLUE);
        label3.setFont(label3.getFont().deriveFont(20f));
        
        add(label3);
        label4.setBounds(10,board_size+init_y*2 + 20,50,20);
        label4.setSize(200,50);
        label4.setForeground(Color.red);
        label4.setFont(label4.getFont().deriveFont(20f));
        
        add(label4);
    }
    public void paintComponent (Graphics g){
        super.paintComponent(g);

        g.setColor(Color.red);
        g.fillRect(init_x,init_y, board_size, board_size);
        boolean flag=true;
        for(int i=init_x;i<=board_size+init_x-cell_size;i+=cell_size*2)
        {
            for(int j=init_y;j<=board_size+init_y-cell_size;j+=cell_size*2)
            {
                g.clearRect(i, j, cell_size, cell_size);
            }
        }
        
        for(int i=init_x+cell_size;i<=board_size+cell_size;i+=cell_size*2)
        {
            for(int j=init_y+cell_size;j<=board_size+cell_size;j+=cell_size*2)
            {
                g.clearRect(i, j, cell_size, cell_size);
            }
        }
        
        int cnt1=0,cnt2=0;
        for(int i=0;i<N;i++)
        {
            for(int j=0;j<N;j++)
            {
                int x=init_x+j*cell_size;
                int y=init_y+i*cell_size;
                if (game.board[i][j]==0) //player 1 red
                {
                    cnt1++;
                    g.drawImage(RedBall,x+10,y+12,30,30, null);
                }
                else if (game.board[i][j]==1) 
                {
                    cnt2++;
                    g.drawImage(GreyBall,x+10,y+12,30,30, null);
                }
                else if (game.board[i][j]==2) //hint
                {
                   
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(x, y, cell_size, cell_size);
                    g.setColor(Color.BLACK);
                    g.draw3DRect(x, y, cell_size, cell_size,false);
                }
            }
        }
        if (game.last_row!=-1)
        {
            int x=init_x+game.last_col*cell_size;
            int y=init_y+game.last_row*cell_size;
            g.setColor(Color.blue);
            g.drawOval(x, y, cell_size, cell_size);
        }
        label3.setText("BLACK: "+cnt1);
        label4.setText("GREY: "+cnt2);
    }
}