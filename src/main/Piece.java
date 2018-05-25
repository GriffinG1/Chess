package main;

import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Board;

public abstract class Piece {
    abstract public char getLetter();
    abstract public Piece canCapture();
    abstract public String getLocation();
    abstract public int[] getLocationAsArray();
    abstract public void move();
    abstract public void setLocation(int locA, int locB);
}

class NoPiece extends Piece {
    private final char name;
    private final int[] location;
    public NoPiece(int a, int b) {
        name = '-';
        location = new int[]{a, b};
    }
    public char getLetter() {
        return name;
    }
    public Piece canCapture() {
        return Board.getIndex(location[0], location[1]);
    }
    public String getLocation() {
        return Arrays.toString(location);
    }
    public int[] getLocationAsArray() {
        return location;
    }
    public void move(){}
    public void setLocation(int locA, int locB) {
        location[0] = locA;
        location[1] = locB;
    }
}

class Pawn extends Piece{
    private int[] location; // row location @ [0], col location @ [1]
    private int team; // White: 0 - Black: 1
    private boolean firstTurn;
    private char name;
    
    public Pawn(int locA, int locB, int t) {
        location = new int[]{locA, locB};
        team = t;
        firstTurn = true;
        if (team == 0) {name = 'P';} else {name = 'p';};
    }
    
    public void move() {
        Piece enemy = canCapture();
        int oldLoc[] = new int[]{location[0], location[1]};
        boolean cap = false;
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        if (enemy.getClass() != NoPiece.class) {
            System.out.println("You can capture a piece! Would you like to? (yes or no)");
            String capture;
            try {
                capture = r.readLine();
            } catch (IOException ex) {capture = "";}
            while (!cap) {
                if (capture.toLowerCase().equals("yes") || capture.toLowerCase().equals("no")) {
                    if (capture.equals("yes")) {
                        cap = true;
                        int[] enLoc = enemy.getLocationAsArray();
                        setLocation(enLoc[0], enLoc[1]);
                        enemy.setLocation(-1, -1);
                        System.out.println("Successfully captured piece at " + getLocation());
                        break;
                    }
                    else {
                        System.out.println("Alright then... moving on.");
                        cap = true;
                    }
                }
                else {
                    System.out.println("Error, input must be \"yes\" or \"no\". Capture? ");
                    try {
                        capture = r.readLine();
                    } catch (IOException ex) {capture = "";}
                }
            }
        }
        if (Board.getIndex(location[0]+1, location[1]).getClass() != NoPiece.class) {
            System.out.println("Cannot move this piece! Location blocked.");
        }
        else if (!firstTurn) {
            System.out.println("Moving one spot forward. Press enter to continue.");
            try{System.in.read();}
            catch(Exception e){}
            if (team == 0) {location[0]++;} else {location[0]--;}
        }
        else {
            System.out.println("First turn! Move 1 or 2 spaces?");
            Integer mv;
            try {
                mv = Integer.parseInt(r.readLine());
            } catch (Exception ex) {
                System.out.println("Error, please only send an integer.");
                mv = new Integer(0);
            }
            while (true) {
                if (mv.equals(new Integer(1)) || mv.equals(new Integer(2))) {
                    if (mv == 1) {
                        System.out.println("Moving one spot forward. Press enter to continue.");
                        try{System.in.read();}
                        catch(Exception e){}
                        if (team == 0) {location[0]++;} else {location[0]--;}
                    }
                    else {
                        System.out.println("Moving two spots forward. Press enter to continue.");
                        try{System.in.read();}
                        catch(Exception e){};
                        if (team == 0) {location[0]+= 2;} else {location[0]-= 2;}
                    }
                    break;
                }
                else {
                    System.out.println("Error, answer must be 1 or 2. Move 1 or 2 spaces?");
                    try {
                        mv = Integer.parseInt(r.readLine());
                    } catch (Exception ex) {
                        System.out.println("Error, please only send an integer.");
                    }
                }
            }
            firstTurn = false;
        }
        Board.updateBoard(oldLoc, this);
    }
    
    public void setLocation(int locA, int locB) {
        location[0] = locA;
        location[1] = locB;
    }
    
    public Piece canCapture() {
        int[] none = new int[]{-1, -1};
        if (team == 0) {
            if ((Board.getIndex(location[0]+1, location[1]+1).getClass() != NoPiece.class)) {
                return Board.getIndex(location[0]+1, location[1]+1);
            }
            else if ((Board.getIndex(location[0]+1, location[1]-1).getClass() != NoPiece.class)) {
                return Board.getIndex(location[0]+1, location[1]-1);
            }
        }
        return new NoPiece(-1, -1);
    }
    
    public char getLetter() {
        return name;
    }
    
    public String getLocation() {
        return Arrays.toString(location);
    }
    
    public int[] getLocationAsArray() {
        return location;
    }
}