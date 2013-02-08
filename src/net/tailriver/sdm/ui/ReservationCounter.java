package net.tailriver.sdm.ui;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import net.tailriver.sdm.Seat;
import net.tailriver.sdm.Series;
import net.tailriver.sdm.Station;
import net.tailriver.sdm.Train;
import net.tailriver.sdm.UserRole;
import net.tailriver.sdm.database.Database;
import net.tailriver.sdm.database.Log;
import net.tailriver.sdm.database.Reservation;
import net.tailriver.sdm.database.User;

public class ReservationCounter implements Application {
	private User clerk;

	private Reservation baseReservation = new Reservation();
	private Collection<Train> matchedTrains;
	private boolean isFirstGrade;
	private boolean isSmokingCar;
	private Collection<Seat> matchedSeats;
	private Collection<Seat> selectedSeats = new HashSet<Seat>();

	public ReservationCounter() {
		baseReservation = new Reservation();
		selectedSeats = new HashSet<Seat>();
	}

	public void setDay(Date day) {
		baseReservation.setDay(day);
	}

	public void setDeparture(Station departure) {
		baseReservation.setDeparture(departure);
	}

	public void setArrival(Station arrival) {
		baseReservation.setArrival(arrival);
	}

	public void searchTrains() {
		Date day = baseReservation.getDay();
		Station departure = baseReservation.getDeparture();
		Station arrival = baseReservation.getArrival();

		if (day.before(new Date(System.currentTimeMillis())))
			throw new IllegalArgumentException();
		if (day.after(new Date(System.currentTimeMillis() + 86400 * 7 * 1000)))
			throw new IllegalArgumentException();
		if (departure.equals(arrival))
			throw new IllegalArgumentException();

		matchedTrains = EnumSet.noneOf(Train.class);
		for (Train t : Train.values()) {
			if (t.available(departure, arrival))
				matchedTrains.add(t);
		}
	}

	public Set<Train> getMatchedTrains() {
		Set<Train> temp = new HashSet<Train>(matchedTrains);
		return Collections.unmodifiableSet(temp);
	}

	public void selectTrain(Train train) {
		if (!matchedTrains.contains(train))
			throw new IllegalArgumentException();
		baseReservation.setTrain(train);
	}

	public void setIsFirstGrade(boolean isFirstGrade) {
		this.isFirstGrade = isFirstGrade;
	}

	public void setIsSmokingCar(boolean isSmokingCar) {
		this.isSmokingCar = isSmokingCar;
	}

	public void searchSeats() {
		matchedSeats = new HashSet<Seat>();
		Series series = baseReservation.getTrain().getSeries();
		for (Seat seat : series.getSeats()) {
			// TODO
		}
	}

	public Collection<Seat> getMatchedSeat() {
		Set<Seat> temp = new HashSet<Seat>(matchedSeats);
		return Collections.unmodifiableSet(temp);
	}

	public void selectSeat(Seat seat) {
		if (!selectedSeats.contains(seat))
			throw new IllegalArgumentException();
		selectedSeats.add(seat);
	}

	public void unselectSeat(Seat seat) {
		selectedSeats.remove(seat);
	}

	public void clearSelectedSeats() {
		selectedSeats.clear();
	}

	public Set<Seat> getSelectedSeats() {
		Set<Seat> temp = new HashSet<Seat>(selectedSeats);
		return Collections.unmodifiableSet(temp);
	}

	public void setPhoneNumber(String phoneNumber) {
		baseReservation.setPhoneNumber(phoneNumber);
	}

	public String getPhoneNumber() {
		return baseReservation.getPhoneNumber();
	}

	public void reserve() {
		try {
			for (Seat s : selectedSeats) {
				Reservation r = new Reservation();
				r.setDay(baseReservation.getDay());
				r.setTrain(baseReservation.getTrain());
				r.setDeparture(baseReservation.getDeparture());
				r.setArrival(baseReservation.getArrival());
				r.setSeat(s);
				long id = r.insert();

				Log log = new Log();
				log.setUser(clerk);
				log.setDate(new Date(System.currentTimeMillis()));
				log.setCategory(getClass().getSimpleName());
				log.setMessage(Long.toString(id));
				log.insert();
			}
			Database.commit();
		} catch (SQLException e) {
			Database.rollback();
			e.printStackTrace();
		}
	}

	@Override
	public void start(Application parentApplication) {
		clerk = parentApplication.getUser();
		if (!clerk.hasRole(UserRole.CounterClerk))
			throw new IllegalArgumentException("not your work!");
	}

	@Override
	public void abort() {
	}

	@Override
	public void quit() {
	}

	@Override
	public User getUser() {
		return clerk;
	}
}
