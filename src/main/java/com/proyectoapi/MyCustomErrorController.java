package com.proyectoapi;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

/**
 * Class MyCustomErrorController: Esta clase permite gestionar los errores de la API.
 * @author Lucía Batista Flores
 * @version 1.0
 */

@RestController
@ApiIgnore
public class MyCustomErrorController implements ErrorController {

	//@RequestMapping(value = "/error",  method = RequestMethod.GET)
	@GetMapping(path = "/error")
	@ResponseBody
	public String handleError(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
		return String.format(
				"<html><body><h2>Error Page</h2><div>Status code: <b>%s</b></div>"
						+ "<div>Exception Message: <b>%s</b></div><body></html>",
				statusCode, exception == null ? "N/A" : exception.getMessage());
	}

	public String getErrorPath() {
		return "/error";
	}
}
