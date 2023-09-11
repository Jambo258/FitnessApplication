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
public class UserHealthDataEndpointTests {

    private static String authToken;
    private static String createdUserId;

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
    public void apiAddUserHealthDataByIdPOST() throws Exception {
        String healthData = "{"
            + "\"height\": \"1.8\","
            + "\"weight\": \"100.0\","
            + "\"targetWeight\": \"85.5\","
            + "\"targetSteps\": 10000,"
            + "\"targetCalories\": 2500"
            + "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/health/"+ createdUserId + "/addstatus")
               .header("Authorization", "Bearer " + authToken)
               .contentType(MediaType.APPLICATION_JSON)
               .content(healthData))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"));
    }

    @Test
    @Order(2)
    public void apiGetUserHealthDataByIdGET() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/health/"+ createdUserId + "/status")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"));
    }

    @Test
    @Order(3)
    public void apiGetAllUsersHealthDataGET() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/health/getAllStatuses")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"));
    }

    @Test
    @Order(4)
    public void apiUsersGetUserByIdGET() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/id/"+ createdUserId)
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"));
    }

    @Test
    @Order(5)
    public void apiDeleteUsersHealthDataDELETE() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/health/" + createdUserId + "/status/delete")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isOk());

    }


    @Test
    @Order(6)
    public void apiUsersDeleteByIdDELETE() throws Exception {
         mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/id/"+ createdUserId + "/delete")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isOk());
               
    }

}
