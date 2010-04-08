package gaej.beachvolley.server;

import gaej.beachvolley.client.DateHourOverlapException;
import gaej.beachvolley.client.Reservation;
import gaej.beachvolley.client.ReservationService;
import gaej.beachvolley.server.data.ReservationDAO;
import gaej.beachvolley.server.data.ReservationDAOJDO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public class ReservationServiceImpl extends RemoteServiceServlet implements ReservationService {

	private static final long serialVersionUID = 1L;
    private ReservationDAO reservationDAO = new ReservationDAOJDO();//new ReservationDAOMock();

	public List<Reservation> getAllReservations() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Reservation> getReservationByDate(Date d) {
		List<Reservation> listPlayers = reservationDAO.getReservationsByDate(d);
        return new ArrayList<Reservation> (listPlayers);
	}

	public void createReservation(Reservation r) throws DateHourOverlapException {
		Date now = new Date();
		now.setHours(r.getDate().getHours()-1);
		now.setMinutes(r.getDate().getMinutes());
		now.setSeconds(0);
		
		if (r.getDate().before(now)) {
			throw new DateHourOverlapException("Date cannot be before today");
		}
		if ( overlapExists(r)) {
			throw new DateHourOverlapException("Overlap in hours and date");
		}
		
		UserService us = UserServiceFactory.getUserService();
		if (us.isUserLoggedIn()) {
			r.setPlayer(us.getCurrentUser().getUserId());
			r.setPlayerFriendlyName(us.getCurrentUser().getNickname());
		}
		reservationDAO.createReservation(r);
	}


	private boolean overlapExists(Reservation r) {
		List<Reservation> reservationsOnDate = this.getReservationByDate(r.getDate());
		for ( Reservation res : reservationsOnDate ) {
			if ( r.getStartHour() <= res.getStartHour() && r.getEndHour() <= res.getEndHour()
					&& res.getStartHour() < res.getEndHour()) {
				return true;
			}
			if ( res.getStartHour() <= r.getStartHour() && res.getEndHour() <= r.getEndHour()
					&& r.getStartHour() < res.getEndHour()) {
				return true;
			}
			if ( r.getStartHour() <= res.getStartHour() && res.getEndHour() <= r.getEndHour()){
				return true;
			}
			if ( res.getStartHour() <= r.getStartHour() && r.getEndHour() <= res.getEndHour()) {
				return true;
			}
		}
		return false;
	}

	public List<Reservation> listReservationsByUserFromDate(Date d) {
		UserService us = UserServiceFactory.getUserService();
		List<Reservation> returnList = new ArrayList<Reservation>();
		if (us.isUserLoggedIn()) {
			String userString = us.getCurrentUser().getUserId();
			returnList = reservationDAO.getReservationsByUserIdAfterDate(userString,d);
		}
		return new ArrayList<Reservation> (returnList);
	}


	public void removeReservation(Reservation r) {
		reservationDAO.deleteReservation(r);
		
	}

	public void cleanUp(Date d) {
		reservationDAO.cleanUp(d);
		
	}

}
