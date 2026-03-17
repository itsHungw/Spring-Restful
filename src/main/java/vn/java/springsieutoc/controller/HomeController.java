

package vn.java.springsieutoc.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.java.springsieutoc.service.UserService;

@RestController
public class HomeController {

	private final UserService userService;

	public HomeController(UserService userService) {
		this.userService = userService;
	}

	@Value("${hoidanit.secret:default-value}")
	private String name;

	@GetMapping("/")
	public String index() {
		this.userService.testJPA();
		return "Hello World from Spring Boot - @hoidanit devtool: " + name;
	}
}
