package com.formation.velo.controllers;


import com.formation.velo.model.User;
import com.formation.velo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class UserController {
	
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}


	@GetMapping("users")
	public ResponseEntity<List<User>> getAll(){
		List<User> users = userService.findAll();

		return ResponseEntity.ok(users);
	}

	@GetMapping("users/{id}")
	public ResponseEntity<Optional<User>> getPersoneById(@PathVariable Integer id){
		Optional<User> user = userService.findById(id);
		
		return ResponseEntity.ok(user);
	}

	@PostMapping("users/add")
	public ResponseEntity<User> add(@RequestParam String name,@RequestParam String surname){

		User user = userService.save(User.builder().surname(surname).name(name).build());
		return ResponseEntity.ok(user);
	}



	@DeleteMapping("users/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id){
		userService.deleteById(id);
		return ResponseEntity.ok("deleted");
	}

	@PostMapping("users/update")
	public ResponseEntity<String> update(@RequestBody User user){
		userService.save(user);
		return ResponseEntity.ok("updated");
	}

// FRONT

	@GetMapping("users/list")
	public String showUpdateForm(Model model) {
		model.addAttribute("users", userService.findAll());
		return "index";
	}
	@PostMapping("users/addUser")
	public String addUser(@Valid User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "add-user";
		}

		userService.save(user);
		return "redirect:list";
	}

	@GetMapping("users/edit/{id}")
	public String edit(@PathVariable("id") Integer id, Model model) {
		User user = userService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		model.addAttribute("user", user);
		return "update-user";
	}

	@GetMapping("users/view/{id}")
	public String view(@PathVariable("id") Integer id, Model model) {
		User user = userService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		model.addAttribute("user", user);
		return "view-user";
	}

	@PostMapping("users/update/{id}")
	public String updateUser(@PathVariable("id") Integer id, @Valid User user, BindingResult result,
								Model model) {
		if (result.hasErrors()) {
			user.setId(id);
			return "index";
		}

		userService.save(user);
		model.addAttribute("users", userService.findAll());
		return "index";
	}
	@GetMapping("users/delete/{id}")
	public String deleteUser(@PathVariable("id") Integer id, Model model) {
		User user = userService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		userService.delete(user);
		model.addAttribute("users", userService.findAll());
		return "index";
	}

	@GetMapping("users/signup")
	public String showSignUpForm(User user) {
		return "add-user";
	}













}
