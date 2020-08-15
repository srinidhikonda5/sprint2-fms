package com.capg.fms.passenger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.capg.fms.passenger.exceptions.InvalidInputException;
import com.capg.fms.passenger.model.Passenger;
import com.capg.fms.passenger.repository.IPassengerRepo;
import com.capg.fms.passenger.service.IPassengerService;

@SpringBootTest
class FmsPassengerMsApplicationTests {

	@Autowired
	IPassengerService passengerService;

	@Autowired
	IPassengerRepo passengerRepo;

	@Test
	public void addPassengerTest() throws URISyntaxException /// to add passenger
	{
		RestTemplate restTemplate = new RestTemplate();

		final String baseUrl = "http://localhost:8989/passenger/add";
		URI uri = new URI(baseUrl);
		Passenger passenger = new Passenger();
		passenger.setPassengerNum(9393549898l);
		passenger.setPassengerName("Arjun");
		passenger.setPassengerAge(21);
		passenger.setPassengerUIN(789456123214l);
		passenger.setLuggage(45);
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-COM-PERSIST", "true");
		HttpEntity<Passenger> request = new HttpEntity<>(passenger, headers);

		ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);
		Assertions.assertEquals(200, result.getStatusCodeValue());
		Assertions.assertNotNull(passenger);
	}

	@Test
	public void deletePassengerTest() throws URISyntaxException // to delete passenger by number
	{
		RestTemplate restTemplate = new RestTemplate();

		final String baseUrl = "http://localhost:8989/passenger/delete/num/7329802982";
		URI uri = new URI(baseUrl);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.DELETE, null, String.class);
		Assertions.assertEquals(200, result.getStatusCodeValue());
	}

	@Test
	public void getPassengerTest() throws URISyntaxException // get passenger details by number
	{
		RestTemplate restTemplate = new RestTemplate();

		final String baseUrl = "http://localhost:8989/passenger/num/8686460808";
		URI uri = new URI(baseUrl);

		ResponseEntity<Passenger> result = restTemplate.getForEntity(uri, Passenger.class);
		Passenger data = result.getBody();
		Assertions.assertEquals(200, result.getStatusCodeValue());
		Assertions.assertNotNull(data);
	}

	@Test
	public void getAllPassengersTest1() throws URISyntaxException // to get passenger list
	{
		RestTemplate restTemplate = new RestTemplate();

		final String baseUrl = "http://localhost:8989/passenger/all";
		URI uri = new URI(baseUrl);

		ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

		Assertions.assertEquals(200, result.getStatusCodeValue());
		Assertions.assertEquals(true, result.getBody().contains("PassengerList"));
	}

	@Test
	public void updatePassengerTest() throws URISyntaxException /// to update passenger
	{
		RestTemplate restTemplate = new RestTemplate();

		final String baseUrl = "http://localhost:8989/passenger/update";
		URI uri = new URI(baseUrl);
		Passenger passenger = new Passenger();
		passenger.setPassengerNum(9290562277l);
		passenger.setPassengerName("venkatesh");
		passenger.setPassengerAge(21);
		passenger.setPassengerUIN(789456123214l);
		passenger.setLuggage(45);
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-COM-PERSIST", "true");
		HttpEntity<Passenger> request = new HttpEntity<>(passenger, headers);

		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, request, String.class);
		Assertions.assertEquals(200, result.getStatusCodeValue());
		Assertions.assertNotNull(passenger);

	}

	@Test
	public void testValidPassengerNumber() throws InvalidInputException {
		assertEquals(true, passengerService.validatePassengerNumber(9959369426l));
	}

	@Test
	public void testValidPassengerUIN() throws InvalidInputException {
		assertEquals(true, passengerService.validatePassengerUIN(123456789012L));
	}

	@Test
	public void testValidatePassengerName() throws InvalidInputException {
		assertEquals(true, passengerService.validatePassengerName("Srinidhi"));
	}

	@Test
	public void testValidatePassengeAge() throws InvalidInputException {
		assertEquals(true, passengerService.validatePassengerAge(21));
	}

	@Test
	public void testPassengerNumberWithLessDigits() throws InvalidInputException {

		Exception exception = assertThrows(InvalidInputException.class, () -> {
			passengerService.validatePassengerNumber(4567890L);
		});

		String expectedMessage = "Passenger number should be of 10 digits";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void testPassengerNumberWithMoreDigits() throws InvalidInputException {

		Exception exception = assertThrows(InvalidInputException.class, () -> {
			passengerService.validatePassengerNumber(4567890123123L);
		});

		String expectedMessage = "Passenger number should be of 10 digits";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void testPassengerUINWithLessDigits() throws InvalidInputException {

		Exception exception = assertThrows(InvalidInputException.class, () -> {
			passengerService.validatePassengerUIN(4567890L);
		});

		String expectedMessage = "Passenger UIN should be of 12 digits";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void testPassengerUINWithMoreDigits() throws InvalidInputException {

		Exception exception = assertThrows(InvalidInputException.class, () -> {
			passengerService.validatePassengerUIN(1234345678907856L);
		});

		String expectedMessage = "Passenger UIN should be of 12 digits";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

}
