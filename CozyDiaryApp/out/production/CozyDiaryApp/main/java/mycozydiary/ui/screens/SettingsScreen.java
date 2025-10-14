package mycozydiary.ui.screens;

import mycozydiary.styles.Theme;
import mycozydiary.data.DataManager;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import mycozydiary.styles.ThemeManager;

public class SettingsScreen extends JPanel {
    private DataManager dataManager;
    private Runnable backAction;
    private Runnable showSetPinScreen;

    private Runnable refreshCallback;

    public SettingsScreen(DataManager dataManager, Runnable backAction, Runnable showSetPinScreen, Runnable refreshCallback) {
        this.dataManager = dataManager;
        this.backAction = backAction;
        this.showSetPinScreen = showSetPinScreen;
        this.refreshCallback = refreshCallback;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_LILAC);

        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_LILAC);
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Mood emoji on left
        JLabel moodLabel = new JLabel("😊");
        moodLabel.setFont(new Font("Arial", Font.PLAIN, 40));

        // Title in center
        JLabel titleLabel = new JLabel("My Settings");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(Theme.TEXT_DARK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // User icon on right
        JLabel userLabel = new JLabel("");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 40));

        header.add(moodLabel, BorderLayout.WEST);
        header.add(titleLabel, BorderLayout.CENTER);
        header.add(userLabel, BorderLayout.EAST);

        return header;
    }

    private JScrollPane createContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_LILAC);
        content.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Quote Card
        JPanel quoteCard = createQuoteCard();

        // Security Card
        JPanel securityCard = createSecurityCard();

        // Statistics Card
        JPanel statsCard = createStatsCard();

        // Action Buttons
        JPanel actionButtons = createActionButtons();

        content.add(quoteCard);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(securityCard);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(statsCard);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(actionButtons);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBackground(Theme.BG_LILAC);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }

    private JPanel createQuoteCard() {
        JPanel card = createRoundedCard();
        card.setLayout(new BorderLayout(15, 10));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel emoji = new JLabel("💜");
        emoji.setFont(new Font("Arial", Font.PLAIN, 40));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel title = new JLabel("Personalize your diary");
        title.setFont(new Font("Serif", Font.BOLD, 18));
        title.setForeground(Theme.TEXT_DARK);

        JLabel subtitle = new JLabel("Make it truly yours");
        subtitle.setFont(new Font("Serif", Font.ITALIC, 14));
        subtitle.setForeground(Theme.ACCENT_PURPLE);

        textPanel.add(title);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(subtitle);

        card.add(emoji, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createSecurityCard() {
        JPanel card = createRoundedCard();
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("🔒 Security");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Theme.TEXT_DARK);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // PIN Status Row
        JPanel pinRow = new JPanel(new BorderLayout());
        pinRow.setOpaque(false);
        pinRow.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel pinLabel = new JLabel("PIN Protection");
        pinLabel.setFont(Theme.BODY_FONT);
        pinLabel.setForeground(Theme.TEXT_DARK);

        JLabel pinStatus = new JLabel(dataManager.isUsingDefaultPin() ? "Not Set" : "Enabled");
        pinStatus.setFont(Theme.BODY_FONT);
        pinStatus.setForeground(dataManager.isUsingDefaultPin() ? Theme.TEXT_LIGHT : Theme.ACCENT_PURPLE);

        pinRow.add(pinLabel, BorderLayout.WEST);
        pinRow.add(pinStatus, BorderLayout.EAST);

        // Set PIN Button
        JButton setPinBtn = createStyledButton(
                dataManager.isUsingDefaultPin() ? "Set PIN" : "Change PIN",
                Theme.ACCENT_PURPLE
        );
        setPinBtn.addActionListener(e -> showSetPinScreen.run());

        contentPanel.add(pinRow);
        contentPanel.add(setPinBtn);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createStatsCard() {
        JPanel card = createRoundedCard();
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("📊 Your Journey");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Theme.TEXT_DARK);

        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        statsPanel.setOpaque(false);

        int totalEntries = dataManager.getTotalEntries();

        statsPanel.add(createStatBox("📝", String.valueOf(totalEntries), "Entries"));
        statsPanel.add(createStatBox("✨", "12", "Days Active"));
        statsPanel.add(createStatBox("🔥", "5", "Streak"));
        statsPanel.add(createStatBox("💜", "89%", "Habits"));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(statsPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createStatBox(String emoji, String value, String label) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(Theme.LIGHT_LILAC);
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT_PURPLE, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        emojiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        valueLabel.setForeground(Theme.ACCENT_PURPLE);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelText = new JLabel(label);
        labelText.setFont(Theme.SMALL_FONT);
        labelText.setForeground(Theme.TEXT_DARK);
        labelText.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(emojiLabel);
        box.add(Box.createRigidArea(new Dimension(0, 5)));
        box.add(valueLabel);
        box.add(labelText);

        return box;
    }

    private JPanel createActionButtons() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.setBackground(Theme.BG_LILAC);
        panel.setMaximumSize(new Dimension(400, 80));

        JButton aboutBtn = createActionButton("✨", "About");
        aboutBtn.addActionListener(e -> showAboutDialog());

        JButton themeBtn = createActionButton("🎨", "Theme");
        themeBtn.addActionListener(e -> showThemePicker());

        JButton backBtn = createActionButton("←", "Back");
        backBtn.addActionListener(e -> backAction.run());

        panel.add(aboutBtn);
        panel.add(themeBtn);
        panel.add(backBtn);

        return panel;
    }

    private JButton createActionButton(String emoji, String text) {
        JButton btn = new JButton("<html><center>" + emoji + "<br>" + text + "</center></html>");
        btn.setFont(Theme.SMALL_FONT);
        btn.setBackground(Theme.LIGHT_LILAC);
        btn.setForeground(Theme.TEXT_DARK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT_PURPLE, 2),
                BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(Theme.LILAC);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Theme.LIGHT_LILAC);
            }
        });

        return btn;
    }

    private void showThemePicker() {
        JDialog themeDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Choose Your Theme", true);
        themeDialog.setSize(400, 500);
        themeDialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Theme.BG_LILAC);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Pick Your Color Theme 🎨");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Theme.TEXT_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Theme options
        String[][] themes = {
                {"💜 Lilac", "230, 220, 245", "Current"},
                {"🌸 Cherry Blossom", "255, 228, 235", "Pink & Soft"},
                {"🌊 Ocean", "220, 235, 255", "Calm & Cool"},
                {"🌿 Mint", "220, 245, 235", "Green & Fresh"},
                {"🌅 Sunset", "255, 235, 220", "Warm & Cozy"},
                {"🍑 Peachy", "255, 230, 220", "Sweet Peach"},
                {"☁️ Cloud", "245, 245, 250", "Light Gray"}
        };

        for (String[] theme : themes) {
            JButton themeBtn = createThemeButton(theme[0], theme[1], theme[2], themeDialog);
            mainPanel.add(themeBtn);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        themeDialog.add(scrollPane);
        themeDialog.setVisible(true);
    }

    private JButton createThemeButton(String name, String rgb, String description, JDialog dialog) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout(15, 0));
        btn.setBackground(Theme.LIGHT_LILAC);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT_PURPLE, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(350, 70));

        // Color preview
        String[] rgbValues = rgb.split(", ");
        Color themeColor = new Color(
                Integer.parseInt(rgbValues[0]),
                Integer.parseInt(rgbValues[1]),
                Integer.parseInt(rgbValues[2])
        );

        JPanel colorPreview = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(themeColor);
                g2.fillOval(0, 0, 50, 50);
                g2.dispose();
            }
        };
        colorPreview.setPreferredSize(new Dimension(50, 50));
        colorPreview.setOpaque(false);

        // Text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setForeground(Theme.TEXT_DARK);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(Theme.SMALL_FONT);
        descLabel.setForeground(Theme.TEXT_LIGHT);

        textPanel.add(nameLabel);
        textPanel.add(descLabel);

        btn.add(colorPreview, BorderLayout.WEST);
        btn.add(textPanel, BorderLayout.CENTER);

        btn.addActionListener(e -> {
            // Get theme key from name
            String themeKey = name.toUpperCase()
                    .replace("💜 ", "")
                    .replace("🌸 ", "")
                    .replace("🌊 ", "")
                    .replace("🌿 ", "")
                    .replace("🌅 ", "")
                    .replace("🌙 ", "")
                    .replace("🍑 ", "")
                    .replace("☁️ ", "")
                    .replace(" ", "_");

            // Apply the theme!
            ThemeManager.applyTheme(themeKey);

            dialog.dispose();

            JOptionPane.showMessageDialog(this,
                    "Theme '" + name + "' applied! ✨\n\nRestart the app to see changes! 💜",
                    "Theme Applied",
                    JOptionPane.INFORMATION_MESSAGE);


            // Refresh the UI
            if (refreshCallback != null) {
                refreshCallback.run();
            }
        });


        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(Theme.LILAC);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Theme.LIGHT_LILAC);
            }
        });

        return btn;
    }

    private JPanel createRoundedCard() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Theme.LIGHT_LILAC);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));

                g2.setColor(new Color(200, 180, 230, 20));
                g2.fill(new RoundRectangle2D.Float(2, 4, getWidth() - 2, getHeight() - 2, 20, 20));

                g2.dispose();
                super.paintComponent(g);
            }
        };
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.BUTTON_FONT);
        btn.setBackground(color);
        btn.setForeground(Theme.DEEP_LILAC);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });

        return btn;
    }

    private void showAboutDialog() {
        String message = "Cozy Diary App\n\n" +
                "Version 1.0.0\n\n" +
                "A cozy place to keep your thoughts,\n" +
                "memories, and daily reflections. 💜\n\n" +
                "Made with love ✨";

        JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }
}