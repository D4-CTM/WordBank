package com.wordbank.Logic;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import com.wordbank.GUI.Texts;

public class GameGraphics {
    private final LetterMatrixManager letterMatrix;   
    private final int matrixDimension = 12;
    //Mouse position variables
    private boolean initialPress;
    private Point initialPos;
    private Point finalPos;
    private Color fillColor;

    public GameGraphics() {
        letterMatrix = new LetterMatrixManager();
        fillColor = Texts.RANDOM_COLOR();
        initialPress = true;
        initialPos = null;
        finalPos = null;
    }

    public void saveData() {
        letterMatrix.saveData();
    }
    
    public void renderBackgroundImage(Graphics2D graphic, String filepath) {
        try {
            BufferedImage image = ImageIO.read(new java.io.File(filepath));
            graphic.drawImage(image, 0, 0, null);
        } catch (IOException e) {}
    }

    private int calculateOffset() {
        return 100 + (((12 - matrixDimension)/2) * 50);
    }

    public void renderWordBox(Graphics2D graphic) {
        graphic.setColor(new Color(0,0,0,200));
        graphic.fillRect(50, 725, 700, 150);
        renderWordsOnBox(graphic);
    }

    private void renderWordsOnBox(Graphics2D graphic) {
        ArrayList<String> words = letterMatrix.getWords();
        ArrayList<String> wordsFound = letterMatrix.getWordsFound();
        
        graphic.setFont(Texts.LITTLE_LIGHT_FONT);
        final float x = 100;
        final float y = 725;
        float xOffset, yOffset;
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);

            if (i < 4) {
                yOffset = y + (float) 39.5; 
                xOffset = x + ((float) (i * 175)) - (word.length()/2);
            } else if (i < 8) {
                yOffset = y + (float) 89.5;
                xOffset = x + ((float) ((i - 4) * 175)) - (word.length()/2);
            } else {
                yOffset = y + (float) 139.5;
                xOffset = x + ((float) ((i - 8) * 350))  - (word.length()/2);
            }

            graphic.setColor(!wordsFound.contains(word) ? Texts.BORDER_COLOR : Texts.TEXT_COLOR);
            graphic.drawString(word, xOffset, yOffset);
        }
    }

    public void renderMatrixBackground(Graphics2D graphic) {
        final int offset = calculateOffset();
        graphic.setColor(new Color(0,0,0, 200));
        graphic.fillRect(offset, offset, (matrixDimension * 50), (matrixDimension * 50));
    }

    public void renderLetterMatrix(Graphics2D graphic) {
        final int offset = calculateOffset();
        graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        graphic.setColor(Texts.TEXT_COLOR);
        graphic.setFont(Texts.LIGHT_FONT);

        for (int i = 0; i < matrixDimension; i++) {

            for (int j = 0; j < matrixDimension; j++) {
                graphic.drawString(String.valueOf(letterMatrix.getLetterAt(i, j).word), offset + 15 +  (i * 50), offset + 40 + (j * 50));
            }

        }
    }

    private Point getMouseCoords(Point mousePointer) {
        Point coords = new Point();

        coords.x = ((mousePointer.x - calculateOffset())/50);           
        coords.y = ((mousePointer.y - calculateOffset())/50);           

        return coords;
    }

    private void correctCoords(Point initialPointer, Point finalPointer) {
        int deltaX = finalPointer.x - initialPointer.x;
        int deltaY = finalPointer.y - initialPointer.y;
        int radius;
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            radius = 1 + Math.abs(deltaX)/2;
        } else if (Math.abs(deltaX) < Math.abs(deltaY)) {
            radius = 1 + Math.abs(deltaY)/2;
        } else return ;

        if ((initialPointer.x + radius) > finalPointer.x && finalPointer.x > (initialPointer.x - radius)) {
            finalPointer.x = initialPointer.x;
            return ;
        } else if ((initialPointer.y + radius) > finalPointer.y && finalPointer.y > (initialPointer.y - radius)) {
            finalPointer.y = initialPointer.y;
            return ;
        }

        if (deltaX > 0 && deltaY < 0) {

            for (int i = 0; i < matrixDimension; i++) {

                if (initialPointer.x + i >= matrixDimension) {
                    break;
                }
                if (initialPointer.y - i < 0) {
                    break;
                }

                if (finalPointer.x == initialPointer.x + i) {
                    finalPointer.y = initialPointer.y - i;
                } 
                if (finalPointer.y == initialPointer.y - i) {
                    finalPointer.x = initialPointer.x + i;
                }

            }

        } else if (deltaY > 0 && deltaX < 0) {

            for (int i = 0; i < matrixDimension; i++) {

                if (initialPointer.x - i < 0) {
                    break;
                }
                if (initialPointer.y + i >= matrixDimension) {
                    break;
                }

                if (finalPointer.x == initialPointer.x - i) {
                    finalPointer.y = initialPointer.y + i;
                } 
                if (finalPointer.y == initialPointer.y + i) {
                    finalPointer.x = initialPointer.x - i;
                }

            }

        } else if (deltaX > 0 && deltaY > 0) {

            for (int i = 0; i < matrixDimension; i++) {

                if (initialPointer.x + i >= matrixDimension) {
                    break;
                }
                if (initialPointer.y + i >= matrixDimension) {
                    break;
                }

                if (finalPointer.x == initialPointer.x + i) {
                    finalPointer.y = initialPointer.y + i;
                }
                if (finalPointer.y == initialPointer.y + i) {
                    finalPointer.x = initialPointer.x + i;
                }

            }

        } else if (deltaX < 0 && deltaY < 0) {

            for (int i = 0; i < matrixDimension; i++) {

                if (initialPointer.x - i < 0) {
                    break;
                }
                if (initialPointer.y - i < 0) {
                    break;
                }

                if (finalPointer.x == initialPointer.x - i) {
                    finalPointer.y = initialPointer.y - i;
                } 
                if (finalPointer.y == initialPointer.y - i) {
                    finalPointer.x = initialPointer.x - i;
                }

            }

        }
    }

    private void showSelectedLetters(Graphics2D graphic, Point initialPointer, Point finalPointer, Color bg) {
        AffineTransform originalTranform = graphic.getTransform();
        graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        graphic.setColor(bg == null ? fillColor : bg);

        final int deltaX = finalPointer.x - initialPointer.x;
        final int deltaY = finalPointer.y - initialPointer.y;
        final int offset = calculateOffset();
        int x = offset + initialPointer.x * 50;
        int y = offset + initialPointer.y * 50;
        final double hypot = 50 + Math.hypot(deltaX * 50, (deltaY * 50));
        int angle = 0;

        if (deltaY == 0) {
            angle = deltaX > 0 ? 0 : 180;
        } else if (deltaX == 0) {
            angle = deltaY > 0 ? 90 : 270;
        } else if (deltaX > 0) {
            angle = deltaY > 0 ? 45 : 315;
        } else if (deltaX < 0) {
            angle = deltaY > 0 ? 135 : 225;
        }

        graphic.rotate(Math.toRadians(angle), x, y);
        switch (angle) {
            case 45 -> { 
                x += 10;
                y -= 25;
            }
            case 90 -> y -= 50;
            case 135 -> { 
                y -= 60;
                x -= 25;
            }
            case 180 -> {
                y -= 50;
                x -= 50;
            }
            case 225 -> {
                y -= 30;
                x -= 60;
            }
            case 270 -> x -= 50;
            case 315 -> {
                x -= 25;
                y += 10;
            }
        }

        graphic.fillOval(x, y, (int) hypot, 50);
        graphic.setTransform(originalTranform);
    }

    private WordCoord getWord() {
        WordCoord wc = new WordCoord();        
        if (initialPress) return wc;

        boolean loop = true;
        int Xpos = wc.initialCoords.x = initialPos.x;
        int Ypos = wc.initialCoords.y = initialPos.y;
        while (loop) {
            if (Xpos == finalPos.x && Ypos == finalPos.y) {
                loop = false;
            }
            
            Letter letter = letterMatrix.getLetterAt(Xpos, Ypos);
            if (letter != null) {
                wc.word += letter.word;
                wc.finalCoords.x = Xpos;
                wc.finalCoords.y = Ypos;
            }            

            if (Xpos > finalPos.x) {
                Xpos--;
            } else if (Xpos < finalPos.x) {
                Xpos++;
            }

            if (Ypos > finalPos.y) {
                Ypos--;
            } else if (Ypos < finalPos.y) {
                Ypos++;
            }

        }

        return wc;
    }

    private void showWord(Graphics2D graphic) {
        String word = getWord().word;
        if (word.isEmpty()) return ;
        int wordGraphicSize = (word.length() * 27)/2;
        int x = (calculateOffset() + (matrixDimension * 50)/2) - wordGraphicSize;
        graphic.setColor(Texts.BUTTON_COLOR);
        graphic.fillOval(x - 25, 25, 40 + wordGraphicSize * 2, 55);

        graphic.setColor(Texts.BORDER_COLOR);
        graphic.setFont(Texts.NORMAL_LIGHT_FONT);
        for (int i = 0; i < word.length(); i++) {
            graphic.drawString(String.valueOf(word.charAt(i)), x - 5 + (i * 27), 65);
        }
    }

    public void renderSelectedTiles(Graphics2D graphic, Point mousePointer) {
        final int offset = calculateOffset();
        boolean positionValid = true;
        if (mousePointer.x + 50 > (850 - offset) || mousePointer.y + 50 > (850 - offset)) {
            if (initialPress) {
                return ;
            }
            positionValid = false;
        } else if (mousePointer.x < offset || mousePointer.y < offset) {
            if (initialPress) {
                return ;
            }
            positionValid = false;
        }

        if (positionValid) {
            if (initialPress) {
                initialPos = getMouseCoords(mousePointer);
                initialPress = false;
            }
            finalPos = getMouseCoords(mousePointer);
            correctCoords(initialPos, finalPos);
        }

        showWord(graphic);
        showSelectedLetters(graphic, initialPos, finalPos, null);
    }

    public void showFoundWords(Graphics2D graphic) {
        for (final WordCoord wordCoord : letterMatrix.getWordCoords()) {
            showSelectedLetters(graphic, wordCoord.initialCoords, wordCoord.finalCoords, wordCoord.background);
        }
    }

    public void checkWord() {
        ArrayList<String> words = letterMatrix.getWords();
        WordCoord wc = getWord();
        for (final String word : words) {
            if (!wc.compareWords(word)) continue;
            wc.word = word;
            wc.background = fillColor;
            letterMatrix.addWordCoord(wc);

            break;
        }
        fillColor = Texts.RANDOM_COLOR();
        initialPress = true;
    }

    public boolean isJumbleComplete() {
        return letterMatrix.isJumbleComplete();
    }

    public void generateNewMatrix() {
        letterMatrix.createNewMatrix();
    }

}
