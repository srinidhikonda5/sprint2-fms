package com.capg.fms.passenger.service;

import com.capg.fms.passenger.model.Passenger;
import com.capg.fms.passenger.model.PassengerList;

public interface IPassengerService {

	Passenger addPassenger(Passenger passenger);

	boolean deletePassenger(long passengerNum);

	Passenger getPassenger(long passengerNum);

	PassengerList getAllPassengers();

	Passenger updatePassenger(Passenger passenger);

	boolean validatePassengerUIN(long passengerUIN);

	boolean validatePassengerNumber(long passengerNum);

	boolean validatePassengerName(String passengerName);

	boolean validatePassengerAge(int passengerAge);
}
