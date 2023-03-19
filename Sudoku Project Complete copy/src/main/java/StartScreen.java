
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;

public class StartScreen implements ActionListener {

    private static Scanner sc;
    private static File file;
    private Sudoku game;
    private ArrayList<Sudoku> games;

    public StartScreen(){
        try{
            file = new File ("SudokuInputFile.txt");
            sc = new Scanner(file);
        }
        catch (Exception e){
            System.out.println("File input name incorrect");
        }
        
        games = new ArrayList<Sudoku>();
        fillGames();

        JFrame startFrame = new JFrame("Start Screen");
        startFrame.setSize(200, 200);
        startFrame.setResizable(false);
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton startBtn = new JButton("Start");
        JButton rulesBtn = new JButton("Rules");
        JButton exitBtn = new JButton ("Exit");

        startBtn.addActionListener(this);
        rulesBtn.addActionListener(this);
        exitBtn.addActionListener(this);

        Container pane = startFrame.getContentPane();
        pane.setLayout(new FlowLayout());
        pane.add(startBtn);
        pane.add(rulesBtn);
        pane.add(exitBtn);

        startFrame.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae){
        if (ae.getActionCommand().equals("Rules")) {
            RulesPage rules = new RulesPage();
            rules.displayRules();
        }
        if (ae.getActionCommand().equals("Start")) {
            System.out.println("A new game has started.");
            newGame();
            SudokuBoard play = new SudokuBoard(game);
        }
        if(ae.getActionCommand().equalsIgnoreCase("Exit")){
            System.exit(0);
        }
    }
    
    public void fillGames(){
        outerloop: //labelling so that we can continue from inside a nested loop
        while (sc.hasNext()){
            int[][] tempBoard = new int[9][9];
            for (int r = 0; r < 9; r++){
                for (int c = 0; c < 9; c++){
                    if (sc.hasNextInt()){
                        tempBoard[r][c] = sc.nextInt();
                        //System.out.print(tempBoard[r][c] + " "); //for debugging
                    }
                    else{
                        continue outerloop; //skips to next line and start new board if it doesn't have 9 integers
                    }
                }
                sc.nextLine();
            }
            
            //Make a copy of tempBoard
            int[][] tempBoard2 = new int[9][9];
            for (int i = 0; i < 9; i++){
                for (int j = 0; j < 9; j++){
                    tempBoard2[i][j] = tempBoard[i][j];
                }
            }
            Sudoku tempGame1 = new Sudoku(tempBoard); //Made two temps to check for solvable
            if (tempGame1.solve()){
                Sudoku tempGame2 = new Sudoku(tempBoard2); //made tempGame2 because tempGame1 is now solved
                //tempGame2.printBoard(); //for debugging
                games.add(tempGame2);
            } //only adds if solvable
        }
        //System.out.println(games.size()); //for debugging
    }
    
    public void newGame (){
        if (games.size() == 0){
            System.out.println("No more games");
            System.exit(0);
        }
        int temp = (int)(Math.random()*(games.size()));
        game = games.get(temp);
        games.remove(temp);
    }
}
