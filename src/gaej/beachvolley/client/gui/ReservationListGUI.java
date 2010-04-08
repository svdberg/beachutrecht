package gaej.beachvolley.client.gui;

import gaej.beachvolley.client.Reservation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.datepicker.client.DatePicker;

public class ReservationListGUI {
	/* Constants. */
	private static final String CALENDAR_LISTING_ROOT_PANEL = "calendarPanel";
	private static final String RESERVATION_LISTING_ROOT_PANEL = "reservationOverview";
	private static final String RESERVATION_FORM_ROOT_PANEL = "newReservation";
	private static final String EXISTING_RESERVATION_LISTING_ROOT_PANEL = "existingReservations";
	private static final String DAY_OVERVIEW_PANEL = "dayoverview";

	private HorizontalPanel mainPanel = new HorizontalPanel();
	private HorizontalPanel calendarPanel = new HorizontalPanel();
	private VerticalPanel reservationListPanel = new VerticalPanel();
	private VerticalPanel newReservationPanel = new VerticalPanel();
	private VerticalPanel existingReservationsPanel = new VerticalPanel();
	private VerticalPanel dayoverviewPanel = new VerticalPanel();

	/* GUI Widgets */
	public Button addButton;
	public ListBox startHourPicker;
	public ListBox endHourPicker;
	public TextBox dateField;
	public FlexTable reservationGrid;
	public FlexTable existingReservationsGrid;
	public DatePicker calendar;
	public FlexTable hourTable;

	/* Data model */
	private List<Reservation> reservations;
	private List<Reservation> existingReservations;
	public ReservationServiceDelegate reservationService;

	public void init() {
		addButton = new Button("Add new Reservation");
		reservationGrid = new FlexTable();
		existingReservationsGrid = new FlexTable();
		hourTable = new FlexTable();
		setUpHourTable(hourTable);
		calendar = new DatePicker();
		startHourPicker = new ListBox();
		endHourPicker = new ListBox();
		setUpHourPicker(startHourPicker);
		setUpHourPicker(endHourPicker);
		dateField = new TextBox();

		// buildForm();
		buildPanels();
		placeWidgets();
	}

	private void setUpHourTable(FlexTable hourTable2) {
		//fill the table with two colums:
		// 1 - 24 hours
		// blocked or not
		hourTable2.setText(0, 0, "Hours");
		hourTable2.setText(0, 1, "Reserved?");
		for ( int i=1; i<25; i++) {
		    hourTable2.setText(i, 0, String.valueOf(i));
		}
		for ( int i=1; i<25; i++) {
		    hourTable2.setText(i, 1, "Free");
		}
	}

	/**
	 * Sets up the hourpicker. Sets up the listbox to show hourstrings
	 * 
	 * @param hourPicker the listbox to fill
	 */
	private void setUpHourPicker(ListBox hourPicker) {
		for (int i = 7; i <= 24; i++) {
			hourPicker.addItem(i + ":00", Integer.toString(i));
		}
	}

	private void buildPanels() {

		this.mainPanel.add(calendarPanel);
		this.mainPanel.add(reservationListPanel);
		this.mainPanel.add(existingReservationsPanel);
		this.mainPanel.add(dayoverviewPanel);

		calendarPanel.add(calendar);

		reservationListPanel.add(reservationGrid);

		newReservationPanel.add(dateField);
		newReservationPanel.add(startHourPicker);
		newReservationPanel.add(endHourPicker);
		newReservationPanel.add(addButton);
		newReservationPanel.setBorderWidth(1);

		existingReservationsPanel.add(existingReservationsGrid);
		dayoverviewPanel.add(hourTable);
		
		this.existingReservationsGrid.setBorderWidth(1);

	}

	private void placeWidgets() {
		RootPanel.get(CALENDAR_LISTING_ROOT_PANEL).add(calendarPanel);
		RootPanel.get(RESERVATION_LISTING_ROOT_PANEL).add(reservationListPanel);
		RootPanel.get(RESERVATION_FORM_ROOT_PANEL).add(newReservationPanel);
		RootPanel.get(EXISTING_RESERVATION_LISTING_ROOT_PANEL).add(
				existingReservationsPanel);
		RootPanel.get(DAY_OVERVIEW_PANEL).add(dayoverviewPanel);
		
		reloadUserReservations();
	}

	public void gui_eventCalenderHighlighted(Date d) {
		reportMessage("Now loading reservations");
		reservationService.listReservationsOnDate(d);
		reloadUserReservations();
	}

	private void reloadUserReservations() {
		Date date = new Date();
		date.setHours(12);
		reservationService.listReservationsByUserFromDate(date);
	}

	public void service_eventListReservationsByDateSucess(
			List<Reservation> result) {
		clearGrid(reservationGrid);
		setUpHourTable(hourTable);
		this.reservations = result;
		if (this.reservations.size() == 0) {
			reportMessage("No reservations found");
		} else {
			int i = 1;
			for (Reservation reservation : result) {
				reservationGrid.setText(i, 1, reservation.toString()
						+ " by user: " + reservation.getPlayerFriendlyName());
				setHourTableRowOccupied(reservation);
				i++;
			}
		}
	}

	private void setHourTableRowOccupied(Reservation reservation) {
		int start = reservation.getStartHour();
		int end = reservation.getEndHour();
		for ( int i=start; i<=end; i++) {
			hourTable.setText(i, 1, "Occupied : "+reservation.getPlayerFriendlyName());
		}
	}

	private void clearGrid(FlexTable reservationGrid2) {
		for (int i = 0; i < reservationGrid2.getRowCount(); i++) {
			for (int j = 0; j < reservationGrid2.getCellCount(i); j++) {
				reservationGrid2.clearCell(i, j);
			}
		}
	}

	public void gui_eventAddButtonClick() {
		// show a popup for the new reservation and store it
		reportMessage("Storing your reservation..");
		
		// build the reservation
		Reservation newReservation = new Reservation(Integer
				.parseInt(this.startHourPicker.getValue(this.startHourPicker
						.getSelectedIndex())), Integer
				.parseInt(this.endHourPicker.getValue(this.endHourPicker
						.getSelectedIndex())), this.dateField.getText());

		if (newReservation.validate() != null) {
			reportMessage(newReservation.validate());
			return;
		}
		reservationService.storeReservation(newReservation);
	}

	public void gui_eventCalendarChosen(String date) {
		// set the selected date in the texbox.
		dateField.setText(date);
	}

	public void service_eventReservationStored() {
		reportMessage("Your reservation was stored!");
		reloadUserReservations();
	}

	public void service_eventReservationFailed(Throwable caught) {
		reportMessage("Storing reservation failed: "
				+ caught.getMessage());
	}

	public void service_eventReservationsByUserLoaded(List<Reservation> result) {
		clearGrid(this.existingReservationsGrid);

		if (result.size() == 0) {
			existingReservationsGrid.setText(1, 1,
					"No existing reservations for this user");
			existingReservations = new ArrayList<Reservation>();
		} else {
			existingReservations = result;
			int i = 1;
			for (Reservation reservation : result) {
				existingReservationsGrid.setText(i, 0, reservation.toString());
				
				Button deleteButton = new Button();
				deleteButton.setText("Delete Reservation");
				deleteButton.addClickHandler(new ClickHandler() {
					
					public void onClick(ClickEvent event) {
						Cell clickedCell = existingReservationsGrid.getCellForEvent(event);
						int index = clickedCell.getRowIndex();
						Reservation res = existingReservations.get(index-1);
						//set the client's reference to the clicked reservation
						if ( res != null ) {
							reservationService.removeReservation(res);
						}
						existingReservationsGrid.removeRow(index);
					}
				});
				existingReservationsGrid.setWidget(i, 1, deleteButton);
				i++;
			}
		}

	}

	public void service_eventRemoveReservation() {		
		reportMessage("Removed reservation");
	}

	public void service_eventRemoveReservationFailed(String message) {
		reportMessage("Removing reservation failed: "+message);
	}
	
	private void reportMessage(String message) {
		reservationGrid.setText(1, 1, message);
	}

}
