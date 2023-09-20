package com.tictactoebackend.projectapi.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tictactoebackend.projectapi.Constants;
import com.tictactoebackend.projectapi.domain.HealthStatus;
import com.tictactoebackend.projectapi.domain.Training;
import com.tictactoebackend.projectapi.domain.user;
import com.tictactoebackend.projectapi.exceptions.EtAuthException;
import com.tictactoebackend.projectapi.healthRepository.HealthRepository;
import com.tictactoebackend.projectapi.services.userService;
import com.tictactoebackend.projectapi.trainingRepository.TrainingRepository;
import com.tictactoebackend.projectapi.userRepository.userRepository;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

// @CrossOrigin
@RestController
@RequestMapping("/api/users")
public class userResource {

    @Autowired
    userService Userservice;

    @Autowired
    userRepository Userrepository;

    @Autowired
    TrainingRepository trainingRepository;

    @Autowired
    HealthRepository healthRepository;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, Object> userMap) {

        List<String> missingFields = new ArrayList<>();
        List<String> unexpectedFields = new ArrayList<>();

// Check and collect missing and unexpected fields
        for (String requiredField : Arrays.asList("email", "password")) {
            if (!userMap.containsKey(requiredField) || userMap.get(requiredField) == null) {
                missingFields.add(requiredField);
        }
    }

        for (String field : userMap.keySet()) {
            if (!Arrays.asList("email", "password").contains(field)) {
                unexpectedFields.add(field);
        }
    }

    Object emailValueObj = userMap.get("email");
        if (emailValueObj != null) {
            if (emailValueObj instanceof String) {
                String emailValue = (String) emailValueObj;
                if (emailValue != null && !isValidEmail(emailValue)) {
                    missingFields.add("email must be in a valid email format");
                }
    } else {
        unexpectedFields.add("email must be a string");
    }
}

    Object passwordValueObj = userMap.get("password");
        if (passwordValueObj != null) {
            if (passwordValueObj instanceof String) {
                String passwordValue = (String) passwordValueObj;
                if (passwordValue.length() < 5) {
                    missingFields.add("password must have a minimum length of 5 characters");
                }
    } else {
        unexpectedFields.add("password must be a string");
    }
}


        if (!missingFields.isEmpty() || !unexpectedFields.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid request body fields");
            if (!missingFields.isEmpty()) {
                errorResponse.put("missingFields", missingFields);
            }
            if (!unexpectedFields.isEmpty()) {
                errorResponse.put("unexpectedFields", unexpectedFields);
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

    }

        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");


        user User = Userservice.validateUser(email, password);

        return new ResponseEntity<>(generateJWTToken(User), HttpStatus.OK );

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> userMap){

        List<String> missingFields = new ArrayList<>();
        List<String> unexpectedFields = new ArrayList<>();

// Check and collect missing and unexpected fields
        for (String requiredField : Arrays.asList("username", "email", "password", "role")) {
            if (!userMap.containsKey(requiredField) || userMap.get(requiredField) == null) {
                missingFields.add(requiredField);
        }

    }

        for (String field : userMap.keySet()) {
            if (!Arrays.asList("username", "email", "password", "role").contains(field)) {
                unexpectedFields.add(field);
        }
    }

        Object usernameValueObj = userMap.get("username");
        if (usernameValueObj != null) {
            if (usernameValueObj instanceof String) {
                String usernameValue = (String) usernameValueObj;
                if (usernameValue != null && usernameValue.length() < 5) {
                    missingFields.add("username must have a minimum length of 5 characters");
                }
    } else {
        unexpectedFields.add("username must be a string");
    }
}

        Object roleValueObj = userMap.get("role");
        if (roleValueObj != null) {
            if (roleValueObj instanceof String) {
                String roleValue = (String) roleValueObj;
                if (roleValue != null && !roleValue.equals("admin") && !roleValue.equals("guest")) {
                    missingFields.add("role must be either 'admin' or 'guest'");
                }
    } else {
        unexpectedFields.add("role must be a string");
    }
}

        Object emailValueObj = userMap.get("email");
        if (emailValueObj != null) {
            if (emailValueObj instanceof String) {
                String emailValue = (String) emailValueObj;
                if (emailValue != null && !isValidEmail(emailValue)) {
                    missingFields.add("email must be in a valid email format");
                }
    } else {
        unexpectedFields.add("email must be a string");
    }
}

        Object passwordValueObj = userMap.get("password");
        if (passwordValueObj != null) {
            if (passwordValueObj instanceof String) {
                String passwordValue = (String) passwordValueObj;
                if (passwordValue.length() < 5) {
                    missingFields.add("password must have a minimum length of 5 characters");
                }
    } else {
        unexpectedFields.add("password must be a string");
    }
}

        if (!missingFields.isEmpty() || !unexpectedFields.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid request body fields");
            if (!missingFields.isEmpty()) {
                errorResponse.put("missingFields", missingFields);
            }
            if (!unexpectedFields.isEmpty()) {
                errorResponse.put("unexpectedFields", unexpectedFields);
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

    }
        String username = (String) userMap.get("username");
        String id = (String) userMap.get("id");
        System.out.println(id + "id");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        String role = (String) userMap.get("role");
        System.out.println(role + "role");

        user User = Userservice.registerUser(username,email,password,role);

        return new ResponseEntity<>(generateJWTToken(User), HttpStatus.OK);

    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<Map<String, Object>> getUserById(HttpServletRequest request, @PathVariable("userId") String userId) {
        String Id = (String) request.getAttribute("id");

        System.out.println("UserId" + Id);
        user user = Userrepository.findById(userId);

        if (user != null) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("id", user.getId());
            responseMap.put("username", user.getUsername());
            responseMap.put("email", user.getEmail());
            responseMap.put("role", user.getRole());
            responseMap.put("password", user.getPassword());

            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid userId");
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<user> userList = Userrepository.getAllUsers();

        List<Map<String, Object>> responseList = new ArrayList<>();
        for (user user : userList) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("email", user.getEmail());
            userMap.put("role", user.getRole());
            userMap.put("password",user.getPassword());

            responseList.add(userMap);
        }

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }


    @PutMapping("/id/{userId}/update")
    public ResponseEntity<Map<String, Object>> updateUserById(@PathVariable("userId") String userId,
                                                          @RequestBody Map<String, Object> userMap) {

        List<String> missingFields = new ArrayList<>();
        List<String> unexpectedFields = new ArrayList<>();

// Check and collect missing and unexpected fields
        for (String requiredField : Arrays.asList("username", "email", "password", "role")) {
            if (!userMap.containsKey(requiredField) || userMap.get(requiredField) == null) {
                missingFields.add(requiredField);
        }
    }

        for (String field : userMap.keySet()) {
            if (!Arrays.asList("username", "email", "password", "role").contains(field)) {
                unexpectedFields.add(field);
        }
    }

    Object usernameValueObj = userMap.get("username");
        if (usernameValueObj != null) {
            if (usernameValueObj instanceof String) {
                String usernameValue = (String) usernameValueObj;
                if (usernameValue != null && usernameValue.length() < 5) {
                    missingFields.add("username must have a minimum length of 5 characters");
                }
    } else {
        unexpectedFields.add("username must be a string");
    }
}

        Object roleValueObj = userMap.get("role");
        if (roleValueObj != null) {
            if (roleValueObj instanceof String) {
                String roleValue = (String) roleValueObj;
                if (roleValue != null && !roleValue.equals("admin") && !roleValue.equals("guest")) {
                    missingFields.add("role must be either 'admin' or 'guest'");
                }
    } else {
        unexpectedFields.add("role must be a string");
    }
}

        Object emailValueObj = userMap.get("email");
        if (emailValueObj != null) {
            if (emailValueObj instanceof String) {
                String emailValue = (String) emailValueObj;
                if (emailValue != null && !isValidEmail(emailValue)) {
                    missingFields.add("email must be in a valid email format");
                }
    } else {
        unexpectedFields.add("email must be a string");
    }
}

        Object passwordValueObj = userMap.get("password");
        if (passwordValueObj != null) {
            if (passwordValueObj instanceof String) {
                String passwordValue = (String) passwordValueObj;
                if (passwordValue.length() < 5) {
                    missingFields.add("password must have a minimum length of 5 characters");
                }
    } else {
        unexpectedFields.add("password must be a string");
    }
}

        if (!missingFields.isEmpty() || !unexpectedFields.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid request body fields");
            if (!missingFields.isEmpty()) {
                errorResponse.put("missingFields", missingFields);
            }
            if (!unexpectedFields.isEmpty()) {
                errorResponse.put("unexpectedFields", unexpectedFields);
            }
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

        String username = (String) userMap.get("username");

        System.out.println(userMap.toString() + "UserMap");
        String id = (String) userMap.get("id");
        System.out.println(userId + "UserId");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        String role = (String) userMap.get("role");
        System.out.println(role + "role");



    if (email != null) {
    email = email.toLowerCase();

    user UsersData = Userrepository.findById(userId);
    System.out.println(UsersData.getEmail()+ "email");

    if (!email.equalsIgnoreCase(UsersData.getEmail())) {
        Integer count = Userrepository.getCountByEmail(email);
        if (count > 0) {
            throw new EtAuthException("Email already exists");
        }
    }
}

        if (password != null) {
            user UsersData = Userrepository.findById(userId);
            System.out.println(UsersData.getPassword().toString() + "difference" + password);

                if (!password.equals(UsersData.getPassword().toString())) {
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
                    System.out.println("hashedpw" + hashedPassword);
                    password = hashedPassword;
                }
                else {
                    System.out.println("password" + password);
                }
            }


        Userrepository.updateUser(userId, username, email, password, role);
    try {
        user user = Userrepository.findById(userId);
        System.out.println(user + "details");

        if (user != null) {

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("id", user.getId());
            responseMap.put("username", user.getUsername());
            responseMap.put("email", user.getEmail());
            responseMap.put("role", user.getRole());
            responseMap.put("password", user.getPassword());
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } catch (EtAuthException e) {
        // Handle authentication exception
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

@DeleteMapping("/id/{userId}/delete")
public ResponseEntity<Map<String, Object>> deleteUserById(@PathVariable("userId") String userId) {
    try {
        user user = Userrepository.findById(userId);
        List<HealthStatus> healthStatus = healthRepository.findByCreator(userId);
        List<Training> trainingList = trainingRepository.findUserTrainingDays(userId);

        if (user != null) {
            Userrepository.deleteUserById(userId);
            //return new ResponseEntity<>(HttpStatus.OK);
            if(healthStatus != null && !healthStatus.isEmpty()){
                    healthRepository.deleteUserHealthStatusById(userId);
            }
            if( trainingList != null && !trainingList.isEmpty()){
                    trainingRepository.deleteAllUserTrainingDaysById(userId);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid userId");
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        }
    } catch (EtAuthException e) {
        // Handle authentication exception
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

    private Map<String, String> generateJWTToken(user User) {
        long timestamp = System.currentTimeMillis();
        System.out.println(new Date(timestamp +  Constants.TOKEN_VALIDITY).getTime() + "times");
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.MY_SECRET_KEY)
        .setIssuedAt(new Date(timestamp))
        .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
        .claim("id", User.getId())
        .claim("username", User.getUsername())
        .claim("email", User.getEmail())
        .claim("role", User.getRole())
        .claim("healthdata",User.getHealthData())
        .compact();

        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }

    private boolean isValidEmail(String email) {
    Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if(!pattern.matcher(email).matches()){
            return false;
        }
    return true;
}

@GetMapping("/getAllUsersData")
    public ResponseEntity<?> getAllUsersData() {
        List<Map<String, Object>> userList = Userrepository.getAllUsersHealthAndTraining();
        System.out.println(userList.isEmpty() + "empty?");
        if (userList.isEmpty()) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "No user data found.");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
        List<Map<String, Object>> responseList = new ArrayList<>();
        for (Map<String, Object> user : userList) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("creator", user.get("creator"));
            userMap.put("username", user.get("username"));
            userMap.put("weight", user.get("weight"));
            userMap.put("current_weight", user.get("current_weight"));
            Double current_weight = (Double) user.get("current_weight");
            Double weight = (Double) user.get("weight");
            userMap.put("weight_difference", current_weight - weight);


            responseList.add(userMap);
        }

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

}
