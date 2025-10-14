package mycozydiary;

import mycozydiary.data.DataManager;
import mycozydiary.styles.Theme;
import mycozydiary.styles.ThemeManager;
import mycozydiary.ui.components.BottomNav;
import mycozydiary.ui.screens.*;
import mycozydiary.ui.Utils;
import javax.swing.*;
import java.awt.*;

public class CozyDiaryApp extends JFrame {
    private DataManager dataManager;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private boolean isLocked = true;

    private PinScreen pinScreen;
    private SetPinScreen setPinScreen;
    private MainJournalScreen mainJournalScreen;

    private HomeScreen homeScreen;
    private JournalScreen journalScreen;
    private CalendarScreen calendarScreen;
    private SettingsScreen settingsScreen;

    public CozyDiaryApp() {
        setTitle("My Cozy Diary ♡");
        setSize(Theme.WINDOW_WIDTH, Theme.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        dataManager = new DataManager();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Theme.BG_LILAC);

        initializeScreens();

        add(mainPanel);

        cardLayout.show(mainPanel, "MAIN");  // Opens to MAIN diary screen, NOT PIN!
    }

    private void initializeScreens() {
        mainJournalScreen = new MainJournalScreen(
                dataManager,
                () -> {
                    journalScreen.clearFields();
                    cardLayout.show(mainPanel, "JOURNAL");
                },
                () -> cardLayout.show(mainPanel, "MAIN")

        );


        // Main Journal with Bottom Nav
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.add(mainJournalScreen, BorderLayout.CENTER);
        mainContainer.add(createBottomNav(), BorderLayout.SOUTH);

        // Home/Habits Screen
        homeScreen = new HomeScreen(dataManager);
        JPanel homeContainer = new JPanel(new BorderLayout());
        homeContainer.add(homeScreen, BorderLayout.CENTER);
        homeContainer.add(createBottomNav(), BorderLayout.SOUTH);

        // Journal Entry Screen (for creating new entries)
        journalScreen = new JournalScreen(
                dataManager,
                () -> cardLayout.show(mainPanel, "MAIN"),
                () -> {
                    mainJournalScreen.refresh();
                    cardLayout.show(mainPanel, "MAIN");
                }
        );

        // Calendar Screen
        calendarScreen = new CalendarScreen(
                dataManager,
                () -> cardLayout.show(mainPanel, "MAIN"),
                () -> {
                    journalScreen.clearFields();
                    cardLayout.show(mainPanel, "JOURNAL");
                }
        );

        // Settings Screen (PIN is OPTIONAL)
        settingsScreen = new SettingsScreen(
                dataManager,
                () -> cardLayout.show(mainPanel, "MAIN"),
                () -> cardLayout.show(mainPanel, "SETPIN"),
                () -> refreshAllScreens()
        );

        // Set PIN Screen (OPTIONAL - only from Settings!)
        setPinScreen = new SetPinScreen(dataManager, () -> {
            Utils.showInfoDialog(this, "PIN set successfully! 🔒", "Success");
            cardLayout.show(mainPanel, "SETTINGS");
        });

        // PIN Entry Screen (NOT USED on startup)
        pinScreen = new PinScreen(dataManager, () -> {
            isLocked = false;
            cardLayout.show(mainPanel, "MAIN");
        });

        // Add all screens to card layout
        mainPanel.add(mainContainer, "MAIN");
        mainPanel.add(homeContainer, "HOME");
        mainPanel.add(journalScreen, "JOURNAL");
        mainPanel.add(calendarScreen, "CALENDAR");
        mainPanel.add(settingsScreen, "SETTINGS");
        mainPanel.add(setPinScreen, "SETPIN");
        mainPanel.add(pinScreen, "PIN");
    }

    private BottomNav createBottomNav() {
        return new BottomNav(
                e -> cardLayout.show(mainPanel, "MAIN"),  // Diary/Journal is now main!
                e -> cardLayout.show(mainPanel, "HOME"),  // Habits screen
                e -> cardLayout.show(mainPanel, "CALENDAR"),
                e -> cardLayout.show(mainPanel, "SETTINGS")
        );
    }

    public void refreshAllScreens() {
        mainPanel.removeAll();
        initializeScreens();
        mainPanel.revalidate();
        mainPanel.repaint();
        cardLayout.show(mainPanel, "SETTINGS");
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