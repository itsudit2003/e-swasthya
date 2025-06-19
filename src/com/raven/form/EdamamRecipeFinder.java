package com.raven.form;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raven.connection.DatabaseConnection;
import com.raven.model.ModelUser;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EdamamRecipeFinder {

    private static final String API_ID = "2a5194f9";
    private static final String API_KEY = "88f00ab62eaee10bad482d4f7ad272ac";

    public List<String> getRecipes() {
        List<String> recipes = new ArrayList<>();
        try {
            List<ModelUser> users = retrieveUsersFromDatabase(); // Retrieve ModelUser objects

            // Check if any user has null age or weight values
            for (ModelUser user : users) {
                if (user.getAge() == null || user.getWeight() == null) {
                    // Handle the situation where age or weight is null (e.g., log an error, skip
                    // this user, etc.)
                    System.err.println("Error: Null age or weight for user " + user.getUserName());
                } else {
                    // Construct the API request URL
                    String apiUrl = "https://api.edamam.com/search?q=recipe";
                    apiUrl += "&app_id=" + API_ID;
                    apiUrl += "&app_key=" + API_KEY;
                    apiUrl += "&from=5&to=30"; // Example: Fetch up to 10-30 recipes

                    // Add user-specific parameters
                    apiUrl += "&calories=" + calculateCalories(user); // Calculate recommended calories based on user's
                                                                      // age and weight
                    apiUrl += "&health=vegan"; // Example health parameter

                    // Make HTTP request to Edamam API
                    URL url = new URL(apiUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    // Check response status
                    if (conn.getResponseCode() != 200) {
                        throw new RuntimeException(
                                "Failed to fetch recipes: HTTP error code " + conn.getResponseCode());
                    }

                    // Parse JSON response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    reader.close();

                    // Convert JSON response to JsonNode
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode responseJson = mapper.readTree(responseBuilder.toString());

                    // Process the JSON response
                    for (JsonNode hit : responseJson.get("hits")) {
                        JsonNode recipe = hit.get("recipe");
                        String recipeName = recipe.get("label").asText();
                        String recipeUrl = recipe.get("url").asText(); // Recipe URL
                        JsonNode ingredientLines = recipe.get("ingredientLines"); // Ingredients
                        int calories = recipe.get("calories").asInt(); // Calories
                        int totalTime = recipe.get("totalTime").asInt(); // Total time

                        // Add recipe data to the list
                        String recipeData = "Recipe Name: " + recipeName + "\n"
                                + "Recipe Url: " + recipeUrl + "\n"
                                + "Calories: " + calories + "\n"
                                + "Time: " + totalTime + " minutes\n"
                                + "Ingredients:\n";

                        for (JsonNode ingredient : ingredientLines) {
                            recipeData += "- " + ingredient.asText() + "\n";
                        }
                        recipes.add(recipeData);
                    }
                }       Collections.shuffle(recipes);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions
        }
        return recipes;
    }

    private List<ModelUser> retrieveUsersFromDatabase() {
        List<ModelUser> users = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT * FROM user"; // Query to retrieve users from the 'user' table

            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Retrieve user data from the result set
                int userID = resultSet.getInt("UserID");
                String userName = resultSet.getString("UserName");
                String email = resultSet.getString("Email");
                String password = resultSet.getString("Password");
                String verifyCode = resultSet.getString("VerifyCode");
                String status = resultSet.getString("Status");
                Integer age = resultSet.getInt("Age");
                String gender = resultSet.getString("Gender");
                Integer weight = resultSet.getInt("Weight");

                // Create ModelUser object with retrieved data
                ModelUser user = new ModelUser(userID, userName, email, password, verifyCode, status, age, gender,
                        weight);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        } finally {
            // Close resources
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

        return users;
    }

    private String calculateCalories(ModelUser user) {
        // Calculate BMR based on age, weight, and gender
        double bmr;
        if (user.getGender().equalsIgnoreCase("male")) {
            bmr = 88.362 + (13.397 * user.getWeight()) + (4.799 * user.getAge()) - (5.677 * user.getAge());
        } else {
            bmr = 447.593 + (9.247 * user.getWeight()) + (3.098 * user.getAge()) - (4.330 * user.getAge());
        }

        // Adjust BMR based on activity level (assuming moderate activity level)
        double activityFactor = 1.55; // Moderate activity level
        int recommendedCalories = (int) (bmr * activityFactor);

        return recommendedCalories + "-";
    }

}
