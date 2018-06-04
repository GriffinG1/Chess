package main;

import java.util.*;
import java.io.*;
import main.Board;

public abstract class Piece {
    abstract public char getLetter();
    abstract public Piece canCapture();
    abstract public String getLocation();
    abstract public int[] getLocationAsArray();
    abstract public void move();
    abstract public void setLocation(int locA, int locB);
    abstract public int getTeam();
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
    public int getTeam() {
        return -1;
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
        if (enemy.getClass() != NoPiece.class && enemy.getTeam() != team) {
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
        if (team == 0 && Board.getIndex(location[0]+1, location[1]).getClass() != NoPiece.class) {
            System.out.println("Cannot move this piece! Location blocked.");
        }
        else if (team == 1 && Board.getIndex(location[0]-1, location[1]).getClass() != NoPiece.class) {
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
                mv = new Integer(-1);
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
    
    public int getTeam() {
        return team;
    }
}

class King extends Piece {
    private int[] location; // row location @ [0], col location @ [1]
    private int team; // White: 0 - Black: 1
    private char name;

    public King(int locA, int locB, int t) {
        location = new int[]{locA, locB};
        team = t;
        if (team == 0) {name = 'K';} else {name = 'k';};
    }

    public char getLetter() {
        return name;
    }
    
    public void setLocation(int locA, int locB) {
        location[0] = locA;
        location[1] = locB;
    }
    
    public String getLocation() {
        return Arrays.toString(location);
    }

    public int[] getLocationAsArray() {
        return location;
    }
    
    public int getTeam() {
        return team;
    }

    public Piece canCapture() {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        List<Piece> surroundings = getSurroundings(Board.board, location[0], location[1]);
        List<int[]> capture = new ArrayList<int[]>();
        for (Piece o : surroundings) {
            if (o.getClass() != NoPiece.class && o.getTeam() != team) {
                capture.add(o.getLocationAsArray());
            }
        }
        if (capture.size() > 0) {
            System.out.println("You can capture pieces at: ");
            for (int i = 0; i < capture.size(); i++) {
                System.out.print(Arrays.toString(capture.get(i)) + ", ");
            }
            System.out.println();
            System.out.println("Which row would you like to capture?");
            int row;
            try {
                row = Integer.parseInt(r.readLine());
            } catch (IOException ex) {
                System.out.println("Error, please only send an integer.");
                row = new Integer(-1);
            }
            System.out.println("Which column would you like to capture?");
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
                for (int[] i : capture) {
                    if (i[0] == captureLoc[0] && i[1] == captureLoc[1]) contains = true;
                }
                if (contains) return Board.getIndex(captureLoc[0], captureLoc[1]);
                else {
                    System.out.println("Error, space at row and column is not capturable. Which row would you like to capture? ");
                    try {
                        row = Integer.parseInt(r.readLine());
                    } catch (IOException ex) {
                        System.out.println("Error, please only send an integer.");
                        row = new Integer(-1);
                    }
                    System.out.println("Which column would you like to capture? ");
                    try {
                        col = Integer.parseInt(r.readLine());
                    } catch (IOException ex) {
                        System.out.println("Error, please only send an integer.");
                        col = new Integer(-1);
                    }
                }
                return Board.getIndex(row, col);
            }
        }
        return new NoPiece(-1, -1);
    }
    
    private static int[][] directions = new int[][]{{-1,-1}, {-1,0}, {-1,1},  {0,1}, {1,1},  {1,0},  {1,-1},  {0, -1}};

    private List<Piece> getSurroundings(Piece[][] matrix, int y, int x){ // Taken from https://stackoverflow.com/a/12743915
        List<Piece> res = new ArrayList<Piece>();
        for (int[] direction : directions) {
            int cx = x + direction[0];
            int cy = y + direction[1];
            if(cy >=0 && cy < matrix.length)
                if(cx >= 0 && cx < matrix[cy].length)
                    res.add(matrix[cy][cx]);
        }
        return res;
    }

    public void move() {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        List<Piece> surroundings = getSurroundings(Board.board, location[0], location[1]);
        List<int[]> capture = new ArrayList<int[]>();
        int oldLoc[] = new int[]{location[0], location[1]};
        for (Piece o : surroundings) {
            if (o.getClass() == NoPiece.class) {
                capture.add(o.getLocationAsArray());
            }
        }
        Piece enemy = canCapture();
        if (enemy.getClass() != NoPiece.class) {
            int[] enLoc = enemy.getLocationAsArray();
            setLocation(enLoc[0], enLoc[1]);
            enemy.setLocation(-1, -1);
            System.out.println("Successfully captured piece at " + getLocation());
        }
        else {
            Integer row;
            Integer col;
            System.out.println("You can move to the following spaces: ");
            for (int i = 0; i < capture.size(); i++) {
                System.out.print(Arrays.toString(capture.get(i)) + ", ");
            }
            System.out.println();
            System.out.println("Which row would you like to move to?");
            try {
                row = Integer.parseInt(r.readLine());
            } catch (Exception ex) {
                System.out.println("Error, please only send an integer.");
                row = new Integer(-1);
            }
            System.out.println("Which column would you like to move to?");
            try {
                col = Integer.parseInt(r.readLine());
            } catch (Exception ex) {
                System.out.println("Error, please only send an integer.");
                col = new Integer(-1);
            }
            int[] captureLoc = new int[]{row, col};
            while (true) {
                boolean contains = false;
                for (int[] i : capture) {
                    if (i[0] == captureLoc[0] && i[1] == captureLoc[1]) contains = true;
                }
                if (contains) {
                    location = new int[]{captureLoc[0], captureLoc[1]};
                    break;
                }
                else {
                    System.out.println("Error, cannot move to space at row and column. Which row would you like to move to? ");
                    try {
                        row = Integer.parseInt(r.readLine());
                    } catch (IOException ex) {
                        System.out.println("Error, please only send an integer.");
                        row = new Integer(-1);
                    }
                    System.out.println("Which column would you like to move to? ");
                    try {
                        col = Integer.parseInt(r.readLine());
                    } catch (IOException ex) {
                        System.out.println("Error, please only send an integer.");
                        col = new Integer(-1);
                    }
                }
            }
        }
        Board.updateBoard(oldLoc, this);
    }

}

class Rook extends Piece {

    
    public char getLetter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public Piece canCapture() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public String getLocation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public int[] getLocationAsArray() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public void move() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public void setLocation(int locA, int locB) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public int getTeam() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

class Bishop extends Piece {

    
    public char getLetter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public Piece canCapture() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public String getLocation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public int[] getLocationAsArray() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public void move() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public void setLocation(int locA, int locB) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public int getTeam() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

class Knight extends Piece {

    
    public char getLetter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public Piece canCapture() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public String getLocation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public int[] getLocationAsArray() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public void move() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public void setLocation(int locA, int locB) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public int getTeam() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

class Queen extends Piece {

    
    public char getLetter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public Piece canCapture() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public String getLocation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public int[] getLocationAsArray() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public void move() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public void setLocation(int locA, int locB) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public int getTeam() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}