package com.tictactoebackend.projectapi;

import org.junit.jupiter.api.Assertions;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.hamcrest.Matchers.*;
import org.json.JSONObject;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class UserEndpointTests {

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
    public void wrongEndpointGET() throws Exception {
        mockMvc.perform(get("/api/"))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Order(2)
    public void apiUsersGetAllUsersGET() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/getAll")
            .header("Authorization", "Bearer " + authToken))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
            .andExpect(jsonPath("$", anyOf(empty(), hasSize(greaterThan(0)))));
        /*
         mockMvc.perform(MockMvcRequestBuilders.get("/api/users/getAll")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
                */

                /*
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/getAll")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();


        responseContent = result.getResponse().getContentAsString();
        System.out.println(responseContent);


    ObjectMapper objectMapper = new ObjectMapper();


    List<JsonNode> jsonNodes = objectMapper.readValue(responseContent, new TypeReference<List<JsonNode>>() {});

    
    for (JsonNode jsonNode : jsonNodes) {
        String id = jsonNode.get("id").asText();
        System.out.println("Parsed id: " + id);
    }
    */
    }



    @Test
    @Order(3)
    public void apiUsersGetUserByIdGET() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/id/"+ createdUserId)
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"));
    }


    @Test
    @Order(4)
    public void apiUsersUpdateByIdPUT() throws Exception {

        String changeUserData = "{"
            + "\"username\": \"testfellow\","
            + "\"password\": \"testfellow\","
            + "\"role\": \"admin\","
            + "\"email\": \"testfellow@example.com\""
            + "}";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/id/" + createdUserId + "/update")
               .header("Authorization", "Bearer " + authToken)
               .contentType(MediaType.APPLICATION_JSON)
               .content(changeUserData))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andDo(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    JSONObject jsonResponse = new JSONObject(responseContent);
                    System.out.println(jsonResponse);
                    Assertions.assertEquals("testfellow@example.com", jsonResponse.getString("email"));
                    Assertions.assertEquals("testfellow", jsonResponse.getString("username"));
    });
}



    @Test
    @Order(5)
    public void apiUsersDeleteByIdDELETE() throws Exception {
         mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/id/"+ createdUserId + "/delete")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isOk());
               //.andExpect(content().contentType("application/json"));
    }


}
