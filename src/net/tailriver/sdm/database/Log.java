package net.tailriver.sdm.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public class Log extends Database {
	private static final String TABLE = "log";

	private Date date;
	private User user;
	private String category;
	private String message;

	public Log() {
	}

	private Log(long id) {
		super(id);
	}

	private Log(ResultSet rs) throws SQLException {
		this(rs.getLong("id"));
		date = rs.getDate("date");
		user = User.select(rs.getLong("user"));
		category = rs.getString("category");
		message = rs.getString("message");
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static Log select(long id) throws SQLException {
		String sql = "SELECT FROM " + TABLE + " WHERE id=?";
		try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				return new Log(rs);
			}
		}
	}

	public static Collection<Log> select(User user) throws SQLException {
		String sql = "SELECT FROM " + TABLE + " WHERE user=?";
		try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
			ps.setLong(1, user.getId());
			HashSet<Log> results = new HashSet<Log>();
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					results.add(new Log(rs));
			}
			return results;
		}
	}

	public static Collection<Log> select(String category) throws SQLException {
		String sql = "SELECT FROM " + TABLE + " WHERE category=?";
		try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
			ps.setString(1, category);
			HashSet<Log> results = new HashSet<Log>();
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					results.add(new Log(rs));
			}
			return results;
		}
	}

	public static boolean delete(long id) throws SQLException {
		return new Log(id).delete();
	}

	@Override
	protected String getTable() {
		return TABLE;
	}

	@Override
	protected PreparedStatement prepareForInsert(Connection connection)
			throws SQLException {
		String sql = "INSERT INTO " + TABLE + " (date,user,category,message)";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setDate(1, date);
		ps.setLong(2, user.getId());
		ps.setString(3, category);
		ps.setString(4, message);
		return ps;
	}

	@Override
	protected PreparedStatement prepareForUpdate(Connection connection)
			throws SQLException {
		throw new UnsupportedOperationException();
	}
}
