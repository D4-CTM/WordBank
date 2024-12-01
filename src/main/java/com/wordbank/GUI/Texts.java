package com.wordbank.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class Texts {

    private static java.awt.Font readFont(String filepath, float size) {
        try {
            final File fontFile = new File(filepath);
            Font font = Font.createFont(java.awt.Font.TRUETYPE_FONT, fontFile);
            font = font.deriveFont(java.awt.Font.BOLD);
            font = font.deriveFont(size);
            return font;
        } catch (FontFormatException | IOException e) {
            System.out.println("ERROR loading font: " + filepath + '\n');
            System.out.println("please check for the existence of this file on the resources folder\nor correct a spelling mistake at: './src/Logic/Texts.java'\n");
            return null;
        }
    }

    /*
    *   If there was an error loading the font, try the following:
    *   - See if the filepath typed on readFont has any spelling or pathing mistake.
    *   - Check for the existence of the '.ttf' on the 'resources/font' folder or check if the folder itself exists.
    *   
    *   If you tried to use a custom font just remember:
    *       There has to be 2 types of fonts, one for the bolded text and one for the light text.
    */
    public static Font NORMAL_LIGHT_FONT = readFont("./src/main/resources/font/Light_Text.ttf", 25f);
    public static Font LIGHT_FONT = readFont("./src/main/resources/font/Light_Text.ttf", 50f);
    public static Font BOLD_FONT = readFont("./src/main/resources/font/Bold_Text.ttf", 50f);

    public static Color BACKGROUND_COLOR = Color.decode("#fdcae1");
    public static Color BORDER_COLOR = Color.decode("#faf5ef");
    public static Color BUTTON_COLOR = Color.decode("#eba8a0");
    public static Color TEXT_COLOR = Color.decode("#ff6961");


}