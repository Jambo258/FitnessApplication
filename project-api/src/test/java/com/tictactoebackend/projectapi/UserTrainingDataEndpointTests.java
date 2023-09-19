package com.tictactoebackend.projectapi;


import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.json.JSONObject;



@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class UserTrainingDataEndpointTests {

    private static String authToken;
    private static String createdUserId;
    private static String trainingId;

    @Autowired
    private MockMvc mockMvc;


    @BeforeAll
    public void apiUsersRegisterAndLoginPOST() throws Exception {

        // Perform user registration
        String registrationData = "{"
            + "\"username\": \"testguy\","
            + "\"password\": \"testguy\","
            + "\"role\": \"admin\","
            + "\"email\": \"testguy20@example.com\""
            + "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registrationData))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));

        // Perform user login and store the authToken
        String loginData = "{"
            + "\"password\": \"testguy\","
            + "\"email\": \"testguy20@example.com\""
            + "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginData))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andDo(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    JSONObject jsonResponse = new JSONObject(responseContent);
                    System.out.println(jsonResponse);
                    authToken = jsonResponse.getString("token");

                    Claims claims = Jwts.parser()
                        .setSigningKey(Constants.MY_SECRET_KEY)
                        .parseClaimsJws(authToken)
                        .getBody();

                    System.out.println("Token Payload: " + claims);

                    createdUserId = (String) claims.get("id");

                    System.out.println(createdUserId + "user id");

                });
    }

    @Test
    @Order(1)
    public void apiAddTrainingToUserWithoutValuesPOST() throws Exception {

        String trainingData = "{"
            + "\"currentWeight\": \"\","
            + "\"dailySteps\": \"\","
            + "\"dailyCalories\": \"\""
            + "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/training/"+ createdUserId + "/addtraining")
               .header("Authorization", "Bearer " + authToken)
               .contentType(MediaType.APPLICATION_JSON)
               .content(trainingData))
               .andExpect(status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$.error", is("Invalid request body fields")))
               .andExpect(MockMvcResultMatchers.jsonPath("$.missingFields", containsInAnyOrder(
                        "currentWeight value is null"
                )))
                .andExpect(MockMvcResultMatchers.jsonPath("$.unexpectedFields", containsInAnyOrder(
                        "dailyCalories must be an integer",
                        "dailySteps must be an integer"
                )));

    }

    @Test
    @Order(2)
    public void apiAddTrainingToUserWithWrongIdPOST() throws Exception {
        String trainingData = "{"
            + "\"currentWeight\": \"85.5\","
            + "\"dailySteps\": 10000,"
            + "\"dailyCalories\": 2500"
            + "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/training/123456789/addtraining")
               .header("Authorization", "Bearer " + authToken)
               .contentType(MediaType.APPLICATION_JSON)
               .content(trainingData))
               .andExpect(status().isNotFound())
               .andExpect(content().json("{\"error\":\"Invalid userId\"}"));
    }

    @Test
    @Order(3)
    public void apiAddTrainingToUserPOST() throws Exception {
        String trainingData = "{"
            + "\"currentWeight\": \"85.5\","
            + "\"dailySteps\": 10000,"
            + "\"dailyCalories\": 2500"
            + "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/training/"+ createdUserId + "/addtraining")
               .header("Authorization", "Bearer " + authToken)
               .contentType(MediaType.APPLICATION_JSON)
               .content(trainingData))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andDo(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    JSONObject jsonResponse = new JSONObject(responseContent);
                    System.out.println(jsonResponse);
                    trainingId = jsonResponse.getString("id");

    });
    }

    @Test
    @Order(4)
    public void apiAddTrainingToUserOnceEveryDayOnlyPOST() throws Exception {
        String trainingData = "{"
            + "\"currentWeight\": \"85.5\","
            + "\"dailySteps\": 10000,"
            + "\"dailyCalories\": 2500"
            + "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/training/"+ createdUserId + "/addtraining")
               .header("Authorization", "Bearer " + authToken)
               .contentType(MediaType.APPLICATION_JSON)
               .content(trainingData))
               .andExpect(status().isTooManyRequests())
               .andExpect(content().json("{\"error\":\"You can make another request in 23 hours.\"}"));

    }

    @Test
    @Order(5)
    public void apiGetUserAllTrainingsWithWrongIdGET() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/training/123456789/trainingdays")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isNotFound())
               .andExpect(content().json("{\"error\":\"Invalid userId\"}"));
    }

    @Test
    @Order(6)
    public void apiGetUserAllTrainingsGET() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/training/"+ createdUserId + "/trainingdays")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"));
    }

    @Test
    @Order(7)
    public void apiGetAllTrainingsGET() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/training/getAllTrainingDays")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"));
    }

    @Test
    @Order(8)
    public void apiDeleteUserSingularTrainingWithFalseIdDELETE() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/training/123/delete")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isNotFound())
               .andExpect(content().json("{\"error\":\"Invalid trainingId\"}"));

    }

    @Test
    @Order(9)
    public void apiDeleteUserSingularTrainingDELETE() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/training/"+ trainingId + "/delete")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isOk());

    }

    @Test
    @Order(10)
    public void apiDeleteUserAllTrainingsWrongIdDELETE() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/training/123456789/deleteall")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isNotFound())
               .andExpect(content().json("{\"error\":\"nothing to delete\"}"));


    }

    @Test
    @Order(11)
    public void apiDeleteUserAllTrainingsDELETE() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/training/"+ createdUserId + "/deleteall")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isNotFound());


    }

    @Test
    @Order(12)
    public void apiUsersDeleteByIdDELETE() throws Exception {
         mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/id/"+ createdUserId + "/delete")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isOk());

    }

}
