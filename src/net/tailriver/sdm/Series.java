package net.tailriver.sdm;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public enum Series {
	A, B;

	private int numberOfCars;
	private int[] firstGradeCars;
	private int[] smokingCars;
	private Collection<Seat> seats;

	Series() {
		int numberOfSeats;
		char[] seatLabels;
		switch (name()) {
		case "A":
			numberOfCars = 8;
			firstGradeCars = new int[] { 5, 6 };
			smokingCars = new int[] { 1, 2, 5 };
			numberOfSeats = 20;
			seatLabels = new char[] { 'A', 'B', 'C', 'D' };
			break;
		case "B":
			numberOfCars = 10;
			firstGradeCars = new int[] {};
			smokingCars = new int[] { 1, 2, 3 };
			numberOfSeats = 20;
			seatLabels = new char[] { 'A', 'B', 'C', 'D', 'E' };
			break;
		default:
			throw new IllegalArgumentException();
		}

		Arrays.sort(firstGradeCars);
		Arrays.sort(smokingCars);
		seats = new HashSet<Seat>();
		for (int car = 1; car <= numberOfCars; car++) {
			for (int seat = 1; seat <= numberOfSeats; seat++) {
				for (char label : seatLabels) {
					seats.add(new Seat(car, seat, label));
				}
			}
		}
	}

	public Collection<Seat> getSeats() {
		return seats;
	}

	public int getNumberOfCars() {
		return numberOfCars;
	}

	public boolean isFirstGrade(int car) {
		if (car < 1 || car > numberOfCars)
			throw new IllegalArgumentException();
		return Arrays.binarySearch(firstGradeCars, car) < 0;
	}

	public boolean isSmokingCar(int car) {
		if (car < 1 || car > numberOfCars)
			throw new IllegalArgumentException();
		return Arrays.binarySearch(smokingCars, car) < 0;
	}
}
