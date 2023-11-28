package com.expensetracker.expensetrackerapi.repository;

import com.expensetracker.expensetrackerapi.exceptions.EtAuthException;
import com.expensetracker.expensetrackerapi.model.User;

import java.sql.Statement;
import java.sql.PreparedStatement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String SQL_CREATE = "INSERT INTO ET_USERS(USER_ID, FIRST_NAME, LAST_NAME, EMAIL_ID, PASSWORD) VALUES(NEXTVAL('ET_USERS_SEQ'), ?, ?, ?, ?)";
    private static final String SQL_COUNT_BY_EMAIL_ID = "SELECT COUNT(*) FROM ET_USERS WHERE EMAIL_ID = ?";
    private static final String SQL_FIND_BY_USER_ID = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL_ID, PASSWORD FROM ET_USERS WHERE USER_ID = ?";
    private static final String SQL_FIND_BY_EMAIL_ID = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL_ID, PASSWORD FROM ET_USERS WHERE EMAIL_ID = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Integer create(String firstName, String lastName, String emailId, String password) throws EtAuthException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, emailId);
                ps.setString(4, password);
                return ps;
            }, keyHolder);

            return (Integer) keyHolder.getKeys().get("USER_ID");
        } catch (Exception e) {
            throw new EtAuthException("Invalid user details");
        }
    }

    @Override
    public User findByEmailAndPassword(String emailId, String password) throws EtAuthException {
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL_ID, new Object[] { emailId }, userRowMapper);
            if (!password.equals(user.getPassword())) {
                throw new EtAuthException("Invalid email/password");

            }
            return user;
        } catch (EmptyResultDataAccessException e) {
            throw new EtAuthException("Invalid email/password");
        }
    }

    @Override
    public Integer getCountByEmail(String emailId) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL_ID, new Object[] { emailId }, Integer.class);
    }

    @Override
    public User findById(Integer userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_USER_ID, new Object[] { userId }, userRowMapper);
    }

    private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        return new User(rs.getInt("USER_ID"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"),
                rs.getString("EMAIL_ID"), rs.getString("PASSWORD"));
    });
}
