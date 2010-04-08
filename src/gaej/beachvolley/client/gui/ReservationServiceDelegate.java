package gaej.beachvolley.client.gui;

import gaej.beachvolley.client.Reservation;
import gaej.beachvolley.client.ReservationService;
import gaej.beachvolley.client.ReservationServiceAsync;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ReservationServiceDelegate {
	private ReservationServiceAsync reservationService = GWT
			.create(ReservationService.class);
	public ReservationListGUI gui;

	public void listReservationsOnDate(Date d) {
		reservationService.getReservationByDate(d, new AsyncCallback<List<Reservation>>() {
			public void onFailure(Throwable caught) {
				// gui.service_eventListPlayersFailed(caught);
			}

			public void onSuccess(List<Reservation> result) {
				gui.service_eventListReservationsByDateSucess(result);
				
			}
		});
	}

	public void storeReservation(Reservation newReservation) {
		reservationService.createReservation(newReservation, new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				gui.service_eventReservationFailed(caught);
				
			}

			public void onSuccess(Void result) {
				gui.service_eventReservationStored();
				
			}
			
		});
		
	}

	public void listReservationsByUserFromDate(Date d) {
		reservationService.listReservationsByUserFromDate(d, new AsyncCallback<List<Reservation>>() {

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			public void onSuccess(List<Reservation> result) {
				gui.service_eventReservationsByUserLoaded(result);
				
			}
			
		});
		
	}

	public void removeReservation(Reservation reservation) {
		reservationService.removeReservation(reservation, new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				gui.service_eventRemoveReservationFailed(caught.getMessage());
				
			}

			public void onSuccess(Void result) {
				gui.service_eventRemoveReservation();
				
			}
		});
	}

	
}
