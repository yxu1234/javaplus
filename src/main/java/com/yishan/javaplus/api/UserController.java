package com.yishan.javaplus.api;

import com.yishan.javaplus.domain.User;
import com.yishan.javaplus.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.BeanIds;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Repository
@RequestMapping(value = {"/api/users", "/api/user"},produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {


    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    @Qualifier(BeanIds.AUTHENTICATION_MANAGER)
    private AuthenticationManager authenticationManager;


//    @Autowired
//    public UserController(@Qualifier(BeanIds.AUTHENTICATION_MANAGER) AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }

//    @RequestMapping(method = RequestMethod.GET)
//    public List<User> getUserList(){
//        logger.debug("list users");
//        return new ArrayList<User>();
//    }


    @Autowired
    private UserService userService;

    @RequestMapping(value = "signup",method = RequestMethod.POST)
    public User generateUser(@RequestBody User user) {
        return userService.save(user);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getUserList(Long userId) {
        logger.debug("list users");
        return userService.findAll();
    }

//            @RequestMapping(value = "/{Id}", method = RequestMethod.GET)
//            @JasonView(Car.WithImageView.class)
//            public Car getCarById(@PathVariable PathVariable("Id") Long carId){
//                logger.debug("id is: "+carId);
//                return carService.findBy(new Car(carId)).get();

    @RequestMapping(value = "/{Id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable("Id") Long userId) {
        logger.debug("list users by id:" + userId);
        User result = userService.findById(userId);
        return result;
    }

    @RequestMapping(value ="/login", method = RequestMethod.POST, params = {"username", "password"})
    public User getUserByUsername(@RequestParam("username") String username, @RequestParam("password") String password,Device device) {
        logger.debug("username is: " + username);
        logger.debug("password is: " + password);
        Authentication notFullyAuthenticated = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(notFullyAuthenticated);
        return null;
    }

}
