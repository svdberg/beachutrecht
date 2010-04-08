package gaej.beachvolley.server;

import gaej.beachvolley.server.data.ReservationDAO;
import gaej.beachvolley.server.data.ReservationDAOJDO;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class CronCleanUpServlet extends HttpServlet {
	
	private ReservationDAO reservationDAO = new ReservationDAOJDO();
	private static final Logger _logger = Logger
			.getLogger(CronCleanUpServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		try {

			_logger.info("Cron Job has been executed");
			_logger.info("Cleaning up Reservations from before: ");
			Date d = new Date();
			d.setHours(1);
			_logger.info("Cleaning up Reservations from before: "+d);
			reservationDAO.cleanUp(d);
			_logger.info("Done Cleaning up Reservations from before: "+d);

		}

		catch (Exception ex) {

			// Log any exceptions in your Cron Job
			_logger.log(Level.SEVERE, "Error cleaning up the store for old reservations: "+ex.getMessage());

		}

	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		doGet(req, resp);

	}

}
