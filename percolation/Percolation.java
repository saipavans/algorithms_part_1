import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.lang.IllegalArgumentException;

public class Percolation {
	
	private static final int BLOCK_VALUE = 1;
	private static final int OPEN_VALUE = 0;
	
	private int[][] grid = null;
	private int openSitesCount = 0;
	private WeightedQuickUnionUF weightedTreeList = null;
	private int virtualTopNodeIndex = 0;
	private int virtualBottomNodeIndex = 0;

	public Percolation(int n) {
		// Creates n by n grid with all sites blocked
		if (n <= 0) {
			throw new IllegalArgumentException();
		}
		grid = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				grid[i][j] = BLOCK_VALUE;
			}
		}
		modelGridDetails();
		connectVirtualNodes();
	}
	
	private void modelGridDetails() {
		int linearizedGridLength = (int) Math.pow(grid.length, 2);
		weightedTreeList = new WeightedQuickUnionUF(linearizedGridLength + 2); // Includes virtual top and bottom node
		virtualBottomNodeIndex = linearizedGridLength + 1;
	}
	
	private  int gridToTreeListIndexing(int row, int col) {
		// returns the treeList[index] for grid[row][col]
		return ((row * grid.length) + col + 1);
	}
	
	private void connectVirtualNodes() {
		// Connects the virtual top and bottom nodes to the nodes in the top and the bottom most rows of the grid.
		int[] boundaryRows = { 0, grid.length -1};
		
		int virtualNode = virtualTopNodeIndex;
		
		for (int col = 0; col < grid.length; col++) {
			int linearizedIndex = gridToTreeListIndexing(boundaryRows[0], col);
			weightedTreeList.union(virtualNode, linearizedIndex);
		}
		
		virtualNode = virtualBottomNodeIndex;
		for (int col = 0; col < grid.length; col++) { 
			int linearizedIndex = gridToTreeListIndexing(boundaryRows[1], col);
			weightedTreeList.union(virtualNode, linearizedIndex);
		}
			
	}
	
	public void open(int row, int col) {
		// Open the site at row, col if not open already
		// NOTE: Here row and col start from 1 not 0 (According to Percolation grader requirement)
		boolean isClosed = true;
		
		if (row <= 0 || row > grid.length){
			throw new IllegalArgumentException();
		} else {
			row -= 1;
		}
		
		if (col <= 0 || col > grid.length){
			throw new IllegalArgumentException();
		} else {
			col -= 1;
		}
		
		grid[row][col] = (isClosed = (grid[row][col] == BLOCK_VALUE)) ? OPEN_VALUE : BLOCK_VALUE;
		/* if(row == 0 || row == grid.length-1){
			return; // top and bottom nodes
		} */
		if (isClosed) {
			int linearizedIndex = gridToTreeListIndexing(row, col);
			// connect this site to its adjacent sites
			if (row-1 >= 0 && isOpen((row-1) + 1, col + 1)) { // adding 1 to row-1 and col as isOpen method decrements 1 before processing
				weightedTreeList.union(linearizedIndex, gridToTreeListIndexing(row-1, col));
			}
			if (row+1 <= grid.length-1 && isOpen((row+1) + 1, col + 1)) {
				weightedTreeList.union(linearizedIndex, gridToTreeListIndexing(row+1, col));
			}
			if (col-1 >= 0 && isOpen(row + 1, (col-1) + 1)) {
				weightedTreeList.union(linearizedIndex, gridToTreeListIndexing(row, col-1));
			}
			if (col+1 <= grid.length-1 && isOpen(row + 1, (col+1) + 1)) {
				weightedTreeList.union(linearizedIndex, gridToTreeListIndexing(row, col+1));
			}
			openSitesCount++;
		}
	}
	
	
	public boolean  isOpen(int row, int col) {
		// returns true if the site at row, col is open
		if (row <= 0 || row > grid.length){
			throw new IllegalArgumentException();
		} else {
			row -= 1;
		}
		
		if (col <= 0 || col > grid.length){
			throw new IllegalArgumentException();
		} else {
			col -= 1;
		}
		return grid[row][col] == OPEN_VALUE ? true : false;
	}
	
	public int numberOfOpenSites() {
		return openSitesCount;
	}
	
	public boolean isFull(int row, int col) {
		boolean isFull = false;
		if (row <= 0 || row > grid.length){
			throw new IllegalArgumentException();
		} else {
			row -= 1;
		}
		
		if (col <= 0 || col > grid.length){
			throw new IllegalArgumentException();
		} else {
			col -= 1;
		}
		
		if (grid[row][col] == OPEN_VALUE) {
			int linearizedIndex = gridToTreeListIndexing(row, col);
			isFull =  weightedTreeList.connected(virtualTopNodeIndex, linearizedIndex);
		}
		
		return isFull;
	}
	
	public boolean percolates() {
		return weightedTreeList.connected(virtualTopNodeIndex, virtualBottomNodeIndex);
	}
		

}
