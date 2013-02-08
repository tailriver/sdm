package net.tailriver.sdm.ui;

import net.tailriver.sdm.database.User;

public interface Application {
	void start(Application parentApplication);

	void abort();

	void quit();

	User getUser();
}
