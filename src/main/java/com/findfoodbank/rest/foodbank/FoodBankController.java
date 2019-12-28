package com.findfoodbank.rest.foodbank;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class FoodBankController {

	@Autowired
	private FoodBankGeocodeService geocodeService;

	@Autowired
	private FoodBankRepository foodBankRepository;

	@GetMapping("/food-bank")
	public List<FoodBank> retrieveAllStudents() {
		return foodBankRepository.findAll();
	}

	@GetMapping("/food-bank/{id}")
	public FoodBank retrieveStudent(@PathVariable long id) {
		Optional<FoodBank> student = foodBankRepository.findById(id);

		if (!student.isPresent())
			throw new FoodBankNotFoundException("id-" + id);

		return student.get();
	}

	@DeleteMapping("/food-bank/{id}")
	public void deleteStudent(@PathVariable long id) {
		foodBankRepository.deleteById(id);
	}

	/**
	 * Create a food bank entry
	 * - duplicate entry - don't save
	 * - add latitude and longitude form Goodle then save
	 * @param foodbank
	 * @return
	 */
	@PostMapping("/food-bank")
	public ResponseEntity<Object> createStudent(@RequestBody FoodBank foodbank) throws UnsupportedEncodingException, InterruptedException {

		FoodBank savedFoodBank = foodBankRepository.save(foodbank);

		//call async method
		geocodeService.setGeoCode(savedFoodBank, foodBankRepository);
System.out.println("something");

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedFoodBank.getId()).toUri();

		return ResponseEntity.created(location).build();

	}
	
	@PutMapping("/food-bank/{id}")
	public ResponseEntity<Object> updateStudent(@RequestBody FoodBank foodbank, @PathVariable long id) {

		Optional<FoodBank> studentOptional = foodBankRepository.findById(id);

		if (!studentOptional.isPresent())
			return ResponseEntity.notFound().build();

		foodbank.setId(id);
		
		foodBankRepository.save(foodbank);

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/food-bank/nearest/{lat}/{lng}/{distance}")
	public List<FoodBank> findNearest(@PathVariable BigDecimal lat, @PathVariable BigDecimal lng, @PathVariable int distance) {

		List<FoodBank> nearByFoodBanks = foodBankRepository.findNearByFoodBanks(lat, lng, distance);

		return nearByFoodBanks;
	}

}
