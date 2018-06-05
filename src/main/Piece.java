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
        boolean captured = false;
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
                        captured = true;
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
        else if (!captured) {
            if (!firstTurn) {
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
        else if (team == 1) {
            if ((Board.getIndex(location[0]-1, location[1]+1).getClass() != NoPiece.class)) {
                return Board.getIndex(location[0]-1, location[1]+1);
            }
            else if ((Board.getIndex(location[0]-1, location[1]-1).getClass() != NoPiece.class)) {
                return Board.getIndex(location[0]-1, location[1]-1);
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
            System.out.println("Would you like to capture a piece? (yes or no)");
            String wantCapture;
            boolean cap = false;
            try {
                wantCapture = r.readLine();
            } catch (IOException ex) {wantCapture = "";}
            while (true) {
                if (wantCapture.toLowerCase().equals("yes") || wantCapture.toLowerCase().equals("no")) {
                    if (!wantCapture.equals("yes")) {
                        System.out.println("Alright then... moving on.");
                        break;
                    }
                    else { cap = true; }
                }
                else {
                    System.out.println("Error, input must be \"yes\" or \"no\". Capture? ");
                    try {
                        wantCapture = r.readLine();
                    } catch (IOException ex) {wantCapture = "";}
                }
            }
            if (cap != true) return new NoPiece(-1, -1);;
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
    private int[] location; // row location @ [0], col location @ [1]
    private int team; // White: 0 - Black: 1
    private char name;

    public Rook(int locA, int locB, int t) {
        location = new int[]{locA, locB};
        team = t;
        if (team == 0) {name = 'R';} else {name = 'r';};
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
    
    public void setLocation(int locA, int locB) {
        location[0] = locA;
        location[1] = locB;
    }

    
    public int getTeam() {
        return team;
    }
    
    public Piece canCapture() {return null;}
    
    public Piece canCapture(int direction) {
        if (direction == 0) {
            Piece up = Board.getIndex(location[0] + (distanceToEnd(0) - 1), location[1]);
            if ((up.getClass() != NoPiece.class) && up != this && (up.getTeam() != team)) {return up;}
        }
        else if (direction == 1) {
            Piece down = Board.getIndex(location[0] + (distanceToEnd(1) + 1), location[1]);
            if ((down.getClass() != NoPiece.class) && down != this && (down.getTeam() != team)) {return down;}
        }
        else if (direction == 2) {
            Piece left = Board.getIndex(location[0], location[1] + (distanceToEnd(2) - 1));
            if ((left.getClass() != NoPiece.class) && left != this && (left.getTeam() != team)) {return left;}
        }
        else if (direction == 3) {
            Piece right = Board.getIndex(location[0], location[1] + (distanceToEnd(3) + 1));
            if ((right.getClass() != NoPiece.class) && right != this && (right.getTeam() != team)) {return right;}
        }
        return new NoPiece(-1, -1);
    }
    
    public void move() {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        int oldLoc[] = new int[]{location[0], location[1]};
        List<int[]> capture = new ArrayList<int[]>();
        List<int[]> capturePiece = new ArrayList<int[]>();
        boolean cap = false;
        boolean captureUp = canCapture(0).getClass() != NoPiece.class;
        boolean captureDown = canCapture(1).getClass() != NoPiece.class;
        boolean captureLeft = canCapture(2).getClass() != NoPiece.class;
        boolean captureRight = canCapture(3).getClass() != NoPiece.class;
        boolean canCap = captureUp || captureDown || captureLeft || captureRight;
        if (canCap) {
            System.out.println("You can capture pieces at the following spaces: ");
            if (captureUp) {System.out.print(canCapture(0).getLocation() + ", "); capturePiece.add(canCapture(0).getLocationAsArray());}
            if (captureDown) {System.out.print(canCapture(1).getLocation() + ", "); capturePiece.add(canCapture(1).getLocationAsArray());}
            if (captureLeft) {System.out.print(canCapture(2).getLocation() + ", "); capturePiece.add(canCapture(2).getLocationAsArray());}
            if (captureRight) {System.out.print(canCapture(3).getLocation() + ", "); capturePiece.add(canCapture(3).getLocationAsArray());}
            System.out.println();
            System.out.println("Would you like to capture a piece? (yes or no)");
            String wantCapture;
            try {
                wantCapture = r.readLine();
            } catch (IOException ex) {wantCapture = "";}
            while (true) {
                if (wantCapture.toLowerCase().equals("yes") || wantCapture.toLowerCase().equals("no")) {
                    if (!wantCapture.equals("yes")) {
                        break;
                    }
                    else { cap = true; break;}
                }
                else {
                    System.out.println("Error, input must be \"yes\" or \"no\". Capture? ");
                    try {
                        wantCapture = r.readLine();
                    } catch (IOException ex) {wantCapture = "";}
                }
            }
            if (cap == true) {
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
                    for (int[] i : capturePiece) {
                        if (i[0] == captureLoc[0] && i[1] == captureLoc[1]) contains = true;
                    }
                    if (contains) {
                        location = new int[]{captureLoc[0], captureLoc[1]};
                        break;
                    }
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
                }
            }
        }
        if (cap == false) {
            System.out.println("You can move to the following spaces: ");
            System.out.println("Upwards: ");
            for (int i = location[0] - 1; i > location[0] + distanceToEnd(0) - 1; i--) {
                System.out.print("[" + i + ", " + location[1] + "], ");
                capture.add(new int[]{i, location[1]});
            }
            System.out.println();
            System.out.println("Downwards: ");
            for (int i = location[0] + 1; i < location[0] + distanceToEnd(1) + 1; i++) {
                System.out.print("[" + i + ", " + location[1] + "], ");
                capture.add(new int[]{i, location[1]});
            }
            System.out.println();
            System.out.println("To the left: ");
            for (int i = location[1] - 1; i > location[1] + distanceToEnd(2) - 1; i--) {
                System.out.print("[" + location[0] + ", " + i + "], ");
                capture.add(new int[]{location[0], i});
            }
            System.out.println();
            System.out.println("To the right: ");
            for (int i = location[1] + 1; i < location[1] + distanceToEnd(3) + 1; i++) {
                System.out.print("[" + location[0] + ", " + i + "], ");
                capture.add(new int[]{location[0], i});
            }
            System.out.println();
            System.out.println("Which row would you like to move to? ");
            int row;
            try {
                row = Integer.parseInt(r.readLine());
            } catch (IOException ex) {
                System.out.println("Error, please only send an integer.");
                row = new Integer(-1);
            }
            System.out.println("Which column would you like to move to? ");
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
    
    public int distanceToEnd(int direction) { // 0 = up, 1 = down, 2 = left, 3 = right
        if (direction == 0) {
            for (int i = location[0] - 1; i > 0; i--) {
                if (Board.getIndex(i, location[1]).getClass() != NoPiece.class) {
                    if (team == 0) return i - location[0] + 1;
                    else return location[0] - i + 1;
                }
            }
            return 0 - location[0];
        }
        else if (direction == 1) {
            for (int i = location[0] + 1; i < 5; i++) {
                if (Board.getIndex(i, location[1]).getClass() != NoPiece.class) {
                    if (team == 0) return i - location[0] - 1;
                    else return location[0] - i - 1;
                }
            }
            return 5 - location[0];
        }
        else if (direction == 2) {
            for (int i = location[1] - 1; i > 0; i--) {
                if (Board.getIndex(location[0], i).getClass() != NoPiece.class) {
                    return i - location[1] + 1;
                }
            }
            return location[1] * -1 + 1;
        }
        else if (direction == 3) {
            for (int i = location[1] + 1; i < 5; i++) {
                if (Board.getIndex(location[0], i).getClass() != NoPiece.class) {
                    return i - location[1] - 1;
                }
            }
            return 5 - location[1] - 1;
        }
        return 0;
    }
    
}

class Bishop extends Piece {
    private int[] location; // row location @ [0], col location @ [1]
    private int team; // White: 0 - Black: 1
    private char name;

    public Bishop(int locA, int locB, int t) {
        location = new int[]{locA, locB};
        team = t;
        if (team == 0) {name = 'B';} else {name = 'b';};
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
    
    public void setLocation(int locA, int locB) {
        location[0] = locA;
        location[1] = locB;
    }
    
    public int getTeam() {
        return team;
    }
    
    public void move() {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        int oldLoc[] = new int[]{location[0], location[1]};
        List<int[]> capture = new ArrayList<int[]>();
        List<int[]> capturePiece = new ArrayList<int[]>();
        boolean cap = false;
        boolean captureUp = canCapture(0).getClass() != NoPiece.class;
        boolean captureDown = canCapture(1).getClass() != NoPiece.class;
        boolean captureLeft = canCapture(2).getClass() != NoPiece.class;
        boolean captureRight = canCapture(3).getClass() != NoPiece.class;
        boolean canCap = captureUp || captureDown || captureLeft || captureRight;
        if (canCap) {
            System.out.println("You can capture pieces at the following spaces: ");
            if (captureUp) {System.out.print(canCapture(0).getLocation() + ", "); capturePiece.add(canCapture(0).getLocationAsArray());}
            if (captureDown) {System.out.print(canCapture(1).getLocation() + ", "); capturePiece.add(canCapture(1).getLocationAsArray());}
            if (captureLeft) {System.out.print(canCapture(2).getLocation() + ", "); capturePiece.add(canCapture(2).getLocationAsArray());}
            if (captureRight) {System.out.print(canCapture(3).getLocation() + ", "); capturePiece.add(canCapture(3).getLocationAsArray());}
            System.out.println();
            System.out.println("Would you like to capture a piece? (yes or no)");
            String wantCapture;
            try {
                wantCapture = r.readLine();
            } catch (IOException ex) {wantCapture = "";}
            while (true) {
                if (wantCapture.toLowerCase().equals("yes") || wantCapture.toLowerCase().equals("no")) {
                    if (!wantCapture.equals("yes")) {
                        break;
                    }
                    else { cap = true; break;}
                }
                else {
                    System.out.println("Error, input must be \"yes\" or \"no\". Capture? ");
                    try {
                        wantCapture = r.readLine();
                    } catch (IOException ex) {wantCapture = "";}
                }
            }
            if (cap == true) {
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
                    for (int[] i : capturePiece) {
                        if (i[0] == captureLoc[0] && i[1] == captureLoc[1]) contains = true;
                    }
                    if (contains) {
                        location = new int[]{captureLoc[0], captureLoc[1]};
                        break;
                    }
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
                }
            }
        }
        if (cap == false) {
            System.out.println("You can move to the following spaces: ");
            System.out.println("Top-left: ");
            int i;
            int j;
            i = location[0]; j = location[1];
            i--; j--;
            while(i > 0 && j > 0) {
                if (Board.getIndex(i, j).getClass() == NoPiece.class) {
                    System.out.print("[" + i + ", " + j + "]");
                    i--;
                    j--;
                }
                else {
                    break;
                }
            }
            System.out.println();
            System.out.println("Top-right: ");
            i = location[0]; j = location[1];
            i--; j++;
            while(i > 0 && j < 5) {
                if (Board.getIndex(i, j).getClass() == NoPiece.class) {
                    System.out.print("[" + i + ", " + j + "]");
                    i--;
                    j++;
                }
                else {
                    break;
                }
            }
            System.out.println();
            System.out.println("Bottom-left: ");
            i = location[0]; j = location[1];
            i++; j--;
            while(i < 5 && j > 0) {
                if (Board.getIndex(i, j).getClass() == NoPiece.class) {
                    System.out.print("[" + i + ", " + j + "]");
                    i++;
                    j--;
                }
                else {
                    break;
                }
            }
            System.out.println();
            System.out.println("Bottom-right: ");
            i = location[0]; j = location[1];
            i++; j++;
            while(i < 5 && j < 5) {
                if (Board.getIndex(i, j).getClass() == NoPiece.class) {
                    System.out.print("[" + i + ", " + j + "]");
                    i++;
                    j++;
                }
                else {
                    break;
                }
            }
            System.out.println();
            System.out.println("Which row would you like to move to? ");
            int row;
            try {
                row = Integer.parseInt(r.readLine());
            } catch (IOException ex) {
                System.out.println("Error, please only send an integer.");
                row = new Integer(-1);
            }
            System.out.println("Which column would you like to move to? ");
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
                for (int[] k : capture) {
                    if (k[0] == captureLoc[0] && k[1] == captureLoc[1]) contains = true;
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
    
    public Piece canCapture() {return null;}
    
    public Piece canCapture(int direction) {
        if (direction == 0) {
            int[] endLoc = distanceToEnd(0);
            Piece up = Board.getIndex(Math.abs(endLoc[0]), Math.abs(endLoc[1]));
            if ((up.getClass() != NoPiece.class) && up != this && (up.getTeam() != team)) {return up;}
        }
        else if (direction == 1) {
            int[] endLoc = distanceToEnd(1);
            Piece down = Board.getIndex(Math.abs(endLoc[0]), endLoc[1]);
            if ((down.getClass() != NoPiece.class) && down != this && (down.getTeam() != team)) {return down;}
        }
        else if (direction == 2) {
            int[] endLoc = distanceToEnd(2);
            Piece left = Board.getIndex(endLoc[0], Math.abs(endLoc[1]));
            if ((left.getClass() != NoPiece.class) && left != this && (left.getTeam() != team)) {return left;}
        }
        else if (direction == 3) {
            int[] endLoc = distanceToEnd(3);
            Piece right = Board.getIndex(endLoc[0], endLoc[1]);
            if ((right.getClass() != NoPiece.class) && right != this && (right.getTeam() != team)) {return right;}
        }
        return new NoPiece(-1, -1);
    }
    
    public int[] distanceToEnd(int direction) { // 0 = up-left, 1 = up-right, 2 = down-left, 3 = down-right
        int i = location[0]; int j = location[1];
        if (direction == 0) {
            i--; j--;
            while(i > 0 && j > 0) {
                if (Board.getIndex(i, j).getClass() == NoPiece.class) {
                    i--;
                    j--;
                }
                else {
                    break;
                }
            }
            return new int[]{i, j};
        }
        else if (direction == 1) {
            i--; j++;
            while(i > 0 && j < 5) {
                if (Board.getIndex(i, j).getClass() == NoPiece.class) {
                    i--;
                    j++;
                }
                else {
                    break;
                }
            }
            if (j > 5) j = 6;
            return new int[]{i, j};
        }
        else if (direction == 2) {
            i++; j--;
            while(i < 5 && j > 0) {
                if (Board.getIndex(i, j).getClass() == NoPiece.class) {
                    i++;
                    j--;
                }
                else {
                    break;
                }
            }
            if (i > 5) i = 5;
            return new int[]{i, j};
        }
        else if (direction == 3) {
            i++; j++;
            while(i < 5 && j < 5) {
                if (Board.getIndex(i, j).getClass() == NoPiece.class) {
                    i++;
                    j++;
                }
                else {
                    break;
                }
            }
            if (i > 5) i = 5; if (j > 5) j = 5;
            return new int[]{i, j};
        }
        return new int[]{-1, -1};
    }
    
}

class Queen extends Piece {
    private int[] location; // row location @ [0], col location @ [1]
    private int team; // White: 0 - Black: 1
    private char name;

    public Queen(int locA, int locB, int t) {
        location = new int[]{locA, locB};
        team = t;
        if (team == 0) {name = 'Q';} else {name = 'q';};
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
    
    public void setLocation(int locA, int locB) {
        location[0] = locA;
        location[1] = locB;
    }
    
    public int getTeam() {
        return team;
    }
    
    public void move() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Piece canCapture() {return null;}
    
    public Piece canCaptureDiag(int direction) {
        if (direction == 0) {
            int[] endLoc = distanceToEndDiag(0);
            Piece up = Board.getIndex(Math.abs(endLoc[0]), Math.abs(endLoc[1]));
            if ((up.getClass() != NoPiece.class) && up != this && (up.getTeam() != team)) {return up;}
        }
        else if (direction == 1) {
            int[] endLoc = distanceToEndDiag(1);
            Piece down = Board.getIndex(Math.abs(endLoc[0]), endLoc[1]);
            if ((down.getClass() != NoPiece.class) && down != this && (down.getTeam() != team)) {return down;}
        }
        else if (direction == 2) {
            int[] endLoc = distanceToEndDiag(2);
            Piece left = Board.getIndex(endLoc[0], Math.abs(endLoc[1]));
            if ((left.getClass() != NoPiece.class) && left != this && (left.getTeam() != team)) {return left;}
        }
        else if (direction == 3) {
            int[] endLoc = distanceToEndDiag(3);
            Piece right = Board.getIndex(endLoc[0], endLoc[1]);
            if ((right.getClass() != NoPiece.class) && right != this && (right.getTeam() != team)) {return right;}
        }
        return new NoPiece(-1, -1);
    }
    
    public int[] distanceToEndDiag(int direction) { // 0 = up-left, 1 = up-right, 2 = down-left, 3 = down-right
        int i = location[0]; int j = location[1];
        if (direction == 0) {
            i--; j--;
            while(i > 0 && j > 0) {
                if (Board.getIndex(i, j).getClass() == NoPiece.class) {
                    i--;
                    j--;
                }
                else {
                    break;
                }
            }
            return new int[]{i, j};
        }
        else if (direction == 1) {
            i--; j++;
            while(i > 0 && j < 5) {
                if (Board.getIndex(i, j).getClass() == NoPiece.class) {
                    i--;
                    j++;
                }
                else {
                    break;
                }
            }
            if (j > 5) j = 6;
            return new int[]{i, j};
        }
        else if (direction == 2) {
            i++; j--;
            while(i < 5 && j > 0) {
                if (Board.getIndex(i, j).getClass() == NoPiece.class) {
                    i++;
                    j--;
                }
                else {
                    break;
                }
            }
            if (i > 5) i = 5;
            return new int[]{i, j};
        }
        else if (direction == 3) {
            i++; j++;
            while(i < 5 && j < 5) {
                if (Board.getIndex(i, j).getClass() == NoPiece.class) {
                    i++;
                    j++;
                }
                else {
                    break;
                }
            }
            if (i > 5) i = 5; if (j > 5) j = 5;
            return new int[]{i, j};
        }
        return new int[]{-1, -1};
    }
    
    public Piece canCaptureHoriz(int direction) {
        if (direction == 0) {
            Piece up = Board.getIndex(location[0] + (distanceToEndHoriz(0) - 1), location[1]);
            if ((up.getClass() != NoPiece.class) && up != this && (up.getTeam() != team)) {return up;}
        }
        else if (direction == 1) {
            Piece down = Board.getIndex(location[0] + (distanceToEndHoriz(1) + 1), location[1]);
            if ((down.getClass() != NoPiece.class) && down != this && (down.getTeam() != team)) {return down;}
        }
        else if (direction == 2) {
            Piece left = Board.getIndex(location[0], location[1] + (distanceToEndHoriz(2) - 1));
            if ((left.getClass() != NoPiece.class) && left != this && (left.getTeam() != team)) {return left;}
        }
        else if (direction == 3) {
            Piece right = Board.getIndex(location[0], location[1] + (distanceToEndHoriz(3) + 1));
            if ((right.getClass() != NoPiece.class) && right != this && (right.getTeam() != team)) {return right;}
        }
        return new NoPiece(-1, -1);
    }
    
    public int distanceToEndHoriz(int direction) { // 0 = up, 1 = down, 2 = left, 3 = right
        if (direction == 0) {
            for (int i = location[0] - 1; i > 0; i--) {
                if (Board.getIndex(i, location[1]).getClass() != NoPiece.class) {
                    if (team == 0) return i - location[0] + 1;
                    else return location[0] - i + 1;
                }
            }
            return 0 - location[0];
        }
        else if (direction == 1) {
            for (int i = location[0] + 1; i < 5; i++) {
                if (Board.getIndex(i, location[1]).getClass() != NoPiece.class) {
                    if (team == 0) return i - location[0] - 1;
                    else return location[0] - i - 1;
                }
            }
            return 5 - location[0];
        }
        else if (direction == 2) {
            for (int i = location[1] - 1; i > 0; i--) {
                if (Board.getIndex(location[0], i).getClass() != NoPiece.class) {
                    return i - location[1] + 1;
                }
            }
            return location[1] * -1 + 1;
        }
        else if (direction == 3) {
            for (int i = location[1] + 1; i < 5; i++) {
                if (Board.getIndex(location[0], i).getClass() != NoPiece.class) {
                    return i - location[1] - 1;
                }
            }
            return 5 - location[1] - 1;
        }
        return 0;
    }
    
}