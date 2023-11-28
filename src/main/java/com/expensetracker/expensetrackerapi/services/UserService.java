package com.expensetracker.expensetrackerapi.services;

import com.expensetracker.expensetrackerapi.exceptions.EtAuthException;
import com.expensetracker.expensetrackerapi.model.User;

public interface UserService {
    User validateUser(String email, String password) throws EtAuthException;

    User registerUser(String firstName, String lastName, String emailId, String password);
}
