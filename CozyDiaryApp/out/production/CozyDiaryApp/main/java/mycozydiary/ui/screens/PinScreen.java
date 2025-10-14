package mycozydiary.ui.screens;

import mycozydiary.styles.Theme;
import mycozydiary.data.DataManager;
import mycozydiary.ui.Utils;
import javax.swing.*;
import java.awt.*;

public class PinScreen extends JPanel {
    private DataManager dataManager;
    private Runnable onUnlockCallback;

    public PinScreen(DataManager dataManager, Runnable onUnlockCallback) {
        this.dataManager = dataManager;
        this.onUnlockCallback = onUnlockCallback;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_LILAC);

        add(createCenterPanel(), BorderLayout.CENTER);
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.BG_LILAC);
        panel.setBorder(BorderFactory.createEmptyBorder(100, 40, 100, 40));

        JLabel lockLabel = new JLabel("🔒", SwingConstants.CENTER);
        lockLabel.setFont(new Font("Arial", Font.PLAIN, 60));
        lockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Enter PIN");
        titleLabel.setFont(Theme.SUBTITLE_FONT);
        titleLabel.setForeground(Theme.TEXT_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

        JPanel dotsPanel = createDotsPanel();
        dotsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel keypadPanel = createKeypadPanel(dotsPanel);
        keypadPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lockLabel);
        panel.add(titleLabel);
        panel.add(dotsPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(keypadPanel);

        return panel;
    }

    private JPanel createDotsPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(Theme.BG_LILAC);

        for (int i = 0; i < 4; i++) {
            JLabel dot = new JLabel("○");
            dot.setFont(new Font("Arial", Font.PLAIN, 30));
            dot.setForeground(Theme.ACCENT_PURPLE);
            dot.setName("dot" + i);
            panel.add(dot);
        }

        return panel;
    }

    private JPanel createKeypadPanel(JPanel dotsPanel) {
        JPanel panel = new JPanel(new GridLayout(4, 3, 10, 10));
        panel.setBackground(Theme.BG_LILAC);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        panel.setMaximumSize(new Dimension(300, 300));

        StringBuilder currentPin = new StringBuilder();
        String[] buttons = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "×", "0", "⌫"};

        for (String btn : buttons) {
            JButton button = new JButton(btn);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setBackground(Theme.ACCENT_PURPLE);
            button.setForeground(Theme.TEXT_DARK);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            button.addActionListener(e -> handleKeypadInput(btn, currentPin, dotsPanel));
            panel.add(button);
        }

        return panel;
    }

    private void handleKeypadInput(String input, StringBuilder currentPin, JPanel dotsPanel) {
        Component[] dots = dotsPanel.getComponents();

        if (input.equals("×")) {
            currentPin.setLength(0);
            for (Component c : dots) {
                if (c instanceof JLabel) ((JLabel) c).setText("○");
            }
        } else if (input.equals("⌫")) {
            if (currentPin.length() > 0) {
                currentPin.setLength(currentPin.length() - 1);
                ((JLabel) dots[currentPin.length()]).setText("○");
            }
        } else if (currentPin.length() < 4) {
            currentPin.append(input);
            ((JLabel) dots[currentPin.length() - 1]).setText("●");

            if (currentPin.length() == 4) {
                if (dataManager.verifyPin(currentPin.toString())) {
                    onUnlockCallback.run();
                    currentPin.setLength(0);
                    for (Component c : dots) {
                        if (c instanceof JLabel) ((JLabel) c).setText("○");
                    }
                } else {
                    Utils.showErrorDialog(this, "Incorrect PIN!");
                    currentPin.setLength(0);
                    for (Component c : dots) {
                        if (c instanceof JLabel) ((JLabel) c).setText("○");
                    }
                }
            }
        }
    }
}
