package com.tictactoebackend.projectapi.userRepository;

import java.util.List;

import com.tictactoebackend.projectapi.domain.user;
import com.tictactoebackend.projectapi.exceptions.EtAuthException;

public interface userRepository {

    String create(String username, String email, String password , String role) throws EtAuthException;

    user findByEmailandPassword(String email, String password) throws EtAuthException;

    Integer getCountByEmail(String email);

    user findById(String id);

    List<user> getAllUsers();

    void updateUser(String id, String username, String email, String password, String role);

    void deleteUserById(String id) throws EtAuthException;

}
