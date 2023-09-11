package com.tictactoebackend.projectapi.trainingRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tictactoebackend.projectapi.domain.Training;
import com.tictactoebackend.projectapi.exceptions.EtAuthException;

@Repository
public class TrainingRepositoryimpl  implements TrainingRepository{

    private static final String SQL_CREATE_TRAINING_DAY = "INSERT INTO public.dailytrainingandcalories (id, creator, current_weight, daily_calories, daily_steps) " +
                                          "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY__TRAINING_ID = "SELECT * FROM public.dailytrainingandcalories where id = ?";

    private static final String SQL_FIND_ALL_TRAINING_DAYS_BY_CREATOR_ID = "SELECT * FROM public.dailytrainingandcalories where creator = ?";

    private static final String SQL_FIND_ALL_TRAINING_DAYS = "SELECT * FROM public.dailytrainingandcalories";

    private static final String SQL_DELETE = "DELETE FROM public.dailytrainingandcalories " +
                                          "WHERE creator = ?";

    private static final String SQL_DELETE_BY_TRAINING_ID = "DELETE FROM public.dailytrainingandcalories " +
                                          "WHERE id = ?";

     @Autowired
     JdbcTemplate jdbcTemplate;

    @Override
    public String createTrainingDay(String userId, String currentWeight, Integer dailyCalories, Integer dailySteps ) throws EtAuthException {
        String uuid = UUID.randomUUID().toString();
        Double parsedCurrentWeight = Double.parseDouble(currentWeight);



        try {
            jdbcTemplate.update(SQL_CREATE_TRAINING_DAY, uuid, userId,/*new Timestamp(System.currentTimeMillis()),*/
             parsedCurrentWeight,dailyCalories, dailySteps);
            return uuid;
        } catch (Exception e) {
            e.printStackTrace();
            throw new EtAuthException("Failed to create users training day" + e);
        }
    }





    @Override
    public Training findByTrainingId(String id){
       String sql = SQL_FIND_BY__TRAINING_ID;
       System.out.println(sql + "query");

    try {
        System.out.println("is working?");


        Training training = jdbcTemplate.queryForObject(sql, trainingRowMapper, new Object[]{id});

        return training;
    } catch (Exception e) {
        // Handle any exceptions or return null if no user found
        return null;
    }
    }

    @Override
    public List<Training> findUserTrainingDays(String id){
       String sql = SQL_FIND_ALL_TRAINING_DAYS_BY_CREATOR_ID;
       System.out.println(sql + "query");

    try {
        System.out.println("is working?");


        List<Training> trainingList = jdbcTemplate.query(sql, trainingRowMapper, new Object[]{id});
        return trainingList;

    } catch (Exception e) {
        // Handle any exceptions or return null if no user found
        return null;
    }
    }




    private RowMapper<Training> trainingRowMapper = ((rs, rowNumber) -> {
    return new Training(
        rs.getString("id"),
        rs.getString("creator"),
        rs.getInt("daily_calories"),
        rs.getInt("daily_steps"),
        rs.getString("current_weight"),
        rs.getTimestamp("created_at")

    );
});



@Override
    public List<Training> getAllTrainingDays(){
       String sql = SQL_FIND_ALL_TRAINING_DAYS;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Training.class));

    }




@Override
public void deleteAllUserTrainingDaysById(String id) throws EtAuthException {
    String sql = SQL_DELETE;

    try {
        int deletedRows = jdbcTemplate.update(sql, id);
        if (deletedRows == 0) {
            throw new EtAuthException("User training days not found for delete.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        throw new EtAuthException("Failed to delete." + e);
    }
}

@Override
public void deleteTrainingDayById(String id) throws EtAuthException {
    String sql = SQL_DELETE_BY_TRAINING_ID;

    try {
        int deletedRows = jdbcTemplate.update(sql, id);
        if (deletedRows == 0) {
            throw new EtAuthException("User training day not found for delete.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        throw new EtAuthException("Failed to delete." + e);
    }
}

}
