package com.formation.velo.controllers;

import com.formation.velo.model.Stations;
import com.formation.velo.service.StationsService;
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
public class StationsController {
    private final StationsService stationsService;

	public StationsController(StationsService stationsService) {
		this.stationsService = stationsService;
	}


	@GetMapping("stations")
	public ResponseEntity<List<Stations>> getAll(){
		stationsService.saveRecords();
		List<Stations> stations = stationsService.findAll();

		return ResponseEntity.ok(stations);
	}

	@GetMapping("stations/{id}")
	public ResponseEntity<Optional<Stations>> getPersoneById(@PathVariable Integer id){
		Optional<Stations> stations = stationsService.findById(id);
		
		return ResponseEntity.ok(stations);
	}

	@PostMapping("stations/add")
	public ResponseEntity<Stations> add(@RequestParam String name,@RequestParam String surname){

		Stations stations = stationsService.save(Stations.builder().name(name).build());
		return ResponseEntity.ok(stations);
	}



	@DeleteMapping("stations/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id){
		stationsService.deleteById(id);
		return ResponseEntity.ok("deleted");
	}

	@PostMapping("stations/update")
	public ResponseEntity<String> update(@RequestBody Stations stations){
		stationsService.save(stations);
		return ResponseEntity.ok("updated");
	}

// FRONT

	@GetMapping("stations/list")
	public String showUpdateForm(Model model) {
		model.addAttribute("Stations", stationsService.findAll());
		return "index";
	}
	@PostMapping("stations/addStation")
	public String addStation(@Valid Stations stations, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "add-Station";
		}

		stationsService.save(stations);
		return "redirect:list";
	}

	@GetMapping("stations/edit/{id}")
	public String edit(@PathVariable("id") Integer id, Model model) {
		Stations stations = stationsService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Station Id:" + id));
		model.addAttribute("stations", stations);
		return "update-Station";
	}

	@GetMapping("stations/view/{id}")
	public String view(@PathVariable("id") Integer id, Model model) {
		Stations stations = stationsService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Station Id:" + id));
		model.addAttribute("Station", stations);
		return "view-Station";
	}

	@PostMapping("stations/update/{id}")
	public String updateStation(@PathVariable("id") Integer id, @Valid Stations stations, BindingResult result, Model model) {
		if (result.hasErrors()) {
			stations.setId(id);
			return "index";
		}

		stationsService.save(stations);
		model.addAttribute("Stations", stationsService.findAll());
		return "index";
	}
	@GetMapping("stations/delete/{id}")
	public String deleteStation(@PathVariable("id") Integer id, Model model) {
		Stations stations = stationsService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Station Id:" + id));
		stationsService.delete(stations);
		model.addAttribute("stations", stationsService.findAll());
		return "index";
	}

	@GetMapping("stations/signup")
	public String showSignUpForm(Stations stations) {
		return "add-Station";
	}
}
