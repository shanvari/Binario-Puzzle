import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.min;

public class Main {

    public static void main(String[] args) {

        File input = new File("inputs/input2.txt");
        ArrayList<ArrayList<String>> board = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<String>>> domain = new ArrayList<>();
        try {
            Scanner reader = new Scanner(input);
            int n = reader.nextInt();
            for (int i = 0; i < n; i++) {
                board.add(new ArrayList<>());
                domain.add(new ArrayList<>());
                for (int j = 0; j < n; j++) {
                    board.get(i).add("E"); //empty
                    domain.get(i).add(new ArrayList<>(Arrays.asList(
                            "w",
                            "b"
                    )));
                }
            }
            int m = reader.nextInt();
            for (int i = 0; i < m; i++) {
                int y = reader.nextInt();
                int x = reader.nextInt();
                int z = reader.nextInt();
                String a = null;
                switch (z) {
                    case 0 : {
                        a ="W"; //white
                        break;
                    }
                    case 1 : a = "B"; //black
                };

                board.get(y).set(x, a);
                domain.get(y).set(x, new ArrayList<>(List.of(
                        "n"
                )));
            }
            //helping array
            int [][] rowCount = new int [n][2];
            int [][] colCount = new int [n][2];
            for (int i =0 ; i < n ; i ++){
                rowCount[i][0] = 0;
                rowCount[i][1] = 0;
                colCount[i][0] = 0;
                colCount[i][1] = 0;
            }
            int count = 0;
            ArrayList<ArrayList<Integer>> unassigned = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    switch(board.get(i).get(j)) {
                        case "E" :{
                            unassigned.add(new ArrayList<>());
                            unassigned.get(count).add(0,i);
                            unassigned.get(count).add(1,j);
                            unassigned.get(count).add(2,0);
                            count ++;
                        break;
                        }
                        case "W":{
                            rowCount[i][0] ++;
                            colCount[j][0] ++;
                            break;
                        }
                        case "B":{
                            rowCount[i][1] ++;
                            colCount[j][1] ++;
                            break;
                        }
                    }
                }
            }
            System.out.println("unassigned = " + count);
            //Board and Domain initialized
            Binairo binairo = new Binairo(board, domain, n,unassigned,count-1,colCount,rowCount);
            binairo.start();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
