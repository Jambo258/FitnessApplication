package com.tictactoebackend.projectapi.healthRepository;

import java.util.List;

import com.tictactoebackend.projectapi.domain.HealthStatus;
import com.tictactoebackend.projectapi.exceptions.EtAuthException;

public interface HealthRepository {

    String createHealthStatus(String userId, String height, String weight, String targetWeight, Integer targetCalories, Integer targetSteps ) throws EtAuthException;

    List<HealthStatus> getAllHealthStatuses();

    List<HealthStatus> findByCreator(String id);

    void deleteUserHealthStatusById(String id) throws EtAuthException;

}
