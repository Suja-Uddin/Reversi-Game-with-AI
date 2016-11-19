/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author baDcoder
 */
public class AlphaBetaAgent extends Agent {

    ArrayList values = new ArrayList();
    ArrayList RowCol = new ArrayList();

    int MAX_DEPTH;

    public AlphaBetaAgent(String name) {
        super(name);
        this.MAX_DEPTH = 8;
    }

    void memset(int[][] board, int N, int val) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = val;
            }
        }
    }

    void print(int val) {
        System.out.println(val);
    }

    void print(String val) {
        System.out.println(val);
    }

    void print(int[][] board, int N) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(board[i][j] + " ");
            }
            print("");
        }
    }

    boolean isValid(int i, int j, int N) {
        if (i < 0 || j < 0 || i >= N || j >= N) {
            return false;
        }
        return true;
    }

    boolean iterate(int row, int col, int x, int y, int myrole, int opprole, int N, int[][] board) {
        if (isValid(row, col, N) == false) {
            return false;
        }
        if (board[row][col] == opprole) {
            return iterate(row + x, col + y, x, y, myrole, opprole, N, board);
        }
        if (board[row][col] == myrole) {
            return true;
        }
        return false;
    }

    void fill(int row, int col, int x, int y, int myrole, int[][] board) {
        if (board[row][col] == myrole) {
            return;
        }
        board[row][col] = myrole;
        fill(row + x, col + y, x, y, myrole, board);
    }

    public ArrayList findState(int[][] board, int N, int myrole) {
        int opprole = (myrole == 1) ? 0 : 1;
        int dir_x[] = {0, 1, 0, -1, 1, -1, 1, -1};
        int dir_y[] = {1, 0, -1, 0, 1, 1, -1, -1};

        ArrayList states = new ArrayList();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == 2) //empty cell
                {
                    //check for hint

                    int[][] tempBoard;
                    tempBoard = new int[N + 1][N + 1];
                    for (int i1 = 0; i1 < N; i1++) {
                        for (int j1 = 0; j1 < N; j1++) {
                            if (board[i1][j1] == 2) {
                                tempBoard[i1][j1] = -1;
                            } else {
                                tempBoard[i1][j1] = board[i1][j1];
                            }
                        }
                    }
                    tempBoard[i][j] = myrole;
                    fillUp(i, j, tempBoard, N, myrole);

                    states.add(tempBoard);
                    RowCol.add(i);
                    RowCol.add(j);
                }
            }
        }
        return states;
    }

    boolean isTerminal(int[][] board, int N, int role) {
        showHint(board,N,role);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == 2) {
                    undoHint(board,N);
                    return false;
                }
            }
        }
        undoHint(board,N);
        showHint(board,N,(role+1)%2);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == 2) {
                    undoHint(board,N);
                    return false;
                }
            }
        }
        undoHint(board,N);
        return true;
    }

    int utility(int[][] board, int N, int role) {
        int cnt1 = 0, cnt2 = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == role) {
                    cnt1++;
                } else {
                    cnt2++;
                }
            }
        }
        //return cnt1;
        if (cnt1 > cnt2) {
            return N * N;
        } else if (cnt1 < cnt2) {
            return -1;
        }

        return 0;
    }

    public int Evaluate(int[][] board, int N, int role) {
        int cnt1 = 0, cnt2 = 0;
        

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == role) {
                    cnt1++;
                } else {
                    cnt2++;
                }
            }
        }
        return cnt1 - cnt2;
    }

    public int MaxValue(int[][] board, int N, int role, int alpha, int beta, int depth) {
        if (isTerminal(board, N, role) == true) {
            int val = utility(board, N, role);
            //print(val);
            return val;
        }
        if (depth == MAX_DEPTH) {
            return Evaluate(board, N, role);
        }
        int v = -(1 << 30);
        showHint(board, N, role);
        ArrayList states = findState(board, N, role);
        undoHint(board, N);

        //print("maxvalue states:"+states.size());
        for (int i = 0; i < states.size(); i++) {
            int[][] state = (int[][]) states.get(i);
            int val = MinValue(state, N, (role + 1) % 2, alpha, beta, depth + 1);
            if (depth == 0) {
                values.add(val);
            }
//            print("max value");
//            print(state,N);
//            print("end");
            v = Math.max(v, val);
            if (v >= beta) {
                break;
            }
            alpha = Math.max(alpha, v);
        }
        return v;
    }

    public int MinValue(int[][] board, int N, int role, int alpha, int beta, int depth) {
        if (isTerminal(board, N, role) == true) {
            int val = utility(board, N, role);
            return val;
        }
        if (depth == MAX_DEPTH) {
            return Evaluate(board, N, role);
        }
        int v = (1 << 30);

        showHint(board, N, role);
        ArrayList states = findState(board, N, role);
        undoHint(board, N);

        for (int i = 0; i < states.size(); i++) {
            int[][] state = (int[][]) states.get(i);
            v = Math.min(v, MaxValue(state, N, (role + 1) % 2, alpha, beta, depth + 1));
            if (v <= alpha) {
                break;
            }
            beta = Math.min(beta, v);
        }
        return v;
    }

    int[][] AlphaBetaSearch(Reversi mygame, int depth) {
        int v = MaxValue(mygame.board, mygame.N, role, -(1 << 30), (1 << 30), depth);
        //int v=1;
        showHint(mygame.board, mygame.N, role);
        RowCol.clear();
        ArrayList states = findState(mygame.board, mygame.N, role);
        undoHint(mygame.board, mygame.N);
        System.out.println("main " + states.size());
        System.out.println("main " + values.size());

        for (int i = 0; i < states.size(); i++) {

            int[][] temp = (int[][]) states.get(i);
            //print("first child");
            //print(temp,N);print("end");
            if ((int) values.get(i) == v) {
                mygame.last_row = (int) RowCol.get(i * 2);
                mygame.last_col = (int) RowCol.get(i * 2 + 1);
                values.clear();
                return temp;
            }
        }
        values.clear();
        return null;
    }

    public void showHint(Game game) {
        Reversi mygame = (Reversi) game;
        int myrole = role;
        int opprole = (role == 1) ? 0 : 1;
        int dir_x[] = {0, 1, 0, -1, 1, -1, 1, -1};
        int dir_y[] = {1, 0, -1, 0, 1, 1, -1, -1};

        for (int i = 0; i < mygame.N; i++) {
            for (int j = 0; j < mygame.N; j++) {
                if (mygame.board[i][j] == -1) //empty cell
                {
                    //check for hint
                    for (int k = 0; k < 8; k++) {
                        int x = dir_x[k];
                        int y = dir_y[k];
                        int temp_row = i + x;//immediate neighbor
                        int temp_col = j + y;

                        if (isValid(temp_row, temp_col, mygame.N) && mygame.board[temp_row][temp_col] == opprole) {
                            if (iterate(temp_row, temp_col, x, y, myrole, opprole, mygame.N, mygame.board) == true) {
                                mygame.board[i][j] = 2;//hint=2
                                break;
                            }
                        }
                    }
                }
            }
        }
        mygame.myGui.repaint();
    }

    public void showHint(int[][] board, int N, int myrole) {
        int opprole = (myrole == 1) ? 0 : 1;
        int dir_x[] = {0, 1, 0, -1, 1, -1, 1, -1};
        int dir_y[] = {1, 0, -1, 0, 1, 1, -1, -1};

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == -1) //empty cell
                {
                    //check for hint
                    for (int k = 0; k < 8; k++) {
                        int x = dir_x[k];
                        int y = dir_y[k];
                        int temp_row = i + x;//immediate neighbor
                        int temp_col = j + y;

                        if (isValid(temp_row, temp_col, N) && board[temp_row][temp_col] == opprole) {
                            if (iterate(temp_row, temp_col, x, y, myrole, opprole, N, board) == true) {
                                board[i][j] = 2;//hint=2
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    void undoHint(int[][] board, int N) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == 2) {
                    board[i][j] = -1;
                }
            }
        }
    }

    public void fillUp(int row, int col, Reversi mygame) {
        int myrole = role;
        int opprole = (role == 1) ? 0 : 1;
        int dir_x[] = {0, 1, 0, -1, 1, -1, 1, -1};
        int dir_y[] = {1, 0, -1, 0, 1, 1, -1, -1};

        for (int k = 0; k < 8; k++) {
            int x = dir_x[k];
            int y = dir_y[k];
            int temp_row = row + x;//immediate neighbor
            int temp_col = col + y;

            if (isValid(temp_row, temp_col, mygame.N) && mygame.board[temp_row][temp_col] == opprole) {
                if (iterate(temp_row, temp_col, x, y, myrole, opprole, mygame.N, mygame.board) == true) {
                    fill(temp_row, temp_col, x, y, myrole, mygame.board);
                }
            }
        }

    }

    public void fillUp(int row, int col, int[][] board, int N, int myrole) {
        int opprole = (myrole == 1) ? 0 : 1;
        int dir_x[] = {0, 1, 0, -1, 1, -1, 1, -1};
        int dir_y[] = {1, 0, -1, 0, 1, 1, -1, -1};

        for (int k = 0; k < 8; k++) {
            int x = dir_x[k];
            int y = dir_y[k];
            int temp_row = row + x;//immediate neighbor
            int temp_col = col + y;

            if (isValid(temp_row, temp_col, N) && board[temp_row][temp_col] == opprole) {
                if (iterate(temp_row, temp_col, x, y, myrole, opprole, N, board) == true) {
                    fill(temp_row, temp_col, x, y, myrole, board);
                }
            }
        }

    }

    void ArrayCopy(int[][] src, int[][] dest, int N) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                dest[i][j] = src[i][j];
            }
        }
    }

    boolean isMovePossible(int[][] board, int N) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void makeMove(Game game) {
        System.out.println("com turn");
        showHint(game);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        Reversi mygame = (Reversi) game;
        if (isMovePossible(mygame.board, mygame.N) == false) {
            JOptionPane.showMessageDialog(null, "NO AVAILABLE MOVE !");
            return;
        }
        undoHint(mygame.board, mygame.N);

        int[][] nextMove = AlphaBetaSearch(mygame, 0);

        ArrayCopy(nextMove, mygame.board, mygame.N);

//        try {
//            mygame.moveLock.acquire();
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
//       // while(mygame.row==-1) {}
//        //System.out.println(mygame.row+" "+mygame.col);
//        mygame.board[mygame.row][mygame.col]=role;
        //mygame.moveLock.unlock();
    }

}
