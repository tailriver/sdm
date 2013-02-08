package net.tailriver.sdm.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;

import net.tailriver.sdm.UserRole;

public class User extends Database {
	private static final String TABLE = "user";
	private static final String ROLE_DELIMITER = "|";

	private String name;
	private String encryptedPassword;
	private Collection<UserRole> roles;
	private boolean isAuthorized;

	public User() {
	}

	private User(long id) {
		super(id);
	}

	private User(ResultSet rs) throws SQLException {
		super(rs.getLong("id"));
		name = rs.getString("name");
		encryptedPassword = rs.getString("password");
		roles = deserializeRole(rs.getString("roles"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean hasRole(UserRole role) {
		return roles.contains(role);
	}

	public boolean authorize(String password) {
		isAuthorized = checkPassword(password);
		return isAuthorized;
	}

	public boolean isAuthorized() {
		return isAuthorized;
	}

	public boolean checkPassword(String password) {
		return encryptedPassword.equals(encrypt(password));
	}

	public void setPassword(String newPassword) {
		encryptedPassword = encrypt(newPassword);
	}

	private String encrypt(String raw) {
		String encrypted = raw; // dummy
		return encrypted;
	}

	public static User select(long id) throws SQLException {
		String sql = "SELECT FROM " + TABLE + " WHERE id=?";
		try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				return new User(rs);
			}
		}
	}

	public static User select(String name) throws SQLException {
		String sql = "SELECT FROM " + TABLE + " WHERE name=?";
		try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
			ps.setString(1, name);
			try (ResultSet rs = ps.executeQuery()) {
				return new User(rs);
			}
		}
	}

	public static Collection<User> select(UserRole role) throws SQLException {
		String sql = "SELECT FROM " + TABLE + " WHERE roles LIKE ?";
		try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
			ps.setString(1, "%" + role.name() + "%");
			Collection<User> users = new HashSet<User>();
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					if (deserializeRole(rs.getString("roles")).contains(role))
						users.add(new User(rs));
				}
			}
			return users;
		}
	}

	public static boolean delete(long id) throws SQLException {
		return new User(id).delete();
	}

	@Override
	protected String getTable() {
		return TABLE;
	}

	@Override
	protected PreparedStatement prepareForInsert(Connection connection)
			throws SQLException {
		String sql = "INSERT INTO " + TABLE
				+ " (name,password,roles) VALUES (?,?,?)";
		PreparedStatement ps = getConnection().prepareStatement(sql);
		ps.setString(1, name);
		ps.setString(2, encryptedPassword);
		ps.setString(3, serializeRole(roles));
		return ps;
	}

	@Override
	protected PreparedStatement prepareForUpdate(Connection connection)
			throws SQLException {
		String sql = "UPDATE " + TABLE
				+ " SET name=?, password=?, roles=? WHERE id=?";
		PreparedStatement ps = getConnection().prepareStatement(sql);
		ps.setString(1, name);
		ps.setString(2, encryptedPassword);
		ps.setString(3, serializeRole(roles));
		ps.setLong(4, getId());
		return ps;
	}

	private static String serializeRole(Collection<UserRole> roles) {
		StringBuilder sb = new StringBuilder();
		for (UserRole r : UserRole.values()) {
			if (roles.contains(r))
				sb.append(r.name()).append(ROLE_DELIMITER);
		}
		sb.delete(sb.length() - ROLE_DELIMITER.length(), sb.length());
		return sb.toString();
	}

	private static Collection<UserRole> deserializeRole(String roleString) {
		EnumSet<UserRole> roles = EnumSet.noneOf(UserRole.class);
		for (String s : roleString.split(ROLE_DELIMITER)) {
			roles.add(UserRole.valueOf(s));
		}
		return roles;
	}
}
