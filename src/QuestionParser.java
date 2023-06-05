import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuestionParser {
    public static void main(String[] args) {
        String inputFile = "otazkyRAW.txt"; // Update with your input file path
        String outputFile = "questions.json"; // Update with your output file path

        List<Question> questions = parseQuestions(inputFile);
        generateJsonFile(questions, outputFile);

        System.out.println("JSON file generated successfully.");
    }

    private static List<Question> parseQuestions(String inputFile) {
        List<Question> questions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            String currentQuestionText = null;
            List<Answer> currentQuestionAnswers = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if (line.matches("\\d+\\..*")) {
                    if (currentQuestionText != null) {
                        questions.add(new Question(currentQuestionText, currentQuestionAnswers));
                        currentQuestionAnswers = new ArrayList<>();
                    }
                    currentQuestionText = line.substring(line.indexOf(".") + 1).trim();
                } else if (line.startsWith("+")) {
                    String answerText = line.substring(1).trim();
                    currentQuestionAnswers.add(new Answer(answerText, true));
                } else {
                    String answerText = line.trim();
                    String[] pole = answerText.split("- ");
                    currentQuestionAnswers.add(new Answer(pole[1], false));
                }
            }

            // Add the last question
            if (currentQuestionText != null) {
                questions.add(new Question(currentQuestionText, currentQuestionAnswers));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return questions;
    }
    private static void generateJsonFile(List<Question> questions, String outputFile) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        QuestionSet questionSet = new QuestionSet(questions);
        String json = gson.toJson(questionSet);

        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
