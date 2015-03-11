package Problem1;

public class Pair implements Comparable<Pair>{
	private long node;
	private int degree;
	
	Pair(long n, int d)
	{
		this.node = n;
		this.degree = d;
	}
	

	public long getNode() {
		return node;
	}

	public void setNode(long node) {
		this.node = node;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Pair)) 
			return false;
		return this.node == ((Pair) o).node;
	}


	@Override
	public int compareTo(Pair o) {
		return o.degree-this.degree;
	}
	
}
