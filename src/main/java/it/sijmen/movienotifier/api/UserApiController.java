package it.sijmen.movienotifier.api;

import it.sijmen.jump.Jump;
import it.sijmen.jump.JumpRequest;
import it.sijmen.movienotifier.model.User;
import it.sijmen.movienotifier.model.exceptions.BadRequestException;
import it.sijmen.movienotifier.model.exceptions.UnauthorizedException;
import it.sijmen.movienotifier.model.requests.LoginDetails;
import it.sijmen.movienotifier.repositories.UserRepository;
import it.sijmen.movienotifier.util.PasswordAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserApiController  {

    private final Jump<User> userJump;

    private final UserRepository userRepository;

    @Inject
    public UserApiController(Jump<User> userJump, UserRepository userRepository) {
        this.userJump = userJump;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public User login(
            @RequestBody LoginDetails loginDetails) {
        if (loginDetails == null)
            throw new BadRequestException("No details provided");

        loginDetails.validate();

        User user = userRepository.findFirstByName(loginDetails.getName());
        if(user == null)
            throw new UnauthorizedException();

        if(!PasswordAuthentication.authenticate(loginDetails.getPassword(), user.getPassword()))
            throw new UnauthorizedException();
        return user;
    }

    @RequestMapping(value = {"/{urldata:.*}", "/"})
    public HttpEntity<?> genericUserMethod(HttpMethod requestMethod,
                                           @RequestHeader(required = false) Map<String, String> requestHeaders,
                                           @PathVariable(required = false) String urldata,
                                           @RequestBody(required = false) String body) throws Exception{
        return userJump.handle(new JumpRequest(
                requestMethod, requestHeaders, urldata, body
        ));
    }
}
