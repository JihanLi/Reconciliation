package Problem3;

import java.util.Date;

public class Place {
	
	private Date date;
	private Double lattitude;
	private Double longitude;
	private String name;
	
	public Place()
	{
		date = new Date();
		lattitude = null;
		longitude = null;
		name = new String();
	}
	
	public Place(Place A)
	{
		date = A.date;
		lattitude = A.lattitude;
		longitude = A.longitude;
		name = A.name;
	}
	
	public Date getDate()
	{
		return this.date;
	}
	
	public void setDate(Date ted)
	{
		this.date = ted;
	}
	
	public Double getLattitude()
	{
		return this.lattitude;
	}
	
	public void setLattitude(Double lat)
	{
		this.lattitude = lat;
	}
	
	public Double getLongitude()
	{
		return this.longitude;
	}
	
	public void setLongitude(Double log)
	{
		this.longitude = log;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	//Compute Haversine distance.
	public double computePlaceDiff1(Place plt)
	{
		if(this.name.equals(plt.name))
			return 0;
		else
			return 2*6371.393*Math.asin(Math.sqrt(Math.pow(Math.sin((double)(plt.getLattitude()-
				this.lattitude)/2),2)+Math.cos(plt.getLattitude())*Math.cos(this.lattitude)*
				Math.pow(Math.sin((double)(plt.getLongitude()-this.longitude)/2),2)));
	}
	
	//Compute Euclidean distance.
	public double computePlaceDiff2(Place plt)
	{
		if(this.name.equals(plt.name))
			return 0;
		else
			return Math.sqrt((plt.getLattitude()-this.lattitude)*
				(plt.getLattitude()-this.lattitude)+
				(plt.getLongitude()-this.longitude)*
				(plt.getLongitude()-this.longitude));
	}
	
	//Compute Time-spatio distance.
	public double computeSpaTemDiff(Place plt)
	{
		double tau = 100;
		return this.computePlaceDiff1(plt)*Math.exp(Math.abs
				(plt.getDate().getTime()-this.date.getTime())/tau);
	}
	
	public void clearAll()
	{
		date = null;
		lattitude = null;
		longitude = null;
		name = new String();
	}

}
