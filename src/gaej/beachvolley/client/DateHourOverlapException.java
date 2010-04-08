package gaej.beachvolley.client;

import java.io.Serializable;


public class DateHourOverlapException extends Exception implements Serializable {
	private static final long serialVersionUID = 4769186539406535452L;
	private String message = "";
	
	public DateHourOverlapException() {
		
	}
	
	public DateHourOverlapException(String m) {
		this.message = m;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}

}
