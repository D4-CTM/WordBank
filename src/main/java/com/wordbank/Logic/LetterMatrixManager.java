package com.wordbank.Logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.awt.Point;

public class LetterMatrixManager {
    //LETTERS MATRIX VARIABLES
    private final String matrixFilePath;
    private LetterMatrix letterMatrix;
    private final YamlReader yaml;
    //LETTERS MATRIX VARIABLES

    public LetterMatrixManager() {
        yaml = new YamlReader("./SaveData/UserData.yaml", "./src/main/resources/WordList.txt");
        matrixFilePath = "./SaveData/letterMatrix.bin";
        Object matrix = readMatrixFile();
        if (matrix != null)  {
            letterMatrix = (LetterMatrix) matrix;
        } else {
            createNewMatrix();
        }
    }

    public Object readMatrixFile() {
        if (!new File(matrixFilePath).exists()) {
            return null;
        }

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(matrixFilePath))) {
            return input.readObject();
        } catch (Exception e) {
            e.printStackTrace();            
        }

        return null;
    } 

    public void saveData() {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(matrixFilePath))) {
            output.writeObject(letterMatrix);
            yaml.saveYaml();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void createNewMatrix() {
        final int dimension = 12;
        String[] words = null;
        do {
            words = yaml.generateWords();
        } while (words == null);

        letterMatrix = new LetterMatrix();

        for (final String word : words) {
            letterMatrix.insertWord(word, dimension);
            System.gc();
        }

        yaml.updateTurn();
        yaml.updateWords(words);
    }

    public void addWordCoord(WordCoord wordCoord) {
        letterMatrix.addWordCoord(wordCoord);
    }

    public boolean isWordFound(String word) {
        return letterMatrix.isWordFound(word);
    }

    public ArrayList<String> getWords() {
        return yaml.getWords();
    }

    public Letter getLetterAt(int x, int y) {
        return letterMatrix.getLetterAt(x, y);
    }

}
class LetterMatrix implements Serializable {
    public static final short MATRIX_DIMENSION = 12;
    private final Letter[][] letterMatrix;
    private final HashMap<String, WordCoord> wordCoords;

    public LetterMatrix() {
        letterMatrix = new Letter[LetterMatrix.MATRIX_DIMENSION][LetterMatrix.MATRIX_DIMENSION];
        wordCoords = new HashMap<>();
        for (int i = 0; i < LetterMatrix.MATRIX_DIMENSION; i++) {

            for (int j = 0; j < LetterMatrix.MATRIX_DIMENSION; j++) {
                letterMatrix[i][j] = new Letter(generateRandomCharacter());
            }
        }
    }

    public void insertWord(final String word, final int dimension) {
        final int x = getRandomNumber(dimension);
        final int y = getRandomNumber(dimension);
        final int length = word.length() - 1;

        final int direction = getRandomNumber(8);
        Queue<Point> points = new LinkedList<Point>();
        switch (direction) {
            //left ->
            case 0 -> {
                if (x + length >= dimension) {
                    insertWord(word, dimension);
                    return ;
                } 

                for (int i = 0; i <= length; i++) {
                    if (!getLetterAt(x + i, y).spaceTaken || getLetterAt(x + i, y).word == word.charAt(i)) {
                        points.add(new Point(x + i, y));
                    } else {
                        insertWord(word, dimension);
                        return;
                    }
                }
            }
            //right <-
            case 1 -> {
                if (x - length < 0) {
                    insertWord(word, dimension);
                    return ;
                }

                for (int i = 0; i <= length; i++) {
                    if (!getLetterAt(x - i, y).spaceTaken || getLetterAt(x - i, y).word == word.charAt(i)) {
                        points.add(new Point(x - i, y));
                    } else {
                        insertWord(word, dimension);
                        return;
                    }
                }

            }
            //up /\
            case 2 -> {
                if (y + length >= dimension) {
                    insertWord(word, dimension);
                    return ;
                } 

                for (int i = 0; i <= length; i++) {
                    if (!getLetterAt(x, y + i).spaceTaken || getLetterAt(x, y + i).word == word.charAt(i)) {
                        points.add(new Point(x, y + i));
                    } else {
                        insertWord(word, dimension);
                        return;
                    }
                }
            }
            //down \/
            case 3 -> {
                if (y - length < 0) {
                    insertWord(word, dimension);
                    return ;
                } 

                for (int i = 0; i <= length; i++) {
                    if (!getLetterAt(x, y - i).spaceTaken || getLetterAt(x, y - i).word == word.charAt(i)) {
                        points.add(new Point(x, y - i));
                    } else {
                        insertWord(word, dimension);
                        return;
                    }
                }
            }
            //Diagonal left up ->/\
            case 4 -> {
                if (x + length >= dimension || y + length >= dimension) {
                    insertWord(word, dimension);
                    return ;
                } 

                for (int i = 0; i <= length; i++) {
                    if (!getLetterAt(x + i, y + i).spaceTaken || getLetterAt(x + i, y + i).word == word.charAt(i)) {
                        points.add(new Point(x + i, y + i));
                    } else {
                        insertWord(word, dimension);
                        return;
                    }
                }
            }
            //Diagonal left down ->/\
            case 5 -> {
                if (x + length >= dimension || y - length < 0) {
                    insertWord(word, dimension);
                    return ;
                } 

                for (int i = 0; i <= length; i++) {
                    if (!getLetterAt(x + i, y - i).spaceTaken || getLetterAt(x + i, y - i).word == word.charAt(i)) {
                        points.add(new Point(x + i, y - i));
                    } else {
                        insertWord(word, dimension);
                        return;
                    }
                }
            }

            //Diagonal right up ->/\
            case 6 -> {
                if (x - length < 0 || y + length >= dimension) {
                    insertWord(word, dimension);
                    return ;
                } 

                for (int i = 0; i <= length; i++) {
                    if (!getLetterAt(x - i, y + i).spaceTaken || getLetterAt(x - i, y + i).word == word.charAt(i)) {
                        points.add(new Point(x - i, y + i));
                    } else {
                        insertWord(word, dimension);
                        return;
                    }
                }
            }
            //Diagonal rigth down ->/\
            case 7 -> {
                if (x - length < 0 || y - length < 0) {
                    insertWord(word, dimension);
                    return ;
                } 

                for (int i = 0; i <= length; i++) {
                    if (!getLetterAt(x - i, y - i).spaceTaken || getLetterAt(x - i, y - i).word == word.charAt(i)) {
                        points.add(new Point(x - i, y - i));
                    } else {
                        insertWord(word, dimension);
                        return;
                    }
                }
            }
        }

        int charPos = 0;
        while (!points.isEmpty()) {
            Point point = points.poll();
            letterMatrix[point.x][point.y].word = word.charAt(charPos);
            letterMatrix[point.x][point.y].spaceTaken = true;
            charPos ++;
        }

        System.gc();
    }

    public Letter getLetterAt(int x, int y) {
        try {
            return letterMatrix[x][y];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void addWordCoord(WordCoord wordCoord) {
        if (wordCoords.containsKey(wordCoord.word)) {
            return ;
        }

        wordCoords.put(wordCoord.word, wordCoord);
    }

    public boolean isWordFound(String word) {
        return wordCoords.containsKey(word);
    }
    
    private int getRandomNumber(int limit) {
        return new Random().nextInt(Integer.MAX_VALUE) % limit;
    }

    private char generateRandomCharacter() {
        return (char) new Random().nextInt('A', 'Z');
    }

}
class Letter implements Serializable {
    private static final long serialVersionUID = 1L;
    //If this space is use for another word
    public boolean spaceTaken;
    //If this letter was found by the player
    public boolean found;
    //The letter itself
    public char word;

    Letter(final char _word) {
        spaceTaken = false;
        found = false;
        word = _word;
    }
}
class WordCoord {
    public Point initialCoords;
    public Point finalCoords;
    public String word;

    public WordCoord() {
        initialCoords = new Point();
        finalCoords = new Point();
        word = "";
    }

    private boolean reverseCompare(final String word1, final String word2) {
        if (word1.length() != word2.length()) return false;
        for (int i1 = 0, i2 = word2.length() - 1; i1 < word1.length(); i1++, i2--) {
            if (word1.charAt(i1) != word2.charAt(i2)) return false;
        }

        return true;
    }

    protected boolean compareWords(String _word) {
        return word.equals(_word) || reverseCompare(word, _word);
    } 
}
