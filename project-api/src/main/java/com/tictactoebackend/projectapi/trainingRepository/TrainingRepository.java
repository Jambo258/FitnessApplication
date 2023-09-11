package com.tictactoebackend.projectapi.trainingRepository;

import java.util.List;


import com.tictactoebackend.projectapi.domain.Training;
import com.tictactoebackend.projectapi.exceptions.EtAuthException;

public interface TrainingRepository {

    String createTrainingDay(String userId, String currentWeight, Integer dailyCalories, Integer dailySteps ) throws EtAuthException;

    List<Training> getAllTrainingDays();

    Training findByTrainingId(String id);

    List<Training> findUserTrainingDays(String id);

    void deleteAllUserTrainingDaysById(String id) throws EtAuthException;

    void deleteTrainingDayById(String id) throws EtAuthException;

}
