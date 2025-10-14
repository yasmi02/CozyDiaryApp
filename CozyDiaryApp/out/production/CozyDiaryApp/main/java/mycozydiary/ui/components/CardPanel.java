package mycozydiary.ui.components;

import mycozydiary.styles.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CardPanel extends JPanel {

    public CardPanel(String emoji, String title, String subtitle) {
        setLayout(new BorderLayout(15, 0));
        setOpaque(false);
        setMaximumSize(new Dimension(400, 100));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel emojiPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.SOFT_PINK);
                g2.fillOval(5, 5, 60, 60);
                g2.dispose();
            }
        };
        emojiPanel.setOpaque(false);
        emojiPanel.setPreferredSize(new Dimension(70, 70));
        emojiPanel.setLayout(new GridBagLayout());

        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Arial", Font.PLAIN, 35));
        emojiPanel.add(emojiLabel);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("<html>" + title.replace("\n", "<br>") + "</html>");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        titleLabel.setForeground(Theme.TEXT_DARK);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));
        subtitleLabel.setForeground(Theme.ACCENT_PURPLE);

        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(subtitleLabel);

        add(emojiPanel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Theme.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 25, 25));

        g2.setColor(new Color(200, 180, 230, 30));
        g2.fill(new RoundRectangle2D.Float(2, 4, getWidth() - 2, getHeight() - 2, 25, 25));

        g2.dispose();
        super.paintComponent(g);
    }
}
