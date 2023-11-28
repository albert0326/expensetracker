package com.expensetracker.expensetrackerapi.resources;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expensetracker.expensetrackerapi.model.User;
import com.expensetracker.expensetrackerapi.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userMap){
        String emailId = (String) userMap.get("emailId");
        String password = (String) userMap.get("password");

        User user = userService.validateUser(emailId, password);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Logged in Successfully");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> userMap) {
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String emailId = (String) userMap.get("emailId");
        String password = (String) userMap.get("password");
        User user = userService.registerUser(firstName, lastName, emailId, password);

        Map<String, String> map = new HashMap<>();
        map.put("message", "Registered Successfully");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
