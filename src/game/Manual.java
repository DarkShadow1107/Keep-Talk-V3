package game;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.util.List;

public class Manual extends JPanel {
    private App app;

    public Manual(App app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.PANEL_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, Theme.ACCENT_ORANGE),
            BorderFactory.createEmptyBorder(15, 30, 15, 30)
        ));
        
        JLabel title = new JLabel("BOMB DEFUSAL MANUAL");
        title.setFont(Theme.FONT_TITLE.deriveFont(32f));
        title.setForeground(Theme.ACCENT_ORANGE);
        header.add(title, BorderLayout.WEST);
        
        JButton backBtn = Theme.createButton("RETURN TO MENU");
        backBtn.setPreferredSize(new Dimension(200, 40));
        backBtn.setBackground(Theme.DANGER_RED);
        backBtn.addActionListener(e -> app.showMenu());
        header.add(backBtn, BorderLayout.EAST);
        
        add(header, BorderLayout.NORTH);

        // Content
        JEditorPane content = new JEditorPane();
        content.setEditable(false);
        content.setContentType("text/html");
        content.setBackground(Theme.BG_COLOR);
        
        // CSS Styling for Dark Theme
        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body { font-family: 'Segoe UI', sans-serif; margin: 30px; color: #E0E0E0; background-color: #0A0A0F; }");
        styleSheet.addRule("h1 { font-size: 28px; color: #E74C3C; border-bottom: 2px solid #E74C3C; padding-bottom: 10px; margin-top: 40px; }");
        styleSheet.addRule("h2 { font-size: 22px; color: #3498DB; margin-top: 30px; margin-bottom: 10px; }");
        styleSheet.addRule("h3 { font-size: 18px; color: #F1C40F; margin-top: 20px; }");
        styleSheet.addRule("p { font-size: 16px; line-height: 1.6; margin-bottom: 15px; }");
        styleSheet.addRule("ul { margin-left: 20px; margin-bottom: 20px; }");
        styleSheet.addRule("li { font-size: 16px; margin-bottom: 8px; }");
        styleSheet.addRule(".highlight { color: #2ECC71; font-weight: bold; }");
        styleSheet.addRule(".warning { color: #E74C3C; font-weight: bold; }");
        styleSheet.addRule(".box { background-color: #1E1E24; padding: 15px; border: 1px solid #333; border-radius: 5px; }");
        styleSheet.addRule("table { width: 100%; border-collapse: collapse; margin-top: 10px; }");
        styleSheet.addRule("th { text-align: left; color: #F1C40F; border-bottom: 1px solid #555; padding: 10px; }");
        styleSheet.addRule("td { padding: 10px; border-bottom: 1px solid #333; }");
        content.setEditorKit(kit);

        // Build Content
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        
        html.append("<h1>INTRODUCTION</h1>");
        html.append("<p>Welcome to the <b>Keep Talking and Nobody Explodes v3</b> Defusal Manual.</p>");
        html.append("<p>You are the Expert. You have this manual. Your partner has the bomb.</p>");
        html.append("<div class='box'><span class='warning'>WARNING:</span> Do not look at the bomb screen if you are the Expert. Do not look at this manual if you are the Defuser. Communication is your only tool.</div>");

        html.append("<h1>CONTROLS</h1>");
        html.append("<ul>");
        html.append("<li><b>Mouse:</b> Interact with modules and UI elements.</li>");
        html.append("<li><b>Keypad Input:</b> Use keys <span class='highlight'>1-4</span>.</li>");
        html.append("<li><b>Wires Input:</b> Use keys <span class='highlight'>1-6</span> to cut wires.</li>");
        html.append("<li><b>Maze Input:</b> Use <span class='highlight'>WASD</span> or <span class='highlight'>Arrow Keys</span> to move.</li>");
        html.append("</ul>");

        html.append("<h1>MODULES</h1>");
        
        // Existing Modules
        html.append("<h2>1. Wires</h2>");
        html.append("<div class='box'><ul>");
        html.append("<li>If there are <b>no red wires</b>, cut the second wire.</li>");
        html.append("<li>Otherwise, if the <b>last wire is white</b>, cut the last wire.</li>");
        html.append("<li>Otherwise, if there is <b>more than one blue wire</b>, cut the last blue wire.</li>");
        html.append("<li>Otherwise, cut the last wire.</li>");
        html.append("</ul></div>");

        html.append("<h2>2. The Button</h2>");
        html.append("<div class='box'><ul>");
        html.append("<li>If the button is <b>Blue</b> and says <b>Abort</b>, hold it.</li>");
        html.append("<li>If the button says <b>Detonate</b>, press and release immediately.</li>");
        html.append("<li>If the button is <b>White</b> and there is a lit indicator <b>CAR</b>, hold it.</li>");
        html.append("<li>If there are > 2 batteries and a lit indicator <b>FRK</b>, press and release.</li>");
        html.append("<li>Otherwise, hold the button.</li>");
        html.append("</ul></div>");

        html.append("<h2>3. Keypad</h2>");
        html.append("<p>Identify the symbols. Press them in order of appearance in the columns below.</p>");

        html.append("<h2>4. Simon Says</h2>");
        html.append("<p>Flash the colors back. Mapping depends on <b>Vowel</b> in Serial Number.</p>");
        
        html.append("<h2>5. Maze</h2>");
        html.append("<p>Find the path. Avoid the invisible walls. Use the circle markers to identify the maze.</p>");

        html.append("<h2>6. Memory</h2>");
        html.append("<p>A 5-stage memory test. Press the correct button based on display and previous stages.</p>");

        html.append("<h2>7. Morse Code</h2>");
        html.append("<p>Translate the flashing light to a word. Tune the radio to the corresponding frequency.</p>");

        html.append("<h2>8. Complicated Wires</h2>");
        html.append("<p>Wires with LEDs and Stars. Use the Venn Diagram to decide: Cut (C), Don't Cut (D), Cut if Serial is Even (S), Cut if Parallel Port (P), Cut if Batteries >= 2 (B).</p>");

        html.append("<h2>9. Wire Sequences</h2>");
        html.append("<p>Cut wires based on their color and connection (A, B, C) according to the occurrence count.</p>");

        html.append("<h2>10. Password</h2>");
        html.append("<p>Find the correct 5-letter word from the scrolling letters.</p>");

        html.append("<h2>11. Who's On First</h2>");
        html.append("<p>Read the display. Find the button label to read. Press the first button that appears in the list.</p>");

        html.append("<h2>12. Binary</h2>");
        html.append("<div class='box'><p>Convert the decimal number to 5-bit binary (16, 8, 4, 2, 1). Example: 21 = 10101.</p></div>");

        html.append("<h2>13. Logic Gates</h2>");
        html.append("<div class='box'><p>Determine output (AND, OR, XOR, NAND, NOR) based on inputs.</p></div>");

        // New Modules (14-25)
        html.append("<h2>14. Rhythm</h2>");
        html.append("<p>Press the button in time with the flashing LED. The tempo increases with each stage.</p>");

        html.append("<h2>15. Coordinates</h2>");
        html.append("<p>Locate the grid coordinate (e.g., B-4) and press the button. Avoid the mines.</p>");

        html.append("<h2>16. Color Math</h2>");
        html.append("<p>Add the values of the colored numbers: Red=1, Blue=2, Green=3, Yellow=4. If the sum is > 10, subtract 10.</p>");

        html.append("<h2>17. Shapes</h2>");
        html.append("<p>Count the total number of edges on the displayed shapes. Enter the last digit of the count.</p>");

        html.append("<h2>18. Piano</h2>");
        html.append("<p>Play the melody displayed on the sheet music. Keys are labeled C, D, E, F, G, A, B.</p>");

        html.append("<h2>19. Anagrams</h2>");
        html.append("<p>Unscramble the displayed word. Select the correct word from the list of options.</p>");

        html.append("<h2>20. The Clock</h2>");
        html.append("<p>Set the time to match the target time zone displayed (e.g., UTC+2). Current time is UTC.</p>");

        html.append("<h2>21. Chemistry</h2>");
        html.append("<p>Mix the colored liquids in the correct order: Red -> Blue -> Green. Do not shake.</p>");

        html.append("<h2>22. Black Hole</h2>");
        html.append("<p>Do NOT interact with this module unless the digit sum of the timer is 7. Otherwise, wait.</p>");

        html.append("<h2>23. Foreign Exchange</h2>");
        html.append("<p>Convert the currency amount based on the daily rate displayed on the ticker.</p>");

        html.append("<h2>24. Astrology</h2>");
        html.append("<p>Select the Zodiac sign that corresponds to the displayed date range.</p>");

        html.append("<h2>25. Hacking</h2>");
        html.append("<p>Match the scrolling IP address segments to the static target IP. Press 'Connect' when aligned.</p>");

        html.append("<h1>MISSION DOSSIERS</h1>");
        html.append("<p>The following missions are available for deployment:</p>");
        
        html.append("<table>");
        html.append("<tr><th>Mission</th><th>Difficulty</th><th>Time</th><th>Modules</th></tr>");
        
        for (Level l : Level.getLevels()) {
            String color = "#E0E0E0";
            if (l.getDifficulty().equals("EASY")) color = "#2ECC71";
            if (l.getDifficulty().equals("MEDIUM")) color = "#3498DB";
            if (l.getDifficulty().equals("HARD")) color = "#F1C40F";
            if (l.getDifficulty().equals("EXPERT")) color = "#E67E22";
            if (l.getDifficulty().equals("INSANE")) color = "#E74C3C";
            
            html.append("<tr>");
            html.append("<td><b>").append(l.getName()).append("</b><br><span style='font-size:12px; color:#888'>").append(l.getDescription()).append("</span></td>");
            html.append("<td style='color:").append(color).append("'>").append(l.getDifficulty()).append("</td>");
            html.append("<td>").append(l.getTime()).append("s</td>");
            html.append("<td>").append(l.getModuleCount()).append("</td>");
            html.append("</tr>");
        }
        html.append("</table>");

        html.append("</body></html>");
        
        content.setText(html.toString());
        content.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        Theme.customizeScrollBar(scroll);
        add(scroll, BorderLayout.CENTER);
    }
}