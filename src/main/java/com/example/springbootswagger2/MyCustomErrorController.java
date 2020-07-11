package com.example.springbootswagger2;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
public class MyCustomErrorController implements ErrorController {

  @RequestMapping("/error")
  @ResponseBody
  public String handleError(HttpServletRequest request) {
      Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
      Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
      return String.format("<html><body><h2>Error Page</h2><div>Status code: <b>%s</b></div>"
                      + "<div>Exception Message: <b>%s</b></div><body></html>",
              statusCode, exception==null? "N/A": exception.getMessage());
  }

  public String getErrorPath() {
      return "/error";
  }
}
