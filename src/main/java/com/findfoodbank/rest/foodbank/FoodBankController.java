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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class FoodBankController {

	@Autowired
	private FoodBankGeocodeService geocodeService;

	@Autowired
	private FoodBankRepository foodBankRepository;

	@CrossOrigin
	@GetMapping("/food-bank")
	public List<FoodBank> retrieveAllFoodBanks() {
		return foodBankRepository.findAll();
	}

	@CrossOrigin
	@GetMapping("/food-bank/{id}")
	public FoodBank retrieveFoodBank(@PathVariable long id) {
		Optional<FoodBank> foodBank = foodBankRepository.findById(id);

		if (!foodBank.isPresent())
			throw new FoodBankNotFoundException("id-" + id);

		return foodBank.get();
	}

	@DeleteMapping("/food-bank/{id}")
	public void deleteFoodBank(@PathVariable long id) {
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
	public ResponseEntity<Object> createFoodBank(@RequestBody FoodBank foodbank) throws UnsupportedEncodingException, InterruptedException {

		FoodBank savedFoodBank = foodBankRepository.save(foodbank);

		//call async method
		geocodeService.setGeoCode(savedFoodBank, foodBankRepository);
System.out.println("something");

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedFoodBank.getId()).toUri();

		return ResponseEntity.created(location).build();

	}
	
	@PutMapping("/food-bank/{id}")
	public ResponseEntity<Object> updateFoodBank(@RequestBody FoodBank foodbank, @PathVariable long id) {

		Optional<FoodBank> foodBankOptional = foodBankRepository.findById(id);

		if (!foodBankOptional.isPresent())
			return ResponseEntity.notFound().build();

		foodbank.setId(id);
		
		foodBankRepository.save(foodbank);

		return ResponseEntity.noContent().build();
	}

	/**
	 * Get lat and lng from user's phone/device with their permission.
	 *
	 * @param lat
	 * @param lng
	 * @param distance
	 * @return
	 */
	@GetMapping("/food-bank/nearest/{lat}/{lng}/{distance}")
	public List<FoodBank> findNearest(@PathVariable BigDecimal lat, @PathVariable BigDecimal lng, @PathVariable int distance) {

		List<FoodBank> nearByFoodBanks = foodBankRepository.findNearByFoodBanks(lat, lng, distance);

		return nearByFoodBanks;
	}

}
