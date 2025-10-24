import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MemoryPuzzle extends JFrame implements ActionListener {

    private final int GRID_SIZE = 4; // 4x4 grid (8 pairs)
    private JButton[] buttons;
    private Integer[] values;
    private JButton firstClicked, secondClicked;
    private Timer timer;
    private int pairsFound = 0;

    public MemoryPuzzle() {
        setTitle("Memory Puzzle Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        setSize(400, 400);

        buttons = new JButton[GRID_SIZE * GRID_SIZE];
        values = new Integer[GRID_SIZE * GRID_SIZE];

        // Create values (pairs)
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= (GRID_SIZE * GRID_SIZE) / 2; i++) {
            numbers.add(i);
            numbers.add(i);
        }

        // Shuffle values
        Collections.shuffle(numbers);
        for (int i = 0; i < numbers.size(); i++) {
            values[i] = numbers.get(i);
        }

        // Create buttons
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.BOLD, 24));
            buttons[i].addActionListener(this);
            add(buttons[i]);
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();

        // If timer is running, ignore clicks
        if (timer != null && timer.isRunning()) return;

        // If same button clicked again, ignore
        if (clicked == firstClicked) return;

        // Reveal clicked card
        int index = Arrays.asList(buttons).indexOf(clicked);
        clicked.setText(String.valueOf(values[index]));

        if (firstClicked == null) {
            firstClicked = clicked;
        } else {
            secondClicked = clicked;

            // Check for match
            int firstIndex = Arrays.asList(buttons).indexOf(firstClicked);
            int secondIndex = index;

            if (values[firstIndex].equals(values[secondIndex])) {
                firstClicked.setEnabled(false);
                secondClicked.setEnabled(false);
                pairsFound++;

                firstClicked = null;
                secondClicked = null;

                // Check for win
                if (pairsFound == (GRID_SIZE * GRID_SIZE) / 2) {
                    JOptionPane.showMessageDialog(this, "🎉 You won the game!");
                }
            } else {
                // Delay before flipping back
                timer = new Timer(700, new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        firstClicked.setText("");
                        secondClicked.setText("");
                        firstClicked = null;
                        secondClicked = null;
                        timer.stop();
                    }
                });
                timer.start();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MemoryPuzzle::new);
    }
}
