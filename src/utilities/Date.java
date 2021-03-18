package utilities;

public class Date {
	
	private DayOfTheWeek day;
	private int hour;
	private int minute;
	
	public Date() {
		this.day = DayOfTheWeek.MONDAY;
		this.hour = 12;
		this.minute = 0;	
	}
	
	public Date(String str) {
		//PARSE STRING HERE
	}
	
	public Date(DayOfTheWeek day, int hour, int minute) {
		this.day = day;
		this.hour = hour;
		this.minute = minute;	
	}

	public Date(Date date) {
		this.day = date.getDay();
		this.hour = date.getHour();
		this.minute = date.getMinute();
	}
	
	public DayOfTheWeek getDay() {
		return day;
	}

	public void setDay(DayOfTheWeek day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		if(hour < 0) {
			throw new IllegalArgumentException("Hour cannot be less than 0");
		}
		if(hour > 23) {
			throw new IllegalArgumentException("Hour cannot be more than 23");
		}
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		if(minute < 0) {
			throw new IllegalArgumentException("Minute cannot be less than 0");
		}
		if(minute > 59) {
			throw new IllegalArgumentException("Minute cannot be more than 59");
		}
		this.minute = minute;
	}

	@Override
	public String toString() {
		String hourString = String.valueOf(hour);
		if(hour < 10) {
			hourString = "0" + hourString;
		}
		String minuteString = String.valueOf(minute);
		if(minute < 10) {
			minuteString = "0" + minuteString;
		}
		return day.name() + " " + hour + ":" + minute;
	}
}
