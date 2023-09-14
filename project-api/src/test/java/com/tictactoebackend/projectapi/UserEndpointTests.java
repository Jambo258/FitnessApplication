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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;




import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class UserEndpointTests {

    private static String authToken;
    private static String createdUserId;
    private static String dummyUserId;
    private static String dummyUserauthToken;

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
    public void registerDummyUser() throws Exception {
        String dummyUser = "{"
            + "\"username\": \"dummyguy\","
            + "\"password\": \"dummyguy\","
            + "\"role\": \"guest\","
            + "\"email\": \"dummyguy1@example.com\""
            + "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dummyUser))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andDo(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    JSONObject jsonResponse = new JSONObject(responseContent);
                    System.out.println(jsonResponse);
                    dummyUserauthToken = jsonResponse.getString("token");
                    Claims claims = Jwts.parser()
                        .setSigningKey(Constants.MY_SECRET_KEY)
                        .parseClaimsJws(dummyUserauthToken)
                        .getBody();

                    System.out.println("Token Payload: " + claims);

                    dummyUserId = (String) claims.get("id");

                });
    }


    @Test
    @Order(2)
    public void tryingToRegisterUserWithEmailThatExistsPOST() throws Exception {
        // Perform user registration
        String registrationData = "{"
            + "\"username\": \"dummyguy\","
            + "\"password\": \"dummyguy\","
            + "\"role\": \"guest\","
            + "\"email\": \"dummyguy1@example.com\""
            + "}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
               .contentType(MediaType.APPLICATION_JSON)
               .content(registrationData))
               .andExpect(status().isUnauthorized())
               .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("email exists"))
               .andReturn();


        String content = result.getResponse().getContentAsString();
        System.out.println("Response Content: " + content);


}

    @Test
    @Order(3)
    public void wrongEndpointGET() throws Exception {
        mockMvc.perform(get("/api/"))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Order(4)
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
    @Order(5)
    public void apiUsersGetUserByIdGET() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/id/"+ createdUserId)
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json"));
    }


    @Test
    @Order(6)
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
    @Order(7)
    public void tryToUpdateAnotherUserDetailsWithoutAdminRolePUT() throws Exception {

        String changeUserData = "{"
            + "\"username\": \"testfellow1\","
            + "\"password\": \"testfellow1\","
            + "\"role\": \"admin\","
            + "\"email\": \"testfellow1@example.com\""
            + "}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/users/id/" + createdUserId + "/update")
               .header("Authorization", "Bearer " + dummyUserauthToken)
               .contentType(MediaType.APPLICATION_JSON)
               .content(changeUserData))
               .andExpect(status().isForbidden())
               .andReturn();

        MockHttpServletResponse response = result.getResponse();
        String errorMessage = response.getErrorMessage();
        assertEquals("Insufficient permissions", errorMessage);

}

    @Test
    @Order(8)
    public void apiUsersDeleteByIdDELETE() throws Exception {
         mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/id/"+ createdUserId + "/delete")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    public void registerUserWithoutValuesInRequestBodyPOST() throws Exception {
        String registrationData = "{"
            + "\"username\": \"\","
            + "\"password\": \"\","
            + "\"role\": \"\","
            + "\"email\": \"\""
            + "}";

         mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registrationData))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", is("Invalid request body fields")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.missingFields", containsInAnyOrder(
        "username must have a minimum length of 5 characters",
                "role must be either 'admin' or 'guest'",
                "email must be in a valid email format",
                "password must have a minimum length of 5 characters"
                )));
    }

    @Test
    @Order(10)
    public void registerUserWithWrongTypeValuesInRequestBodyPOST() throws Exception {

        String registrationData = "{"
            + "\"username\": 12345,"
            + "\"password\": 12345,"
            + "\"role\": 1,"
            + "\"email\": 12345"
            + "}";
         mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registrationData))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", is("Invalid request body fields")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.unexpectedFields", containsInAnyOrder(
                        "username must be a string",
                        "role must be a string",
                        "password must be a string",
                        "email must be a string"
                )));
    }

    @Test
    @Order(11)
    public void loginUserWithoutValuesRequestBodyPOST() throws Exception {
        String loginData = "{"
            + "\"password\": \"\","
            + "\"email\": \"\""
            + "}";
         mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginData))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", is("Invalid request body fields")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.missingFields", containsInAnyOrder(
                        "email must be in a valid email format",
                                "password must have a minimum length of 5 characters"
                )));
    }

    @Test
    @Order(12)
    public void loginUserWithWrongTypeValuesRequestBodyPOST() throws Exception {
        String loginData = "{"
            + "\"password\": 12345,"
            + "\"email\": 12345"
            + "}";
         mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginData))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", is("Invalid request body fields")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.unexpectedFields", containsInAnyOrder(
                        "email must be a string",
                        "password must be a string"
                )));
    }

    @Test
    @Order(13)
    public void deleteUserWithWrongIdDELETE() throws Exception {
         mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/id/123456789/delete")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isNotFound())
               .andExpect(content().json("{\"error\":\"Invalid userId\"}"));
    }

    @Test
    @Order(14)
    public void getUserWithWrongIdGET() throws Exception {
         mockMvc.perform(MockMvcRequestBuilders.get("/api/users/id/123456789")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isNotFound())
               .andExpect(content().json("{\"error\":\"Invalid userId\"}"));
    }

    @Test
    @Order(15)
    public void updateUserWithoutTokenPUT() throws Exception {

        String changeUserData = "{"
            + "\"username\": \"dummyfellow\","
            + "\"password\": \"dummyfellow\","
            + "\"role\": \"guest\","
            + "\"email\": \"dummyfellow@example.com\""
            + "}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/users/id/" + createdUserId + "/update")
               .header("Authorization", "Bearer ")
               .contentType(MediaType.APPLICATION_JSON)
               .content(changeUserData))
               .andExpect(status().isForbidden())
               .andReturn();

        MockHttpServletResponse response = result.getResponse();
        String errorMessage = response.getErrorMessage();
        assertEquals("Authorization must be Bearer [token]", errorMessage);

}

@Test
    @Order(16)
    public void apiUsersDeleteDummyByIdDELETE() throws Exception {
         mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/id/"+ dummyUserId + "/delete")
               .header("Authorization", "Bearer " + authToken))
               .andExpect(status().isOk());

    }
}
