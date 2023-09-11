package com.tictactoebackend.projectapi.healthRepository;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tictactoebackend.projectapi.domain.HealthStatus;
import com.tictactoebackend.projectapi.exceptions.EtAuthException;

@Repository
public class HealthRepositoryimpl implements HealthRepository{

    private static final String SQL_CREATE_HEALTH_STATUS = "INSERT INTO public.health (id, creator, height, weight, bmi, target_weight, target_calories, target_steps) " +
                                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT * FROM public.health where creator = ?";

    private static final String SQL_FIND_ALL_USER_STATUSES = "SELECT * FROM public.health";

    private static final String SQL_UPDATE = "UPDATE public.users SET healthdata = ? WHERE id = ?";

    private static final String SQL_DELETE = "DELETE FROM public.health " +
                                          "WHERE creator = ?";

     @Autowired
     JdbcTemplate jdbcTemplate;

    @Override
    public String createHealthStatus(String userId, String height, String weight, String targetWeight, Integer targetCalories, Integer targetSteps) throws EtAuthException {
        String uuid = UUID.randomUUID().toString();


        boolean healthdata = true;
        Double parsedWeight = Double.parseDouble(weight);
        Double parsedHeight = Double.parseDouble(height);
        Double parsedTargetWeight = Double.parseDouble(targetWeight);

        Double bmi = parsedWeight / (parsedHeight * parsedHeight);


        try {
            jdbcTemplate.update(SQL_CREATE_HEALTH_STATUS, uuid, userId,/*new Timestamp(System.currentTimeMillis()),*/
             parsedHeight, parsedWeight,bmi, parsedTargetWeight, targetCalories, targetSteps);
             jdbcTemplate.update(SQL_UPDATE,healthdata , userId);
            return uuid;
        } catch (Exception e) {
            e.printStackTrace();
            throw new EtAuthException("Failed to create users health status." + e);
        }
    }

    @Override
    public List<HealthStatus> findByCreator(String id){
       String sql = SQL_FIND_BY_ID;
       System.out.println(sql + "query");

    try {

        List<HealthStatus> healthStatusList = jdbcTemplate.query(SQL_FIND_BY_ID, healthRowMapper, new Object[]{id});
        System.out.println(healthStatusList.get(0).getCreatedAt() + "value");
        return healthStatusList;
    } catch (Exception e) {
        // Handle any exceptions or return null if no user found
        return null;
    }
    }




    private RowMapper<HealthStatus> healthRowMapper = ((rs, rowNumber) -> {
    return new HealthStatus(
        rs.getString("id"),
        rs.getString("creator"),
        rs.getString("height"),
        rs.getString("weight"),
        rs.getDouble("bmi"),
        rs.getString("target_weight"),
        rs.getInt("target_calories"),
        rs.getInt("target_steps"),
        rs.getTimestamp("created_at")

    );
});



@Override
    public List<HealthStatus> getAllHealthStatuses(){
       String sql = SQL_FIND_ALL_USER_STATUSES;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(HealthStatus.class));

    }




@Override
public void deleteUserHealthStatusById(String id) throws EtAuthException {
    String sql = SQL_DELETE;
    boolean healthdata = false;

    try {
        jdbcTemplate.update(SQL_UPDATE,healthdata , id);
        int deletedRows = jdbcTemplate.update(sql, id);

        if (deletedRows == 0) {
            throw new EtAuthException("User health status not found for delete.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        throw new EtAuthException("Failed to delete user." + e);
    }
}


}
