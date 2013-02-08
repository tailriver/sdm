package net.tailriver.sdm.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Database {
	private static Connection connection;

	private long id;

	public Database() {
		this(-1);
	}

	protected Database(long id) {
		setId(id);
	}

	public final long getId() {
		return id;
	}

	protected final void setId(long id) {
		this.id = id;
	}

	abstract protected String getTable();

	protected static final Connection getConnection() throws SQLException {
		if (connection == null || connection.isClosed())
			connection = DriverManager.getConnection(null); // TODO
		return connection;
	}

	public final void close() throws SQLException {
		connection.close();
	}

	public long insert() throws SQLException {
		try (PreparedStatement ps = prepareForInsert(getConnection())) {
			ps.execute();
			setId(id); // TODO
		}
		return id;
	}

	abstract protected PreparedStatement prepareForInsert(Connection connection)
			throws SQLException;

	public void update() throws SQLException {
		if (id < 0)
			throw new IllegalArgumentException();
		try (PreparedStatement ps = prepareForUpdate(getConnection())) {
			ps.execute();
		}
	}

	abstract protected PreparedStatement prepareForUpdate(Connection connection)
			throws SQLException;

	public boolean delete() throws SQLException {
		if (id < 0)
			throw new IllegalArgumentException();
		String sql = "DELETE FROM " + getTable() + " WHERE id=?";
		try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
			ps.setLong(1, id);
			return ps.execute();
		}
	}

	public static void commit() throws SQLException {
		getConnection().commit();
	}

	public static void rollback() {
		try {
			getConnection().rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
