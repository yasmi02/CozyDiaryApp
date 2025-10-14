package mycozydiary.ui.components;

import mycozydiary.styles.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BottomNav extends JPanel {

    public BottomNav(ActionListener diaryAction, ActionListener habitsAction,
                     ActionListener calendarAction, ActionListener settingsAction) {
        setLayout(new GridLayout(1, 4));
        setBackground(Theme.LIGHT_LILAC);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        add(createNavButton("📔", "Diary", diaryAction));
        add(createNavButton("✨", "Habits", habitsAction));
        add(createNavButton("📅", "Calendar", calendarAction));
        add(createNavButton("⚙", "Settings", settingsAction));
    }

    private JButton createNavButton(String icon, String text, ActionListener action) {
        JButton btn = new JButton("<html><center>" + icon + "<br><small>" + text + "</small></center></html>");
        btn.setFont(Theme.SMALL_FONT);
        btn.setBackground(Theme.LIGHT_LILAC);
        btn.setForeground(Theme.TEXT_DARK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(Theme.ACCENT_PURPLE);
                btn.setForeground(Theme.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Theme.LIGHT_LILAC);
                btn.setForeground(Theme.TEXT_DARK);
            }
        });

        return btn;
    }
}