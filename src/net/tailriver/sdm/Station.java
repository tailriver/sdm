package net.tailriver.sdm;

public enum Station {
	Oookayama("大岡山"), Nagoya("名古屋"), Kyoto("京都"), ShinOsaka("新大阪");

	private final String name;

	Station(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
