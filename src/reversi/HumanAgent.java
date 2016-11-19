/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author baDcoder
 */
public class HumanAgent extends Agent{

    public HumanAgent(String name) {
        super(name);
    }
    boolean isValid (int i,int j,int N)
    {
        if (i<0 || j<0 || i>=N || j>=N) return false;
        return true;
    }
    boolean iterate(int row,int col,int x,int y,int myrole,int opprole,int N,int[][] board)
    {
        if (isValid(row,col,N)==false) return false;
        if (board[row][col]==opprole) return iterate(row+x,col+y,x,y,myrole,opprole,N,board);
        if (board[row][col]==myrole) return true;
        return false;
    }
    
    void fill(int row,int col,int x,int y,int myrole,int[][] board)
    {
        if (board[row][col]==myrole) return ;
        board[row][col]=myrole;
        fill(row+x,col+y,x,y,myrole,board);
    }
    
    public  void showHint(Game game)
    {
        Reversi mygame = (Reversi) game;
        int myrole=role;
        int opprole=(role==1)?0:1;
        int dir_x[]={0,1,0,-1,1,-1,1,-1};
        int dir_y[]={1,0,-1,0,1,1,-1,-1};
        
        for(int i=0;i<mygame.N;i++)
        {
            for(int j=0;j<mygame.N;j++)
            {
                if (mygame.board[i][j]==-1) //empty cell
                {
                    //check for hint
                    for(int k=0;k<8;k++)
                    {
                        int x=dir_x[k];int y=dir_y[k];
                        int temp_row=i+x;//immediate neighbor
                        int temp_col=j+y;
                        
                        if (isValid(temp_row,temp_col,mygame.N) && mygame.board[temp_row][temp_col]==opprole)
                        {
                            if (iterate(temp_row,temp_col,x,y,myrole,opprole,mygame.N,mygame.board) == true) 
                            {
                                mygame.board[i][j]=2;//hint=2
                                break;
                            }
                        }
                    }
                } 
            }
        }
        mygame.myGui.repaint();
    }
    public void fillUp(int row,int col,Reversi mygame)
    {
        int myrole=role;
        int opprole=(role==1)?0:1;
        int dir_x[]={0,1,0,-1,1,-1,1,-1};
        int dir_y[]={1,0,-1,0,1,1,-1,-1};
        
        for(int k=0;k<8;k++)
        {
            int x=dir_x[k];int y=dir_y[k];
            int temp_row=row+x;//immediate neighbor
            int temp_col=col+y;

            if (isValid(temp_row,temp_col,mygame.N) && mygame.board[temp_row][temp_col]==opprole)
            {
                if (iterate(temp_row,temp_col,x,y,myrole,opprole,mygame.N,mygame.board) == true) 
                {
                    fill(temp_row,temp_col,x,y,myrole,mygame.board);
                }
            }
        }
        
    }
    boolean isMovePossible(int[][] board,int N)
    {
        for(int i=0;i<N;i++)
        {
            for(int j=0;j<N;j++)
                if(board[i][j]==2) return true;
        }
        return false;
    }
    @Override
    public void makeMove(Game game) {
        
        System.out.println("my turn");
        showHint(game);
        Reversi mygame = (Reversi) game;
        if (isMovePossible(mygame.board, mygame.N)==false)
        {
            JOptionPane.showMessageDialog(null,"NO AVAILABLE MOVE!");
            return;
        }
        try {
            mygame.moveLock.acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        //while(mygame.row==-1){}
        mygame.board[mygame.row][mygame.col]=role;
        mygame.last_row=mygame.row;
        mygame.last_col=mygame.col;
        fillUp(mygame.row,mygame.col,mygame);
    }
    
}
