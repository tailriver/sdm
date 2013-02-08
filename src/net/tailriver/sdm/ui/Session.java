package net.tailriver.sdm.ui;

import java.sql.SQLException;

import net.tailriver.sdm.database.User;

public class Session implements Application {
	private User user;

	@Override
	public void start(Application parentApplication) {
		// generate window
	}

	@Override
	public void abort() {
		logout();
	}

	@Override
	public void quit() {
		logout();
	}

	@Override
	public User getUser() {
		return user;
	}

	public void login(String id, String password) {
		if (user != null)
			throw new IllegalArgumentException("already authorized.");
		if (id == null || password == null)
			throw new IllegalArgumentException("please fill the fields.");

		try {
			user = User.select(id);
			if (user == null || !user.authorize(password)) {
				user = null;
				throw new IllegalArgumentException("wrong id or password.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void logout() {
		user = null;
	}

	public void startProcess(Application app) {
		if (user == null || !user.isAuthorized())
			throw new IllegalArgumentException("denied.");
		app.start(this);
	}

	public static void main(String... args) {
		Session app = new Session();
		app.start(null);
	}
}
