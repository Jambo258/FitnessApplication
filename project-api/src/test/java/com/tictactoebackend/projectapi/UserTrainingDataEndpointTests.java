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
    @Order(2)
    public void apiGetUserAllTrainingsGET() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/training/"+ createdUserId + "/trainingdays")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"));
    }

    @Test
    @Order(3)
    public void apiGetAllTrainingsGET() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/training/getAllTrainingDays")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"));
    }

    @Test
    @Order(4)
    public void apiDeleteUserSingularTrainingDELETE() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/training/"+ trainingId + "/delete")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isOk());

    }

    @Test
    @Order(5)
    public void apiDeleteUserAllTrainingsDELETE() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/training/"+ createdUserId + "/deleteall")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isNotFound());


    }

    @Test
    @Order(6)
    public void apiUsersDeleteByIdDELETE() throws Exception {
         mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/id/"+ createdUserId + "/delete")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isOk());
               
    }

}
