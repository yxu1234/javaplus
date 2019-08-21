package com.yishan.javaplus.api;import com.amazonaws.services.alexaforbusiness.model.NotFoundException;import com.yishan.javaplus.domain.User;import com.yishan.javaplus.extend.security.JwtAuthenticationResponse;import com.yishan.javaplus.extend.security.JwtTokenUtil;import com.yishan.javaplus.extend.security.RestAuthenticationRequest;import com.yishan.javaplus.service.UserService;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.beans.factory.annotation.Qualifier;import org.springframework.http.HttpStatus;import org.springframework.http.MediaType;import org.springframework.http.ResponseEntity;import org.springframework.mobile.device.Device;import org.springframework.security.authentication.AuthenticationManager;import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;import org.springframework.security.config.BeanIds;import org.springframework.security.core.Authentication;import org.springframework.security.core.AuthenticationException;import org.springframework.security.core.context.SecurityContextHolder;import org.springframework.security.core.userdetails.UserDetails;import org.springframework.stereotype.Controller;import org.springframework.web.bind.annotation.*;import java.util.HashMap;import java.util.List;import java.util.Map;import static org.springframework.http.ResponseEntity.status;@Controller@RequestMapping(value = {"/api/users", "/api/user"}, produces = MediaType.APPLICATION_JSON_VALUE)public class UserController {    private final Logger logger = LoggerFactory.getLogger(getClass());    @Autowired    @Qualifier(BeanIds.AUTHENTICATION_MANAGER)    private AuthenticationManager authenticationManager;    @Autowired    private UserService userService;    @Autowired    private JwtTokenUtil jwtTokenUtil;    @RequestMapping(method = RequestMethod.GET)    public List<User> getUserList() {        logger.debug("List Users!");        return userService.findAll();    }    @RequestMapping(method = RequestMethod.GET, params = {"username"})    public List<User> getUsernamesList(@RequestParam("username") String usernames) {        logger.debug("List usernames!");        return userService.findAllOrderByUsername(usernames);    }    @RequestMapping(value = "/signup", method = RequestMethod.POST)    public User generateUser(@RequestBody User user) {        return userService.createNewUser(user);    }    @RequestMapping(method = RequestMethod.GET, params = {"lastName"})    public List<User> getUserByLastName(@RequestParam(value = "lastName") String lastName) {        logger.debug("parameter name is " + lastName);        List<User> user = userService.findByLastName(lastName);        return user;    }    @RequestMapping(method = RequestMethod.GET, params = {"firstName"})    public List<User> getUserByFirstName(@RequestParam(value = "firstName") String firstName) {        logger.debug("parameter name is " + firstName);        List<User> user = userService.findByFirstName(firstName);        return user;    }    @RequestMapping(value ="/{Id}", method = RequestMethod.PATCH, params = {"username"})    public User changeUserName(@PathVariable("Id") Long userId, @RequestParam("username") String username) {        logger.debug("New username will be: " + username);        User user = userService.findByIdWithLol(userId);        return userService.changeUserName(user, username);    }    @RequestMapping(value = "/{Id}", method = RequestMethod.GET)    public User getUserById(@PathVariable("Id") Long userId) {        logger.debug("list users by id:" + userId);        User result = userService.findById(userId);        return result;    }    @RequestMapping(method = RequestMethod.PUT)    public User updateUser(@RequestBody User user) {        logger.debug("New user will be: " + user);        return userService.changeUserEverything(user);    }    @RequestMapping(value = "/login", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)    @ResponseStatus(HttpStatus.OK)    @ResponseBody    public ResponseEntity<?> login(@RequestBody RestAuthenticationRequest restAuthenticationRequest, Device device) {        String username = restAuthenticationRequest.getUsername();        String password = restAuthenticationRequest.getPassword();        logger.debug("username is: " + username);        logger.debug("password is: " + password);        try {            Authentication notFullyAuthenticated = new UsernamePasswordAuthenticationToken(                    restAuthenticationRequest.getUsername(),                    restAuthenticationRequest.getPassword()            );            final Authentication authentication = authenticationManager.authenticate(notFullyAuthenticated);            SecurityContextHolder.getContext().setAuthentication(authentication);            try {            final UserDetails userDetails = userService.findByUsername(restAuthenticationRequest.getUsername());            final String token = jwtTokenUtil.generateToken(userDetails, device);            JwtAuthenticationResponse jsonToken = new JwtAuthenticationResponse(token);            return new ResponseEntity<JwtAuthenticationResponse>(jsonToken, HttpStatus.OK);            } catch (NotFoundException e ){                   logger.error("User can not be found!", e);                   return ResponseEntity.notFound().build();               }        } catch (AuthenticationException ex) {            return status(HttpStatus.UNAUTHORIZED).body("Please check your username and password! ");        }    }    @RequestMapping(value = "/login", method = RequestMethod.POST, params = {"username", "password"})    public ResponseEntity<Map<String, Object>> login(@RequestParam(value = "username") String username, @RequestParam("password") String password, Device device) {        logger.debug("username is: " + username);        logger.debug("password is: " + password);        try {            Authentication notFullyAuthenticated = new UsernamePasswordAuthenticationToken(username, password);            final Authentication authentication = authenticationManager.authenticate(notFullyAuthenticated);            SecurityContextHolder.getContext().setAuthentication(authentication);            final UserDetails userDetails = userService.findByUsername(username);            final String token = jwtTokenUtil.generateToken(userDetails, device);            Map<String, Object> jsontoken = new HashMap<>();            jsontoken.put("token", token);            return ResponseEntity.ok(jsontoken);        } catch (AuthenticationException ex) {            logger.debug("Nein! Nein! Nein! ");            Map<String, Object> map = new HashMap<>();            map.put("error", "Please check your username and password!");            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);        }    }}