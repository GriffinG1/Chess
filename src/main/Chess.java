package main;

public class Chess {
    public static void main(String[] args) {
        Board board = new Board();
        System.out.println(board.getIndex(7, 3).canCapture());
    }
    
}

