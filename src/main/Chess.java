package main;

public class Chess {
    public static void main(String[] args) {
        Board board = new Board();
        board.getIndex(6, 4).move();
        board.getBoard();
        board.getIndex(7, 4).move();
        board.getBoard();
    }
    
}

