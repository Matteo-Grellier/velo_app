package com.formation.velo.controllers;

import com.formation.velo.model.BusStop;
import com.formation.velo.service.BusStopService;
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
public class BusStopController {
    private final BusStopService BusStopService;

	public BusStopController(BusStopService BusStopService) {
		this.BusStopService = BusStopService;
	}


	@GetMapping("busStop")
	public ResponseEntity<List<BusStop>> getAll(){
		// BusStopService.saveRecords();
		List<BusStop> busStop = BusStopService.findAll();

		return ResponseEntity.ok(busStop);
	}

	@GetMapping("busStop/{id}")
	public ResponseEntity<Optional<BusStop>> getBusStopById(@PathVariable Integer id){
		Optional<BusStop> busStop = BusStopService.findById(id);
		
		return ResponseEntity.ok(busStop);
	}

	@PostMapping("busStop/add")
	public ResponseEntity<BusStop> add(@RequestParam String name,@RequestParam String surname){

		BusStop busStop = BusStopService.save(BusStop.builder().stop_name(name).build());
		return ResponseEntity.ok(busStop);
	}

	@DeleteMapping("busStop/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id){
		BusStopService.deleteById(id);
		return ResponseEntity.ok("deleted");
	}

	@PostMapping("busStop/update")
	public ResponseEntity<String> update(@RequestBody BusStop busStop){
		BusStopService.save(busStop);
		return ResponseEntity.ok("updated");
	}

// FRONT

	@GetMapping("busStop/list")
	public String showUpdateForm(Model model) {
		model.addAttribute("BusStop", BusStopService.findAll());
		return "index";
	}
	@PostMapping("busStop/addStation")
	public String addStation(@Valid BusStop busStop, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "add-Station";
		}

		BusStopService.save(busStop);
		return "redirect:list";
	}

	@GetMapping("busStop/edit/{id}")
	public String edit(@PathVariable("id") Integer id, Model model) {
		BusStop busStop = BusStopService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Station Id:" + id));
		model.addAttribute("busStop", busStop);
		return "update-Station";
	}

	@GetMapping("busStop/view/{id}")
	public String view(@PathVariable("id") Integer id, Model model) {
		BusStop busStop = BusStopService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Station Id:" + id));
		model.addAttribute("Station", busStop);
		return "view-Station";
	}

	@PostMapping("busStop/update/{id}")
	public String updateStation(@PathVariable("id") Integer id, @Valid BusStop busStop, BindingResult result, Model model) {
		if (result.hasErrors()) {
			busStop.setId(id);
			return "index";
		}

		BusStopService.save(busStop);
		model.addAttribute("BusStop", BusStopService.findAll());
		return "index";
	}
	@GetMapping("busStop/delete/{id}")
	public String deleteStation(@PathVariable("id") Integer id, Model model) {
		BusStop busStop = BusStopService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Station Id:" + id));
		BusStopService.delete(busStop);
		model.addAttribute("busStop", BusStopService.findAll());
		return "index";
	}

	@GetMapping("busStop/signup")
	public String showSignUpForm(BusStop busStop) {
		return "add-Station";
	}
}
