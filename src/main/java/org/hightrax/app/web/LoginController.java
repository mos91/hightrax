package org.hightrax.app.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class LoginController {

  private final static String LAST_EXCEPTION_HTTP_EXCEPTION = "SPRING_SECURITY_LAST_EXCEPTION";

  private final static String VIEW_NAME = "login";

  private final Logger logger = LoggerFactory.getLogger(LoginController.class);

  @RequestMapping(value = "/login")
  public String login(HttpServletRequest httpRequest,
    @RequestParam(value = "expired", required = false)
    boolean expired, Map<String, Object> model){
    Exception ex = (Exception) httpRequest.getSession().getAttribute(LAST_EXCEPTION_HTTP_EXCEPTION);

    if (ex != null && ex instanceof BadCredentialsException){
      model.put("badCredentials", true);
    } else if (expired){
      model.put("expired", true);
    }

    return VIEW_NAME;
  }
}
