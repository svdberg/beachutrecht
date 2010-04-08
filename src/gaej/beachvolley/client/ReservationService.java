package gaej.beachvolley.client;


import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("reservations")
public interface ReservationService extends RemoteService {
	List<Reservation> getAllReservations();
	List<Reservation> getReservationByDate(Date d);
	List<Reservation> listReservationsByUserFromDate(Date d);
	void removeReservation(Reservation r);
	void createReservation(Reservation r) throws DateHourOverlapException;
	void cleanUp(Date d);
}
