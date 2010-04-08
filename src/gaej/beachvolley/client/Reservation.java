package gaej.beachvolley.client;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.i18n.client.DateTimeFormat;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Reservation implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1913760273763769057L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private Date date;
	@Persistent
	private int startHour;
	@Persistent
	private int endHour;
	@Persistent
	private String player;
	@Persistent
	private String playerFriendlyName;
	
	
	public Reservation() {
	
	}
	
	public Reservation(int startHour, int endHour, String dateRepresentation) {
		Date date = DateTimeFormat.getMediumDateFormat().parse(dateRepresentation);
		date.setHours(12);	
		
		this.date = date;
		this.startHour = startHour;
		this.endHour = endHour;
	}
	
	public String getPlayer() {
		return player;
	}
	public void setPlayer(String player) {
		this.player = player;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getDate() {
		return date;
	}
	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}
	public int getStartHour() {
		return startHour;
	}
	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}
	public int getEndHour() {
		return endHour;
	}
	
	@Override
	public String toString() {
		return "Reservation from "+startHour+" to "+endHour+", equalling "
			+(endHour-startHour)+" hours, on "+DateTimeFormat.getMediumDateFormat().format(date);
		
	}
	
	public void setPlayerFriendlyName(String playerFriendlyName) {
		this.playerFriendlyName = playerFriendlyName;
	}
	public String getPlayerFriendlyName() {
		return playerFriendlyName;
	}
	
	public String validate() {
		if ( startHour > endHour ) {
			return "Start hour is later then end hour";
		}
		if ( this.date == null) {
			return "Date of reservation is not set!";
		}
		return null;
		
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
