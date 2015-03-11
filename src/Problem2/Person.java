package Problem2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class Person{
	private long userID;
	private Vector<Place> placeList;
	private HashMap<String,Double> frequency;
	private HashSet<String> placeNameList;

	
	public Person()
	{
		userID = -1;
		setPlaceList(new Vector<Place>());
		setFrequency(new HashMap<String,Double>());
		setPlaceNameList(new HashSet<String>());
	}
		
	public long getUserID()
	{
		return this.userID;
	}
	
	public void setUserID(long uid)
	{
		this.userID = uid;
	}
	
	public Vector<Place> getPlaceList()
	{
		return this.placeList;
	}
	
	public void setPlaceList(Vector<Place> plt)
	{
		this.placeList = plt;
	}
	
	public HashMap<String,Double> getFrequency() {
		return frequency;
	}

	public void setFrequency(HashMap<String,Double> frequency) {
		this.frequency = frequency;
	}
	
	public HashSet<String> getPlaceNameList() {
		return placeNameList;
	}

	public void setPlaceNameList(HashSet<String> placeNameList) {
		this.placeNameList = placeNameList;
	}
	
	//Only use space information to compute the multinomial frequency.
	public void computeFrequency(double alpha, int locationSize)
	{
		for(Place plt : this.placeList)
		{
			String temp = plt.getName();
			if(this.frequency.containsKey(temp))
			{
				this.frequency.put(temp, (double)this.frequency.get(temp)+1);
			}
			else
				this.frequency.put(temp,1.0);
			if(!this.placeNameList.contains(temp))
			{
				this.placeNameList.add(temp);
			}
		}
		double size = this.placeList.size();
		for(String keys : this.frequency.keySet())
		{
			this.frequency.put(keys,(this.frequency.get(keys)+alpha)/(size+alpha*locationSize));
		}
	}
	
	//Time-spatio method to compute multinomial frequency.
	public void computeFrequency2(double alpha, int locationSize)
	{
		for(Place plt : this.placeList)
		{
			StringBuffer tBfr = new StringBuffer(plt.getName());
			StringBuffer add  = new StringBuffer();
			int time = plt.getDate().getHours();
			if(time >= 0 && time < 6)
				add.append("Night");
			if(time >= 6 && time < 12)
				add.append("Morning");
			if(time >= 12 && time < 18)
				add.append("Afternoon");
			if(time >= 18 && time < 24)
				add.append("Evening");
			tBfr.append(add);
			String temp = tBfr.toString();
				
			if(this.frequency.containsKey(temp))
			{
				this.frequency.put(temp, (double)this.frequency.get(temp)+1);
			}
			else
				this.frequency.put(temp,1.0);
			if(!this.placeNameList.contains(temp))
			{
				this.placeNameList.add(temp);
			}
		}
		double size = this.placeList.size();
		for(String keys : this.frequency.keySet())
		{
			this.frequency.put(keys,(this.frequency.get(keys)+alpha)/(size+alpha*locationSize));
		}
	}
	
	//Compute the trajectory distance.
	public double computeDistance(Person per)
	{
		double sum = 0;
		for(Place plt1 : this.placeList)
		{
			double minDist = Double.MAX_VALUE;
			for(Place plt2 : per.placeList)
			{
				double temp = plt1.computePlaceDiff1(plt2);
				if(minDist > temp)
					minDist = temp;
			}
			sum += minDist;
		}
		sum /= this.placeList.size();
		return sum;
	}
	
	public void clearAll()
	{
		userID = -1;
		setPlaceList(new Vector<Place>());
		setFrequency(new HashMap<String,Double>());
		setPlaceNameList(new HashSet<String>());
	}


}
