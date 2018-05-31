package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    
    public static Piece[][] board;
    
    public Board() {
        board = new Piece[8][8];
        makeBoard(board);
        getBoard();
    }
    
    public static Piece getIndex(int a, int b) {
        return board[a][b];
    }
    
    
    private void makeBoard(Piece[][] board) {
        for (int c = 0; c < board[0].length; c++) {
            board[1][c] = new Pawn(1, c, 0);
            board[6][c] = new Pawn(6, c, 1);
        }
        
//        board[0][0] = 'R'; board[7][0] = 'R'; board[0][7] = 'R'; board[7][7] = 'R';
//        board[0][1] = 'N'; board[7][1] = 'N'; board[0][6] = 'N'; board[7][6] = 'N';
//        board[0][2] = 'B'; board[7][2] = 'B'; board[0][5] = 'B'; board[7][5] = 'B';
//        board[0][3] = 'Q'; board[7][3] = 'Q';
        board[0][4] = new King(0, 4, 0);; board[7][4] = new King(7, 4, 1);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == null) board[i][j] = new NoPiece(i, j);
            }
        }
    }
    
    public void getBoard() {
        System.out.println("\t      Black");
        int count = 97;
        System.out.print("    ");
        for (int i = 0; i < board.length; i++) {
            System.out.print(Character.toUpperCase((char) count) + "   ");
            count++;
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

 class Pieces {
    private List<Object> allPieces;
    
    public Pieces() {
        allPieces = new ArrayList<Object>();
    }
    
    public void addPiece(Object obj) {
        allPieces.add(obj);
    }
    
    public String toString() {
        String items = "";
        for(Object obj : allPieces) {
            items += obj + ", ";
        }
        items = items.substring(0, items.length()-2);
        return items;
    }
}