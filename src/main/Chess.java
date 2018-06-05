package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Chess {
    public static void main(String[] args) {
        game();
    }
    
    static void game() {
        Board board = new Board();
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            goWhite(board, r);
            board.getBoard();
            goBlack(board, r);
            board.getBoard();
        }
    }
    
    static void goWhite(Board board, BufferedReader r) {
        System.out.println("White team's move.");
        System.out.println("What is the row of the piece you would like to move? ");
        int row;
        try {
            row = Integer.parseInt(r.readLine());
        } catch (IOException ex) {
            System.out.println("Error, please only send an integer.");
            row = new Integer(-1);
        }
        System.out.println("What is the column of the piece you would like to move? ");
        int col;
        try {
            col = Integer.parseInt(r.readLine());
        } catch (IOException ex) {
            System.out.println("Error, please only send an integer.");
            col = new Integer(-1);
        }
        int[] captureLoc = new int[]{row, col};
        while (true) {
            boolean contains = false;
            for (int[] i : board.getPieces(0)) {
                if (i[0] == captureLoc[0] && i[1] == captureLoc[1]) contains = true;
            }
            if (contains) {
                Board.getIndex(row, col).move();
                break;
            }
            else {
                System.out.println("Error, cannot move piece at row and column. Are you sure that's your piece?");
                System.out.println("What is the row of the piece you would like to move? ");
                try {
                    row = Integer.parseInt(r.readLine());
                } catch (IOException ex) {
                    System.out.println("Error, please only send an integer.");
                    row = new Integer(-1);
                }
                System.out.println("What is the column of the piece you would like to move? ");
                try {
                    col = Integer.parseInt(r.readLine());
                } catch (IOException ex) {
                    System.out.println("Error, please only send an integer.");
                    col = new Integer(-1);
                }
            }
        }
    }
    
    static void goBlack(Board board, BufferedReader r) {
        System.out.println("Black team's move.");
        System.out.println("What is the row of the piece you would like to move? ");
        int row;
        try {
            row = Integer.parseInt(r.readLine());
        } catch (IOException ex) {
            System.out.println("Error, please only send an integer.");
            row = new Integer(-1);
        }
        System.out.println("What is the column of the piece you would like to move? ");
        int col;
        try {
            col = Integer.parseInt(r.readLine());
        } catch (IOException ex) {
            System.out.println("Error, please only send an integer.");
            col = new Integer(-1);
        }
        int[] captureLoc = new int[]{row, col};
        while (true) {
            boolean contains = false;
            for (int[] i : board.getPieces(1)) {
                if (i[0] == captureLoc[0] && i[1] == captureLoc[1]) contains = true;
            }
            if (contains) {
                Board.getIndex(row, col).move();
                break;
            }
            else {
                System.out.println("Error, cannot move piece at row and column. Are you sure that's your piece?");
                System.out.println("What is the row of the piece you would like to move? ");
                try {
                    row = Integer.parseInt(r.readLine());
                } catch (IOException ex) {
                    System.out.println("Error, please only send an integer.");
                    row = new Integer(-1);
                }
                System.out.println("What is the column of the piece you would like to move? ");
                try {
                    col = Integer.parseInt(r.readLine());
                } catch (IOException ex) {
                    System.out.println("Error, please only send an integer.");
                    col = new Integer(-1);
                }
            }
        }
    }
    
}
