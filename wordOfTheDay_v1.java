import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.regex.Pattern;

class wordOfTheDay_v1 {

    static String getWordOfTheDay() {
        try {
            Document wordOfTheDayDocument = Jsoup.connect("https://www.merriam-webster.com/word-of-the-day").get();
            return summarizeWordOfTheDay(wordOfTheDayDocument);
        } catch (IOException error) {
            error.printStackTrace();
        }
        return "Unable to fetch today's word of the day";
    }

    private static String summarizeWordOfTheDay(Document wordOfTheDayDocument) {
        String wordOfTheDayDate = getWordDate(wordOfTheDayDocument);
        String wordOfTheDay = getTodaysWord(wordOfTheDayDocument);
        String wordAttributes = getWordAttributes(wordOfTheDayDocument);
        String definition = getDefinition(wordOfTheDayDocument);
        return wordOfTheDayDate + wordOfTheDay + wordAttributes + definition;
    }

    private static String getWordDate(Document wordOfTheDayDocument) {
        Elements dateElement = wordOfTheDayDocument.select("span.w-a-title");
        String wordDate = dateElement.text();
        return wordDate.substring(18);
    }

    private static String getTodaysWord(Document wordOfTheDayDocument) {
        String wordTitle = wordOfTheDayDocument.title();
        wordTitle = wordTitle.substring(0, wordTitle.length() - 18); //Remove " | Merriam Webster"
        return "\nMerriam-Webster " + wordTitle;
    }

    private static String getWordAttributes(Document wordOfTheDayDocument) {
        Elements mainAttributeElement = wordOfTheDayDocument.select("span.getWordOfTheDay-attr");
        String mainAttribute = mainAttributeElement.text();
        Elements wordSyllablesElement = wordOfTheDayDocument.select("span.word-syllables");
        String wordSyllables = wordSyllablesElement.text();
        return "\n" + mainAttribute + " | " + wordSyllables;
    }

    private static String getDefinition(Document wordOfTheDayDocument) {
        StringBuilder definitionBody = new StringBuilder();
        definitionBody.append("\nDefinition:");
        String[] parsedDefinitions = parseDefinitions(wordOfTheDayDocument);
        int i = 1;
        for (String definition : parsedDefinitions) {
            String[] parsedSubDefinitions = parseSubDefinitions(definition);
            if (parsedSubDefinitions.length > 1) {
                for (String subDefinition : parsedSubDefinitions) {
                    subDefinition = formatSubDefinitions(subDefinition);
                    subDefinition = "\n" + i + "." + subDefinition;
                    definitionBody.append(subDefinition);
                    i++;
                }
                continue;
            }
            definition = "\n" + i + "." + definition;
            definitionBody.append(definition);
            i++;
        }
        return definitionBody.toString();
    }

    private static String[] parseDefinitions(Document wordOfTheDayDocument) {
        String definitionContainer = parseDefinitionContainer(wordOfTheDayDocument);
        String definitionBody = removeDidYouKnowFooter(definitionContainer);
        String definitionListRegex = "\\d\\s\\W";
        Pattern definitionListPattern = Pattern.compile(definitionListRegex);
        return definitionListPattern.split(definitionBody);
    }

    private static String parseDefinitionContainer(Document wordOfTheDayDocument) {
        Elements definitionContainerElement = wordOfTheDayDocument.select("div.wod-definition-container");
        String definitionContainer = definitionContainerElement.text();
        if (definitionContainer.substring(0, 12).equals("Definition :")) {
            return definitionContainer.substring(12);
        }
        return definitionContainer.substring(14);
    }

    private static String removeDidYouKnowFooter(String definitionContainer) {
        String didYouKnowRegex = "Did You Know";
        Pattern didYouKnowPattern = Pattern.compile(didYouKnowRegex);
        String[] parsedDefinitionContainer = didYouKnowPattern.split(definitionContainer);
        return parsedDefinitionContainer[0];
    }

    private static String[] parseSubDefinitions(String definition) {
        String subDefinitionRegex = "\\s\\d";
        Pattern subDefinitionPattern = Pattern.compile(subDefinitionRegex);
        return subDefinitionPattern.split(definition);
    }

    private static String formatSubDefinitions(String subDefinition) {
        subDefinition = subDefinition.replaceAll(" a :", " (A)");
        subDefinition = subDefinition.replaceAll(" b :", " (B)");
        subDefinition = subDefinition.replaceAll(" c :", " (C)");
        subDefinition = subDefinition.replaceAll(" d :", " (D)");
        subDefinition = subDefinition.replaceAll(" e :", " (E)");
        return subDefinition;
    }
}
