package Problem1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Vector;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Problem1 {
	
	private static UndirectedGraph<String, DefaultEdge> G1 = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
	private static UndirectedGraph<String, DefaultEdge> G2 = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
	private static Vector<Long> Link = new Vector<Long>();
	private static Vector<HashSet<Long>> pairSet = new Vector<HashSet<Long>>();
	private static PriorityQueue<Pair> onePair = new PriorityQueue<Pair>();
	private static int threshold = 1;
	private static Vector<Long> L1 = new Vector<Long>();
	private static Vector<Long> L2 = new Vector<Long>();
	
	
	//The main function running the algorithm.
	public static void main(String[] args) throws IOException
	{
		long start = System.currentTimeMillis();
		
		Problem1 pr1 = new Problem1();
		
		//The input file names for each task. Can be changed here.
		String filename1 = "G1.txt";
		String filename2 = "G2.txt";
		String linkfile = "L.txt";		
		
		//Start getting and testing the result.
		
		pr1.Initialize(filename1,filename2,linkfile);
		pr1.checkCorrectness();
		
		long end = System.currentTimeMillis();
		System.out.println((end-start)/1000/60.0);

	}
	
	
	//Read the data into a structure.
	public static void ReadFile(String filename,UndirectedGraph<String, DefaultEdge> g) throws IOException
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
				if(!g.containsVertex(nodes[0]))
       		 	{
       			 	g.addVertex(nodes[0]);
       		 	}
       		 	if(!g.containsVertex(nodes[1]))
       		 	{
       		 		g.addVertex(nodes[1]);
       		 	}
       		 	if(!g.containsEdge(nodes[0], nodes[1]) && !(nodes[0].equals(nodes[1])))
       		 	{
       		 		g.addEdge(nodes[0], nodes[1]);
       		 	}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        reader.close();
        br.close();
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
	
	//Getting each pair as result.
	public void Initialize(String filename1, String filename2, String filename3) throws IOException
	{
		ReadFile(filename1,G1);
		ReadFile(filename2,G2);	
		ReadLink(filename3,Link);
		
		File f = new File("output1.txt");
		PrintWriter output = new PrintWriter(new FileWriter(f,false));
		
		for(int i=0;i<10;i++)
		{
			HashSet<Long> set = new HashSet<Long>();
			pairSet.add(set);
		}
		
		
		for(long ll : Link)
		{
			String node1 = Long.toString(ll >> 20);
			String node2 = Long.toString(ll & 1048575);
			if(G1.containsVertex(node1) && G2.containsVertex(node2))
			{
				for(DefaultEdge neighbor1 : G1.edgesOf(node1))
				{
					for(DefaultEdge neighbor2 : G2.edgesOf(node2))
					{
						String s1 = G1.getEdgeTarget(neighbor1);
						if(s1.equals(node1))
							s1 = G1.getEdgeSource(neighbor1);
						String s2 = G2.getEdgeTarget(neighbor2);
						if(s2.equals(node2))
							s2 = G2.getEdgeSource(neighbor2);
						int degree = G1.degreeOf(s1)+G2.degreeOf(s2);
						
						long temp = Long.parseLong(s1) << 20 | Long.parseLong(s2);

						boolean exist = false;
						for(int i=0;i<5;i++)
						{
							if(pairSet.get(i).contains(temp))
							{
								pairSet.get(i).remove(temp);
								pairSet.get(i+1).add(temp);
								if(i == 0)
									onePair.remove(new Pair(temp,0));
								exist = true;
								break;
							}
						}
						if(exist == false)
						{
							pairSet.get(0).add(temp);
							onePair.add(new Pair(temp,degree));
						}
					}
				}
			}
			else
				Link.remove(ll);
		}
		
		for(long nodes : Link)
		{
			String node1 = Long.toString(nodes >> 20);
			String node2 = Long.toString(nodes & 1048575);
			G1.removeVertex(node1);
			G2.removeVertex(node2);
		}
		
		Vector<Long> delete = new Vector<Long>();
		Vector<Integer> number = new Vector<Integer>();
		
		for(long rcd : Link)
		{
			long start = (rcd >> 20);
			long end = (rcd & 1048575);
			for(int i=0;i<10;i++)
			{
				for(long key : pairSet.get(i))
				{
					long rm1 = (key >> 20);
					long rm2 = (key & 1048575);
					if(rm1 == start || rm2 == end)
					{
						delete.add(key);
						number.add(i);
					}
				}	
			}
		}
		for(int i=0;i<delete.size();i++)
		{
			pairSet.get(number.get(i)).remove(delete.get(i));
			if(number.get(i) == 0)
			{
				onePair.remove(new Pair(delete.get(i),0));
			}
		}
		
		boolean flag = true;
		while(flag)
		{		
			long record = 0;
			flag = false;
			int max = 0;
			
			for(int i=9;i>=threshold-1;i--)
			{
				if(pairSet.get(i).size()!=0)
				{
					max = i;
					break;
				}
			}
			
		
			if(max == 0)
			{
				Pair nodeTemp= onePair.peek();
				record = nodeTemp.getNode();
			}
			else
			{
				int	ram = (int)(Math.random()*pairSet.get(max).size());
				
				int iter = 0;		
				for(long key : pairSet.get(max))
				{
					if(iter == ram)
					{
						record = key;
						break;
					}
					iter++;
				}
			}
					
			long start = (record >> 20);
			long end = (record & 1048575);
			
			String node1 = Long.toString(start);
			String node2 = Long.toString(end);
			for(DefaultEdge neighbor1 : G1.edgesOf(node1))
			{
				for(DefaultEdge neighbor2 : G2.edgesOf(node2))
				{
					String s1 = G1.getEdgeTarget(neighbor1);
					if(s1.equals(node1))
						s1 = G1.getEdgeSource(neighbor1);
					String s2 = G2.getEdgeTarget(neighbor2);
					if(s2.equals(node2))
						s2 = G2.getEdgeSource(neighbor2);
					int degree = G1.degreeOf(s1)+G2.degreeOf(s2);
					
					long temp = Long.parseLong(s1) << 20 | Long.parseLong(s2);
					boolean exist = false;
					for(int i=0;i<10;i++)
					{
						if(pairSet.get(i).contains(temp))
						{
							pairSet.get(i).remove(temp);
							pairSet.get(i+1).add(temp);
							if(i == 0)
								onePair.remove(new Pair(temp,0));
							exist = true;
							break;
						}
					}
					if(exist == false)
					{
						pairSet.get(0).add(temp);
						onePair.add(new Pair(temp,degree));
					}
				}
			}
			
			delete = new Vector<Long>();
			number = new Vector<Integer>();
		
			for(int i=0;i<10;i++)
			{
				for(long key : pairSet.get(i))
				{
					long rm1 = (key >> 20);
					long rm2 = (key & 1048575);
					if(rm1 == start || rm2 == end)
					{
						delete.add(key);
						number.add(i);
					}
				}	
			}
			for(int i=0;i<delete.size();i++)
			{
				pairSet.get(number.get(i)).remove(delete.get(i));
				if(number.get(i) == 0)
				{
					onePair.remove(new Pair(delete.get(i),0));
				}
			}
			
			G1.removeVertex(node1);
			G2.removeVertex(node2);
			
			output.println(node1+" "+node2);
			output.flush();	
			
			for(int i=threshold-1;i<10;i++)
			{
				if(pairSet.get(i).size()!=0)
				{
					flag = true;
					break;
				}
			}
			
		}
		
		output.close();
	}

	//Check the accuracy.
	public void checkCorrectness() throws IOException
	{
		String filename1 = "T1.txt";
		String filename2 = "output1.txt";
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
