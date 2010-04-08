package gaej.beachvolley.server.data;

import gaej.beachvolley.client.Reservation;

import java.util.Date;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

public class ReservationDAOJDO implements ReservationDAO {

	private static final PersistenceManagerFactory pmfInstance = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	public static PersistenceManagerFactory getPersistenceManagerFactory() {
		return pmfInstance;
	}

	public void createReservation(Reservation r) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		try {
			pm.makePersistent(r);
		} finally {
			pm.close();
		}
	}

	
	public List<Reservation> getReservationsByDate(Date d) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		String query = "select from " + Reservation.class.getName();
		Query q = pm.newQuery(query);
		q.setFilter("date == dateparam");
		q.declareImports("import java.util.Date");
		q.declareParameters("Date dateparam");
		return (List<Reservation>) q.execute(d);

	}

	
	public List<Reservation> getReservationsByUserId(String userString) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		String query = "select from " + Reservation.class.getName();
		Query q = pm.newQuery(query);
		q.setFilter("player == playerParam");
		q.declareParameters("String playerParam");
		return (List<Reservation>) q.execute(userString);
	}

	
	public List<Reservation> getReservationsByUserIdAfterDate(
			String userString, Date d) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		String query = "select from " + Reservation.class.getName();

		Query q = pm.newQuery(query);

		q.setFilter("player == playerParam && date >= dateparam ");
		// q.declareParameters("String playerParam");

		//q.setFilter("date >= dateparam");
		q.declareImports("import java.util.Date");
		q.declareParameters("String playerParam, Date dateparam");

		return (List<Reservation>) q.execute(userString, d);
	}

	
	public void deleteReservation(Reservation r) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();
		try {
			pm.currentTransaction().begin();

			// We don't have a reference to the selected Product.
			// So we have to look it up first,
			r = pm.getObjectById(Reservation.class, r.getId());
			pm.deletePersistent(r);

			pm.currentTransaction().commit();
		} catch (Exception ex) {
			pm.currentTransaction().rollback();
			throw new RuntimeException(ex);
		} finally {
			pm.close();
		}
	}

	public void cleanUp(Date d) {
		PersistenceManager pm = getPersistenceManagerFactory()
				.getPersistenceManager();

		String query = "select from " + Reservation.class.getName();

		Query q = pm.newQuery(query);

		q.setFilter("date < dateparam");
		q.declareImports("import java.util.Date");
		q.declareParameters("Date dateparam");

		List<Reservation> resultList = (List<Reservation>) q.execute(d);
		for (Reservation res : resultList) {
			try {
				pm.currentTransaction().begin();

				// We don't have a reference to the selected Product.
				// So we have to look it up first,
				Reservation toBeDeleted = null;
				toBeDeleted = pm.getObjectById(Reservation.class, res.getId());
				if ( toBeDeleted != null )
					pm.deletePersistent(toBeDeleted);

				pm.currentTransaction().commit();
			} catch (Exception ex) {
				pm.currentTransaction().rollback();
				throw new RuntimeException(ex);
			} finally {
				pm.close();
			}
		}

	}
}
