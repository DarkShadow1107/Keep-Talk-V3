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
        styleSheet.addRule(".box { background-color: #1E1E24; padding: 15px; border: 1px solid #333; border-radius: 5px; margin-bottom: 20px; }");
        styleSheet.addRule("table { width: 100%; border-collapse: collapse; margin-top: 10px; margin-bottom: 20px; }");
        styleSheet.addRule("th { text-align: left; color: #F1C40F; border-bottom: 1px solid #555; padding: 10px; background-color: #1A1A20; }");
        styleSheet.addRule("td { padding: 10px; border-bottom: 1px solid #333; }");
        content.setEditorKit(kit);

        // Build Content
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        
        html.append("<h1>INTRODUCTION</h1>");
        html.append("<p>Welcome to the <b>Keep Talking and Nobody Explodes v3</b> Defusal Manual.</p>");
        html.append("<div class='box'><span class='warning'>WARNING:</span> Do not look at the bomb screen if you are the Expert. Communication is your only tool.</div>");

        html.append("<h1>MODULES</h1>");
        
        // 1. Wires
        html.append("<h2>1. Wires</h2>");
        html.append("<div class='box'><p>Wires are arranged horizontally. To defuse this module:</p><ul>");
        html.append("<li><span class='highlight'>Always cut the last wire.</span></li>");
        html.append("</ul></div>");

        // 2. The Button
        html.append("<h2>2. The Button</h2>");
        html.append("<div class='box'>");
        html.append("<h3>Step 1: Determine whether to Tap or Hold</h3>");
        html.append("<ul>");
        html.append("<li>If the button is <b>Blue</b> and says <b>Abort</b>, <span class='highlight'>Hold</span> it.</li>");
        html.append("<li>If there is more than 1 battery and the button says <b>Detonate</b>, <span class='highlight'>Tap</span> it.</li>");
        html.append("<li>If the button is <b>White</b> and there is a lit indicator <b>CAR</b>, <span class='highlight'>Hold</span> it.</li>");
        html.append("<li>If there are more than 2 batteries and a lit indicator <b>FRK</b>, <span class='highlight'>Tap</span> it.</li>");
        html.append("<li>If the button is <b>Yellow</b>, <span class='highlight'>Hold</span> it.</li>");
        html.append("<li>If the button is <b>Red</b> and says <b>Hold</b>, <span class='highlight'>Tap</span> it.</li>");
        html.append("<li>Otherwise, <span class='highlight'>Hold</span> it.</li>");
        html.append("</ul>");
        html.append("<h3>Step 2: Releasing a Held Button</h3>");
        html.append("<p>If you hold the button, a colored strip will light up. Release the button when the timer contains the corresponding digit:</p>");
        html.append("<ul>");
        html.append("<li><b>Blue Strip:</b> 4</li>");
        html.append("<li><b>White Strip:</b> 1</li>");
        html.append("<li><b>Yellow Strip:</b> 5</li>");
        html.append("<li><b>Any other color:</b> 1</li>");
        html.append("</ul></div>");

        // 3. Keypad
        html.append("<h2>3. Keypad</h2>");
        html.append("<div class='box'><p>Press the four symbols in <span class='highlight'>alphabetical order</span> (based on their character code).</p></div>");

        // 4. Simon Says
        html.append("<h2>4. Simon Says</h2>");
        html.append("<div class='box'><p>Flash the colors back. The mapping depends on whether the Serial Number contains a <b>Vowel</b> (A, E, I, O, U) and the current number of <b>Strikes</b>.</p>");
        html.append("<table>");
        html.append("<tr><th>Flash</th><th>0 Strikes</th><th>1 Strike</th><th>2+ Strikes</th></tr>");
        html.append("<tr><td colspan='4' style='background:#222; text-align:center;'><b>Serial Number with Vowel</b></td></tr>");
        html.append("<tr><td>Red</td><td>Blue</td><td>Yellow</td><td>Green</td></tr>");
        html.append("<tr><td>Blue</td><td>Red</td><td>Green</td><td>Red</td></tr>");
        html.append("<tr><td>Green</td><td>Yellow</td><td>Blue</td><td>Yellow</td></tr>");
        html.append("<tr><td>Yellow</td><td>Green</td><td>Red</td><td>Blue</td></tr>");
        html.append("<tr><td colspan='4' style='background:#222; text-align:center;'><b>Serial Number without Vowel</b></td></tr>");
        html.append("<tr><td>Red</td><td>Blue</td><td>Red</td><td>Yellow</td></tr>");
        html.append("<tr><td>Blue</td><td>Yellow</td><td>Blue</td><td>Green</td></tr>");
        html.append("<tr><td>Green</td><td>Green</td><td>Yellow</td><td>Blue</td></tr>");
        html.append("<tr><td>Yellow</td><td>Red</td><td>Green</td><td>Red</td></tr>");
        html.append("</table></div>");

        // 5. Maze
        html.append("<h2>5. Maze</h2>");
        html.append("<div class='box'><p>Navigate the white triangle to the red circle. <span class='highlight'>Walls are visible as dark green blocks.</span> Moving into a wall causes a strike.</p></div>");

        // 6. Memory
        html.append("<h2>6. Memory</h2>");
        html.append("<div class='box'>");
        html.append("<p><b>Stage 1:</b><br>If display is 1: Pos 2 | If 2: Pos 2 | If 3: Pos 3 | If 4: Pos 4</p>");
        html.append("<p><b>Stage 2:</b><br>If 1: Label 4 | If 2: Pos Stage 1 | If 3: Pos 1 | If 4: Pos Stage 1</p>");
        html.append("<p><b>Stage 3:</b><br>If 1: Label Stage 2 | If 2: Label Stage 1 | If 3: Pos 3 | If 4: Label 4</p>");
        html.append("<p><b>Stage 4:</b><br>If 1: Pos Stage 1 | If 2: Pos 1 | If 3: Pos Stage 2 | If 4: Pos Stage 2</p>");
        html.append("<p><b>Stage 5:</b><br>If 1: Label Stage 1 | If 2: Label Stage 2 | If 3: Label Stage 4 | If 4: Label Stage 3</p>");
        html.append("</div>");

        // 7. Morse Code
        html.append("<h2>7. Morse Code</h2>");
        html.append("<div class='box'><table>");
        html.append("<tr><th>Word</th><th>Freq</th><th>Word</th><th>Freq</th></tr>");
        html.append("<tr><td>SHELL</td><td>3.505</td><td>BOMBS</td><td>3.565</td></tr>");
        html.append("<tr><td>HALLS</td><td>3.515</td><td>BREAK</td><td>3.572</td></tr>");
        html.append("<tr><td>SLICK</td><td>3.522</td><td>BRICK</td><td>3.575</td></tr>");
        html.append("<tr><td>TRICK</td><td>3.532</td><td>STEAK</td><td>3.582</td></tr>");
        html.append("<tr><td>BOXES</td><td>3.535</td><td>STING</td><td>3.592</td></tr>");
        html.append("<tr><td>LEAKS</td><td>3.542</td><td>VECTOR</td><td>3.595</td></tr>");
        html.append("<tr><td>STROBE</td><td>3.545</td><td>BEATS</td><td>3.600</td></tr>");
        html.append("<tr><td>BISTRO</td><td>3.552</td><td>FLICK</td><td>3.555</td></tr>");
        html.append("</table></div>");

        // 8. Complicated Wires
        html.append("<h2>8. Complicated Wires</h2>");
        html.append("<div class='box'><p>Look at each wire: does it have <b>Red</b> coloring, <b>Blue</b> coloring, a <b>Star</b>, and a lit <b>LED</b>?</p>");
        html.append("<table><tr><th>R</th><th>B</th><th>S</th><th>L</th><th>Action</th></tr>");
        html.append("<tr><td>X</td><td>X</td><td>X</td><td>X</td><td>D</td></tr>");
        html.append("<tr><td>X</td><td>X</td><td>X</td><td></td><td>P</td></tr>");
        html.append("<tr><td>X</td><td>X</td><td></td><td>X</td><td>S</td></tr>");
        html.append("<tr><td>X</td><td>X</td><td></td><td></td><td>S</td></tr>");
        html.append("<tr><td>X</td><td></td><td>X</td><td>X</td><td>B</td></tr>");
        html.append("<tr><td>X</td><td></td><td>X</td><td></td><td>C</td></tr>");
        html.append("<tr><td>X</td><td></td><td></td><td>X</td><td>B</td></tr>");
        html.append("<tr><td>X</td><td></td><td></td><td></td><td>S</td></tr>");
        html.append("<tr><td></td><td>X</td><td>X</td><td>X</td><td>P</td></tr>");
        html.append("<tr><td></td><td>X</td><td>X</td><td></td><td>D</td></tr>");
        html.append("<tr><td></td><td>X</td><td></td><td>X</td><td>P</td></tr>");
        html.append("<tr><td></td><td>X</td><td></td><td></td><td>S</td></tr>");
        html.append("<tr><td></td><td></td><td>X</td><td>X</td><td>B</td></tr>");
        html.append("<tr><td></td><td></td><td>X</td><td></td><td>C</td></tr>");
        html.append("<tr><td></td><td></td><td></td><td>X</td><td>D</td></tr>");
        html.append("<tr><td></td><td></td><td></td><td></td><td>C</td></tr>");
        html.append("</table>");
        html.append("<p><b>C:</b> Cut | <b>D:</b> Don't Cut | <b>S:</b> Cut if Serial Even | <b>P:</b> Cut if Parallel Port | <b>B:</b> Cut if Batteries >= 2</p></div>");

        // 9. Password
        html.append("<h2>9. Password</h2>");
        html.append("<div class='box'><p>Possible words: ABOUT, AFTER, AGAIN, BELOW, COULD, EVERY, FIRST, FOUND, GREAT, HOUSE, LARGE, LEARN, NEVER, OTHER, PLACE, PLANT, POINT, RIGHT, SMALL, SOUND, SPELL, STILL, STUDY, THEIR, THERE, THESE, THING, THINK, THREE, WATER, WHERE, WHICH, WORLD, WOULD, WRITE.</p></div>");

        // 10. Who's on First
        html.append("<h2>10. Who's on First</h2>");
        html.append("<div class='box'><p><b>Step 1:</b> Look at display and find button position.</p>");
        html.append("<table><tr><th>Display</th><th>Pos</th><th>Display</th><th>Pos</th></tr>");
        html.append("<tr><td>YES</td><td>ML</td><td>FIRST</td><td>TR</td></tr>");
        html.append("<tr><td>DISPLAY</td><td>BR</td><td>OKAY</td><td>TR</td></tr>");
        html.append("<tr><td>SAYS</td><td>BR</td><td>NOTHING</td><td>ML</td></tr>");
        html.append("<tr><td>(BLANK)</td><td>BL</td><td>BLANK</td><td>MR</td></tr>");
        html.append("<tr><td>NO</td><td>BR</td><td>LED</td><td>ML</td></tr>");
        html.append("<tr><td>LEAD</td><td>BR</td><td>READ</td><td>MR</td></tr>");
        html.append("<tr><td>RED</td><td>MR</td><td>REED</td><td>BL</td></tr>");
        html.append("<tr><td>LEED</td><td>BL</td><td>HOLD ON</td><td>BR</td></tr>");
        html.append("<tr><td>YOU</td><td>MR</td><td>YOU ARE</td><td>BR</td></tr>");
        html.append("<tr><td>YOUR</td><td>MR</td><td>YOU'RE</td><td>MR</td></tr>");
        html.append("<tr><td>UR</td><td>TL</td><td>THERE</td><td>BR</td></tr>");
        html.append("<tr><td>THEY'RE</td><td>BL</td><td>THEIR</td><td>MR</td></tr>");
        html.append("<tr><td>THEY ARE</td><td>ML</td><td>SEE</td><td>BR</td></tr>");
        html.append("<tr><td>C</td><td>TR</td><td>CEE</td><td>BR</td></tr>");
        html.append("</table>");
        html.append("<p><b>Step 2:</b> Read the word on that button and press the first word in its list that ALSO appears on one of the 6 buttons:</p>");
        html.append("<div style='font-size:12px'>");
        html.append("<b>READY:</b> YES, OKAY, WHAT, MIDDLE, LEFT, PRESS, RIGHT, BLANK, READY<br>");
        html.append("<b>FIRST:</b> LEFT, OKAY, YES, MIDDLE, NO, RIGHT, NOTHING, UHHH, WAIT, READY, BLANK, WHAT, PRESS, FIRST<br>");
        html.append("<b>NO:</b> BLANK, UHHH, WAIT, FIRST, WHAT, READY, RIGHT, YES, NOTHING, LEFT, PRESS, OKAY, NO<br>");
        html.append("<b>BLANK:</b> WAIT, RIGHT, OKAY, MIDDLE, BLANK<br>");
        html.append("<b>NOTHING:</b> UHHH, RIGHT, OKAY, MIDDLE, YES, BLANK, NO, PRESS, LEFT, WHAT, WAIT, FIRST, NOTHING<br>");
        html.append("<b>YES:</b> OKAY, RIGHT, UHHH, MIDDLE, FIRST, WHAT, PRESS, READY, NOTHING, YES<br>");
        html.append("<b>WHAT:</b> UHHH, WHAT<br>");
        html.append("<b>UHHH:</b> READY, NOTHING, LEFT, WHAT, OKAY, YES, RIGHT, NO, PRESS, BLANK, UHHH<br>");
        html.append("<b>LEFT:</b> RIGHT, LEFT<br>");
        html.append("<b>RIGHT:</b> YES, NOTHING, READY, PRESS, NO, WAIT, WHAT, RIGHT<br>");
        html.append("<b>MIDDLE:</b> BLANK, READY, OKAY, WHAT, NOTHING, PRESS, NO, WAIT, LEFT, MIDDLE<br>");
        html.append("<b>OKAY:</b> MIDDLE, NO, FIRST, YES, UHHH, NOTHING, WAIT, OKAY<br>");
        html.append("<b>WAIT:</b> UHHH, NO, BLANK, OKAY, YES, LEFT, FIRST, PRESS, WHAT, WAIT<br>");
        html.append("<b>PRESS:</b> RIGHT, MIDDLE, YES, READY, PRESS<br>");
        html.append("<b>YOU:</b> SURE, YOU ARE, YOUR, YOU'RE, NEXT, UH HUH, UR, HOLD, WHAT?, YOU<br>");
        html.append("<b>YOU ARE:</b> YOUR, NEXT, LIKE, HUH, WHAT?, DONE, UH UH, HOLD, YOU, U, YOU'RE, SURE, UR, YOU ARE<br>");
        html.append("<b>YOUR:</b> UH UH, YOU ARE, UH HUH, YOUR<br>");
        html.append("<b>YOU'RE:</b> YOU, YOU'RE<br>");
        html.append("<b>UR:</b> DONE, U, UR<br>");
        html.append("<b>U:</b> UH HUH, SURE, NEXT, WHAT?, YOU'RE, UR, UH UH, DONE, U<br>");
        html.append("<b>UH HUH:</b> UH HUH<br>");
        html.append("<b>UH UH:</b> UR, U, YOU ARE, YOU'RE, NEXT, UH HUH<br>");
        html.append("<b>WHAT?:</b> YOU, HOLD, YOU'RE, YOUR, U, DONE, UH UH, LIKE, YOU ARE, UH HUH, UR, NEXT, WHAT?<br>");
        html.append("<b>DONE:</b> SURE, UH HUH, NEXT, WHAT?, YOUR, UR, YOU'RE, HOLD, LIKE, YOU, U, YOU ARE, UH UH, DONE<br>");
        html.append("<b>NEXT:</b> WHAT?, UH HUH, UH UH, YOUR, HOLD, SURE, NEXT<br>");
        html.append("<b>HOLD:</b> YOU ARE, U, DONE, UH UH, YOU, UR, SURE, WHAT?, HOLD<br>");
        html.append("<b>SURE:</b> YOU ARE, DONE, LIKE, YOU'RE, YOU, HOLD, UH HUH, UR, SURE<br>");
        html.append("<b>LIKE:</b> YOU'RE, NEXT, U, UR, HOLD, DONE, UH UH, WHAT?, UH HUH, YOU, LIKE<br>");
        html.append("</div></div>");

        // 11. Binary
        html.append("<h2>11. Binary</h2>");
        html.append("<div class='box'><p>Convert decimal to 5-bit binary. Switch values from left to right: <span class='highlight'>16, 8, 4, 2, 1</span>.</p></div>");

        // 12. Logic
        html.append("<h2>12. Logic</h2>");
        html.append("<div class='box'><p>Complete 3 stages of logic gates: <b>AND, OR, XOR, NAND, NOR</b>.</p></div>");

        html.append("<h1>MISSION DOSSIERS</h1>");
        html.append("<table><tr><th>Mission</th><th>Difficulty</th><th>Time</th><th>Modules</th></tr>");
        for (Level l : Level.getLevels()) {
            String color = "#E0E0E0";
            if (l.getDifficulty().equals("EASY")) color = "#2ECC71";
            if (l.getDifficulty().equals("MEDIUM")) color = "#3498DB";
            if (l.getDifficulty().equals("HARD")) color = "#F1C40F";
            if (l.getDifficulty().equals("EXPERT")) color = "#E67E22";
            if (l.getDifficulty().equals("INSANE")) color = "#E74C3C";
            html.append("<tr><td><b>").append(l.getName()).append("</b><br><span style='font-size:12px; color:#888'>").append(l.getDescription()).append("</span></td>");
            html.append("<td style='color:").append(color).append("'>").append(l.getDifficulty()).append("</td>");
            html.append("<td>").append(l.getTime()).append("s</td><td>").append(l.getModuleCount()).append("</td></tr>");
        }
        html.append("</table></body></html>");
        
        content.setText(html.toString());
        content.setCaretPosition(0);
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        Theme.customizeScrollBar(scroll);
        add(scroll, BorderLayout.CENTER);
    }
}
