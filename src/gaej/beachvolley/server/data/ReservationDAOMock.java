package gaej.beachvolley.server.data;

import gaej.beachvolley.client.Reservation;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReservationDAOMock implements ReservationDAO {

	private List<Reservation> store;

	public ReservationDAOMock() {
		DateFormat df = DateFormat.getDateInstance();
		Reservation a = new Reservation();
		a.setEndHour(24);
		a.setStartHour(23);
		try {
			a.setDate(df.parse("12-Dec-2009"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Reservation b = new Reservation();
		b.setStartHour(11);
		b.setEndHour(13);
		try {
			b.setDate(df.parse("14-Dec-2009"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Reservation> c = new ArrayList<Reservation>();
		c.add(a);
		c.add(b);
		store = c;
	}

	public List<Reservation> getReservationsByDate(Date d) {
		List<Reservation> returnList = new ArrayList<Reservation>();
		for (Reservation res : store) {
			if (res.getDate() != null && d != null) {
				if (res.getDate().getDay() == d.getDay()
						&& res.getDate().getMonth() == d.getMonth()) {
					returnList.add(res);
				}
			}
		}

		return returnList;
	}

	public void createReservation(Reservation r) {
		store.add(r);
		
	}

	public List<Reservation> getReservationsByUserId(String userString) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Reservation> getReservationsByUserIdAfterDate(
			String userString, Date d) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteReservation(Reservation r) {
		// TODO Auto-generated method stub
		
	}

	public void cleanUp(Date d) {
		// TODO Auto-generated method stub
		
	}

}
