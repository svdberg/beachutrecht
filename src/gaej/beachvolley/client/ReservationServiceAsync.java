package gaej.beachvolley.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReservationServiceAsync {

	void createReservation(Reservation r, AsyncCallback<Void> callback);

	void getAllReservations(AsyncCallback<List<Reservation>> callback);

	void getReservationByDate(Date d, AsyncCallback<List<Reservation>> callback);

	void listReservationsByUserFromDate(Date d,
			AsyncCallback<List<Reservation>> callback);

	void removeReservation(Reservation r, AsyncCallback<Void> callback);

	void cleanUp(Date d, AsyncCallback<Void> callback);

}
