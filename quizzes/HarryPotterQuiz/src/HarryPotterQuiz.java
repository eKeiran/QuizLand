import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class HarryPotterQuiz extends JFrame {
    private int questionIndex = 0;
    private int[] characterScores = new int[5];

    private JLabel questionLabel;
    private ButtonGroup answerGroup;
    private JRadioButton[] answerButtons;
    private JButton nextButton;

    private JLabel characterImageLabel;

    private String[] questions = {
            "What's your favorite magical creature?",
            "Which subject at Hogwarts do you excel in?",
            "What would you do if you found a lost item?",
            "Choose your favorite Hogwarts house?",
            "What's your preferred mode of transportation?",
            "Pick a magical pet:",
            "How will you spend a weekend at Hogwarts?"
    };

    private String[][] answerChoices = {
            {"Hippogriff", "Phoenix", "House-Elf", "Thestral", "Dragon"},
            {"Defense Against the Dark Arts", "Transfiguration", "Potions", "Charms", "Divination"},
            {"Try to find its owner", "Leave it there", "Take it to the authorities", "Keep it", "Use a spell to locate the owner"},
            {"Gryffindor", "Hufflepuff", "Slytherin", "Ravenclaw", "I don't care about houses"},
            {"Broomstick", "FLying Carpet", "Apparate", "Thestrals", "Floo Network"},
            {"Owl", "Rat", "Cat", "Pygmy Puff", "I hate pets"},
            {"Practicing spells", "Exploring the Forbidden Forest", "Reading in the library", "Solving mysteries", "Visiting Hogsmeade"}
    };

    public HarryPotterQuiz() {
        setTitle("Which Harry Potter Character Are You?");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 450); 
        setLocationRelativeTo(null);

        // Load the background image
        BufferedImage backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(getClass().getResource("img/bg.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a JPanel with a background image
        BufferedImage finalBackgroundImage = backgroundImage;
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (finalBackgroundImage != null) {
                    g.drawImage(finalBackgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        setContentPane(mainPanel); // Use setContentPane to set the content pane with the background image

        // Add margin before the title
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        questionLabel = new JLabel(questions[questionIndex]);
        questionLabel.setFont(new Font("Lucida Calligraphy", Font.BOLD, 25));
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        questionLabel.setForeground(Color.WHITE); 



        mainPanel.add(questionLabel);
        characterImageLabel = new JLabel();
        characterImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(characterImageLabel);
        // Add margin after the title (before radio options)
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        answerGroup = new ButtonGroup();
        answerButtons = new JRadioButton[5];

        for (int i = 0; i < 5; i++) {
            answerButtons[i] = new JRadioButton(answerChoices[questionIndex][i]);
            answerButtons[i].setOpaque(false); // Set the background of radio buttons to be transparent
            answerButtons[i].setFont(new Font("Lucida Calligraphy", Font.BOLD, 22));
            answerButtons[i].setForeground(Color.WHITE); 
            answerButtons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            answerGroup.add(answerButtons[i]);
            mainPanel.add(answerButtons[i]);
        }

        // Add margin on top of the "Next" button
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        nextButton = new JButton("Next");
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center-align the button
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processAnswer();
                if (questionIndex < 6) {
                    questionIndex++;
                    updateQuestion();
                } else {
                    showResult();
                }
            }
        });

        nextButton.setBackground(Color.WHITE);
        nextButton.setForeground(Color.DARK_GRAY); 
        nextButton.setFont(new Font("Lucida Calligraphy", Font.BOLD, 15)); 
        mainPanel.add(nextButton);
        setVisible(true);
    }

    private void processAnswer() {
        for (int i = 0; i < 5; i++) {
            if (answerButtons[i].isSelected()) {
                characterScores[i]++;
                break;
            }
        }
    }

    private void updateQuestion() {
        questionLabel.setText(questions[questionIndex]);
        for (int i = 0; i < 5; i++) {
            answerButtons[i].setText(answerChoices[questionIndex][i]);
            answerButtons[i].setSelected(false);

        }
    }

    private void showResult() {
        int maxScoreIndex = 0;
        for (int i = 1; i < 5; i++) {
            if (characterScores[i] > characterScores[maxScoreIndex]) {
                maxScoreIndex = i;
            }
        }

        String character = "";
        String imageName = "";

        switch (maxScoreIndex) {
            case 0:
                character = "Harry Potter";
                imageName = "img/harry.jpg"; 
                break;
            case 1:
                character = "Ron Weasley";
                imageName = "img/ron.jpg"; 
                break;
            case 2:
                character = "Hermione";
                imageName = "img/hermione.jpg";
                break;
            case 3:
                character = "Luna Lovegood";
                imageName = "img/luna.jpg";
                break;
            case 4:
                character = "Draco Malfoy";
                imageName = "img/draco.jpg"; 
                break;
        }


        // Remove the questionLabel, nextButton answerButtons when showing result
        questionLabel.setVisible(false);
        nextButton.setVisible(false);
        for (int i = 0; i < 5; i++) {
            answerButtons[i].setVisible(false);
        }
        // Display character image
        try {
            BufferedImage characterImage = ImageIO.read(getClass().getResource(imageName));
            characterImageLabel.setIcon(new ImageIcon(characterImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JDialog resultDialog = new JDialog(this, "Result", true);
        resultDialog.setLayout(new BorderLayout());
        JLabel resultLabel = new JLabel("You're " + character + "!");
        resultLabel.setHorizontalAlignment(JLabel.CENTER);
        resultLabel.setFont(new Font("Lucida Calligraphy", Font.BOLD, 30)); 

        resultDialog.add(resultLabel, BorderLayout.NORTH);
        resultDialog.add(characterImageLabel, BorderLayout.CENTER);
        resultDialog.setSize(400, 500); // Adjust the size as needed
        resultDialog.setLocationRelativeTo(this);
        resultDialog.setVisible(true);

        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HarryPotterQuiz();
            }
        });
    }
}
