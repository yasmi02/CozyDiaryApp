package mycozydiary.styles;

import java.awt.*;
import java.io.*;
import java.util.Properties;

public class ThemeManager {
    private static final String THEME_FILE = "theme_preference.properties";
    private static String currentTheme = "LILAC_DREAM";

    // Theme definitions
    public static class ThemeColors {
        public Color bgColor;
        public Color lightColor;
        public Color mediumColor;
        public Color deepColor;
        public Color accentColor;
        public Color textDark;
        public Color textLight;
        public Color calendarDay;
        public Color calendarToday;
        public Color darkerPurple;

        public ThemeColors(Color bg, Color light, Color medium, Color deep, Color accent,
                           Color textD, Color textL, Color calDay, Color calToday, Color darker) {
            this.bgColor = bg;
            this.lightColor = light;
            this.mediumColor = medium;
            this.deepColor = deep;
            this.accentColor = accent;
            this.textDark = textD;
            this.textLight = textL;
            this.calendarDay = calDay;
            this.calendarToday = calToday;
            this.darkerPurple = darker;
        }
    }

    public static ThemeColors getThemeColors(String themeName) {
        switch (themeName) {
            case "CHERRY_BLOSSOM":
                return new ThemeColors(
                        new Color(255, 228, 235),  // bg
                        new Color(255, 240, 245),  // light
                        new Color(255, 182, 193),  // medium
                        new Color(219, 112, 147),  // deep
                        new Color(255, 192, 203),  // accent
                        new Color(139, 69, 19),    // text dark
                        new Color(160, 100, 120),  // text light
                        new Color(255, 218, 225),  // calendar day
                        new Color(219, 112, 147),  // calendar today
                        new Color(199, 92, 127)    // darker
                );

            case "OCEAN":
                return new ThemeColors(
                        new Color(220, 235, 255),  // bg
                        new Color(240, 248, 255),  // light
                        new Color(173, 216, 230),  // medium
                        new Color(100, 149, 237),  // deep
                        new Color(135, 206, 250),  // accent
                        new Color(25, 25, 112),    // text dark
                        new Color(70, 130, 180),   // text light
                        new Color(230, 240, 255),  // calendar day
                        new Color(100, 149, 237),  // calendar today
                        new Color(70, 130, 180)    // darker
                );

            case "MINT_FRESH":
                return new ThemeColors(
                        new Color(200, 255, 220),  // bg - BRIGHT mint
                        new Color(220, 255, 235),  // light
                        new Color(100, 230, 150),  // medium - VIVID green
                        new Color(34, 139, 34),    // deep - FOREST green
                        new Color(50, 205, 50),    // accent - LIME green
                        new Color(0, 100, 0),      // text dark - DARK green
                        new Color(34, 139, 34),    // text light - forest
                        new Color(210, 255, 230),  // calendar day
                        new Color(34, 139, 34),    // calendar today
                        new Color(0, 128, 0)       // darker - GREEN
                );

            case "SUNSET":
                return new ThemeColors(
                        new Color(255, 235, 220),  // bg
                        new Color(255, 245, 238),  // light
                        new Color(255, 218, 185),  // medium
                        new Color(255, 160, 122),  // deep
                        new Color(255, 200, 170),  // accent
                        new Color(139, 69, 19),    // text dark
                        new Color(205, 133, 63),   // text light
                        new Color(255, 228, 215),  // calendar day
                        new Color(255, 160, 122),  // calendar today
                        new Color(235, 140, 102)   // darker
                );


            case "PEACHY":
                return new ThemeColors(
                        new Color(255, 230, 220),  // bg
                        new Color(255, 245, 240),  // light
                        new Color(255, 204, 188),  // medium
                        new Color(255, 140, 105),  // deep
                        new Color(255, 180, 150),  // accent
                        new Color(139, 69, 19),    // text dark
                        new Color(205, 92, 92),    // text light
                        new Color(255, 235, 225),  // calendar day
                        new Color(255, 140, 105),  // calendar today
                        new Color(235, 120, 85)    // darker
                );

            case "CLOUD":
                return new ThemeColors(
                        new Color(235, 235, 240),  // bg - cool gray
                        new Color(245, 245, 250),  // light - almost white
                        new Color(200, 200, 210),  // medium - silver
                        new Color(150, 150, 170),  // deep - slate gray
                        new Color(180, 180, 200),  // accent - light slate
                        new Color(60, 60, 80),     // text dark - charcoal
                        new Color(100, 100, 120),  // text light - gray
                        new Color(230, 230, 238),  // calendar day
                        new Color(150, 150, 170),  // calendar today - slate
                        new Color(130, 130, 150)   // darker
                );

            default: // LILAC_DREAM
                return new ThemeColors(
                        new Color(230, 220, 245),  // bg
                        new Color(245, 240, 255),  // light
                        new Color(200, 180, 230),  // medium
                        new Color(160, 130, 200),  // deep
                        new Color(180, 150, 220),  // accent
                        new Color(80, 60, 100),    // text dark
                        new Color(120, 100, 140),  // text light
                        new Color(210, 200, 230),  // calendar day
                        new Color(140, 110, 180),  // calendar today
                        new Color(120, 90, 160)    // darker
                );
        }
    }

    public static void applyTheme(String themeName) {
        currentTheme = themeName;
        ThemeColors colors = getThemeColors(themeName);

        // Update Theme class
        Theme.BG_LILAC = colors.bgColor;
        Theme.LIGHT_LILAC = colors.lightColor;
        Theme.LILAC = colors.mediumColor;
        Theme.DEEP_LILAC = colors.deepColor;
        Theme.ACCENT_PURPLE = colors.accentColor;
        Theme.TEXT_DARK = colors.textDark;
        Theme.TEXT_LIGHT = colors.textLight;
        Theme.CALENDAR_DAY = colors.calendarDay;
        Theme.CALENDAR_TODAY = colors.calendarToday;
        Theme.DARKER_PURPLE = colors.darkerPurple;

        // ADD THESE TWO LINES:
        Theme.GRADIENT_START = colors.bgColor;
        Theme.GRADIENT_END = colors.lightColor;

        // Save preference
        saveThemePreference(themeName);
    }

    public static String getCurrentTheme() {
        return currentTheme;
    }

    public static void loadThemePreference() {
        Properties props = new Properties();
        File file = new File(THEME_FILE);

        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
                String savedTheme = props.getProperty("theme", "LILAC_DREAM");
                applyTheme(savedTheme);
            } catch (IOException e) {
                System.err.println("Error loading theme: " + e.getMessage());
            }
        }
    }

    private static void saveThemePreference(String themeName) {
        Properties props = new Properties();
        props.setProperty("theme", themeName);

        try (FileOutputStream fos = new FileOutputStream(THEME_FILE)) {
            props.store(fos, "Cozy Diary Theme Preference");
        } catch (IOException e) {
            System.err.println("Error saving theme: " + e.getMessage());
        }
    }
}
