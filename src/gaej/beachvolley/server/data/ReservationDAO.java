package gaej.beachvolley.server.data;

import gaej.beachvolley.client.Reservation;

import java.util.Date;
import java.util.List;

public interface ReservationDAO {

	List<Reservation> getReservationsByDate(Date d);

	void createReservation(Reservation r);

	List<Reservation> getReservationsByUserId(String userString);

	List<Reservation> getReservationsByUserIdAfterDate(String userString, Date d);

	void deleteReservation(Reservation r);

	void cleanUp(Date d);

}
