package com.example.login.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.login.models.LoginUser;
import com.example.login.models.User;
import com.example.login.repositories.UserRepository;
    
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepo;
    
    // TO-DO: Write register and login methods!

    public User register(User newUser, BindingResult result) {
      Optional<User> user = userRepo.findByEmail(newUser.getEmail());

      //Checks to see if the user is in the Database
      if(user.isPresent()){
        result.rejectValue("email", "Email", "Email already in use");
      }
      //This is checking to see if the input matches IF NOT then display error
      if(!newUser.getPassword().equals(newUser.getConfirm())) {
        result.rejectValue("confirm", "Confirm", "Passwords do not match!");
      }
      if(result.hasErrors()){
        return null;
      }
      // TO-DO: Additional validations!

      //?Hash the password
      String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
      newUser.setPassword(hashed);
      return userRepo.save(newUser);

      
    }
    public User login(LoginUser newLoginObject, BindingResult result) {

        // TO-DO: Additional validations!
        if(!this.checkEmail(newLoginObject.getEmail())) {
          result.rejectValue("email", "noEmail", "Invalid Login");
        }
        if(result.hasErrors()) {
          return null;
        }

          //! At this point we found the email
          User user = userRepo.findByEmail(newLoginObject.getEmail()).orElse(null);
          if(!BCrypt.checkpw(newLoginObject.getPassword(), user.getPassword())) {
            result.rejectValue("password", "Password", "Invalid Login");
          }
          if(result.hasErrors()) {
            return null;
          }
          return user;
    }

    public boolean checkEmail(String email) {
      Optional<User> user = userRepo.findByEmail(email);
      if(user.isPresent()) {
        return true;
      } else {
        return false;
      }
    }

}
