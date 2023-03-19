
import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JFrame;

class RulesPage extends StartScreen {
    
    static JFrame rulesFrame;
    static YourJLabel clsLabel;
    static JPanel pnlJPanel;
    
    public RulesPage(){
        super();
    }
    
    public void displayRules() {
        rulesFrame = new JFrame("Rules of Sudoku");
        rulesFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        rulesFrame.setSize(500, 200);
        rulesFrame.setResizable(false);
        
        pnlJPanel = new JPanel();
        pnlJPanel.setLayout(new GridLayout(5, 1));
        clsLabel = new YourJLabel();
        clsLabel.setText("Every square has to contain a single number");
        pnlJPanel.add(clsLabel);
        
        clsLabel = new YourJLabel();
        clsLabel.setText("Only the numbers from 1 through to 9 can be used");
        pnlJPanel.add(clsLabel);
        
        clsLabel = new YourJLabel();
        clsLabel.setText("Each 3×3 box can only contain each number from 1 to 9 once");
        pnlJPanel.add(clsLabel);
        
        clsLabel = new YourJLabel();
        clsLabel.setText("Each vertical column can only contain each number from 1 to 9 once");
        pnlJPanel.add(clsLabel);
        
        clsLabel = new YourJLabel();
        
        clsLabel.setText("Each horizontal row can only contain each number from 1 to 9 once");
        pnlJPanel.add(clsLabel);
        rulesFrame.add(pnlJPanel);
        
        rulesFrame.setVisible(true);
    }
}