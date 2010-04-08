package gaej.beachvolley.client;

import gaej.beachvolley.client.gui.ReservationListGUI;
import gaej.beachvolley.client.gui.ReservationServiceDelegate;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HighlightEvent;
import com.google.gwt.event.logical.shared.HighlightHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BeachVolley implements EntryPoint {

	private ReservationListGUI gui;
	private ReservationServiceDelegate reservationDelegate;
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label(
			"Please sign in to your Google Account to access the Reservering application.");
	private Anchor signInLink = new Anchor("Sign In");

	public void onModuleLoad() {

		// Check login status using login service.
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {
					public void onFailure(Throwable error) {
					}

					public void onSuccess(LoginInfo result) {
						loginInfo = result;
						if (loginInfo.isLoggedIn()) {
							setup();
						} else {
							loadLogin();
						}
					}
				});
	}

	private void setup() {
		gui = new ReservationListGUI();
		reservationDelegate = new ReservationServiceDelegate();
		gui.reservationService = reservationDelegate;
		reservationDelegate.gui = gui;

		gui.init();
		// delegate.listPlayers();
		wireGUIEvents();
	}

	private void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get("calendarPanel").add(loginPanel);
	}

	private void wireGUIEvents() {
		gui.calendar.addHighlightHandler(new HighlightHandler<Date>() {

			public void onHighlight(HighlightEvent<Date> event) {
				Date d = event.getHighlighted();
				gui.gui_eventCalenderHighlighted(d);

			}
		});
		
		// Set the value in the text box when the user selects a date
		gui.calendar.addValueChangeHandler(new ValueChangeHandler<Date>() {

			public void onValueChange(ValueChangeEvent<Date> event) {
				Date date = event.getValue();
				String d = DateTimeFormat.getMediumDateFormat().format(date);
				gui.gui_eventCalendarChosen(d);

			}

		});

		gui.addButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				gui.gui_eventAddButtonClick();

			}
		});	
	}

}
