package root.etc;

import java.util.List;

import core.base.BaseEntity;

public class EventResponse extends BaseEntity
{
	private static final long serialVersionUID = 6184802267550785087L;
	private List<?> events;

	public EventResponse(List<?> allDateObjects) {
		setEvents(allDateObjects);
	}

	public List<?> getEvents() {
		return events;
	}

	public void setEvents(List<?> events) {
		this.events = events;
	}
}
