package com.wordbank.Logic;

import java.io.FileOutputStream;
import java.io.IOException;
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
        yaml = new YamlReader("./src/main/resources/SaveData.yaml", "./src/main/resources/words.yaml");
        matrixFilePath = "./src/main/resources/letterMatrix.bin";
        letterMatrix = null;
    }

    public void generateMatrix(int dimension) {
        letterMatrix = new Letter[dimension][dimension];
        for (int i = 0; i < dimension; i++) {

            for (int j = 0; j < dimension; j++) {
                letterMatrix[i][j] = new Letter(generateRandomCharacter());
            }

        }
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

    public void saveLetterMatrix() {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(matrixFilePath))){
            output.writeObject(letterMatrix);
            output.close();
        } catch (IOException e) {}
    }

    public class Letter implements Serializable{
        public boolean found;
        public char word;

        Letter(final char _word) {
            found = false;
            word = _word;
        }
    }
}
