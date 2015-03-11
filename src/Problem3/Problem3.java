package Problem3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;


public class Problem3 {
	
	private static Vector<Person> trainList = new Vector<Person>();
	private static Vector<Person> testList = new Vector<Person>();
	private static Vector<Person> pairList = new Vector<Person>();
	private static double alpha = 0.001;
	private static int locationSize = 748456;
	private static Vector<Long> L1 = new Vector<Long>();
	private static Vector<Long> L2 = new Vector<Long>();
	
	
	//The main function running the algorithm.
	public static void main(String[] args) throws IOException
	{
		long start = System.currentTimeMillis();
		
		Problem3 pr3 = new Problem3();
		
		//The input file names for each task. Can be changed here.
		String filename5 = "C1.txt";
		String filename6 = "C2.txt";
		
		
		//These are the test cases generated from Brightkite.txt. 
		//Note: checkCorrectness function is not applicable in this case.
		//The correct pair in this case is a pair with the same node ID.
		
		/*filename5 = "testCases3.txt";
		filename6 = "testCases4.txt";*/
		
		
		//Start getting and testing the result.
		
		pr3.Initialize(filename5,filename6);
		pr3.checkCorrectness();
		
		long end = System.currentTimeMillis();
		System.out.println((end-start)/1000/60.0);

	}
	
	//Read the resulting links to test the accuracy.
	public static void ReadLink(String filename, Vector<Long> l) throws IOException
	{
		File file = new File(filename);
    	InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        
        try {
			while((line = br.readLine()) != null)
			{
				String[] nodes= line.split(" ");
				if(nodes.length < 2)
					continue;
				Long pair = Long.parseLong(nodes[0]) << 20 | Long.parseLong(nodes[1]);
				l.add(pair);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        reader.close();
        br.close();
	}	
	
	//Get the number of total locations in the given data.
	private void getLocationSize(String filename1, String filename2) throws IOException {
		HashSet<String> placeName = new HashSet<String>();
		
		File file = new File(filename1);
    	InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        
        try {
			while((line = br.readLine()) != null)
			{
				String[] items= line.split("\t");
				if(items.length < 5)
					continue;
				String temp = items[4];
				if(!placeName.contains(temp))
					placeName.add(temp);
			}
        } catch (Exception e) {
			e.printStackTrace();
		}
        reader.close();
        br.close();
        
        file = new File(filename2);
    	reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
        br = new BufferedReader(reader);
        line = "";
        
        try {
			while((line = br.readLine()) != null)
			{
				String[] items= line.split("\t");
				if(items.length < 5)
					continue;
				String temp = items[4];
				if(!placeName.contains(temp))
					placeName.add(temp);
			}
        } catch (Exception e) {
			e.printStackTrace();
		}
        reader.close();
        br.close();
        
        locationSize = placeName.size();
		
	}
	
	//Read the data into a structure.
	public static void ReadFile(String filename, Vector<Person> per) throws IOException
	{
		File file = new File(filename);
    	InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        Vector<Place> place = new Vector<Place>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        
        long sofar = -1;
        Person data = new Person();
        Place plt = new Place();
        
        try {
			while((line = br.readLine()) != null)
			{
				String[] items= line.split("\t");
				if(items.length < 4)
					continue;
				long temp = Long.parseLong(items[0]);
				
				if(temp != sofar)
				{				
					if(sofar != -1)
					{
						data.setPlaceList(place);	
						data.computeFrequency(alpha,locationSize);
						per.add(data);
					}
					sofar = temp;
					data = new Person();
					place = new Vector<Place>();
					data.setUserID(temp);
				}
				
				Date time = sdf.parse(items[1]);
				plt = new Place();
				plt.setDate(time);
				plt.setLattitude(Double.parseDouble(items[2]));
				plt.setLongitude(Double.parseDouble(items[3]));
				plt.setName(items[4]);
				place.add(plt);
			}
			data.setPlaceList(place);	
			data.computeFrequency(alpha,locationSize);
			per.add(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
        reader.close();
        br.close();
	}
	
	//Calculate the similarity between two person.
	public double CalculateSimilarity(Person test, Person train)
	{
		double finalDistance = 0;

		for(String plt : test.getPlaceNameList())
		{
			if(train.getFrequency().containsKey(plt))
			{
				finalDistance += test.getFrequency().get(plt);
			}
		}
		return finalDistance;
	}
	
	//Generate test cases.
	public void generateTestCases() throws IOException
	{
		String file = "Brightkite.txt";
		Vector<Person> temp = new Vector<Person>();
		
		ReadFile(file,temp);
		
		Vector<Person> seed = new Vector<Person>();
		
		int ter = 0;
		while(ter < 101)
		{
			Person pr = temp.get((int) Math.floor((Math.random()*temp.size())));
			if(!seed.contains(pr) && pr.getPlaceList().size()>20)
			{
				seed.add(pr);
				ter++;
			}
		}
		
		File f1 = new File("testCases3.txt");
		PrintWriter output1 = new PrintWriter(new FileWriter(f1,false));
		File f2 = new File("testCases4.txt");
		PrintWriter output2 = new PrintWriter(new FileWriter(f2,false));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		
		for(Person cur : seed)
		{
			for(Place info : cur.getPlaceList())
			{
				if(info.getDate().getYear() == 108)
				{
					output1.println(cur.getUserID()+"\t"+df.format(info.getDate())+
							"\t"+info.getLattitude()+"\t"+
							info.getLongitude()+"\t"+info.getName());
					output1.flush();
				}
				else if(info.getDate().getYear() == 109)
				{
					output2.println(cur.getUserID()+"\t"+df.format(info.getDate())+
							"\t"+info.getLattitude()+"\t"+
							info.getLongitude()+"\t"+info.getName());
					output2.flush();
				}
			}
		}
		output1.close();
		output2.close();
		
	}
	
	//Getting each pair as result.
	public void Initialize(String filename1, String filename2) throws IOException
	{
		
		//generateTestCases();
		
		getLocationSize(filename1,filename2);
		
		ReadFile(filename1,testList);
		ReadFile(filename2,trainList);	
		
		File f = new File("output3.txt");
		PrintWriter output = new PrintWriter(new FileWriter(f,false));
		
		for(Person p1 : testList)
		{
			double maxPair = -1;
			Person pair = new Person();
			for(Person p2 : trainList)
			{
				double sim = CalculateSimilarity(p1,p2);
				if(maxPair < sim)
				{
					maxPair = sim;
					pair = p2;
				}
			}
			pairList.add(pair);
			output.println(Long.toString(p1.getUserID())+" "+
					Long.toString(pair.getUserID()));
			output.flush();
		}
		
		output.close();
	}
	
	//Check the accuracy.
	public void checkCorrectness() throws IOException
	{
		String filename1 = "T3.txt";
		String filename2 = "output3.txt";
		double correct = 0;
		
		ReadLink(filename1,L1);
		ReadLink(filename2,L2);
		
		for(long key: L2)
		{
			if(L1.contains(key))
				correct++;
		}
		correct /= (double)L2.size();
		System.out.println(correct*100+"%");
	}

}
