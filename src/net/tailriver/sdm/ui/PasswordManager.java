package net.tailriver.sdm.ui;

import java.sql.SQLException;

import net.tailriver.sdm.database.Database;
import net.tailriver.sdm.database.User;

public class PasswordManager implements Application {
	private User user;

	@Override
	public void start(Application parentApplication) {
		user = parentApplication.getUser();
		if (!user.isAuthorized())
			throw new IllegalArgumentException("not authorized.");
		// generate window
	}

	@Override
	public void abort() {
	}

	@Override
	public void quit() {
	}

	@Override
	public User getUser() {
		return user;
	}

	public void changePassword(String old, String new1, String new2) {
		if (!user.checkPassword(old))
			throw new IllegalArgumentException("wrong old password.");
		if (!new1.equals(new2))
			throw new IllegalArgumentException("new passwords are not same.");

		user.setPassword(new1);
		try {
			user.update();
			Database.commit();
		} catch (SQLException e) {
			Database.rollback();
			e.printStackTrace();
		}
	}
}
