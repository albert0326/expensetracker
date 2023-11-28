package com.expensetracker.expensetrackerapi.repository;

import com.expensetracker.expensetrackerapi.exceptions.EtAuthException;
import com.expensetracker.expensetrackerapi.model.User;

public interface UserRepository {
    Integer create(String firstName, String lastName, String emailId, String password) throws EtAuthException;

    User findByEmailAndPassword(String email, String password) throws EtAuthException;

    Integer getCountByEmail(String emailId);

    User findById(Integer userId);
}
