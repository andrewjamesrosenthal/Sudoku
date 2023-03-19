import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;



public class SudokuBoard implements ActionListener{
    
    private JPanel[][] panelHolder;
    private static Sudoku game;
    private JFrame frame;
    private Container pane;
    private JTextField row;
    private JTextField col;
    private JTextField num;
    private JLabel rLab;
    private JLabel cLab;
    private JLabel nLab;
    private Color sudokuGray;
    private SudokuTimer counting;
    private int time;
    private Timer tmr = new Timer(1000, this);
    private JLabel timeLabel = new JLabel();
    private JLabel undoLabel = new JLabel();
    private boolean isTimerRunning;
    
    public SudokuBoard(Sudoku game) {
        this.game = game;
        frame = new JFrame();
        frame.setSize(1100, 700); //11 by 17 - space around grid and space for buttons
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        isTimerRunning = false;
        time = 0;

        //Create buttons and textfields
        JButton makeMoveBtn = new JButton("Move");
        JButton undoMoveBtn = new JButton("Undo");
        JButton hintBtn = new JButton("Hint");
        JButton solveBtn = new JButton("Solve");
        JButton progressBtn = new JButton("Progress");
        JButton exitBtn = new JButton("Exit");
        JButton pauseBtn = new JButton("Pause");
        JButton resumeBtn = new JButton("Resume");
        JButton checkBtn = new JButton("Check");
        JLabel timeLabel = new JLabel("Timer");
        row = new JTextField(2);
        col = new JTextField(2);
        num = new JTextField(2);
        row.setEditable(true);
        col.setEditable(true);
        num.setEditable(true);
        rLab = new JLabel("Row:");
        cLab = new JLabel("Col:");
        nLab = new JLabel("Num:");
        timeLabel.setText("00:00:00");
        undoLabel.setText("");

        //Add action listener to buttons
        makeMoveBtn.addActionListener(this);
        undoMoveBtn.addActionListener(this);
        hintBtn.addActionListener(this);
        solveBtn.addActionListener(this);
        progressBtn.addActionListener(this);
        exitBtn.addActionListener(this);
        pauseBtn.addActionListener(this);
        resumeBtn.addActionListener(this);
        checkBtn.addActionListener(this);

        Color sudokuGray = new Color(238, 238, 238);

        //Add components to JPanel
        pane = frame.getContentPane();
        pane.setLayout(new GridLayout(11, 17));
        panelHolder = new JPanel[11][17]; //create grid to add labels to
        for (int r = 0; r < 11; r++) { //initialize grid
            for (int c = 0; c < 17; c++) {
                panelHolder[r][c] = new JPanel();
                pane.add(panelHolder[r][c]);
            }
        }
        //Create sudoku grid
        for (int r = 1; r < 10; r++) {
            for (int c = 1; c < 10; c++) {
                panelHolder[r][c].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        }

        for (int r = 1; r < 10; r++) {
            for (int c = 1; c < 10; c++) {
                if (this.game.getBoard(r - 1, c - 1) != 0) {
                    JTextField tempField = new JTextField(this.game.getBoard(r - 1, c - 1) + "");
                    tempField.setBackground(sudokuGray);
                    tempField.setBorder(null);
                    tempField.setEditable(false);
                    Font fieldFont = new Font(tempField.getFont().getName(), tempField.getFont().getStyle(), 45);
                    tempField.setFont(fieldFont);
                    panelHolder[r][c].add(tempField);
                }
            }
        }

        panelHolder[1][12].add(timeLabel);
        panelHolder[1][15].add(pauseBtn);
        panelHolder[2][15].add(resumeBtn);
        panelHolder[3][15].add(undoMoveBtn);
        panelHolder[4][15].add(hintBtn);
        panelHolder[5][15].add(solveBtn);
        panelHolder[6][15].add(progressBtn);
        panelHolder[7][15].add(checkBtn);
        panelHolder[8][15].add(exitBtn);

        panelHolder[4][11].add(rLab);
        panelHolder[5][11].add(cLab);
        panelHolder[6][11].add(nLab);
        panelHolder[4][12].add(row);
        panelHolder[5][12].add(col);
        panelHolder[6][12].add(num);
        panelHolder[7][11].add(makeMoveBtn);

        frame.setVisible(true);
        counting = new SudokuTimer();
        counting.resume();

    }
    
    public void actionPerformed (ActionEvent ae){
        if (ae.getActionCommand().equals("Exit")){
            System.out.println("The game has been ended.");
            frame.dispose();
        }
        if (ae.getActionCommand().equals("Move")){
            try{
                int tempRow = Integer.parseInt(row.getText());
                int tempCol = Integer.parseInt(col.getText());
                int tempNum = Integer.parseInt(num.getText());
                if ((tempRow < 10) && (tempRow > 0) &&
                        (tempCol < 10) && (tempCol > 0) &&
                        (tempNum < 10) && (tempNum > 0)){
                    game.makeMove(tempRow-1, tempCol-1, tempNum);
                    setBoard(game);
                } else System.out.println("Please enter integers between 1 and 9.");
                //game.printBoard(); //for debugging
                updateBoard();
            } catch (Exception e){
                System.out.println("Please enter integers between 1 and 9.");
            }
        }
        if (ae.getActionCommand().equals("Undo")){
            game.undoMove();
            setBoard(game);
            updateBoard();
        }
        if (ae.getActionCommand().equals("Hint")){
            game.getHint();
            setBoard(game);
            updateBoard();
        }
        if (ae.getActionCommand().equals("Solve")){
            game.resetBoard();
            if (game.solve()){
                System.out.println("Solved");
            }
            else {
                System.out.println("Couldn't solve - unsolvable");
            }
            updateBoard();
            //game.printBoard(); //for debugging
        }
        if (ae.getActionCommand().equals("Progress")){
            System.out.println(game.emptyCellCount() + " cells remaining");
        }
        if (ae.getActionCommand().equals("Pause")) {
            counting.pause();
        }
        if (ae.getActionCommand().equals("Check")) {
            game.check();
            setBoard(game);
            updateBoard();
        }
        if (ae.getActionCommand().equals("Resume")) {
            counting.resume();
        }
    }
    
    public void updateBoard() {
        for (int r = 1; r < 10; r++) {
            for (int c = 1; c < 10; c++) {
                panelHolder[r][c].removeAll();
                if (game.getBoard(r - 1, c - 1) != 0) {
                    JTextField temp = new JTextField(game.getBoard(r - 1, c - 1) + "");

                    //remove individual components
                    Component[] components = panelHolder[r][c].getComponents();
                    for (Component component : components) {
                        panelHolder[r][c].remove(component);
                    }
                    temp.setBackground(sudokuGray);
                    temp.setBorder(null);
                    Font fieldFont = new Font(temp.getFont().getName(), temp.getFont().getStyle(), 45);
                    temp.setFont(fieldFont);
                    panelHolder[r][c].removeAll();
                    panelHolder[r][c].add(temp);
                    panelHolder[r][c].revalidate();
                    panelHolder[r][c].repaint();
                }
            }
        }
        //Refresh JFrame
        frame.revalidate();
        frame.repaint();
    }
    
    public void setBoard(Sudoku game){
        this.game = game;
    }
    
    
    public class SudokuTimer implements ActionListener {

        private int count = 0;
        private Timer tmr = new Timer(1000, this);

        public SudokuTimer() {
            count = 0;
            timeLabel.setText(TimeFormat(count));
        }

        public void actionPerformed(ActionEvent ae) {
            if (isTimerRunning) {
                count++;
                timeLabel.setText(TimeFormat(count));
                panelHolder[1][12].removeAll();
                panelHolder[1][12].add(timeLabel);
            }
        }

        public void resume() {
            isTimerRunning = true;
            tmr.restart();
        }
//Sets isTim
        public void pause() {
            isTimerRunning = false;
        }

    }
//Formats the time into the timer format
    private String TimeFormat(int count) {

        int hours = count / 3600;
        int minutes = (count - hours * 3600) / 60;
        int seconds = count - minutes * 60;

        return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }
}