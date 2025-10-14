package mycozydiary;

import mycozydiary.data.DataManager;
import mycozydiary.styles.Theme;
import mycozydiary.ui.components.BottomNav;
import mycozydiary.ui.screens.*;
import javax.swing.*;
import java.awt.*;

public class CozyDiaryApp extends JFrame {
    private DataManager dataManager;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private boolean isLocked = true;

    private PinScreen pinScreen;
    private HomeScreen homeScreen;
    private JournalScreen journalScreen;
    private CalendarScreen calendarScreen;

    public CozyDiaryApp() {
        setTitle("My Cozy Diary ♡");
        setSize(Theme.WINDOW_WIDTH, Theme.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        dataManager = new DataManager();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Theme.BG_PINK);

        initializeScreens();

        add(mainPanel);

        if (isLocked) {
            cardLayout.show(mainPanel, "PIN");
        } else {
            cardLayout.show(mainPanel, "HOME");
        }
    }

    private void initializeScreens() {
        pinScreen = new PinScreen(dataManager, () -> {
            isLocked = false;
            cardLayout.show(mainPanel, "HOME");
        });

        JPanel homeContainer = new JPanel(new BorderLayout());
        homeScreen = new HomeScreen(dataManager);
        homeContainer.add(homeScreen, BorderLayout.CENTER);
        homeContainer.add(createBottomNav(), BorderLayout.SOUTH);

        journalScreen = new JournalScreen(
                dataManager,
                () -> cardLayout.show(mainPanel, "HOME"),
                () -> cardLayout.show(mainPanel, "HOME")
        );

        calendarScreen = new CalendarScreen(
                dataManager,
                () -> cardLayout.show(mainPanel, "HOME"),
                () -> cardLayout.show(mainPanel, "JOURNAL")
        );

        mainPanel.add(pinScreen, "PIN");
        mainPanel.add(homeContainer, "HOME");
        mainPanel.add(journalScreen, "JOURNAL");
        mainPanel.add(calendarScreen, "CALENDAR");
    }

    private BottomNav createBottomNav() {
        return new BottomNav(
                e -> cardLayout.show(mainPanel, "HOME"),
                e -> {
                    journalScreen.clearFields();
                    cardLayout.show(mainPanel, "JOURNAL");
                },
                e -> cardLayout.show(mainPanel, "CALENDAR"),
                e -> {
                    isLocked = true;
                    cardLayout.show(mainPanel, "PIN");
                }
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            CozyDiaryApp app = new CozyDiaryApp();
            app.setVisible(true);
        });
    }
}