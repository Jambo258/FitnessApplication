package com.tictactoebackend.projectapi.userRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tictactoebackend.projectapi.domain.user;
import com.tictactoebackend.projectapi.exceptions.EtAuthException;

@Repository
public class userRepositoryimpl implements userRepository{


    private static final String SQL_CREATE = "INSERT INTO public.users (id, username, email, password, role, healthdata) " +
                                          "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM public.users where email = ?";

    private static final String SQL_FIND_BY_EMAIL = "SELECT * FROM public.users where email = ?";

    private static final String SQL_FIND_BY_ID = "SELECT * FROM public.users where id = ?";

    private static final String SQL_FIND_ALL_USERS = "SELECT * FROM public.users";

    private static final String SQL_UPDATE = "UPDATE public.users SET username = ?, email = ?, password = ?, role = ? WHERE id = ?";

    private static final String SQL_DELETE = "DELETE FROM public.users " +
                                          "WHERE id = ?";

     @Autowired
     JdbcTemplate jdbcTemplate;

    @Override
    public String create(String username, String email, String password, String role) throws EtAuthException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        String uuid = UUID.randomUUID().toString();
        Map<String, Object> params = new HashMap<>();
        params.put("id", uuid);
        params.put("username", username);
        params.put("email", email);
        params.put("password", hashedPassword);
        params.put("role", role);

        try {
            jdbcTemplate.update(SQL_CREATE, uuid, username, email, hashedPassword, role, false);
            return uuid;
        } catch (Exception e) {
            e.printStackTrace();
            throw new EtAuthException("Failed to create user." + e);
        }
    }


    @Override
    public user findByEmailandPassword(String email, String password) throws EtAuthException{
        String sql = SQL_FIND_BY_EMAIL;
        try {
            user User = jdbcTemplate.queryForObject(sql, userRowMapper, new Object[]{email});
            System.out.println("user details" + User.getPassword());
            System.out.println("truthy?" + BCrypt.checkpw(password, User.getPassword()));
            if(!BCrypt.checkpw(password, User.getPassword()))

                throw new EtAuthException("Invalid email / password!");
            return User;

        } catch (EmptyResultDataAccessException e) {
            throw new EtAuthException("Invalid email / password");
        }
    }

    @Override
    public Integer getCountByEmail(String email) {
        String sql = SQL_COUNT_BY_EMAIL;

    try {
        return jdbcTemplate.queryForObject(sql, Integer.class, new Object[]{email});
    } catch (Exception e) {
        // Handle any exceptions or return a default value
        return 0;
    }
    }

    @Override
    public user findById(String id){
       String sql = SQL_FIND_BY_ID;

    try {
        user User = jdbcTemplate.queryForObject(sql, userRowMapper, new Object[]{id});
        return User;
    } catch (Exception e) {
        // Handle any exceptions or return null if no user found
        return null;
    }
    }

    private RowMapper<user> userRowMapper = ((rs, rowNumber) -> {
    return new user(
            rs.getString("id"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("role"),
            rs.getBoolean("healthdata")
    );
});

@Override
    public List<user> getAllUsers(){
       String sql = SQL_FIND_ALL_USERS;

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(user.class));

    }

    @Override
public void updateUser(String id, String username, String email, String password, String role) throws EtAuthException {
    //String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
    String sql = SQL_UPDATE;
    System.out.println("username"+ username);
    System.out.println("email"+ email);
    System.out.println("password"+ password);
    System.out.println("role" + role);
    System.out.println("id"+ id);
    //String Role = "guest";
    try {

        jdbcTemplate.update(SQL_UPDATE, username, email, password, role, id);


    } catch (Exception e) {
        e.printStackTrace();
        throw new EtAuthException("Failed to update user." + e);
    }
}

@Override
public void deleteUserById(String id) throws EtAuthException {
    String sql = SQL_DELETE;

    try {
        int deletedRows = jdbcTemplate.update(sql, id);
        if (deletedRows == 0) {
            throw new EtAuthException("User not found for delete.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        throw new EtAuthException("Failed to delete user." + e);
    }
}


}
