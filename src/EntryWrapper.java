import org.pi4.locutil.trace.TraceEntry;


public class EntryWrapper
{
	public EntryWrapper(TraceEntry t, double dis)
	{
		Entry = t; 
		Distance = dis;
	}
	public TraceEntry Entry;
	public double Distance;
	
	public String toString()
	{
		return String.valueOf(Distance);
	}
	
	public int compareTo(EntryWrapper o2)
	{
		if(o2 == null)
			throw new IllegalArgumentException("The arguments cannot be null.");
		
		return Double.compare(o2.Distance, this.Distance);
	}
}
