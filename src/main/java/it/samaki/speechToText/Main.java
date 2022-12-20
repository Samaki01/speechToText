package it.samaki.speechToText;


import com.profesorfalken.jpowershell.PowerShell;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("src/main/resources/languageFiles/2748.dic");
        configuration.setLanguageModelPath("src/main/resources/languageFiles/2748.lm");

        try {
            LiveSpeechRecognizer liveSpeechRecognizer = new LiveSpeechRecognizer(configuration);
            liveSpeechRecognizer.startRecognition(true);

            SpeechResult speechResult;

            while ((speechResult = liveSpeechRecognizer.getResult()) != null) {
                String voiceCommand = speechResult.getHypothesis();
                System.out.println("Voice Command is: " + voiceCommand);

                switch (voiceCommand) {
                    case "ISAAC OPEN CHROME":
                        Runtime.getRuntime().exec("PowerShell.exe /c start chrome");
                    case "ISAAC CLOSE CHROME":
                        Runtime.getRuntime().exec("PowerShell.exe /c TASKKILL /IM chrome.exe");
                    case "ISAAC TURN ON LIGHT":
                        PowerShell.executeSingleCommand("echo '{\"id\":1,\"method\":\"setState\"," +
                                " \"params\":{\"state\":true}}' | ncat -u -w 1 192.168.1.6 38899");
                    case "ISAAC TURN OFF LIGHT":
                        PowerShell.executeSingleCommand("echo '{\"id\":1,\"method\":\"setState\"," +
                                " \"params\":{\"state\":false}}' | ncat -u -w 1 192.168.1.6 38899");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}