package com.tictactoebackend.projectapi.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

import com.tictactoebackend.projectapi.domain.Training;
import com.tictactoebackend.projectapi.domain.user;
import com.tictactoebackend.projectapi.exceptions.EtAuthException;
import com.tictactoebackend.projectapi.services.userService;
import com.tictactoebackend.projectapi.trainingRepository.TrainingRepository;
import com.tictactoebackend.projectapi.userRepository.userRepository;

@RestController
@RequestMapping("/api/training")
public class trainingResource {

    @Autowired
    userService Userservice;

    @Autowired
    userRepository Userrepository;

    @Autowired
    TrainingRepository trainingRepository;

    @PostMapping("/{userId}/addtraining")
    public ResponseEntity<?> addUserTraining(@PathVariable("userId") String userId, @RequestBody Map<String, Object> userTrainingMap) {

        System.out.println(userTrainingMap.toString() + "UserTrainingMap");

        user user = Userrepository.findById(userId);
        if(user == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid userId");
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        }



        List<String> missingFields = new ArrayList<>();
        List<String> unexpectedFields = new ArrayList<>();

// Check and collect missing and unexpected fields
        for (String requiredField : Arrays.asList("currentWeight","dailyCalories", "dailySteps")) {
            if (!userTrainingMap.containsKey(requiredField) || userTrainingMap.get(requiredField) == null) {
                missingFields.add(requiredField);
        }
    }

        for (String field : userTrainingMap.keySet()) {
            if (!Arrays.asList("currentWeight","dailyCalories", "dailySteps").contains(field)) {
                unexpectedFields.add(field);
        }
    }

        Object currentWeightValueObj = userTrainingMap.get("currentWeight");
        if (currentWeightValueObj != null) {
            if (currentWeightValueObj instanceof String) {
                String currentWeightValue = (String) currentWeightValueObj;
                if (currentWeightValue != null && !currentWeightValue.isEmpty()) {
                    double currentWeight = Double.parseDouble(currentWeightValue);
                    if (currentWeight <= 0 || currentWeight > 500) {
                        missingFields.add("currentWeight value is under zero or over 500(kg)");
                    }
                } else {
                    missingFields.add("currentWeight value is null");
                }
            } else {
                unexpectedFields.add("currentWeight must be a string");
            }
        }

    Object dailyCaloriesValueObj = userTrainingMap.get("dailyCalories");
        if (dailyCaloriesValueObj != null) {
            if (dailyCaloriesValueObj instanceof Integer) {
                Integer dailyCaloriesValue = (Integer) dailyCaloriesValueObj;
                if (dailyCaloriesValue != null) {

                    if (dailyCaloriesValue <= 0 || dailyCaloriesValue > 3500) {
                        missingFields.add("dailyCalories cant be under zero or over 3500(kCal) / day to lose weight");
                    }
                }
            } else {
                unexpectedFields.add("dailyCalories must be an integer");
            }
        }

    Object dailyStepsValueObj = userTrainingMap.get("dailySteps");
        if (dailyStepsValueObj != null) {
            if (dailyStepsValueObj instanceof Integer) {
                Integer dailyStepsValue = (Integer) dailyStepsValueObj;
                if (dailyStepsValue != null) {
                    if (dailyStepsValue < 0 || dailyStepsValue > 100000) {
                        missingFields.add("dailySteps cant be under zero or over 100000 steps /day");
                    }
                }
            } else {
                unexpectedFields.add("dailySteps must be an integer");
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
            String currentWeight = (String) userTrainingMap.get("currentWeight");
            Integer dailyCalories = (Integer) userTrainingMap.get("dailyCalories");
            Integer dailySteps = (Integer) userTrainingMap.get("dailySteps");

            List<Training> trainingList = trainingRepository.findUserTrainingDays(userId);


        if (!trainingList.isEmpty()) {


            Training lastTraining = trainingList.get(trainingList.size() - 1);

            // Calculate time difference in milliseconds
        long currentTime = System.currentTimeMillis();
        long createdAtTimestamp = lastTraining.getCreatedAt().getTime(); // Convert to milliseconds
        long timeDifference = currentTime - createdAtTimestamp;
        long timeThreshold = TimeUnit.HOURS.toMillis(24);

        if (timeDifference < timeThreshold) {
            long remainingTime = timeThreshold - timeDifference;
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "You can make another request in " + TimeUnit.MILLISECONDS.toHours(remainingTime) + " hours.");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
        }
        }

        String trainingId = trainingRepository.createTrainingDay(userId, currentWeight, dailyCalories, dailySteps);

        Training training = trainingRepository.findByTrainingId(trainingId);

        if(training != null) {

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("id", training.getId());
            responseMap.put("creator", training.getCreator());
            responseMap.put("current_weight", training.getCurrentWeight());
            responseMap.put("created_at", training.getCreatedAt());
            responseMap.put("daily_calories", training.getDailyCalories());
            responseMap.put("daily_steps", training.getDailySteps());

            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid userId");
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        }



    }



    @GetMapping("/{userId}/trainingdays")
    public ResponseEntity<?>getUserTrainingDays(@PathVariable("userId") String userId) {

        user user = Userrepository.findById(userId);
        if(user == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid userId");
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        }


        System.out.println("UserId" + userId);
        List<Training> trainingList = trainingRepository.findUserTrainingDays(userId);

        List<Map<String, Object>> responseList = new ArrayList<>();
        if(trainingList != null){
        for (Training status : trainingList) {
            Map<String, Object> trainingMap = new HashMap<>();
            trainingMap.put("id", status.getId());
            trainingMap.put("creator", status.getCreator());
            trainingMap.put("created_at", status.getCreatedAt());
            trainingMap.put("current_weight", status.getCurrentWeight());
            trainingMap.put("daily_calories", status.getDailyCalories());
            trainingMap.put("daily_steps", status.getDailySteps());

            responseList.add(trainingMap);
            //return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
        return new ResponseEntity<>(responseList, HttpStatus.OK);

    }
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid userId");
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);

    }



    @GetMapping("/getAllTrainingDays")
    public ResponseEntity<List<Map<String, Object>>> getAllUsersTrainings() {

        List<Training> trainingList = trainingRepository.getAllTrainingDays();

        List<Map<String, Object>> responseList = new ArrayList<>();
        for (Training status : trainingList) {
            Map<String, Object> trainingMap = new HashMap<>();
            trainingMap.put("id", status.getId());
            trainingMap.put("creator", status.getCreator());
            trainingMap.put("created_at", status.getCreatedAt());
            trainingMap.put("current_weight",status.getCurrentWeight());
            trainingMap.put("daily_calories", status.getDailyCalories());
            trainingMap.put("daily_steps", status.getDailySteps());



            responseList.add(trainingMap);
        }

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }



@DeleteMapping("/{userId}/deleteall")
public ResponseEntity<Map<String, Object>> deleteAllTrainingsUserById(@PathVariable("userId") String userId) {
    try {
        user User = Userrepository.findById(userId);
        List<Training> trainingList = trainingRepository.findUserTrainingDays(userId);

        if (User != null && !trainingList.isEmpty()) {
            trainingRepository.deleteAllUserTrainingDaysById(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "nothing to delete");
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        }
    } catch (EtAuthException e) {
        // Handle authentication exception
        Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "User training data not found for delete.");
        //throw new EtAuthException("User training data not found for delete.");
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
}

@DeleteMapping("/{trainingId}/delete")
public ResponseEntity<Map<String, Object>> deleteTrainingById(@PathVariable("trainingId") String trainingId) {
    try {
        Training training = trainingRepository.findByTrainingId(trainingId);

        if (training != null) {
            trainingRepository.deleteTrainingDayById(trainingId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid trainingId");
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        }
    } catch (EtAuthException e) {
        // Handle authentication exception
        Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "User training data not found for delete.");
        //throw new EtAuthException("User training not found for delete.");
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
}

}
