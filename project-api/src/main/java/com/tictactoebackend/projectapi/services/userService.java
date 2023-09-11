package com.tictactoebackend.projectapi.services;

import com.tictactoebackend.projectapi.domain.user;
import com.tictactoebackend.projectapi.exceptions.EtAuthException;

public interface userService {

    user validateUser(String email, String password) throws EtAuthException;

    user registerUser(String username, String email, String password, String role) throws EtAuthException;

}
