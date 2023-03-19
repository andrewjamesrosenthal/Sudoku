
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Sudoku {

    private int[][] board;
    private final int[][] originalBoard;
    private Scanner sc;
    private File file;
    private int[] lastMove = {0, 0, 0};

    public Sudoku(int[][] newBoard) {
        board = new int[9][9];
        originalBoard = new int[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                board[r][c] = newBoard[r][c];
                originalBoard[r][c] = newBoard[r][c];
            }
        }
        //printBoard(); //for debugging
    }

    public void makeMove(int r, int c, int num) {
        if ((board[r][c] == 0) && (moveLegal(r, c, num)) && (moveCorrect(r, c, num))) {
            board[r][c] = num;
            lastMove[0] = r;
            lastMove[1] = c;
            lastMove[2] = num;
        } else if (board[r][c] != 0) {
            System.out.println("Space is already taken");
        } else if (!moveLegal(r, c, num) || !moveCorrect(r, c, num)) {
            System.out.println("This move is illegal or incorrect");
        }
    }

    public void undoMove() {
        if (lastMove[2] != 0) { //make sure there was a last move
            System.out.println("Your last move has been erased.");
            board[lastMove[0]][lastMove[1]] = 0;
        } else {
            System.out.println("No moves made yet");
        }
    }
//Checks to see if the move would be in the solved puzzle
    public boolean moveCorrect(int row, int col, int num) {
        if (inRow(num, row)) {
            return false;
        }
        if (inCol(num, col)) {
            return false;
        }
        if (inBox(num, row, col)) {
            return false;
        }
        return true;
    }

//Checks to see if the move is legal or not
    public boolean moveLegal(int row, int col, int num) {
        //System.out.println(row + " , " + num + " , " + num);
        if (row > -1 && row < 9 && col > -1 && col < 10 && num > 0 && num < 10) {
            return true;

        }
        return false;
    }

    public int emptyCellCount() {
        int count = 0;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public void printBoard() { //For debugging
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                System.out.print(board[r][c] + " ");
            }
            System.out.println();
        }
    }

    public int getBoard(int r, int c) {
        return board[r][c];
    }

    public boolean inRow(int num, int row) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num) {
                //System.out.println("in row" + num); for debugging
                return true;
            }
        }
        return false;
    }

    public boolean inCol(int num, int col) {
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == num) {
                return true;
            }
        }
        return false;
    }

    public boolean inBox(int num, int row, int col) {
        int effectiveRow = (row / 3); //to figure out which row grid
        int effectiveCol = (col / 3); //to figure out which col grid
        for (int r = (effectiveRow * 3); r < (effectiveRow * 3) + 3; r++) {
            for (int c = (effectiveCol * 3); c < (effectiveCol * 3) + 3; c++) {
                //System.out.print(r + "," + c + " "); //for debugging
                if (board[r][c] == num) {
                    return true;
                }
            }
        }
        return false;
    }

    //Solver works using recursion
    //Starts by finding one valid move
    //Continues by finding if other cells are solvable given that one move
    //Recurs each time
    //Pretty much a smart version of brute forcing the solving
    public boolean solve() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                //For every empty cell, tries first legal entry and continues to next cell
                //If it doesn't work, program goes back to the previous cell and tries a different value
                if (board[r][c] == 0) { //if the cell is empty
                    for (int n = 1; n < 10; n++) { //try for every value
                        if (moveCorrect(r, c, n)) { //checks if move is legal
                            board[r][c] = n;
                            //System.out.println(r + " " + c); //for debugging
                            //Checks that this legal move allows other moves to be legal with reccursion
                            if (solve()) {
                                return true;
                            } else {
                                board[r][c] = 0; //resets to empty if move doesn't work
                            }
                        }
                    }
                    return false; //returns false if there's no combination
                }
            }
        }
        return true; //returns true if move is legal
    }

    public void resetBoard() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                board[r][c] = originalBoard[r][c];
            }
        }
        //printBoard(); //for debugging
    }
    
    public void getHint(){
        int[][] temp = new int[9][9];
        for (int r = 0; r < 9; r++){
            for (int c = 0; c < 9; c++){
                temp[r][c] = board[r][c];
            }
        }
        solve();
        outerloop: //labelling it to break out of outerloop
        for (int r = 0; r < 9; r++){
            for (int c = 0; c < 9; c++){
                if (temp[r][c]==0){
                    temp[r][c] = board[r][c];
                    break outerloop;
                }
            }
        }
        for (int r = 0; r < 9; r++){
            for (int c = 0; c < 9; c++){
                board[r][c] = temp[r][c];
            }
        }
    }
    
    public void check(){ //removes incorrect moves
        //System.out.println("checking"); //for debugging
        int[][] temp = new int[9][9];
        for (int r = 0; r < 9; r++){
            for (int c = 0; c < 9; c++){
                temp[r][c] = board[r][c];
                board[r][c] = originalBoard[r][c];
            }
        }
        solve();
        //printBoard(); //for debugging
        for (int r = 0; r < 9; r++){
            for (int c = 0; c < 9; c++){
                if (!(temp[r][c]==0) && !(temp[r][c]==board[r][c])){
                    temp[r][c] = 0;
                }
            }
        }
        for (int r = 0; r < 9; r++){
            for (int c = 0; c < 9; c++){
                board[r][c] = temp[r][c];
            }
        }
        //printBoard(); //for debugging
    }
}
