package com.tictactoebackend.projectapi.resources;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tictactoebackend.projectapi.domain.HealthStatus;
import com.tictactoebackend.projectapi.domain.user;
import com.tictactoebackend.projectapi.exceptions.EtAuthException;
import com.tictactoebackend.projectapi.healthRepository.HealthRepository;
import com.tictactoebackend.projectapi.services.userService;
import com.tictactoebackend.projectapi.userRepository.userRepository;


@RestController
@RequestMapping("/api/health")
public class healthResource {

    @Autowired
    userService Userservice;

    @Autowired
    userRepository Userrepository;

    @Autowired
    HealthRepository healthRepository;

    @PostMapping("/{userId}/addstatus")
    public ResponseEntity<?> addUserStatus(@PathVariable("userId") String userId, @RequestBody Map<String, Object> userHealthMap) {

        System.out.println(userHealthMap.toString() + "UserHealthMap");
        List<HealthStatus> healthStatusCheck = healthRepository.findByCreator(userId);

        if (healthStatusCheck != null) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "User already has a health status");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
        user user = Userrepository.findById(userId);
        if(user == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid userId");
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        }



        List<String> missingFields = new ArrayList<>();
        List<String> unexpectedFields = new ArrayList<>();

// Check and collect missing and unexpected fields
        for (String requiredField : Arrays.asList("height", "weight", "targetWeight", "targetCalories", "targetSteps")) {
            if (!userHealthMap.containsKey(requiredField) || userHealthMap.get(requiredField) == null) {
                missingFields.add(requiredField);
        }
    }

        for (String field : userHealthMap.keySet()) {
            if (!Arrays.asList("height", "weight", "targetWeight", "targetCalories", "targetSteps").contains(field)) {
                unexpectedFields.add(field);
        }
    }

    Object heightValueObj = userHealthMap.get("height");
        if (heightValueObj != null) {
            if (heightValueObj instanceof String) {
                String heightValue = (String) heightValueObj;
                if (heightValue != null && !heightValue.isEmpty()) {
                    double height = Double.parseDouble(heightValue);
                    if (height <= 0 || height > 2.5) {
                        missingFields.add("Height value is under zero or over 2.5 (meters)");
                    }
                } else {
                    missingFields.add("Height value is null");
                }
            } else {
                unexpectedFields.add("Height must be a string");
            }
        }

    Object weightValueObj = userHealthMap.get("weight");
        if (weightValueObj != null) {
            if (weightValueObj instanceof String) {
                String weightValue = (String) weightValueObj;
                if (weightValue != null && !weightValue.isEmpty()) {
                    double weight = Double.parseDouble(weightValue);
                    if (weight <= 0 || weight > 500) {
                        missingFields.add("Weight value is under zero or over 500(kg)");
                    }
                } else {
                    missingFields.add("Weight value is null");
                }
            } else {
                unexpectedFields.add("Weight must be a string");
            }
        }

    Object targetWeightValueObj = userHealthMap.get("targetWeight");
        if (targetWeightValueObj != null) {
            if (targetWeightValueObj instanceof String) {
                String targetWeightValue = (String) targetWeightValueObj;
                if (targetWeightValue != null && !targetWeightValue.isEmpty()) {
                    double targetWeight = Double.parseDouble(targetWeightValue);
                    if (targetWeight <= 0 || targetWeight > 500) {
                        missingFields.add("targetWeight value is under zero or over 500(kg)");
                    }
                } else {
                    missingFields.add("targetWeight value is null");
                }
            } else {
                unexpectedFields.add("targetWeight must be a string");
            }
        }

    Object targetCaloriesValueObj = userHealthMap.get("targetCalories");
        if (targetCaloriesValueObj != null) {
            if (targetCaloriesValueObj instanceof Integer) {
                Integer targetCaloriesValue = (Integer) targetCaloriesValueObj;
                if (targetCaloriesValue != null) {

                    if (targetCaloriesValue <= 0 || targetCaloriesValue > 3500) {
                        missingFields.add("targetCalories cant be under zero or over 3500(kCal) / day");
                    }
                }
            } else {
                unexpectedFields.add("targetCalories must be an integer");
            }
        }

    Object targetStepsValueObj = userHealthMap.get("targetSteps");
        if (targetStepsValueObj != null) {
            if (targetStepsValueObj instanceof Integer) {
                Integer targetStepsValue = (Integer) targetStepsValueObj;
                if (targetStepsValue != null) {
                    if (targetStepsValue <= 0 || targetStepsValue > 100000) {
                        missingFields.add("targetSteps cant be under zero or over 100000 steps /day");
                    }
                }
            } else {
                unexpectedFields.add("targetSteps must be an integer");
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
            String height = (String) userHealthMap.get("height");
            String weight = (String) userHealthMap.get("weight");
            String targetWeight = (String) userHealthMap.get("targetWeight");
            Integer targetCalories = (Integer) userHealthMap.get("targetCalories");
            Integer targetSteps = (Integer) userHealthMap.get("targetSteps");




            String healthStatusId = healthRepository.createHealthStatus(userId, height, weight, targetWeight, targetCalories, targetSteps);
            System.out.println(userId + "userid");
            System.out.println(healthRepository.findByCreator(userId)+ "found?");
            List<HealthStatus> healthStatus =  healthRepository.findByCreator(userId);
            List<Map<String, Object>> responseList = new ArrayList<>();
        if(healthStatus != null){
        for (HealthStatus status : healthStatus) {
            Map<String, Object> statusMap = new HashMap<>();
            statusMap.put("creator", status.getCreator());
            statusMap.put("created_at", status.getCreatedAt());
            statusMap.put("bmi", status.getBmi());
            statusMap.put("targetWeight", status.getTargetWeight());
            statusMap.put("height", status.getHeight());
            statusMap.put("weight",status.getWeight());
            statusMap.put("targetCalories", status.getTargetCalories());
            statusMap.put("targetSteps",status.getTargetSteps());

            responseList.add(statusMap);
            //return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
        return new ResponseEntity<>(responseList, HttpStatus.OK);

    }

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid userId");
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);



    }



    @GetMapping("/{userId}/status")
    public ResponseEntity<?>getUserHealthById(@PathVariable("userId") String userId) {


        System.out.println("UserId" + userId);
        List<HealthStatus> healthStatus = healthRepository.findByCreator(userId);

        List<Map<String, Object>> responseList = new ArrayList<>();
        if(healthStatus != null){
        for (HealthStatus status : healthStatus) {
            Map<String, Object> statusMap = new HashMap<>();
            statusMap.put("creator", status.getCreator());
            statusMap.put("created_at", status.getCreatedAt());
            statusMap.put("bmi", status.getBmi());
            statusMap.put("targetWeight", status.getTargetWeight());
            statusMap.put("height", status.getHeight());
            statusMap.put("weight",status.getWeight());
            statusMap.put("targetCalories", status.getTargetCalories());
            statusMap.put("targetSteps",status.getTargetSteps());


            responseList.add(statusMap);
            //return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
        return new ResponseEntity<>(responseList, HttpStatus.OK);

    }
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid userId");
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);

    }



    @GetMapping("/getAllStatuses")
    public ResponseEntity<List<Map<String, Object>>> getAllUserStatuses() {

        List<HealthStatus> StatusList = healthRepository.getAllHealthStatuses();

        List<Map<String, Object>> responseList = new ArrayList<>();
        for (HealthStatus status : StatusList) {
            Map<String, Object> statusMap = new HashMap<>();
            statusMap.put("creator", status.getCreator());
            statusMap.put("created_at", status.getCreatedAt());
            statusMap.put("bmi", status.getBmi());
            statusMap.put("targetWeight", status.getTargetWeight());
            statusMap.put("height", status.getHeight());
            statusMap.put("weight",status.getWeight());
            statusMap.put("targetCalories", status.getTargetCalories());
            statusMap.put("targetSteps",status.getTargetSteps());

            responseList.add(statusMap);
        }

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

@DeleteMapping("/{userId}/status/delete")
public ResponseEntity<Map<String, Object>> deleteUserById(@PathVariable("userId") String userId) {
    try {
        user user = Userrepository.findById(userId);
        List<HealthStatus> healthStatus = healthRepository.findByCreator(userId);

        if (user != null && !healthStatus.isEmpty()) {
            healthRepository.deleteUserHealthStatusById(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid userId");
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        }
    } catch (EtAuthException e) {
        // Handle authentication exception
        Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "User health status not found for delete.");
        //throw new EtAuthException("User health status not found for delete.");
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
}


}
