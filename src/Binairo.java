import java.util.ArrayList;

import static java.lang.Math.min;

public class Binairo {
    private final ArrayList<ArrayList<String>> board;
    private final ArrayList<ArrayList<ArrayList<String>>> domain;
    private final int n;
    private ArrayList<ArrayList<Integer>> unassigned ;
    private int[][] rowCount ;
    private int[][] colCount ;
    private int un;
    private ArrayList<State> states;
    public Binairo(ArrayList<ArrayList<String>> board, ArrayList<ArrayList<ArrayList<String>>> domain, int n, ArrayList<ArrayList<Integer>>una, int u,int[][] cc,int[][]rc) {
        this.board = board;
        this.domain = domain;
        this.n = n;
        unassigned = una;
        un = u;
        colCount = cc;
        rowCount = rc;
        states = new ArrayList<State>();
    }
    public void start() {
        long tStart = System.nanoTime();
        State state = new State(board, domain);
        
        drawLine();
        System.out.println("Initial Board: \n");
        state.printBoard();
        drawLine();
        if(backtrack(state,un))
            System.out.println("HOOORRRRAAAAA");
        long tEnd = System.nanoTime();
        System.out.println("Total time: " + (tEnd - tStart)/1000000000.000000000);
    }
    private boolean backtrack(State state,int count){
        if(allAssigned(state)|| count == -1)   return true;
        int x,y,temp;
        //temp = MRV(count);
        //temp = LCV(state,count);
        //x = unassigned.get(temp).get(0);
        //y = unassigned.get(temp).get(1);
        //unassigned.get(count).set(2,1);
        //normal unassigned
        x = unassigned.get(count).get(0);
        y = unassigned.get(count).get(1);
        unassigned.get(count).set(2,1);
            for (String v : domain.get(x).get(y)) {
                System.out.println("(" + x + "," + y + ") v:" + v);
                State s = state.copy();
                s.setIndexBoard(x, y, v);
                if (isConsistent(s) && forwardChecking(s,count)) {
                    if (backtrack(s, count - 1)) {
                        states.add(s);
                        if (count == 0)
                            s.printBoard();
                        System.out.println("count: " + count);
                        return true;
                    } else {
                        backForwardChecking(s, count);
                        s.setIndexBoard(x, y, "E");
                        System.out.println("fail: " + count);
                    }
                } else {
                    backForwardChecking(s, count);
                    System.out.println("notValid: " + count);
                }
            }

        return false;
    }
    private boolean checkNumberOfCircles(State state,int x,int y) {
        ArrayList<ArrayList<String>> cBoard = state.getBoard();
        //row
        int numberOfWhites ,numberOfBlacks;
        for (int i = 0; i < n; i++) {
            numberOfWhites = 0;
            numberOfBlacks = 0;
            for (int j = 0; j < n; j++) {
                String a = cBoard.get(i).get(j);
                switch (a) {
                    case "w":
                    case "W" : {
                        numberOfWhites++;
                        break;
                    }
                    case "b":
                    case "B": {
                        numberOfBlacks++;
                        break;
                    }
                }
            }
            if (numberOfBlacks > n/2 || numberOfWhites > n/2) {
                return false;
            }
        }
        //column
        for (int i = 0; i < n; i++) {
            numberOfWhites = 0;
            numberOfBlacks = 0;
            for (int j = 0; j < n; j++) {
                String a = cBoard.get(j).get(i);
                switch (a) {
                    case "w":
                    case "W" : {
                        numberOfWhites++;
                        break;
                    }
                    case "b":
                    case"B" : {
                        numberOfBlacks++;
                        break;
                    }
                }
            }
            if (numberOfBlacks > n/2 || numberOfWhites > n/2) {
                return false;
            }
        }
        if(x > 0 || y >0){
            numberOfWhites = 0;
            numberOfBlacks = 0;
            for (int j = 0; j < n; j++) {
                String a = cBoard.get(x).get(j);
                switch (a) {
                    case "w":
                    case "W" : {
                        numberOfWhites++;
                        break;
                    }
                    case "b":
                    case "B": {
                        numberOfBlacks++;
                        break;
                    }
                }
            }
            if (numberOfBlacks > n/2 || numberOfWhites > n/2) {
                return false;
            }
            numberOfWhites = 0;
            numberOfBlacks = 0;
            for (int j = 0; j < n; j++) {
                String a = cBoard.get(j).get(y);
                switch (a) {
                    case "w":
                    case "W" : {
                        numberOfWhites++;
                        break;
                    }
                    case "b":
                    case"B" : {
                        numberOfBlacks++;
                        break;
                    }
                }
            }
            if (numberOfBlacks > n/2 || numberOfWhites > n/2) {
                return false;
            }
        }
        return true;
    }
    private boolean checkAdjacency(State state) {
        ArrayList<ArrayList<String>> cBoard = state.getBoard();

        //Horizontal
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n-2; j++) {
                ArrayList<String> row = cBoard.get(i);
                String c1 = row.get(j).toUpperCase();
                String c2 = row.get(j+1).toUpperCase();
                String c3 = row.get(j+2).toUpperCase();
                if (c1.equals(c2) && c2.equals(c3) && !c1.equals("E")) {
                    return false;
                }
            }
        }
        //column
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n-2; i++) {
                String c1 = cBoard.get(i).get(j).toUpperCase();
                String c2 = cBoard.get(i+1).get(j).toUpperCase();
                String c3 = cBoard.get(i+2).get(j).toUpperCase();
                if (c1.equals(c2) && c2.equals(c3) && !c1.equals("E")) {
                    return false;
                }
            }
        }

        return true;
    }
    private boolean checkIfUnique (State state,int x,int y) {
        ArrayList<ArrayList<String>> cBoard = state.getBoard();
        
        //check if two rows are duplicated
        for (int i = 0; i < n-1; i++) {
            for (int j = i+1; j < n; j++) {
                int count = 0;
                for (int k = 0; k < n; k++) {
                    String a = cBoard.get(i).get(k);
                    if (a.equals(cBoard.get(j).get(k)) && !a.equals("E")) {
                        count++;
                    }
                }
                if (count == n) {
                    return false;
                }
            }
        }

        //check if two columns are duplicated

        for (int j = 0; j < n-1; j++) {
            for (int k = j+1; k < n; k++) {
                int count = 0;
                for (int i = 0; i < n; i++) {
                    if (cBoard.get(i).get(j).equals(cBoard.get(i).get(k))) {
                        count++;
                    }
                }
                if (count == n) {
                    return false;
                }
            }
        }

        return true;
    }
    private boolean allAssigned(State state) {
        ArrayList<ArrayList<String>> cBoard = state.getBoard();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                String s = cBoard.get(i).get(j);
                if (s.equals("E"))
                    return false;
            }
        }
        return !unassigned.isEmpty();
        //return true;
    }
    private boolean isFinished(State state) {
        return allAssigned(state) && checkAdjacency(state) && checkNumberOfCircles(state,0,0) && checkIfUnique(state,0,0);
    }
    private boolean isConsistent(State state) {
        return checkNumberOfCircles(state,0,0) && checkAdjacency(state) && checkIfUnique(state,0,0);
}
    private void drawLine() {
        for (int i = 0; i < n*2; i++) {
            System.out.print("\u23E4\u23E4");
        }
        System.out.println();
    }
    //Total time: 0.819276726 with forwardChecking
    // Total time: 0.344224987 without forwardChecking
    //Total time: 0.671447686 whith checkNumberOfCircles(State state,int x,int y)
    private boolean forwardChecking(State s,int c){
        int i;
        State temp = s.copy();
        for(i = unassigned.size()-1 ; i > -1 ; i --){
            if (unassigned.get(i).get(2) == 0) {
                // board.get(unassigned[i][0]).get(unassigned[i][1])
                temp.setIndexBoard(unassigned.get(i).get(0), unassigned.get(i).get(1), "b");
                if (!isConsistent(temp)) {
                    domain.get(unassigned.get(i).get(0)).get(unassigned.get(i).get(1)).remove("b");
                    System.out.println("forwardChecking:" + unassigned.get(i).get(0) + "," + unassigned.get(i).get(1) + "b");
                }
                temp.setIndexBoard(unassigned.get(i).get(0), unassigned.get(i).get(1), "w");
                if (!isConsistent(temp)) {
                    domain.get(unassigned.get(i).get(0)).get(unassigned.get(i).get(1)).remove("w");
                    System.out.println("forwardChecking:" + unassigned.get(i).get(0) + "," + unassigned.get(i).get(1) + "w");
                }

                temp.setIndexBoard(unassigned.get(i).get(0), unassigned.get(i).get(1), "E");
                if (domain.get(unassigned.get(i).get(0)).get(unassigned.get(i).get(1)).isEmpty()) {
                    //   System.out.println("forwardChecking false");
                    return false;
                }
            }
            else System.out.println("exception in forward!!");
        }
        return true;
    }
    private void backForwardChecking(State s,int c) {
        int i,j;
        for (i = unassigned.size() - 1 ;i > -1; i--) {
            if (unassigned.get(i).get(2) == 0) {
                domain.get(unassigned.get(i).get(0)).get(unassigned.get(i).get(1)).clear();
                domain.get(unassigned.get(i).get(0)).get(unassigned.get(i).get(1)).add("w");
                domain.get(unassigned.get(i).get(0)).get(unassigned.get(i).get(1)).add("b");
            }
            else System.out.println("exception in back!!");
        }
    }
    private int MRV(int c){
        int i,ans=-1;
        for(i = unassigned.size()-1;i>=0;i--){
            if(domain.get(unassigned.get(i).get(0)).get(unassigned.get(i).get(1)).size() == 1 && unassigned.get(i).get(2) == 0) {
                ans = i;
                break;
            }
        }
        return ans;
    }
    private int LCV(State s, int c){
        int min = 1000, i, j, ans = 0, domainCount;
        State temp = s.copy();
        for(i = unassigned.size()-1 ; i > -1 ; i --) {
            domainCount = 0;
            for (j = unassigned.size() - 1; j > -1 && unassigned.get(j).get(2) == 0; j--) {
                temp.setIndexBoard(unassigned.get(j).get(0), unassigned.get(j).get(1), "b");
                if (!isConsistent(temp)) {
                    domain.get(unassigned.get(j).get(0)).get(unassigned.get(j).get(1)).remove("b");
                    domainCount++;
                }
                temp.setIndexBoard(unassigned.get(j).get(0), unassigned.get(j).get(1), "w");
                if (!isConsistent(temp)) {
                    domain.get(unassigned.get(j).get(0)).get(unassigned.get(j).get(1)).remove("w");
                    domainCount++;
                }
                temp.setIndexBoard(unassigned.get(j).get(0), unassigned.get(j).get(1), "E");
                if (domain.get(unassigned.get(j).get(0)).get(unassigned.get(j).get(1)).isEmpty()) {
                    break;
                }
                domain.get(unassigned.get(j).get(0)).get(unassigned.get(j).get(1)).clear();
                domain.get(unassigned.get(j).get(0)).get(unassigned.get(j).get(1)).add("w");
                domain.get(unassigned.get(j).get(0)).get(unassigned.get(j).get(1)).add("b");
            }
            if(domainCount < min){
                min = domainCount;
                ans = i;
            }
        }
        return ans;
    }
    private int AC3(State s, int i , int j){
        int xi = unassigned.get(i).get(0);
        int yi = unassigned.get(i).get(1);
        int xj = unassigned.get(j).get(0);
        int yj = unassigned.get(j).get(1);
        State temp = s.copy();
        for(String vi : domain.get(xi).get(yi)) {
            for (String vj : domain.get(xi).get(yi)) {
                temp.setIndexBoard(xi, yi, vi);
                temp.setIndexBoard(xj, yj, vj);
                if (isConsistent(temp)) {

                }
            }
        }
        return  0;
    }
    private boolean revise(int xi, int xj,State s) {
        boolean revised = false;
        for (String vi : domain.get(unassigned.get(xi).get(0)).get(unassigned.get(xi).get(1))) {
            State t = s.copy();
            t.setIndexBoard(unassigned.get(xi).get(0),unassigned.get(xi).get(1),vi);
            boolean consistentExtensionFound = false;
            for (String vj : domain.get(unassigned.get(xj).get(0)).get(unassigned.get(xj).get(1))) {
                t.setIndexBoard(unassigned.get(xj).get(0),unassigned.get(xj).get(1),vj);
                if (isConsistent(s)) {
                    consistentExtensionFound = true;
                    break;
                }
            }
            if (!consistentExtensionFound) {
                unassigned.remove(xi);
                revised = true;
            }
        }
        return revised;
    }
}
