package com.wordbank.Logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.yaml.snakeyaml.Yaml;

public class YamlReader {
    private final String wordsFilepath;
    private final ConfigurationYaml config;

    public YamlReader(String configFilepath, String _wordsFilepath) {
        config = new ConfigurationYaml(configFilepath);
        wordsFilepath = _wordsFilepath;
    }

    public void saveYaml() {
        config.createSaveFile();
    }

    public String[] getWords() {
        if (config.turn == 0) {
            String[] words = {
                "JENNIFER",
                "KRISTINA",
                "LICHA",
                "MANGO",
                "DINOSAUR",
                "LOBSTER",
                "PINGUIN",
                "QUACK",
                "OINK",
                "MOO"
            };
            return words;
        }

        String[] words = new String[10];

        try (BufferedReader reader = new BufferedReader(new FileReader(wordsFilepath))) {
            List<String> wordList = reader.lines().toList();            
            Random rng = new Random();
            for (int wordIndex, i = 0; i < 10; i++) {
                wordIndex = (i * 100) + rng.nextInt(100);
                words[i] = wordList.get(wordIndex).toUpperCase();
                rng.setSeed(rng.nextLong());
            }
            
        } catch (Exception e) {
            return null;
        }

        return words;
    }

    public void updateTurn() {
        config.turn++;
    }

    public void updateWords(String[] words) {
        config.words.clear();
        for (final String word : words) {
            config.words.add(word);
        }
    }

    public class ConfigurationYaml {
        private final String filepath;
        public ArrayList<String> words;
        public int turn;

        public ConfigurationYaml(String _filepath) {
            filepath = _filepath;
            words = new ArrayList<String>();
            turn = 0;

            readSaveFile();
        }

        Map<String, Object> createBasicMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("Words", words);
            map.put("Turn", turn);

            return map;
        }

        @SuppressWarnings("unchecked")
        private void readSaveFile() {
            if (!new File(filepath).exists()) {
                return ;
            }

            Yaml yaml = new Yaml();
            Map<String, Object> map;

            try {
                map = yaml.load(new FileInputStream(filepath));
                words = (ArrayList<String>) map.get("Words");
                turn = (int) map.get("Turn");
            } catch (Exception e) {
                System.out.println("Error reading the yaml");
                e.printStackTrace();
            }
        }

        private void createSaveFile() {
            Yaml yaml = new Yaml();
            try (FileWriter fileWriter = new FileWriter(filepath)) {
                fileWriter.write(yaml.dump(createBasicMap()));
            } catch (IOException e) {}
        }

    }

}
