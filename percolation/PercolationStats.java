import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
	
	private final int n;
	private final double[] openSitesFractionForPercolation;
	private static final double CONFIDENCE_FACTOR = 1.96;

	
	public PercolationStats(int n, int trials) {
		// perform trials independent experiments on an n-by-n grid
		
		this.n = n;
		openSitesFractionForPercolation = new double[trials];
		
		for (int i = 0; i < trials; i++) {
			Percolation grid = new Percolation(n);
			openSitesFractionForPercolation[i] = computeOpenSitesFraction(grid);
		}

		
		
	}
	
	private double computeOpenSitesFraction(Percolation gridSystem) {
		
		while (!gridSystem.percolates()) {

			int randSiteCol = StdRandom.uniform(n) + 1; // To be similar to the grader which has rows and cols starting from 1 instead of 0
			int randSiteRow = StdRandom.uniform(n) + 1;

			
			if (gridSystem.isOpen(randSiteRow, randSiteCol)) {

				continue;
			}
			
			gridSystem.open(randSiteRow, randSiteCol);
		}
		
		return gridSystem.numberOfOpenSites()/Math.pow(n, 2);
	}
	
	public double mean() {
		// sample mean of percolation threshold
		/*
		double sum = 0.0;
		int trials = 0;
		for(int i = 0; i < (trials = openSitesFractionForPercolation.length); i++){
			sum += openSitesFractionForPercolation[i];
		}
		return sum/trials;
		*/
		return StdStats.mean(openSitesFractionForPercolation);
	}
	
	public double stddev() {
		// sample standard deviation of percolation threshold
		/*
		double mean = mean();
		double sum = 0.0;
		int trials = 0;
		for(int i = 0; i < (trials = openSitesFractionForPercolation.length); i++){
			sum += Math.pow((openSitesFractionForPercolation[i] - mean), 2);
		}
		return Math.pow(sum/(trials-1), 0.5);
		*/
		return StdStats.stddev(openSitesFractionForPercolation);
		
	}
	
	public double confidenceLo() {
		// low  endpoint of 95% confidence interval
		return mean() - (CONFIDENCE_FACTOR * stddev()/ Math.sqrt(openSitesFractionForPercolation.length));
	}
	
	public double confidenceHi() {
		// high endpoint of 95% confidence interval
		return mean() + (CONFIDENCE_FACTOR * stddev()/ Math.sqrt(openSitesFractionForPercolation.length));
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PercolationStats percolationStatsCollection = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		System.out.println("Mean is: " + percolationStatsCollection.mean());
		System.out.println("stddev is: " + percolationStatsCollection.stddev());
		System.out.println("95% Confidence Interval is: [" + percolationStatsCollection.confidenceLo() + ", " + percolationStatsCollection.confidenceHi() + "]");
	}

}
