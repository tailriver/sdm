package net.tailriver.sdm.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import net.tailriver.sdm.Seat;
import net.tailriver.sdm.Station;
import net.tailriver.sdm.Train;

public class Reservation extends Database {
	private static final String TABLE = "reservation";

	private Date day;
	private Train train;
	private Station departure;
	private Station arrival;
	private Seat seat;
	private String phoneNumber;

	public Reservation() {
	}

	private Reservation(long id) {
		super(id);
	}

	private Reservation(ResultSet rs) throws SQLException {
		this(rs.getLong("id"));
		day = rs.getDate("day");
		departure = Station.valueOf(rs.getString("departure"));
		arrival = Station.valueOf(rs.getString("arrival"));
		seat = new Seat(rs.getString("seat"));
		phoneNumber = rs.getString("phone");
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Train getTrain() {
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
	}

	public Station getDeparture() {
		return departure;
	}

	public void setDeparture(Station departure) {
		this.departure = departure;
	}

	public Station getArrival() {
		return arrival;
	}

	public void setArrival(Station arrival) {
		this.arrival = arrival;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public static Reservation select(long id) throws SQLException {
		String sql = "SELECT FROM " + TABLE + " WHERE id=?";
		try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				return new Reservation(rs);
			}
		}
	}

	public static Collection<Reservation> select(String phoneNumber)
			throws SQLException {
		String sql = "SELECT FROM " + TABLE + " WHERE phone=?";
		try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
			ps.setString(1, phoneNumber);
			HashSet<Reservation> results = new HashSet<Reservation>();
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					results.add(new Reservation(rs));
			}
			return results;
		}
	}

	public static Collection<Reservation> select(Date day, Train train)
			throws SQLException {
		String sql = "SELECT FROM " + TABLE + " WHERE day=? AND train=?";
		try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
			ps.setDate(1, day);
			ps.setString(2, train.name());
			HashSet<Reservation> results = new HashSet<Reservation>();
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					results.add(new Reservation(rs));
			}
			return results;
		}
	}

	public static boolean delete(long id) throws SQLException {
		return new Reservation(id).delete();
	}

	@Override
	protected String getTable() {
		return TABLE;
	}

	@Override
	protected PreparedStatement prepareForInsert(Connection connection)
			throws SQLException {
		String sql = "INSERT INTO "
				+ TABLE
				+ " (day,train,departure,arrival,seat,phone) VALUES (?,?,?,?,?,?)";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setDate(1, day);
		ps.setString(2, train.getName());
		ps.setString(3, departure.getName());
		ps.setString(4, arrival.getName());
		ps.setString(5, seat.toString());
		ps.setString(6, phoneNumber);
		return ps;
	}

	@Override
	protected PreparedStatement prepareForUpdate(Connection connection)
			throws SQLException {
		String sql = "UPDATE "
				+ TABLE
				+ " SET day=?, train=?, departure=?, arrival=?, seat=?, phone=? WHERE id=?";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setDate(1, day);
		ps.setString(2, train.getName());
		ps.setString(3, departure.getName());
		ps.setString(4, arrival.getName());
		ps.setString(5, seat.toString());
		ps.setString(6, phoneNumber);
		ps.setLong(7, getId());
		return ps;
	}
}
