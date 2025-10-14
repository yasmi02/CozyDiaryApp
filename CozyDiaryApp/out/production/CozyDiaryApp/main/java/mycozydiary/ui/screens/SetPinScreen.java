package mycozydiary.ui.screens;

import mycozydiary.styles.Theme;
import mycozydiary.data.DataManager;
import mycozydiary.ui.Utils;
import javax.swing.*;
import java.awt.*;

public class SetPinScreen extends JPanel {
    private DataManager dataManager;
    private Runnable onCompleteCallback;
    private String firstPin = "";

    public SetPinScreen(DataManager dataManager, Runnable onCompleteCallback) {
        this.dataManager = dataManager;
        this.onCompleteCallback = onCompleteCallback;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_LILAC);

        add(createCenterPanel(), BorderLayout.CENTER);
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.BG_LILAC);
        panel.setBorder(BorderFactory.createEmptyBorder(80, 40, 80, 40));

        JLabel welcomeLabel = new JLabel("✨", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 60));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Welcome to Cozy Diary!");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Theme.TEXT_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel subtitleLabel = new JLabel("Create a 4-digit PIN to secure your diary");
        subtitleLabel.setFont(Theme.BODY_FONT);
        subtitleLabel.setForeground(Theme.TEXT_DARK);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JPanel dotsPanel = createDotsPanel();
        dotsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel statusLabel = new JLabel("Enter your new PIN");
        statusLabel.setFont(Theme.BODY_FONT);
        statusLabel.setForeground(Theme.ACCENT_PURPLE);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JPanel keypadPanel = createKeypadPanel(dotsPanel, statusLabel);
        keypadPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(welcomeLabel);
        panel.add(titleLabel);
        panel.add(subtitleLabel);
        panel.add(dotsPanel);
        panel.add(statusLabel);
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

    private JPanel createKeypadPanel(JPanel dotsPanel, JLabel statusLabel) {
        JPanel panel = new JPanel(new GridLayout(4, 3, 10, 10));
        panel.setBackground(Theme.BG_LILAC);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        panel.setMaximumSize(new Dimension(300, 300));

        StringBuilder currentPin = new StringBuilder();
        String[] buttons = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "×", "0", "⌫"};

        for (String btn : buttons) {
            JButton button = new JButton(btn);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setBackground(Theme.LIGHT_LILAC);
            button.setForeground(Theme.TEXT_DARK);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Add hover effect
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(Theme.LILAC);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(Theme.LIGHT_LILAC);
                }
            });

            button.addActionListener(e -> handleKeypadInput(btn, currentPin, dotsPanel, statusLabel));
            panel.add(button);
        }

        return panel;
    }

    private void handleKeypadInput(String input, StringBuilder currentPin, JPanel dotsPanel, JLabel statusLabel) {
        Component[] dots = dotsPanel.getComponents();

        if (input.equals("×")) {
            currentPin.setLength(0);
            firstPin = "";
            statusLabel.setText("Enter your new PIN");
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
                if (firstPin.isEmpty()) {
                    firstPin = currentPin.toString();
                    statusLabel.setText("Confirm your PIN");
                    currentPin.setLength(0);
                    for (Component c : dots) {
                        if (c instanceof JLabel) ((JLabel) c).setText("○");
                    }
                } else {
                    if (firstPin.equals(currentPin.toString())) {
                        dataManager.getPinManagement().forceChangePin(firstPin);
                        Utils.showInfoDialog(this, "PIN created successfully! ✨", "Success");
                        onCompleteCallback.run();
                    } else {
                        Utils.showErrorDialog(this, "PINs don't match! Please try again.");
                        firstPin = "";
                        statusLabel.setText("Enter your new PIN");
                        currentPin.setLength(0);
                        for (Component c : dots) {
                            if (c instanceof JLabel) ((JLabel) c).setText("○");
                        }
                    }
                }
            }
        }
    }
}