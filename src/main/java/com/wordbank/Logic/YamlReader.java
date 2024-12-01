package com.wordbank.Logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class YamlReader {
    private final String wordsFilepath;
    private final ConfigurationYaml config;

    public YamlReader(String configFilepath, String _wordsFilepath) {
        config = new ConfigurationYaml(configFilepath);
        wordsFilepath = _wordsFilepath;
    }

    public String[] getWords() {
        String[] words = new String[10];

        Yaml yaml = new Yaml();
        Map<String, ArrayList<Object>> map;

        try {
            map = yaml.load(new FileInputStream(wordsFilepath));

        } catch (FileNotFoundException e) {}

        return words;
    }

    private class ConfigurationYaml {
        private final String filepath;

        public List<String> words;
        public String theme;
        public int turn;

        public ConfigurationYaml(String _filepath) {
            filepath = _filepath;
            readSaveFile();
        }

        Map<String, Object> createBasicMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("Words", new String[10]);
            map.put("Theme", theme);
            map.put("Turn", turn);

            return map;
        }

        private void readSaveFile() {
            Yaml yaml = new Yaml();
            Map<String, Object> map;

            try {
                map = yaml.load(new FileInputStream(filepath));
                words = (List<String>) map.get("Words");
                theme = (String) map.get("Theme");
                turn = (int) map.get("Turn");
            } catch (Exception e) {
                words = new ArrayList<>();
                theme = "";
                turn = 0;

                createSaveFile();
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
