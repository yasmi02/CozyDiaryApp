// ==================== FILE: src/main/java/mycozydiary/ui/screens/JournalScreen.java ====================
package mycozydiary.ui.screens;

import mycozydiary.styles.Theme;
import mycozydiary.data.*;
import mycozydiary.ui.Utils;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JournalScreen extends JPanel {
    private DataManager dataManager;
    private Runnable onSaveCallback;
    private JTextField titleField;
    private JTextArea contentArea;
    private String selectedMood = "😊";

    public JournalScreen(DataManager dataManager, Runnable backAction, Runnable onSaveCallback) {
        this.dataManager = dataManager;
        this.onSaveCallback = onSaveCallback;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_LILAC);

        add(createHeader(backAction), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
    }

    private JPanel createHeader(Runnable backAction) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.LIGHT_LILAC);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JButton backBtn = new JButton("←");
        backBtn.setFont(new Font("Arial", Font.BOLD, 20));
        backBtn.setBackground(Theme.LIGHT_LILAC);
        backBtn.setForeground(Theme.TEXT_DARK);
        backBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> backAction.run());

        JLabel titleLabel = new JLabel("New Entry 📝");
        titleLabel.setFont(Theme.SUBTITLE_FONT);
        titleLabel.setForeground(Theme.TEXT_DARK);

        header.add(backBtn, BorderLayout.WEST);
        header.add(titleLabel, BorderLayout.CENTER);

        return header;
    }

    private JScrollPane createContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_LILAC);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel moodPanel = createMoodSelector();

        JLabel titlePrompt = new JLabel("Title");
        titlePrompt.setFont(Theme.SMALL_FONT);
        titlePrompt.setForeground(Theme.TEXT_DARK);

        titleField = new JTextField();
        titleField.setFont(new Font("Arial", Font.BOLD, 18));
        titleField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Theme.ACCENT_PURPLE, 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        titleField.setBackground(Theme.WHITE);

        JLabel textPrompt = new JLabel("What's on your mind?");
        textPrompt.setFont(Theme.SMALL_FONT);
        textPrompt.setForeground(Theme.TEXT_DARK);

        contentArea = new JTextArea(15, 30);
        contentArea.setFont(Theme.BODY_FONT);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentArea.setBackground(Theme.WHITE);

        JScrollPane textScrollPane = new JScrollPane(contentArea);
        textScrollPane.setBorder(new LineBorder(Theme.ACCENT_PURPLE, 2, true));

        JButton saveBtn = Utils.createStyledButton("Save Entry ♡", Theme.DEEP_LILAC, Theme.WHITE);
        saveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveBtn.addActionListener(e -> saveEntry());

        content.add(moodPanel);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(titlePrompt);
        content.add(Box.createRigidArea(new Dimension(0, 5)));
        content.add(titleField);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(textPrompt);
        content.add(Box.createRigidArea(new Dimension(0, 5)));
        content.add(textScrollPane);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(saveBtn);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBackground(Theme.BG_LILAC);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }

    private JPanel createMoodSelector() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Theme.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Theme.ACCENT_PURPLE, 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel label = new JLabel("How are you? ");
        label.setFont(Theme.BODY_FONT);
        label.setForeground(Theme.TEXT_DARK);
        panel.add(label);

        // Mood dropdown button
        JButton moodButton = new JButton(selectedMood + " Tap to change");
        moodButton.setFont(new Font("Arial", Font.PLAIN, 18));
        moodButton.setBackground(Theme.ACCENT_PURPLE);
        moodButton.setForeground(Theme.WHITE);
        moodButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.DEEP_LILAC, 2, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        moodButton.setFocusPainted(false);
        moodButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        moodButton.addActionListener(e -> showMoodPicker(moodButton));

        panel.add(moodButton);

        return panel;
    }

    private void showMoodPicker(JButton moodButton) {
        JDialog moodDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Choose Your Mood", true);
        moodDialog.setSize(300, 400);
        moodDialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Theme.BG_LILAC);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("How are you feeling? 💭");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Theme.TEXT_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // All mood options
        String[][] moods = {
                {"😊", "Happy"},
                {"😄", "Very Happy"},
                {"😐", "Neutral"},
                {"😢", "Sad"},
                {"😴", "Sleepy"},
                {"😡", "Angry"},
                {"🤒", "Sick"},
                {"😍", "Love"},
                {"😎", "Cool"},
                {"🤗", "Excited"},
                {"😰", "Anxious"},
                {"🥳", "Celebrating"},
                {"😌", "Peaceful"},
                {"🤔", "Thoughtful"}
        };

        for (String[] mood : moods) {
            JButton btn = createMoodOptionButton(mood[0], mood[1], moodButton, moodDialog);
            mainPanel.add(btn);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        moodDialog.add(scrollPane);
        moodDialog.setVisible(true);
    }

    private JButton createMoodOptionButton(String emoji, String name, JButton moodButton, JDialog dialog) {
        JButton btn = new JButton(emoji + "  " + name);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btn.setBackground(Theme.WHITE);
        btn.setForeground(Theme.TEXT_DARK);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT_PURPLE, 2, true),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(250, 60));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Highlight if currently selected
        if (selectedMood.equals(emoji)) {
            btn.setBackground(Theme.ACCENT_PURPLE);
            btn.setForeground(Theme.WHITE);
        }

        btn.addActionListener(e -> {
            selectedMood = emoji;
            moodButton.setText(emoji + " Tap to change");
            dialog.dispose();
        });

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalBg = btn.getBackground();
            Color originalFg = btn.getForeground();

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!selectedMood.equals(emoji)) {
                    btn.setBackground(Theme.LIGHT_LILAC);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!selectedMood.equals(emoji)) {
                    btn.setBackground(originalBg);
                }
            }
        });

        return btn;
    }

    private void saveEntry() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Utils.showErrorDialog(this, "Please fill in both title and content!");
            return;
        }

        DiaryEntry entry = new DiaryEntry(title, content, LocalDateTime.now(), selectedMood);
        dataManager.addEntry(LocalDate.now(), entry);
        dataManager.setMood(LocalDate.now(), selectedMood);

        Utils.showInfoDialog(this, "Entry saved! ♡", "Success");

        titleField.setText("");
        contentArea.setText("");

        if (onSaveCallback != null) {
            onSaveCallback.run();
        }
    }

    public void clearFields() {
        titleField.setText("");
        contentArea.setText("");
        selectedMood = "😊";
    }
}

