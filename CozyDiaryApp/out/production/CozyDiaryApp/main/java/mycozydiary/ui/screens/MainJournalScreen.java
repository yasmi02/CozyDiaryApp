package mycozydiary.ui.screens;

import mycozydiary.styles.Theme;
import mycozydiary.data.*;
import mycozydiary.ui.Utils;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class MainJournalScreen extends JPanel {
    private DataManager dataManager;
    private Runnable onCreateNewEntry;
    private Runnable onEntryClick;

    public MainJournalScreen(DataManager dataManager, Runnable onCreateNewEntry, Runnable onEntryClick) {
        this.dataManager = dataManager;
        this.onCreateNewEntry = onCreateNewEntry;
        this.onEntryClick = onEntryClick;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_LILAC);

        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
        add(createNewEntryButton(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.LIGHT_LILAC);
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 15, 20));

        JLabel titleLabel = new JLabel("My Diary 📔");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titleLabel.setForeground(Theme.TEXT_DARK);

        JLabel dateLabel = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd")));
        dateLabel.setFont(Theme.BODY_FONT);
        dateLabel.setForeground(Theme.TEXT_LIGHT);

        JPanel headerTextPanel = new JPanel(new BorderLayout());
        headerTextPanel.setBackground(Theme.LIGHT_LILAC);
        headerTextPanel.add(titleLabel, BorderLayout.NORTH);
        headerTextPanel.add(dateLabel, BorderLayout.SOUTH);

        header.add(headerTextPanel, BorderLayout.CENTER);

        return header;
    }

    private JScrollPane createContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_LILAC);
        content.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        Map<LocalDate, DiaryEntry> allEntries = dataManager.getAllEntries();

        if (allEntries.isEmpty()) {
            JPanel emptyState = createEmptyState();
            content.add(emptyState);
        } else {
            List<Map.Entry<LocalDate, DiaryEntry>> sortedEntries = new ArrayList<>(allEntries.entrySet());
            sortedEntries.sort((a, b) -> b.getKey().compareTo(a.getKey()));

            for (Map.Entry<LocalDate, DiaryEntry> entry : sortedEntries) {
                JPanel entryCard = createEntryCard(entry.getKey(), entry.getValue());
                content.add(entryCard);
                content.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBackground(Theme.BG_LILAC);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }

    private JPanel createEmptyState() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.BG_LILAC);
        panel.setBorder(BorderFactory.createEmptyBorder(100, 40, 100, 40));

        JLabel emoji = new JLabel("📝", SwingConstants.CENTER);
        emoji.setFont(new Font("Arial", Font.PLAIN, 80));
        emoji.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Start Your Journey");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Theme.TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Tap the + button to write your first entry");
        subtitle.setFont(Theme.BODY_FONT);
        subtitle.setForeground(Theme.TEXT_LIGHT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(emoji);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(subtitle);

        return panel;
    }

    private JPanel createEntryCard(LocalDate date, DiaryEntry entry) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Theme.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));

                g2.setColor(new Color(180, 150, 220, 20));
                g2.fill(new RoundRectangle2D.Float(2, 4, getWidth() - 2, getHeight() - 2, 20, 20));

                g2.dispose();
                super.paintComponent(g);
            }
        };

        card.setLayout(new BorderLayout(15, 10));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // FIT INSIDE SCREEN - key fix!
        card.setMaximumSize(new Dimension(390, 150));
        card.setPreferredSize(new Dimension(390, 150));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel datePanel = createDateBadge(date);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel moodLabel = new JLabel(entry.getMood());
        moodLabel.setFont(new Font("Arial", Font.PLAIN, 28));

        JLabel timeLabel = new JLabel(entry.getFormattedTime());
        timeLabel.setFont(Theme.SMALL_FONT);
        timeLabel.setForeground(Theme.TEXT_LIGHT);

        headerPanel.add(moodLabel, BorderLayout.WEST);
        headerPanel.add(timeLabel, BorderLayout.EAST);

        JLabel titleLabel = new JLabel(entry.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Theme.TEXT_DARK);

        String preview = entry.getContent();
        if (preview.length() > 50) {
            preview = preview.substring(0, 50) + "...";
        }
        JLabel previewLabel = new JLabel("<html>" + preview + "</html>");
        previewLabel.setFont(Theme.BODY_FONT);
        previewLabel.setForeground(Theme.TEXT_LIGHT);

        contentPanel.add(headerPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(previewLabel);

        JButton deleteBtn = new JButton("<html><center>🗑️<br><small>Delete</small></center></html>");
        deleteBtn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        deleteBtn.setBackground(new Color(0, 0, 0, 0));
        deleteBtn.setForeground(Theme.TEXT_LIGHT);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.setContentAreaFilled(false);
        deleteBtn.setToolTipText("Delete entry");

        deleteBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Delete \"" + entry.getTitle() + "\"?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                dataManager.deleteEntry(date);
                JOptionPane.showMessageDialog(
                        this,
                        "Entry deleted! 🗑️",
                        "Deleted",
                        JOptionPane.INFORMATION_MESSAGE
                );
                refresh();
            }
        });

        deleteBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteBtn.setForeground(new Color(220, 53, 69));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                deleteBtn.setForeground(Theme.TEXT_LIGHT);
            }
        });

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.add(contentPanel, BorderLayout.CENTER);
        rightPanel.add(deleteBtn, BorderLayout.EAST);

        card.add(datePanel, BorderLayout.WEST);
        card.add(rightPanel, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showFullEntry(date, entry);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(Theme.LIGHT_LILAC);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Theme.WHITE);
            }
        });

        return card;
    }

    private JPanel createDateBadge(LocalDate date) {
        JPanel badge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Theme.ACCENT_PURPLE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));

                g2.dispose();
                super.paintComponent(g);
            }
        };

        badge.setLayout(new BoxLayout(badge, BoxLayout.Y_AXIS));
        badge.setOpaque(false);
        badge.setPreferredSize(new Dimension(60, 70));
        badge.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel monthLabel = new JLabel(date.getMonth().toString().substring(0, 3).toUpperCase());
        monthLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        monthLabel.setForeground(Theme.WHITE);
        monthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel dayLabel = new JLabel(String.valueOf(date.getDayOfMonth()));
        dayLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        dayLabel.setForeground(Theme.WHITE);
        dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        badge.add(monthLabel);
        badge.add(Box.createRigidArea(new Dimension(0, 5)));
        badge.add(dayLabel);

        return badge;
    }

    private JPanel createNewEntryButton() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Theme.BG_LILAC);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton newEntryBtn = new JButton("＋ New Entry");
        newEntryBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        newEntryBtn.setBackground(Theme.ACCENT_PURPLE);
        newEntryBtn.setForeground(Theme.WHITE);
        newEntryBtn.setFocusPainted(false);
        newEntryBtn.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        newEntryBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        newEntryBtn.addActionListener(e -> onCreateNewEntry.run());

        newEntryBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                newEntryBtn.setBackground(Theme.DEEP_LILAC);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                newEntryBtn.setBackground(Theme.ACCENT_PURPLE);
            }
        });

        panel.add(newEntryBtn);

        return panel;
    }

    private void showFullEntry(LocalDate date, DiaryEntry entry) {
        JPanel dialogPanel = new JPanel(new BorderLayout(10, 10));
        dialogPanel.setBackground(Theme.WHITE);
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String message = entry.getMood() + "  " + entry.getFormattedTime() + "\n\n" +
                entry.getTitle() + "\n\n" +
                entry.getContent();

        JTextArea textArea = new JTextArea(message);
        textArea.setFont(Theme.BODY_FONT);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Theme.WHITE);
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 400));

        dialogPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Theme.WHITE);

        JButton deleteBtn = new JButton("🗑️ Delete Entry");
        deleteBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        deleteBtn.setBackground(Theme.DEEP_LILAC);
        deleteBtn.setForeground(Theme.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        deleteBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteBtn.setBackground(new Color(120, 90, 160));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                deleteBtn.setBackground(Theme.DEEP_LILAC);
            }
        });

        buttonPanel.add(deleteBtn);
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                true);

        deleteBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    dialog,
                    "Are you sure you want to delete this entry?\n\nThis action cannot be undone.",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                dataManager.deleteEntry(date);
                dialog.dispose();
                JOptionPane.showMessageDialog(
                        this,
                        "Entry deleted successfully! 🗑️",
                        "Deleted",
                        JOptionPane.INFORMATION_MESSAGE
                );
                refresh();
            }
        });

        dialog.setContentPane(dialogPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public void refresh() {
        removeAll();
        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
        add(createNewEntryButton(), BorderLayout.SOUTH);
        revalidate();
        repaint();
    }
}