/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi;

import java.awt.Color;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author baDcoder
 */
public class Reversi extends Game {
    
    public int[][] board;
    int N;
    int row,col;
    Gui myGui;
    int last_row,last_col;
    int GameType;
    Semaphore moveLock=new Semaphore(0);
    
        
    public Reversi(Agent a, Agent b,int n,int type) {
        super(a, b);
        a.setRole(0); //player1
        b.setRole(1); //player2
        GameType=type;
        name="Reversi Game";
        row=col=-1;
        N=n;
        
        board=new int[N+1][N+1];
        for(int i=0;i<N;i++)
        {
            for(int j=0;j<N;j++)
                board[i][j]=-1;
        }
        
        int x=N/2;x--;
        board[x][x]=0;
        board[x][x+1]=1;
        board[x+1][x]=1;
        board[x+1][x+1]=0;
        //board[x+2][x+2]=1;
        last_row=last_col=-1;
        
        myGui=new Gui(N,this);
        myGui.addMouseListener(new event_handler(this));
        
        JFrame f=new JFrame();
        f.getContentPane().add(myGui);
        f.setSize(myGui.board_size+myGui.init_x*2,myGui.board_size+myGui.init_y*3);
       // f.setLocationRelativeTo(null);
        f.setBackground(Color.RED);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        f.setVisible(true);
    }

    @Override
    boolean isFinished() {
        
        boolean moveAgent1=false;
        if (GameType==0 || GameType==2)
        {
            HumanAgent agent1=(HumanAgent) agent[0];
            agent1.showHint(this);
            for(int i=0;i<N;i++)
            {
                for(int j=0;j<N;j++)
                    if (board[i][j]==2) moveAgent1=true;
            }

            undoHint();
        }
        else 
        {
            AlphaBetaAgent agent1=(AlphaBetaAgent) agent[0];
            agent1.showHint(this);
            for(int i=0;i<N;i++)
            {
                for(int j=0;j<N;j++)
                    if (board[i][j]==2) moveAgent1=true;
            }

            undoHint();
        }
        
        boolean moveAgent2=false;
        if (GameType==2)
        {
            HumanAgent agent2=(HumanAgent) agent[1];
            agent2.showHint(this);
            for(int i=0;i<N;i++)
            {
                for(int j=0;j<N;j++)
                    if (board[i][j]==2) moveAgent1=true;
            }

            undoHint();
        }
        else
        {
             AlphaBetaAgent agent2=(AlphaBetaAgent) agent[1];
         
            agent2.showHint(this);
            for(int i=0;i<N;i++)
            {
                for(int j=0;j<N;j++)
                    if (board[i][j]==2) moveAgent2=true;
            }
            undoHint();
        }
       
        if (moveAgent1 || moveAgent2) return false;
        
        int cnt1=0,cnt2=0;
        for(int i=0;i<N;i++)
        {
            for(int j=0;j<N;j++)
            {
                if (agent[0].role==board[i][j]) cnt1++;
                else if (agent[1].role==board[i][j]) cnt2++;
            }
        }
        if (cnt1>cnt2)
            winner=agent[0];
        else if(cnt1<cnt2)
            winner=agent[1];
        else winner=null;
        return true;
    }

    @Override
    void initialize(boolean fromFile) {
    }

    void undoHint()
    {
        for(int i=0;i<N;i++)
        {
            for(int j=0;j<N;j++)
            {
                if (board[i][j]==2) board[i][j]=-1;
            }
        }
        myGui.repaint();
    }
    @Override
    void showGameState() {
        System.out.println(row+" "+col);
        myGui.repaint();
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
               ex.printStackTrace();
        }
        row=col=-1;
        undoHint();
    }

    @Override
    void updateMessage(String msg) {
        myGui.label.setText(msg);
        myGui.repaint();
    }
    
}
