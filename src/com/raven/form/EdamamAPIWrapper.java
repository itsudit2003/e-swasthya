package com.raven.form;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raven.model.ModelMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class EdamamAPIWrapper {

    ModelMessage ms = new ModelMessage(false, "");
    private final String appId;
    private final String appKey;

    public EdamamAPIWrapper(String appId, String appKey) {
        this.appId = appId;
        this.appKey = appKey;
    }

    public List<String> getNutritionalInfo(String foodName) throws UnsupportedEncodingException {
        List<String> nutritionalInfo = new ArrayList<>();
        String encodedFoodName = URLEncoder.encode(foodName, "UTF-8").replaceAll("\\+", "%20");
        String apiUrl = "https://api.edamam.com/api/nutrition-data?app_id=" + appId + "&app_key=" + appKey + "&ingr="
                + encodedFoodName;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(response.toString());
                    JsonNode nutrientsNode = rootNode.path("totalNutrients");

                    String calories = extractValueFromJsonNode(nutrientsNode, "ENERC_KCAL", "quantity");
                    String protein = extractValueFromJsonNode(nutrientsNode, "PROCNT", "quantity");
                    String carbs = extractValueFromJsonNode(nutrientsNode, "CHOCDF", "quantity");
                    String fat = extractValueFromJsonNode(nutrientsNode, "FAT", "quantity");

                    DecimalFormat decimalFormat = new DecimalFormat("#.###");
                    calories = formatValue(calories, decimalFormat);
                    protein = formatValue(protein, decimalFormat);
                    carbs = formatValue(carbs, decimalFormat);
                    fat = formatValue(fat, decimalFormat);

                    nutritionalInfo.add("Calories: " + (calories.equals("N/A") ? calories : calories + " kcal"));
                    nutritionalInfo.add("Protein: " + (protein.equals("N/A") ? protein : protein + " g"));
                    nutritionalInfo.add("Carbohydrates: " + (carbs.equals("N/A") ? carbs : carbs + " g"));
                    nutritionalInfo.add("Fat: " + (fat.equals("N/A") ? fat : fat + " g"));
                }
            } else {
                nutritionalInfo.add("Error: " + responseCode);
            }

        } catch (IOException e) {
            System.out.println("An error occurred while fetching nutritional information: " + e.getMessage());
            nutritionalInfo.add("An error occurred while fetching nutritional information.");
        }

        return nutritionalInfo;
    }

    private static String extractValueFromJsonNode(JsonNode parentNode, String nutrientName, String valueFieldName) {
        JsonNode nutrientNode = parentNode.path(nutrientName);
        if (nutrientNode.isMissingNode()) {
            return "N/A";
        }

        JsonNode valueNode = nutrientNode.path(valueFieldName);
        if (valueNode.isMissingNode()) {
            return "N/A";
        }

        return valueNode.asText();
    }

    private static String formatValue(String value, DecimalFormat decimalFormat) {
        if (!value.equals("N/A")) {
            return decimalFormat.format(Double.parseDouble(value));
        }
        return value;
    }
}
