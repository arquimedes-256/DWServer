package root.etc;

public enum ReasonCode implements Notificable
{
	CALENDAR_COMPARE;

	@Override
	public String getNotificationText() 
	{
			return this.name();
	}
}
