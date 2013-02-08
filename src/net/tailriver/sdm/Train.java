package net.tailriver.sdm;

import java.util.List;

public enum Train {
	Nozomi1("のぞみ1号", Series.A), //
	Nozomi3("のぞみ3号", Series.B), //
	Nozomi5("のぞみ5号", Series.B), //
	Nozomi7("のぞみ7号", Series.A), //
	Nozomi9("のぞみ9号", Series.B);

	private final String name;
	private final Series series;
	private List<Station> stations;
	private List<Integer> stationHours;
	private List<Integer> stationMinutes;

	public String getName() {
		return name;
	}

	public Series getSeries() {
		return series;
	}

	Train(String name, Series series) {
		this.name = name;
		this.series = series;
		switch (name()) {
		case "Nozomi1":
			stop(Station.Oookayama, 8, 0);
			stop(Station.Nagoya, 9, 30);
			stop(Station.Kyoto, 10, 10);
			stop(Station.ShinOsaka, 10, 30);
			break;
		case "Nozomi3":
			stop(Station.Oookayama, 11, 0);
			stop(Station.Nagoya, 12, 30);
			break;
		case "Nozomi5":
			stop(Station.Nagoya, 12, 45);
			pass(Station.Kyoto);
			stop(Station.ShinOsaka, 13, 40);
			break;
		case "Nozomi7":
			stop(Station.Oookayama, 16, 0);
			pass(Station.Nagoya);
			stop(Station.Kyoto, 18, 10);
			stop(Station.ShinOsaka, 18, 30);
			break;
		case "Nozomi9":
			stop(Station.Oookayama, 2, 0);
			stop(Station.Nagoya, 3, 30);
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	private void stop(Station station, int hour, int minute) {
		stations.add(station);
		stationHours.add(hour);
		stationMinutes.add(minute);
	}

	private void pass(Station station) {
		stations.add(station);
		stationHours.add(null);
		stationMinutes.add(null);
	}

	public boolean available(Station station) {
		int index = stations.indexOf(station);
		return index != -1 && stationHours.get(index) != null;
	}

	public boolean available(Station departure, Station arrival) {
		if (!available(departure) || !available(arrival))
			return false;
		return stations.indexOf(departure) < stations.indexOf(arrival);
	}
}
