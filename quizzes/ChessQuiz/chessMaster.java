import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class chessMaster extends JFrame {
    private static final long serialVersionUID = 1L;
	private int questionIndex = 0;
    private int score = 0;

    private JLabel questionLabel;
    private ButtonGroup answerGroup;
    private JRadioButton[] answerButtons;
    private JButton nextButton;
    private String[] questions = {
            "What are the number of squares on the chess board?",
            "Pawn cannot promote itself to which of the following peices ?",
            "What are the points of all the peices?",
            "Which of the peice is the most tricky peice.",
            "Queen is combination of which two peices"
    };

    private String[][] answerChoices = {
            {"86", "48", "64", "96", "16"},
            {"Rook", "Knight", "Bishop", "Queen", "None of the above"},
            {"(5,3,3,9,undefined,1)", "(8,4,7,2,undefined,1)", "(10,12,5,9,undefined,1)", "(1,5,3,3,undefined,2)","(0,6,7,10,undefined,2)"},
            {"Bishop", "Knight", "Rook", "Queen", "King"},
            {"Knight and Bishop", "Knight and Rook", "Bishop and rook", "Bishop and pawn", "Knight and pawn"}
    };

    public chessMaster() {
        setTitle("Calculus Quiz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(740, 450);
        setLocationRelativeTo(null);

        // Load the background image
        BufferedImage backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(getClass().getResource("images/chess.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage finalBackgroundImage = backgroundImage;
        JPanel mainPanel = new JPanel() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (finalBackgroundImage != null) {
                    g.drawImage(finalBackgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        setContentPane(mainPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        questionLabel = new JLabel(questions[questionIndex]);
        questionLabel.setFont(new Font("Courier New", Font.BOLD, 26));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionLabel.setForeground(Color.WHITE);
        mainPanel.add(questionLabel);

        answerGroup = new ButtonGroup();
        answerButtons = new JRadioButton[5];
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        for (int i = 0; i < 5; i++) {
            answerButtons[i] = new JRadioButton("");
            answerButtons[i].setOpaque(false);
            answerButtons[i].setFont(new Font("Courier New", Font.BOLD, 23));
            answerGroup.add(answerButtons[i]);
            answerButtons[i].setForeground(Color.WHITE);

            answerButtons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(answerButtons[i]);
        }

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        nextButton = new JButton("Next");
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processAnswer();
                if (questionIndex < 4) {
                    questionIndex++;
                    shuffleAnswerIndices();
                    updateQuestion();
                } else {
                    showResult();
                }
            }
        });

        nextButton.setBackground(Color.WHITE);
        nextButton.setForeground(Color.DARK_GRAY);
        nextButton.setFont(new Font("Courier New", Font.BOLD, 15));
        mainPanel.add(nextButton);

        shuffleAnswerIndices();
        setVisible(true);
    }

    private void shuffleAnswerIndices() {
        List<Integer> shuffledIndices = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            shuffledIndices.add(i);
        }
        Collections.shuffle(shuffledIndices, new Random());

        // Update the radio buttons with shuffled answers
        for (int i = 0; i < 5; i++) {
            answerButtons[i].setText(answerChoices[questionIndex][shuffledIndices.get(i)]);
        }
    }

    private void processAnswer() {
        String selectedAnswer = null;
        for (int i = 0; i < 5; i++) {
            if (answerButtons[i].isSelected()) {
                selectedAnswer = answerButtons[i].getText();
                break;
            }
        }

        // Check if the selected answer matches the correct answer (0th index of answerChoices)
        if (selectedAnswer != null && selectedAnswer.equals(answerChoices[questionIndex][0])) {
            score++;
        }
    }

    private void updateQuestion() {
        questionLabel.setText(questions[questionIndex]);
        answerGroup.clearSelection();
        shuffleAnswerIndices();
    }

    private void showResult() {
        questionLabel.setVisible(false);
        for (int i = 0; i < 5; i++) {
            answerButtons[i].setVisible(false);
        }
        nextButton.setVisible(false);
        JLabel resultLabel = new JLabel("Your score: " + score + "/5");
        resultLabel.setHorizontalAlignment(JLabel.CENTER);
        resultLabel.setFont(new Font("Lucida Calligraphy", Font.BOLD, 30));

        JFrame resultFrame = new JFrame("Quiz Result");
        resultFrame.setLayout(new BorderLayout());
        resultFrame.add(resultLabel, BorderLayout.CENTER);
        resultFrame.setSize(400, 200);
        resultFrame.setLocationRelativeTo(this);
        resultFrame.setVisible(true);
        resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new chessMaster();
            }
        });
    }
}
