package com.expensetracker.expensetrackerapi.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expensetracker.expensetrackerapi.exceptions.EtAuthException;
import com.expensetracker.expensetrackerapi.model.User;
import com.expensetracker.expensetrackerapi.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean validateEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Autowired
    UserRepository userRepository;

    @Override
    public User validateUser(String emailId, String password) throws EtAuthException {
       if(emailId != null) emailId=emailId.toLowerCase();
       if(!validateEmail(emailId)){
        throw new EtAuthException("Invalid Email Format");
       }
       return userRepository.findByEmailAndPassword(emailId, password);
    }

    @Override
    public User registerUser(String firstName, String lastName, String emailId, String password)
            throws EtAuthException {

        if(emailId != null) emailId=emailId.toLowerCase();
        if(!validateEmail(emailId)){
            throw new EtAuthException("Invalid Email Format");
        }

        Integer count = userRepository.getCountByEmail(emailId);
        if(count > 0){
            throw new EtAuthException("Email already exists");
        }

        Integer userId = userRepository.create(firstName, lastName, emailId, password);
        return userRepository.findById(userId);
    }
}
