package mycozydiary.ui.components;

import mycozydiary.styles.Theme;
import mycozydiary.data.DataManager;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;

public class HabitRow extends JPanel {
    private String habitName;
    private DataManager dataManager;
    private JProgressBar progressBar;
    private Color habitColor;

    public HabitRow(String icon, String name, Color color, DataManager dataManager) {
        this.habitName = name;
        this.dataManager = dataManager;
        this.habitColor = color;

        setLayout(new BorderLayout(10, 0));
        setOpaque(false);

        JLabel iconLabel = new JLabel(icon + " " + name);
        iconLabel.setFont(Theme.BODY_FONT);
        iconLabel.setForeground(Theme.TEXT_DARK);

        progressBar = new JProgressBar(0, 100) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Theme.LIGHT_LILAC);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));

                int progressWidth = (int) (getWidth() * (getValue() / 100.0));
                g2.setColor(habitColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, progressWidth, getHeight(), 15, 15));

                g2.dispose();
            }
        };
        progressBar.setValue(dataManager.getHabitProgress(name));
        progressBar.setPreferredSize(new Dimension(120, 20));
        progressBar.setBorderPainted(false);
        progressBar.setOpaque(false);

        JCheckBox checkbox = new JCheckBox();
        checkbox.setOpaque(false);
        checkbox.setSelected(dataManager.isHabitCompleted(name, LocalDate.now()));
        checkbox.addActionListener(e -> {
            dataManager.toggleHabit(habitName, LocalDate.now());
            updateProgress();
        });

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(progressBar);
        rightPanel.add(checkbox);

        add(iconLabel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    public void updateProgress() {
        progressBar.setValue(dataManager.getHabitProgress(habitName));
        progressBar.repaint();
    }
}
