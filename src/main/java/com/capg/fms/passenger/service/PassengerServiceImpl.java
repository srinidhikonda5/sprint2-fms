package com.capg.fms.passenger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capg.fms.passenger.exceptions.EmptyPassengerListException;
import com.capg.fms.passenger.exceptions.InvalidInputException;
import com.capg.fms.passenger.exceptions.PassengerAlreadyExistException;
import com.capg.fms.passenger.exceptions.PassengerNotFoundException;
import com.capg.fms.passenger.model.Passenger;
import com.capg.fms.passenger.model.PassengerList;
import com.capg.fms.passenger.repository.IPassengerRepo;

@Service
@Transactional
public class PassengerServiceImpl implements IPassengerService {

	@Autowired
	IPassengerRepo passengerRepo;

	@Override
	public Passenger addPassenger(Passenger passenger) {
		if (passengerRepo.existsById(passenger.getPassengerNum()))
			throw new PassengerAlreadyExistException(
					"Passenger with Number: " + passenger.getPassengerNum() + " is Already Exist");
		return passengerRepo.save(passenger);
	}

	@Override
	public boolean deletePassenger(long passengerNum) {
		if (!passengerRepo.existsById(passengerNum)) {
			throw new PassengerNotFoundException("Passenger with Number " + passengerNum + " Not Found");
		}
		passengerRepo.deleteById(passengerNum);
		return !passengerRepo.existsById(passengerNum);
	}

	@Override
	public Passenger getPassenger(long passengerNum) {
		if (!passengerRepo.existsById(passengerNum)) {
			throw new PassengerNotFoundException("Passenger with Number " + passengerNum + " Not Exist");
		}
		return passengerRepo.getOne(passengerNum);

	}

	@Override
	public PassengerList getAllPassengers() {
		if (passengerRepo.count() == 0) {
			throw new EmptyPassengerListException("No Passenger Found in Passenger Database");
		}
		return new PassengerList(passengerRepo.findAll());
	}

	@Override
	public Passenger updatePassenger(Passenger passenger) {
		if (!passengerRepo.existsById(passenger.getPassengerNum()))
			throw new PassengerNotFoundException(
					"Passenger with Number : " + passenger.getPassengerNum() + " Not Present");
		Passenger newPassenger = passengerRepo.getOne(passenger.getPassengerNum());
		newPassenger.setPassengerName(passenger.getPassengerName());
		newPassenger.setPassengerAge(passenger.getPassengerAge());
		newPassenger.setPassengerUIN(passenger.getPassengerUIN());
		newPassenger.setLuggage(passenger.getLuggage());

		passengerRepo.save(newPassenger);
		return newPassenger;
	}

	@Override
	public boolean validatePassengerNumber(long passengerNum) {
		String s = Long.toString(passengerNum);
		if (!(s.length() == 10 && s.charAt(0) != 0)) {
			throw new InvalidInputException("Passenger number should be of 10 digits");
		}
		return true;
	}

	@Override
	public boolean validatePassengerUIN(long passengerUIN) {
		String s = Long.toString(passengerUIN);
		if (!(s.length() == 12 && s.charAt(0) != 0)) {
			throw new InvalidInputException("Passenger UIN should be of 12 digits");
		}
		return true;
	}

	@Override
	public boolean validatePassengerName(String passengerName) {
		if (!passengerName.matches("[a-zA-Z]+")) {
			throw new InvalidInputException("Name should contain alphabets only");
		}
		return true;
	}

	@Override
	public boolean validatePassengerAge(int passengerAge) {
		String age = Integer.toString(passengerAge);
		if (!(age.length() == 2 && age.charAt(0) != 0)) {
			throw new InvalidInputException("Age should be between 10 and 99 only ");
		}
		return true;
	}

}