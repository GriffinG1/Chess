package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    
    public static Piece[][] board;
    private List<int[]> white;
    private List<int[]> black;
    
    public Board() {
        board = new Piece[6][6];
        white = new ArrayList<int[]>();
        black = new ArrayList<int[]>();
        makeBoard(board);
        getBoard();
    }
    
    public static Piece getIndex(int a, int b) {
        return board[a][b];
    }
    
    public List<int[]> getPieces(int t) {
        if (t == 0) {
            return white;
        }
        else {
            return black;
        }
    }
    
    
    private void makeBoard(Piece[][] board) {
        board[2][2] = new Bishop(2, 2, 0);
        
        board[0][0] = new Rook(0, 0, 0); board[5][0] = new Rook(5, 0, 1); board[0][5] = new Rook(0, 5, 0); board[5][5] = new Rook(5, 5, 1);
        board[0][1] = new Bishop(0, 1, 0); board[5][1] = new Bishop(5, 1, 1); board[0][4] = new Bishop(0, 4, 0); board[5][4] = new Bishop(5, 4, 1);
        board[0][2] = new Queen(0, 2, 0); board[5][2] = new Queen(5, 2, 1);
        board[0][3] = new King(0, 3, 0); board[5][3] = new King(5, 3, 1);
        for (int c = 0; c < board[0].length; c++) {
            board[1][c] = new Pawn(1, c, 0);
            white.add(board[0][c].getLocationAsArray());
            white.add(board[1][c].getLocationAsArray());
            board[4][c] = new Pawn(4, c, 1);
            black.add(board[4][c].getLocationAsArray());
            black.add(board[5][c].getLocationAsArray());
        }
        for (int i = 0; i < black.size(); i++) {
            System.out.print(Arrays.toString(black.get(i)) + ", ");
            
        }
        board[1][2] = null;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == null) board[i][j] = new NoPiece(i, j);
            }
        }
    }
    
    public void getBoard() {
        System.out.println("\t      Black");
        System.out.print("    ");
        for (int i = 0; i < board.length; i++) {
            System.out.print(i + "   ");
        }
        System.out.println("\n");
        for (int i = 0; i < board.length; i++) {
            System.out.print(i + "   ");
            for (int j = 0; j < board.length; j++) {
                System.out.print(board[i][j].getLetter() + "   ");
            }
            if (i < board.length-1) System.out.println("\n");
            else System.out.println("");
        }
        System.out.println("\t      White");
    }
    
    public static void updateBoard(int[] oldLoc, Piece piece) {
        Piece noPiece = new NoPiece(oldLoc[0], oldLoc[1]);
        board[oldLoc[0]][oldLoc[1]] = noPiece;
        int[] newLoc = piece.getLocationAsArray();
        board[newLoc[0]][newLoc[1]] = piece;
    }
}
