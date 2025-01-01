package com.wordbank.Logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

public class LetterMatrix {
    //LETTERS MATRIX VARIABLES
    private final String matrixFilePath;
    private Letter[][] letterMatrix;
    private final YamlReader yaml;
    //LETTERS MATRIX VARIABLES

    public LetterMatrix() {
        yaml = new YamlReader("./src/main/resources/SaveData.yaml", "./src/main/resources/WordList.txt");
        matrixFilePath = "./src/main/resources/letterMatrix.bin";
        Object matrix = readMatrixFile();
        if (matrix != null)  {
            letterMatrix = (Letter[][]) matrix;
        } else {
            System.out.println("it was null");
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
            words = yaml.getWords();
        } while (words == null);

        letterMatrix = new Letter[dimension][dimension];
        for (int i = 0; i < dimension; i++) {

            for (int j = 0; j < dimension; j++) {
                letterMatrix[i][j] = new Letter(generateRandomCharacter());
            }

        }

        yaml.updateTurn();
        yaml.updateWords(words);
    }

    public Letter getLetterAt(int x, int y) {
        try {
            return letterMatrix[x][y];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
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
