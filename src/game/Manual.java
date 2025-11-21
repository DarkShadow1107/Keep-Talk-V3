package game;

import javax.swing.*;
import java.awt.*;

public class Manual extends JPanel {
    public Manual(App app) {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_COLOR);

        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        editorPane.setText(getManualHtml());
        editorPane.setCaretPosition(0);
        editorPane.setBackground(new Color(30, 30, 35));

        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = Theme.createButton("Back to Menu");
        backButton.addActionListener(e -> app.showMenu());
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Theme.BG_COLOR);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private String getManualHtml() {
        return "<html><head><style>" +
               "body { font-family: 'Segoe UI', sans-serif; padding: 40px; color: #e0e0e0; background-color: #1e1e23; }" +
               "h1 { font-size: 42px; color: #e74c3c; text-align: center; border-bottom: 2px solid #e74c3c; padding-bottom: 20px; margin-bottom: 40px; text-transform: uppercase; letter-spacing: 3px; }" +
               "h2 { font-size: 28px; color: #3498db; border-left: 5px solid #3498db; padding-left: 15px; margin-top: 40px; margin-bottom: 20px; text-transform: uppercase; background-color: #2c3e50; padding: 10px; }" +
               "h3 { font-size: 20px; color: #f1c40f; margin-top: 25px; margin-bottom: 10px; border-bottom: 1px solid #555; padding-bottom: 5px; }" +
               ".section { background-color: #25252a; border: 1px solid #444; padding: 25px; margin-bottom: 30px; box-shadow: 0 4px 6px rgba(0,0,0,0.3); border-radius: 5px; }" +
               ".warning { background-color: #3e1a1a; color: #e74c3c; border: 1px solid #c0392b; padding: 15px; margin: 15px 0; font-weight: bold; border-radius: 3px; }" +
               "table { width: 100%; border-collapse: collapse; margin-top: 15px; font-size: 15px; color: #ecf0f1; }" +
               "th, td { border: 1px solid #555; padding: 12px; text-align: left; }" +
               "th { background-color: #2c3e50; font-weight: bold; color: #3498db; }" +
               "tr:nth-child(even) { background-color: #2a2a2f; }" +
               "ul, ol { margin-top: 10px; margin-bottom: 10px; padding-left: 25px; }" +
               "li { margin-bottom: 8px; line-height: 1.5; }" +
               "strong { color: #ffffff; font-weight: bold; }" +
               "a { color: #3498db; text-decoration: none; }" +
               "a:hover { text-decoration: underline; }" +
               ".toc { background-color: #25252a; padding: 20px; border: 1px solid #444; margin-bottom: 40px; }" +
               ".toc h2 { margin-top: 0; background-color: transparent; border-left: none; padding-left: 0; color: #e74c3c; }" +
               ".toc ul { list-style-type: none; padding-left: 0; }" +
               ".toc li { margin-bottom: 5px; }" +
               "</style></head><body>" +

               "<h1>Bomb Defusal Manual <span style='font-size: 18px; color: #7f8c8d; vertical-align: middle; font-weight: normal;'>v2.1 [CLASSIFIED]</span></h1>" +
               
               "<div class='toc'>" +
               "<h2>Table of Contents</h2>" +
               "<ul>" +
               "<li><a href='#wires'>Section 1: Wires</a></li>" +
               "<li><a href='#button'>Section 2: The Button</a></li>" +
               "<li><a href='#keypad'>Section 3: Keypad</a></li>" +
               "<li><a href='#simon'>Section 4: Simon Says</a></li>" +
               "<li><a href='#wof'>Section 5: Who's On First</a></li>" +
               "<li><a href='#memory'>Section 6: Memory</a></li>" +
               "<li><a href='#morse'>Section 7: Morse Code</a></li>" +
               "<li><a href='#password'>Section 8: Password</a></li>" +
               "<li><a href='#maze'>Section 9: Maze</a></li>" +
               "<li><a href='#complicated'>Section 10: Complicated Wires</a></li>" +
               "<li><a href='#appendix'>Appendix: Indicators & Batteries</a></li>" +
               "</ul>" +
               "</div>" +

               "<div class='section' id='appendix'>" +
               "<h2>Appendix: Reference</h2>" +
               "<h3>Indicators</h3>" +
               "<p>Common indicators found on casings:</p>" +
               "<p style='font-family: monospace; font-size: 1.2em; color: #f1c40f;'>SND, CLR, CAR, IND, FRQ, SIG, NSA, MSA, TRN, BOB, FRK</p>" +
               "<h3>Batteries</h3>" +
               "<p>Batteries can be AA, D, or 9V. Count the total number of batteries on the casing.</p>" +
               "</div>" +

               "<div class='section' id='wires'>" +
               "<h2>Section 1: Wires</h2>" +
               "<p>Wires are the lifeblood of electronics! Wait, no, electricity is. Wires are more like the arteries. The arteries that if you cut the wrong one, you die.</p>" +
               "<h3>3 WIRES:</h3>" +
               "<ul>" +
               "<li>If there are no red wires, cut the second wire.</li>" +
               "<li>Otherwise, if the last wire is white, cut the last wire.</li>" +
               "<li>Otherwise, if there is more than one blue wire, cut the last blue wire.</li>" +
               "<li>Otherwise, cut the last wire.</li>" +
               "</ul>" +
               "<h3>4 WIRES:</h3>" +
               "<ul>" +
               "<li>If there is more than one red wire and the last digit of the serial number is odd, cut the last red wire.</li>" +
               "<li>Otherwise, if the last wire is yellow and there are no red wires, cut the first wire.</li>" +
               "<li>Otherwise, if there is exactly one blue wire, cut the first wire.</li>" +
               "<li>Otherwise, if there is more than one yellow wire, cut the last wire.</li>" +
               "<li>Otherwise, cut the second wire.</li>" +
               "</ul>" +
               "<h3>5 WIRES:</h3>" +
               "<ul>" +
               "<li>If the last wire is black and the last digit of the serial number is odd, cut the fourth wire.</li>" +
               "<li>Otherwise, if there is exactly one red wire and there is more than one yellow wire, cut the first wire.</li>" +
               "<li>Otherwise, if there are no black wires, cut the second wire.</li>" +
               "<li>Otherwise, cut the first wire.</li>" +
               "</ul>" +
               "<h3>6 WIRES:</h3>" +
               "<ul>" +
               "<li>If there are no yellow wires and the last digit of the serial number is odd, cut the third wire.</li>" +
               "<li>Otherwise, if there is exactly one yellow wire and there is more than one white wire, cut the fourth wire.</li>" +
               "<li>Otherwise, if there are no red wires, cut the last wire.</li>" +
               "<li>Otherwise, cut the fourth wire.</li>" +
               "</ul>" +
               "</div>" +
               
               "<div class='section' id='button'>" +
               "<h2>Section 2: The Button</h2>" +
               "<ol>" +
               "<li>If the button is blue and the button says \"Abort\", hold the button.</li>" +
               "<li>If there is more than 1 battery on the bomb and the button says \"Detonate\", press and immediately release the button.</li>" +
               "<li>If the button is white and there is a lit indicator with label CAR, hold the button.</li>" +
               "<li>If there are more than 2 batteries on the bomb and there is a lit indicator with label FRK, press and immediately release the button.</li>" +
               "<li>If the button is yellow, hold the button.</li>" +
               "<li>If the button is red and the button says \"Hold\", press and immediately release the button.</li>" +
               "<li>If none of the above apply, hold the button.</li>" +
               "</ol>" +
               "<h3>Releasing a Held Button:</h3>" +
               "<p>If you start holding the button down, a colored strip will light up on the right of the module. Based on its color, you must release the button at a specific point in time:</p>" +
               "<ul>" +
               "<li><strong>Blue strip:</strong> release when the countdown timer has a 4 in any position.</li>" +
               "<li><strong>White strip:</strong> release when the countdown timer has a 1 in any position.</li>" +
               "<li><strong>Yellow strip:</strong> release when the countdown timer has a 5 in any position.</li>" +
               "<li><strong>Any other color strip:</strong> release when the countdown timer has a 1 in any position.</li>" +
               "</ul>" +
               "</div>" +

               "<div class='section' id='keypad'>" +
               "<h2>Section 3: Keypad</h2>" +
               "<p>Only one column below has all four of the symbols from the keypad. Press the four buttons in the order their symbols appear from top to bottom within that column.</p>" +
               "<table>" +
               "<tr><td>Ϙ, Ѧ, ƛ, Ϟ, Ѭ, ϗ, Ͽ</td><td>Ӭ, Ϙ, Ͽ, Ҩ, ☆, ϗ, ¿</td></tr>" +
               "<tr><td>©, Ѽ, Ҩ, Ж, R, ƛ, ☆</td><td>б, ¶, b, Ѭ, Ж, ¿, ☺</td></tr>" +
               "</table>" +
               "</div>" +
               
               "<div class='section' id='simon'>" +
               "<h2>Section 4: Simon Says</h2>" +
               "<p><strong>Vowel in Serial Number:</strong></p>" +
               "<ul>" +
               "<li><strong>0 Strikes:</strong> Red->Blue, Blue->Red, Green->Yellow, Yellow->Green</li>" +
               "<li><strong>1 Strike:</strong> Red->Yellow, Blue->Green, Green->Blue, Yellow->Red</li>" +
               "<li><strong>2+ Strikes:</strong> Red->Green, Blue->Red, Green->Yellow, Yellow->Blue</li>" +
               "</ul>" +
               "<p><strong>No Vowel in Serial Number:</strong></p>" +
               "<ul>" +
               "<li><strong>0 Strikes:</strong> Red->Blue, Blue->Yellow, Green->Green, Yellow->Red</li>" +
               "<li><strong>1 Strike:</strong> Red->Red, Blue->Blue, Green->Yellow, Yellow->Green</li>" +
               "<li><strong>2+ Strikes:</strong> Red->Yellow, Blue->Green, Green->Blue, Yellow->Red</li>" +
               "</ul>" +
               "</div>" +

               "<div class='section' id='wof'>" +
               "<h2>Section 5: Who's On First</h2>" +
               "<p><strong>Step 1:</strong> Read the display. Use the table below to determine which button position to read.</p>" +
               "<table>" +
               "<tr><td>YES: Middle Left</td><td>FIRST: Top Right</td><td>DISPLAY: Bottom Right</td></tr>" +
               "<tr><td>OKAY: Top Right</td><td>SAYS: Bottom Right</td><td>NOTHING: Middle Left</td></tr>" +
               "<tr><td>BLANK: Middle Right</td><td>NO: Bottom Right</td><td>LED: Middle Left</td></tr>" +
               "<tr><td>LEAD: Bottom Right</td><td>READ: Middle Right</td><td>RED: Middle Right</td></tr>" +
               "<tr><td>REED: Bottom Left</td><td>LEED: Bottom Left</td><td>HOLD ON: Bottom Right</td></tr>" +
               "<tr><td>YOU: Middle Right</td><td>YOU ARE: Bottom Right</td><td>YOUR: Middle Right</td></tr>" +
               "<tr><td>YOU'RE: Middle Right</td><td>UR: Top Left</td><td>THERE: Bottom Right</td></tr>" +
               "<tr><td>THEY'RE: Bottom Left</td><td>THEIR: Middle Right</td><td>THEY ARE: Middle Left</td></tr>" +
               "<tr><td>SEE: Bottom Right</td><td>C: Top Right</td><td>CEE: Bottom Right</td></tr>" +
               "</table>" +
               "<p><strong>Step 2:</strong> Using the word from the button at that position, find the corresponding list below. Press the first word in that list that appears on any button.</p>" +
               "<p><em>(Refer to full manual for word lists - Simplified for this view)</em></p>" +
               "<p><strong>READY:</strong> YES, OKAY, WHAT, MIDDLE, LEFT, PRESS, RIGHT, BLANK, READY</p>" +
               "<p><strong>FIRST:</strong> LEFT, OKAY, YES, MIDDLE, NO, RIGHT, NOTHING, UHHH, WAIT, READY, BLANK, WHAT, PRESS, FIRST</p>" +
               "<p><strong>NO:</strong> BLANK, UHHH, WAIT, FIRST, WHAT, READY, RIGHT, YES, NOTHING, LEFT, PRESS, OKAY, NO</p>" +
               "<p><strong>BLANK:</strong> WAIT, RIGHT, OKAY, MIDDLE, BLANK</p>" +
               "<p><strong>NOTHING:</strong> UHHH, RIGHT, OKAY, MIDDLE, YES, BLANK, NO, PRESS, LEFT, WHAT, WAIT, FIRST, NOTHING</p>" +
               "<p><strong>YES:</strong> OKAY, RIGHT, UHHH, MIDDLE, FIRST, WHAT, PRESS, READY, NOTHING, YES</p>" +
               "<p><strong>WHAT:</strong> UHHH, WHAT</p>" +
               "<p><strong>UHHH:</strong> READY, NOTHING, LEFT, WHAT, OKAY, YES, RIGHT, NO, PRESS, BLANK, UHHH</p>" +
               "<p><strong>LEFT:</strong> RIGHT, LEFT</p>" +
               "<p><strong>RIGHT:</strong> YES, NOTHING, READY, PRESS, NO, WAIT, WHAT, RIGHT</p>" +
               "<p><strong>MIDDLE:</strong> BLANK, READY, OKAY, WHAT, NOTHING, PRESS, NO, WAIT, LEFT, MIDDLE</p>" +
               "<p><strong>OKAY:</strong> MIDDLE, NO, FIRST, YES, UHHH, NOTHING, WAIT, OKAY</p>" +
               "<p><strong>WAIT:</strong> UHHH, NO, BLANK, OKAY, YES, LEFT, FIRST, PRESS, WHAT, WAIT</p>" +
               "<p><strong>PRESS:</strong> RIGHT, MIDDLE, YES, READY, PRESS</p>" +
               "<p><strong>YOU:</strong> SURE, YOU ARE, YOUR, YOU'RE, NEXT, UH HUH, UR, HOLD, WHAT?, YOU</p>" +
               "<p><strong>YOU ARE:</strong> YOUR, NEXT, LIKE, HUH, WHAT?, DONE, UH UH, HOLD, YOU, U, YOU'RE, SURE, UR, YOU ARE</p>" +
               "<p><strong>YOUR:</strong> UH UH, YOU ARE, UH HUH, YOUR</p>" +
               "<p><strong>YOU'RE:</strong> YOU, YOU'RE</p>" +
               "<p><strong>UR:</strong> DONE, U, UR</p>" +
               "<p><strong>U:</strong> UH HUH, SURE, NEXT, WHAT?, YOU'RE, UR, UH UH, DONE, U</p>" +
               "<p><strong>UH HUH:</strong> UH HUH</p>" +
               "<p><strong>UH UH:</strong> UR, U, YOU ARE, YOU'RE, NEXT, UH UH</p>" +
               "<p><strong>WHAT?:</strong> YOU, HOLD, YOU'RE, YOUR, U, DONE, UH UH, LIKE, YOU ARE, UH HUH, UR, NEXT, WHAT?</p>" +
               "<p><strong>DONE:</strong> SURE, UH HUH, NEXT, WHAT?, YOUR, UR, YOU'RE, HOLD, LIKE, YOU, U, YOU ARE, UH UH, DONE</p>" +
               "<p><strong>NEXT:</strong> WHAT?, UH HUH, UH UH, YOUR, HOLD, SURE, NEXT</p>" +
               "<p><strong>HOLD:</strong> YOU ARE, U, DONE, UH UH, YOU, UR, SURE, WHAT?, HOLD</p>" +
               "<p><strong>SURE:</strong> YOU ARE, DONE, LIKE, YOU'RE, YOU, HOLD, UH HUH, UR, SURE</p>" +
               "<p><strong>LIKE:</strong> YOU'RE, NEXT, U, UR, HOLD, DONE, UH UH, WHAT?, UH HUH, YOU, LIKE</p>" +
               "</div>" +

               "<div class='section' id='memory'>" +
               "<h2>Section 6: Memory</h2>" +
               "<p><strong>Stage 1:</strong></p>" +
               "<ul><li>Display 1: Press 2nd position.</li><li>Display 2: Press 2nd position.</li><li>Display 3: Press 3rd position.</li><li>Display 4: Press 4th position.</li></ul>" +
               "<p><strong>Stage 2:</strong></p>" +
               "<ul><li>Display 1: Press button labeled '4'.</li><li>Display 2: Press button in same position as Stage 1.</li><li>Display 3: Press 1st position.</li><li>Display 4: Press button in same position as Stage 1.</li></ul>" +
               "<p><strong>Stage 3:</strong></p>" +
               "<ul><li>Display 1: Press button with same label as Stage 2.</li><li>Display 2: Press button with same label as Stage 1.</li><li>Display 3: Press 3rd position.</li><li>Display 4: Press button labeled '4'.</li></ul>" +
               "<p><strong>Stage 4:</strong></p>" +
               "<ul><li>Display 1: Press button in same position as Stage 1.</li><li>Display 2: Press 1st position.</li><li>Display 3: Press button in same position as Stage 2.</li><li>Display 4: Press button in same position as Stage 2.</li></ul>" +
               "<p><strong>Stage 5:</strong></p>" +
               "<ul><li>Display 1: Press button with same label as Stage 1.</li><li>Display 2: Press button with same label as Stage 2.</li><li>Display 3: Press button with same label as Stage 4.</li><li>Display 4: Press button with same label as Stage 3.</li></ul>" +
               "</div>" +

               "<div class='section' id='morse'>" +
               "<h2>Section 7: Morse Code</h2>" +
               "<p>Interpret the flashing light using the Morse Code table to spell a word.</p>" +
               "<p>Tune the radio to the corresponding frequency for that word.</p>" +
               "<p><strong>Frequencies:</strong></p>" +
               "<ul>" +
               "<li>SHELL: 3.505 MHz</li><li>HALLS: 3.515 MHz</li><li>SLICK: 3.522 MHz</li><li>TRICK: 3.532 MHz</li>" +
               "<li>BOXES: 3.535 MHz</li><li>LEAKS: 3.542 MHz</li><li>STROBE: 3.545 MHz</li><li>BISTRO: 3.552 MHz</li>" +
               "<li>FLICK: 3.555 MHz</li><li>BOMBS: 3.565 MHz</li><li>BREAK: 3.572 MHz</li><li>BRICK: 3.575 MHz</li>" +
               "<li>STEAK: 3.582 MHz</li><li>STING: 3.592 MHz</li><li>VECTOR: 3.595 MHz</li><li>BEATS: 3.600 MHz</li>" +
               "</ul>" +
               "</div>" +

               "<div class='section' id='password'>" +
               "<h2>Section 8: Password</h2>" +
               "<p><strong>Possible Passwords:</strong></p>" +
               "<p>ABOUT, AFTER, AGAIN, BELOW, COULD, EVERY, FIRST, FOUND, GREAT, HOUSE, LARGE, LEARN, NEVER, OTHER, PLACE, PLANT, POINT, RIGHT, SMALL, SOUND, SPELL, STILL, STUDY, THEIR, THERE, THESE, THING, THINK, THREE, WATER, WHERE, WHICH, WORLD, WOULD, WRITE</p>" +
               "</div>" +

               "<div class='section' id='maze'>" +
               "<h2>Section 9: Maze</h2>" +
               "<div class='warning'>" +
               "<p>Find the maze with matching circular markings.</p>" +
               "<p>The defuser must navigate the white light to the red triangle.</p>" +
               "<p><strong>Warning:</strong> Do not cross the walls shown in the reference maze.</p>" +
               "</div>" +
               "</div>" +
               
               "<div class='section' id='complicated'>" +
               "<h2>Section 10: Complicated Wires</h2>" +
               "<p>Look at each wire: it has a color, a star, and an LED.</p>" +
               "<p>Use the Venn Diagram rules to decide whether to cut the wire.</p>" +
               "<table>" +
               "<tr><th>Wire Properties</th><th>Instruction</th></tr>" +
               "<tr><td>Red, Blue, Star, LED</td><td>Don't Cut</td></tr>" +
               "<tr><td>Red, Blue, Star</td><td>Parallel Port</td></tr>" +
               "<tr><td>Red, Blue, LED</td><td>Serial Even</td></tr>" +
               "<tr><td>Red, Blue</td><td>Serial Even</td></tr>" +
               "<tr><td>Red, Star, LED</td><td>Batteries >= 2</td></tr>" +
               "<tr><td>Red, Star</td><td>Cut</td></tr>" +
               "<tr><td>Red, LED</td><td>Batteries >= 2</td></tr>" +
               "<tr><td>Red</td><td>Serial Even</td></tr>" +
               "<tr><td>Blue, Star, LED</td><td>Parallel Port</td></tr>" +
               "<tr><td>Blue, Star</td><td>Don't Cut</td></tr>" +
               "<tr><td>Blue, LED</td><td>Parallel Port</td></tr>" +
               "<tr><td>Blue</td><td>Serial Even</td></tr>" +
               "<tr><td>Star, LED</td><td>Batteries >= 2</td></tr>" +
               "<tr><td>Star</td><td>Cut</td></tr>" +
               "<tr><td>LED</td><td>Don't Cut</td></tr>" +
               "<tr><td>Nothing</td><td>Cut</td></tr>" +
               "</table>" +
               "</div>" +

               "</body></html>";
    }
}