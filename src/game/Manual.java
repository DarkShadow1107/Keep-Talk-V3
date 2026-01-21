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
        
        JLabel title = new JLabel(Localization.get("MANUAL_TITLE"));
        title.setFont(Theme.FONT_TITLE.deriveFont(32f));
        title.setForeground(Theme.ACCENT_ORANGE);
        header.add(title, BorderLayout.WEST);
        
        JButton backBtn = Theme.createButton(Localization.get("BTN_RETURN"));
        backBtn.setPreferredSize(new Dimension(200, 40));
        backBtn.setBackground(Theme.DANGER_RED);
        backBtn.addActionListener(e -> app.showMenu());
        header.add(backBtn, BorderLayout.EAST);
        
        add(header, BorderLayout.NORTH);

        // Content
        JEditorPane content = new JEditorPane();
        content.setEditable(false);
        content.setContentType("text/html; charset=UTF-8");
        content.setBackground(Theme.BG_COLOR);
        
        // CSS Styling for Dark Theme
        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body { font-family: 'Segoe UI', Arial, sans-serif; margin: 30px; color: #E0E0E0; background-color: #0A0A0F; }");
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

        String html = buildManualContent();
        content.setText(html);
        content.setCaretPosition(0);
        
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        Theme.customizeScrollBar(scroll);
        add(scroll, BorderLayout.CENTER);
    }

    private String buildManualContent() {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><meta charset='UTF-8'></head><body>");
        
        // Introduction
        html.append("<h1>").append(Localization.get("MANUAL_INTRO")).append("</h1>");
        html.append("<p>").append(Localization.get("MANUAL_INTRO_TEXT")).append("</p>");
        html.append("<div class='box'><span class='warning'>").append(Localization.get("MANUAL_WARNING")).append("</span> ");
        html.append(Localization.get("MANUAL_WARNING_TEXT")).append("</div>");

        html.append("<h1>").append(Localization.get("MANUAL_MODULES")).append("</h1>");
        
        // 1. Wires - More Explicit
        html.append("<h2>1. ").append(Localization.get("MOD_WIRES")).append("</h2>");
        html.append("<div class='box'>");
        html.append("<p>").append(Localization.get("WIRES_DESC")).append("</p>");
        html.append("<p><span class='highlight'>").append(Localization.get("WIRES_RULE")).append("</span></p>");
        html.append("<p>").append(Localization.get("WIRES_NOTE")).append("</p>");
        html.append("</div>");

        // 2. The Button - Complete Rules
        html.append("<h2>2. ").append(Localization.get("MOD_BUTTON")).append("</h2>");
        html.append("<div class='box'>");
        html.append("<h3>").append(Localization.get("BTN_STEP1")).append("</h3>");
        html.append("<p>").append(Localization.get("BTN_FOLLOW_RULES")).append("</p>");
        html.append("<ol>");
        html.append("<li>").append(Localization.get("BTN_BLUE_ABORT")).append("</li>");
        html.append("<li>").append(Localization.get("BTN_DETONATE")).append("</li>");
        html.append("<li>").append(Localization.get("BTN_WHITE_CAR")).append("</li>");
        html.append("<li>").append(Localization.get("BTN_FRK")).append("</li>");
        html.append("<li>").append(Localization.get("BTN_YELLOW")).append("</li>");
        html.append("<li>").append(Localization.get("BTN_RED_HOLD")).append("</li>");
        html.append("<li>").append(Localization.get("BTN_OTHERWISE")).append("</li>");
        html.append("</ol>");
        html.append("<h3>").append(Localization.get("BTN_STEP2")).append("</h3>");
        html.append("<p>").append(Localization.get("BTN_STRIP_INFO")).append("</p>");
        html.append("<table>");
        html.append("<tr><th>").append(Localization.get("BTN_STRIP_HEADER_COLOR")).append("</th><th>").append(Localization.get("BTN_STRIP_HEADER_RELEASE")).append("</th></tr>");
        html.append("<tr><td>").append(Localization.get("BTN_STRIP_BLUE")).append("</td><td><b>4</b></td></tr>");
        html.append("<tr><td>").append(Localization.get("BTN_STRIP_WHITE")).append("</td><td><b>1</b></td></tr>");
        html.append("<tr><td>").append(Localization.get("BTN_STRIP_YELLOW")).append("</td><td><b>5</b></td></tr>");
        html.append("<tr><td>").append(Localization.get("BTN_STRIP_OTHER")).append("</td><td><b>1</b></td></tr>");
        html.append("</table>");
        html.append("</div>");

        // 3. Keypad
        html.append("<h2>3. ").append(Localization.get("MOD_KEYPAD")).append("</h2>");
        html.append("<div class='box'>");
        html.append("<p>").append(Localization.get("KEYPAD_DESC")).append("</p>");
        html.append("<p>").append(Localization.get("KEYPAD_TIP")).append("</p>");
        html.append("</div>");

        // 4. Simon Says - Complete Tables
        html.append("<h2>4. ").append(Localization.get("MOD_SIMON")).append("</h2>");
        html.append("<div class='box'>");
        html.append("<p>").append(Localization.get("SIMON_DESC")).append("</p>");
        html.append("<p>").append(Localization.get("SIMON_SERIAL_CHECK")).append("</p>");
        html.append("<table>");
        html.append("<tr><th>").append(Localization.get("SIMON_FLASH")).append("</th><th>").append(Localization.get("SIMON_0_STRIKES")).append("</th><th>").append(Localization.get("SIMON_1_STRIKE")).append("</th><th>").append(Localization.get("SIMON_2_STRIKES")).append("</th></tr>");
        html.append("<tr style='background:#222'><td colspan='4'><b>").append(Localization.get("SIMON_WITH_VOWEL")).append("</b></td></tr>");
        html.append("<tr><td>").append(Localization.get("COLOR_RED")).append("</td><td>").append(Localization.get("COLOR_BLUE")).append("</td><td>").append(Localization.get("COLOR_YELLOW")).append("</td><td>").append(Localization.get("COLOR_GREEN")).append("</td></tr>");
        html.append("<tr><td>").append(Localization.get("COLOR_BLUE")).append("</td><td>").append(Localization.get("COLOR_RED")).append("</td><td>").append(Localization.get("COLOR_GREEN")).append("</td><td>").append(Localization.get("COLOR_RED")).append("</td></tr>");
        html.append("<tr><td>").append(Localization.get("COLOR_GREEN")).append("</td><td>").append(Localization.get("COLOR_YELLOW")).append("</td><td>").append(Localization.get("COLOR_BLUE")).append("</td><td>").append(Localization.get("COLOR_YELLOW")).append("</td></tr>");
        html.append("<tr><td>").append(Localization.get("COLOR_YELLOW")).append("</td><td>").append(Localization.get("COLOR_GREEN")).append("</td><td>").append(Localization.get("COLOR_RED")).append("</td><td>").append(Localization.get("COLOR_BLUE")).append("</td></tr>");
        html.append("<tr style='background:#222'><td colspan='4'><b>").append(Localization.get("SIMON_WITHOUT_VOWEL")).append("</b></td></tr>");
        html.append("<tr><td>").append(Localization.get("COLOR_RED")).append("</td><td>").append(Localization.get("COLOR_BLUE")).append("</td><td>").append(Localization.get("COLOR_RED")).append("</td><td>").append(Localization.get("COLOR_YELLOW")).append("</td></tr>");
        html.append("<tr><td>").append(Localization.get("COLOR_BLUE")).append("</td><td>").append(Localization.get("COLOR_YELLOW")).append("</td><td>").append(Localization.get("COLOR_BLUE")).append("</td><td>").append(Localization.get("COLOR_GREEN")).append("</td></tr>");
        html.append("<tr><td>").append(Localization.get("COLOR_GREEN")).append("</td><td>").append(Localization.get("COLOR_GREEN")).append("</td><td>").append(Localization.get("COLOR_YELLOW")).append("</td><td>").append(Localization.get("COLOR_BLUE")).append("</td></tr>");
        html.append("<tr><td>").append(Localization.get("COLOR_YELLOW")).append("</td><td>").append(Localization.get("COLOR_RED")).append("</td><td>").append(Localization.get("COLOR_GREEN")).append("</td><td>").append(Localization.get("COLOR_RED")).append("</td></tr>");
        html.append("</table>");
        html.append("<p>").append(Localization.get("SIMON_COMPLETE")).append("</p>");
        html.append("</div>");

        // 5. Maze
        html.append("<h2>5. ").append(Localization.get("MOD_MAZE")).append("</h2>");
        html.append("<div class='box'>");
        html.append("<p>").append(Localization.get("MAZE_DESC1")).append("</p>");
        html.append("<p>").append(Localization.get("MAZE_DESC2")).append("</p>");
        html.append("<p>").append(Localization.get("MAZE_WALLS")).append("</p>");
        html.append("<p>").append(Localization.get("MAZE_BUTTONS")).append("</p>");
        html.append("</div>");

        // 6. Memory - Full Stage Details
        html.append("<h2>6. ").append(Localization.get("MOD_MEMORY")).append("</h2>");
        html.append("<div class='box'>");
        html.append("<p>").append(Localization.get("MEMORY_DESC")).append("</p>");
        html.append("<table>");
        html.append("<tr><th>").append(Localization.get("MEMORY_STAGE")).append("</th><th>").append(Localization.get("MEMORY_DISPLAY")).append("</th><th>").append(Localization.get("MEMORY_ACTION")).append("</th></tr>");
        html.append("<tr><td rowspan='4'><b>").append(Localization.get("MEMORY_STAGE_1")).append("</b></td><td>1</td><td>").append(Localization.get("MEMORY_PRESS_POS")).append(" 2</td></tr>");
        html.append("<tr><td>2</td><td>").append(Localization.get("MEMORY_PRESS_POS")).append(" 2</td></tr>");
        html.append("<tr><td>3</td><td>").append(Localization.get("MEMORY_PRESS_POS")).append(" 3</td></tr>");
        html.append("<tr><td>4</td><td>").append(Localization.get("MEMORY_PRESS_POS")).append(" 4</td></tr>");
        html.append("<tr><td rowspan='4'><b>").append(Localization.get("MEMORY_STAGE_2")).append("</b></td><td>1</td><td>").append(Localization.get("MEMORY_PRESS_LABEL")).append(" '4'</td></tr>");
        html.append("<tr><td>2</td><td>").append(Localization.get("MEMORY_SAME_POS_AS")).append(" 1</td></tr>");
        html.append("<tr><td>3</td><td>").append(Localization.get("MEMORY_PRESS_POS")).append(" 1</td></tr>");
        html.append("<tr><td>4</td><td>").append(Localization.get("MEMORY_SAME_POS_AS")).append(" 1</td></tr>");
        html.append("<tr><td rowspan='4'><b>").append(Localization.get("MEMORY_STAGE_3")).append("</b></td><td>1</td><td>").append(Localization.get("MEMORY_SAME_LABEL_AS")).append(" 2</td></tr>");
        html.append("<tr><td>2</td><td>").append(Localization.get("MEMORY_SAME_LABEL_AS")).append(" 1</td></tr>");
        html.append("<tr><td>3</td><td>").append(Localization.get("MEMORY_PRESS_POS")).append(" 3</td></tr>");
        html.append("<tr><td>4</td><td>").append(Localization.get("MEMORY_PRESS_LABEL")).append(" '4'</td></tr>");
        html.append("<tr><td rowspan='4'><b>").append(Localization.get("MEMORY_STAGE_4")).append("</b></td><td>1</td><td>").append(Localization.get("MEMORY_SAME_POS_AS")).append(" 1</td></tr>");
        html.append("<tr><td>2</td><td>").append(Localization.get("MEMORY_PRESS_POS")).append(" 1</td></tr>");
        html.append("<tr><td>3</td><td>").append(Localization.get("MEMORY_SAME_POS_AS")).append(" 2</td></tr>");
        html.append("<tr><td>4</td><td>").append(Localization.get("MEMORY_SAME_POS_AS")).append(" 2</td></tr>");
        html.append("<tr><td rowspan='4'><b>").append(Localization.get("MEMORY_STAGE_5")).append("</b></td><td>1</td><td>").append(Localization.get("MEMORY_SAME_LABEL_AS")).append(" 1</td></tr>");
        html.append("<tr><td>2</td><td>").append(Localization.get("MEMORY_SAME_LABEL_AS")).append(" 2</td></tr>");
        html.append("<tr><td>3</td><td>").append(Localization.get("MEMORY_SAME_LABEL_AS")).append(" 4</td></tr>");
        html.append("<tr><td>4</td><td>").append(Localization.get("MEMORY_SAME_LABEL_AS")).append(" 3</td></tr>");
        html.append("</table>");
        html.append("</div>");

        // 7. Morse Code
        html.append("<h2>7. ").append(Localization.get("MOD_MORSE")).append("</h2>");
        html.append("<div class='box'>");
        html.append("<p>").append(Localization.get("MORSE_DESC")).append("</p>");
        html.append("<table>");
        html.append("<tr><th>").append(Localization.get("MORSE_WORD")).append("</th><th>").append(Localization.get("MORSE_FREQUENCY")).append("</th><th>").append(Localization.get("MORSE_WORD")).append("</th><th>").append(Localization.get("MORSE_FREQUENCY")).append("</th></tr>");
        html.append("<tr><td>SHELL</td><td>3.505 MHz</td><td>BOMBS</td><td>3.565 MHz</td></tr>");
        html.append("<tr><td>HALLS</td><td>3.515 MHz</td><td>BREAK</td><td>3.572 MHz</td></tr>");
        html.append("<tr><td>SLICK</td><td>3.522 MHz</td><td>BRICK</td><td>3.575 MHz</td></tr>");
        html.append("<tr><td>TRICK</td><td>3.532 MHz</td><td>STEAK</td><td>3.582 MHz</td></tr>");
        html.append("<tr><td>BOXES</td><td>3.535 MHz</td><td>STING</td><td>3.592 MHz</td></tr>");
        html.append("<tr><td>LEAKS</td><td>3.542 MHz</td><td>VECTOR</td><td>3.595 MHz</td></tr>");
        html.append("<tr><td>STROBE</td><td>3.545 MHz</td><td>BEATS</td><td>3.600 MHz</td></tr>");
        html.append("<tr><td>BISTRO</td><td>3.552 MHz</td><td>FLICK</td><td>3.555 MHz</td></tr>");
        html.append("</table>");
        html.append("</div>");

        // 8. Complicated Wires
        html.append("<h2>8. ").append(Localization.get("MOD_COMPWIRES")).append("</h2>");
        html.append("<div class='box'>");
        html.append("<p>").append(Localization.get("COMPWIRES_DESC")).append("</p>");
        html.append("<p>").append(Localization.get("COMPWIRES_TABLE_INFO")).append("</p>");
        html.append("<table><tr><th>").append(Localization.get("COMPWIRES_RED")).append("</th><th>").append(Localization.get("COMPWIRES_BLUE")).append("</th><th>").append(Localization.get("COMPWIRES_STAR")).append("</th><th>").append(Localization.get("COMPWIRES_LED")).append("</th><th>").append(Localization.get("COMPWIRES_ACTION")).append("</th></tr>");
        html.append("<tr><td>✓</td><td>✓</td><td>✓</td><td>✓</td><td>").append(Localization.get("COMPWIRES_DONT_CUT")).append("</td></tr>");
        html.append("<tr><td>✓</td><td>✓</td><td>✓</td><td></td><td>").append(Localization.get("COMPWIRES_CUT_IF_PARALLEL")).append("</td></tr>");
        html.append("<tr><td>✓</td><td>✓</td><td></td><td>✓</td><td>").append(Localization.get("COMPWIRES_CUT_IF_EVEN")).append("</td></tr>");
        html.append("<tr><td>✓</td><td>✓</td><td></td><td></td><td>").append(Localization.get("COMPWIRES_CUT_IF_EVEN")).append("</td></tr>");
        html.append("<tr><td>✓</td><td></td><td>✓</td><td>✓</td><td>").append(Localization.get("COMPWIRES_CUT_IF_BATTERIES")).append("</td></tr>");
        html.append("<tr><td>✓</td><td></td><td>✓</td><td></td><td><b>").append(Localization.get("COMPWIRES_CUT")).append("</b></td></tr>");
        html.append("<tr><td>✓</td><td></td><td></td><td>✓</td><td>").append(Localization.get("COMPWIRES_CUT_IF_BATTERIES")).append("</td></tr>");
        html.append("<tr><td>✓</td><td></td><td></td><td></td><td>").append(Localization.get("COMPWIRES_CUT_IF_EVEN")).append("</td></tr>");
        html.append("<tr><td></td><td>✓</td><td>✓</td><td>✓</td><td>").append(Localization.get("COMPWIRES_CUT_IF_PARALLEL")).append("</td></tr>");
        html.append("<tr><td></td><td>✓</td><td>✓</td><td></td><td>").append(Localization.get("COMPWIRES_DONT_CUT")).append("</td></tr>");
        html.append("<tr><td></td><td>✓</td><td></td><td>✓</td><td>").append(Localization.get("COMPWIRES_CUT_IF_PARALLEL")).append("</td></tr>");
        html.append("<tr><td></td><td>✓</td><td></td><td></td><td>").append(Localization.get("COMPWIRES_CUT_IF_EVEN")).append("</td></tr>");
        html.append("<tr><td></td><td></td><td>✓</td><td>✓</td><td>").append(Localization.get("COMPWIRES_CUT_IF_BATTERIES")).append("</td></tr>");
        html.append("<tr><td></td><td></td><td>✓</td><td></td><td><b>").append(Localization.get("COMPWIRES_CUT")).append("</b></td></tr>");
        html.append("<tr><td></td><td></td><td></td><td>✓</td><td>").append(Localization.get("COMPWIRES_DONT_CUT")).append("</td></tr>");
        html.append("<tr><td></td><td></td><td></td><td></td><td><b>").append(Localization.get("COMPWIRES_CUT")).append("</b></td></tr>");
        html.append("</table>");
        html.append("<p>").append(Localization.get("COMPWIRES_FINAL")).append("</p>");
        html.append("</div>");

        // 9. Password
        html.append("<h2>9. ").append(Localization.get("MOD_PASSWORD")).append("</h2>");
        html.append("<div class='box'>");
        html.append("<p>").append(Localization.get("PASSWORD_DESC")).append("</p>");
        html.append("<p>").append(Localization.get("PASSWORD_POSSIBLE")).append("</p>");
        html.append("<p style='font-size:12px'>").append(Localization.get("PASSWORD_LIST")).append("</p>");
        html.append("</div>");

        // 10. Who's on First
        html.append("<h2>10. ").append(Localization.get("MOD_WHOSONFIRST")).append("</h2>");
        html.append("<div class='box'>");
        html.append("<p>").append(Localization.get("WHOSONFIRST_STEP1")).append("</p>");
        html.append("<table><tr><th>").append(Localization.get("WHOSONFIRST_DISPLAY")).append("</th><th>").append(Localization.get("WHOSONFIRST_LOOK_AT")).append("</th><th>").append(Localization.get("WHOSONFIRST_DISPLAY")).append("</th><th>").append(Localization.get("WHOSONFIRST_LOOK_AT")).append("</th></tr>");
        html.append("<tr><td>YES</td><td>").append(Localization.get("WHOSONFIRST_MIDDLE_LEFT")).append("</td><td>FIRST</td><td>").append(Localization.get("WHOSONFIRST_TOP_RIGHT")).append("</td></tr>");
        html.append("<tr><td>DISPLAY</td><td>").append(Localization.get("WHOSONFIRST_BOTTOM_RIGHT")).append("</td><td>OKAY</td><td>").append(Localization.get("WHOSONFIRST_TOP_RIGHT")).append("</td></tr>");
        html.append("<tr><td>SAYS</td><td>").append(Localization.get("WHOSONFIRST_BOTTOM_RIGHT")).append("</td><td>NOTHING</td><td>").append(Localization.get("WHOSONFIRST_MIDDLE_LEFT")).append("</td></tr>");
        html.append("<tr><td>").append(Localization.get("WHOSONFIRST_EMPTY")).append("</td><td>").append(Localization.get("WHOSONFIRST_BOTTOM_LEFT")).append("</td><td>BLANK</td><td>").append(Localization.get("WHOSONFIRST_MIDDLE_RIGHT")).append("</td></tr>");
        html.append("<tr><td>NO</td><td>").append(Localization.get("WHOSONFIRST_BOTTOM_RIGHT")).append("</td><td>LED</td><td>").append(Localization.get("WHOSONFIRST_MIDDLE_LEFT")).append("</td></tr>");
        html.append("<tr><td>LEAD</td><td>").append(Localization.get("WHOSONFIRST_BOTTOM_RIGHT")).append("</td><td>READ</td><td>").append(Localization.get("WHOSONFIRST_MIDDLE_RIGHT")).append("</td></tr>");
        html.append("<tr><td>RED</td><td>").append(Localization.get("WHOSONFIRST_MIDDLE_RIGHT")).append("</td><td>REED</td><td>").append(Localization.get("WHOSONFIRST_BOTTOM_LEFT")).append("</td></tr>");
        html.append("<tr><td>LEED</td><td>").append(Localization.get("WHOSONFIRST_BOTTOM_LEFT")).append("</td><td>HOLD ON</td><td>").append(Localization.get("WHOSONFIRST_BOTTOM_RIGHT")).append("</td></tr>");
        html.append("<tr><td>YOU</td><td>").append(Localization.get("WHOSONFIRST_MIDDLE_RIGHT")).append("</td><td>YOU ARE</td><td>").append(Localization.get("WHOSONFIRST_BOTTOM_RIGHT")).append("</td></tr>");
        html.append("<tr><td>YOUR</td><td>").append(Localization.get("WHOSONFIRST_MIDDLE_RIGHT")).append("</td><td>YOU'RE</td><td>").append(Localization.get("WHOSONFIRST_MIDDLE_RIGHT")).append("</td></tr>");
        html.append("<tr><td>UR</td><td>").append(Localization.get("WHOSONFIRST_TOP_LEFT")).append("</td><td>THERE</td><td>").append(Localization.get("WHOSONFIRST_BOTTOM_RIGHT")).append("</td></tr>");
        html.append("<tr><td>THEY'RE</td><td>").append(Localization.get("WHOSONFIRST_BOTTOM_LEFT")).append("</td><td>THEIR</td><td>").append(Localization.get("WHOSONFIRST_MIDDLE_RIGHT")).append("</td></tr>");
        html.append("<tr><td>THEY ARE</td><td>").append(Localization.get("WHOSONFIRST_MIDDLE_LEFT")).append("</td><td>SEE</td><td>").append(Localization.get("WHOSONFIRST_BOTTOM_RIGHT")).append("</td></tr>");
        html.append("<tr><td>C</td><td>").append(Localization.get("WHOSONFIRST_TOP_RIGHT")).append("</td><td>CEE</td><td>").append(Localization.get("WHOSONFIRST_BOTTOM_RIGHT")).append("</td></tr>");
        html.append("</table>");
        html.append("<p>").append(Localization.get("WHOSONFIRST_STEP2")).append("</p>");
        html.append("<div style='font-size:11px; line-height:1.4'>");
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
        html.append("</div>");
        html.append("<p>").append(Localization.get("WHOSONFIRST_COMPLETE")).append("</p>");
        html.append("</div>");

        // 11. Binary
        html.append("<h2>11. ").append(Localization.get("MOD_BINARY")).append("</h2>");
        html.append("<div class='box'>");
        html.append("<p>").append(Localization.get("BINARY_DESC")).append("</p>");
        html.append("<p>").append(Localization.get("BINARY_SWITCHES")).append("</p>");
        html.append("<p>").append(Localization.get("BINARY_EXAMPLE")).append("</p>");
        html.append("<table>");
        html.append("<tr><th>").append(Localization.get("BINARY_DECIMAL")).append("</th><th>").append(Localization.get("BINARY_BINARY")).append("</th><th>").append(Localization.get("BINARY_DECIMAL")).append("</th><th>").append(Localization.get("BINARY_BINARY")).append("</th></tr>");
        html.append("<tr><td>0</td><td>00000</td><td>16</td><td>10000</td></tr>");
        html.append("<tr><td>1</td><td>00001</td><td>17</td><td>10001</td></tr>");
        html.append("<tr><td>5</td><td>00101</td><td>20</td><td>10100</td></tr>");
        html.append("<tr><td>10</td><td>01010</td><td>25</td><td>11001</td></tr>");
        html.append("<tr><td>15</td><td>01111</td><td>31</td><td>11111</td></tr>");
        html.append("</table>");
        html.append("</div>");

        // 12. Logic Gates
        html.append("<h2>12. ").append(Localization.get("MOD_LOGIC")).append("</h2>");
        html.append("<div class='box'>");
        html.append("<p>").append(Localization.get("LOGIC_DESC")).append("</p>");
        html.append("<table>");
        html.append("<tr><th>").append(Localization.get("LOGIC_GATE")).append("</th><th>A=0, B=0</th><th>A=0, B=1</th><th>A=1, B=0</th><th>A=1, B=1</th></tr>");
        html.append("<tr><td><b>AND</b></td><td>0</td><td>0</td><td>0</td><td>1</td></tr>");
        html.append("<tr><td><b>OR</b></td><td>0</td><td>1</td><td>1</td><td>1</td></tr>");
        html.append("<tr><td><b>XOR</b></td><td>0</td><td>1</td><td>1</td><td>0</td></tr>");
        html.append("<tr><td><b>NAND</b></td><td>1</td><td>1</td><td>1</td><td>0</td></tr>");
        html.append("<tr><td><b>NOR</b></td><td>1</td><td>0</td><td>0</td><td>0</td></tr>");
        html.append("</table>");
        html.append("<p>").append(Localization.get("LOGIC_CLICK_OUTPUT")).append("</p>");
        html.append("</div>");

        // Mission Dossiers
        html.append("<h1>").append(Localization.get("MANUAL_MISSIONS")).append("</h1>");
        html.append("<table><tr><th>").append(Localization.get("MISSION_NAME")).append("</th><th>").append(Localization.get("MISSION_DIFFICULTY")).append("</th><th>").append(Localization.get("MISSION_TIME")).append("</th><th>").append(Localization.get("MISSION_MODULES")).append("</th></tr>");
        for (Level l : Level.getLevels()) {
            String color = "#E0E0E0";
            String diff = l.getDifficulty();
            if (diff.equals("EASY")) color = "#2ECC71";
            else if (diff.equals("MEDIUM")) color = "#3498DB";
            else if (diff.equals("HARD")) color = "#F1C40F";
            else if (diff.equals("EXPERT")) color = "#E67E22";
            else if (diff.equals("INSANE")) color = "#E74C3C";
            
            html.append("<tr><td><b>").append(l.getName()).append("</b><br><span style='font-size:12px; color:#888'>")
                .append(l.getDescription()).append("</span></td>");
            html.append("<td style='color:").append(color).append("'>").append(diff).append("</td>");
            html.append("<td>").append(l.getTime()).append("s</td><td>").append(l.getModuleCount()).append("</td></tr>");
        }
        html.append("</table>");
        
        html.append("</body></html>");
        return html.toString();
    }
}
