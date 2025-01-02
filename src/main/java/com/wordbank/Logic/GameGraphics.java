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
    //Mouse position variables

    public GameGraphics() {
        letterMatrix = new LetterMatrixManager();
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
                graphic.drawString(String.valueOf(letterMatrix.getLetterAt(i, j).word), offset + 5 +  (i * 50), offset + 45 + (j * 50));

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

    private void showSelectedLetters(Graphics2D graphic, Point initialPointer, Point finalPointer) {
        AffineTransform originalTranform = graphic.getTransform();
        graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        graphic.setColor(Texts.BORDER_COLOR);

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
        int wordGraphicSize = (word.length() * 25)/2;
        int x = (calculateOffset() + (matrixDimension * 50)/2) - wordGraphicSize;
        graphic.setColor(Texts.BUTTON_COLOR);
        graphic.fillOval(x - 25, 5, 50 + wordGraphicSize * 2, 50);

        graphic.setColor(Texts.BORDER_COLOR);
        graphic.setFont(Texts.LIGHT_FONT);        
        graphic.drawString(word, x - 5, 50);
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
        showSelectedLetters(graphic, initialPos, finalPos);
    }

    public void checkWord() {
        ArrayList<String> words = letterMatrix.getWords();
        WordCoord wc = getWord();
        for (final String word : words) {
            if (!wc.compareWords(word)) continue;
            letterMatrix.addWordCoord(wc);

            break;
        }
        initialPress = true;
    }

}
