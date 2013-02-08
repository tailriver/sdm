package net.tailriver.sdm;

public class Seat implements Comparable<Seat> {
	private static final String DELIMITER = "-";

	private final int car;
	private final int seat;
	private final char label;

	public Seat(int car, int seat, char label) {
		this.car = car;
		this.seat = seat;
		this.label = Character.toUpperCase(label);
	}

	public Seat(String seatString) {
		String[] s = seatString.split(DELIMITER);
		car = Integer.parseInt(s[0]);
		seat = Integer.parseInt(s[1]);
		label = s[2].charAt(0);
	}

	public int getCar() {
		return car;
	}

	public int getNumber() {
		return seat;
	}

	public char getCharacter() {
		return label;
	}

	@Override
	public int compareTo(Seat o) {
		if (car != o.car)
			return car > o.car ? 1 : -1;
		if (seat != o.seat)
			return seat > o.seat ? 1 : -1;
		if (label != o.label)
			return label > o.label ? 1 : -1;
		return 0;
	}

	@Override
	public String toString() {
		return car + DELIMITER + seat + DELIMITER + label;
	}
}
