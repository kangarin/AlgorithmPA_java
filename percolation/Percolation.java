import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.Arrays;

public class Percolation {

    private boolean[] isBlocked;
    private WeightedQuickUnionUF weightedQuickUnionUF;
    private int width;
    private int sourceID;//n
    private int desID;//n+1

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        width = n;
        sourceID = n;
        desID = n + 1;
        isBlocked = new boolean[width * width + 2];
        weightedQuickUnionUF = new WeightedQuickUnionUF(width * width + 2);
        Arrays.fill(isBlocked, true);
        isBlocked[width * width] = isBlocked[width * width + 1] = false;
    }

    // turn range (1,n) to (0,n-1)
    private int calcID(int row, int col) {
        // if (row <= 0 || row > width || col <= 0 || col > width)
        //     throw new IllegalArgumentException();
        // return (row - 1) * width + (col - 1);
        if (row < 0 || row >= width || col < 0 || col >= width)
            throw new IllegalArgumentException();
        return row * width + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // if (row <= 0 || row > width || col <= 0 || col > width)
        //     throw new IllegalArgumentException();
        if (row < 0 || row >= width || col < 0 || col >= width)
            throw new IllegalArgumentException();
        int gridID = calcID(row, col);
        // if it has been opened, do nothing
        if (isBlocked[gridID] == false) return;
        isBlocked[gridID] = false;
        if (row == 0) {
            //merge source point with it
            weightedQuickUnionUF.union(sourceID, gridID);
        }
        else if (row == width - 1) {
            //merge des point with it
            weightedQuickUnionUF.union(desID, gridID);
        }
        //merge the neighbor point with it
        if (row > 0) {
            int upperNeighbor = calcID(row - 1, col);
            if (!isBlocked[upperNeighbor]) {
                weightedQuickUnionUF.union(upperNeighbor, gridID);
            }
        }
        if (row < width - 1) {
            int lowerNeighbor = calcID(row + 1, col);
            if (!isBlocked[lowerNeighbor]) {
                weightedQuickUnionUF.union(lowerNeighbor, gridID);
            }
        }
        if (col > 0) {
            int leftNeighbor = calcID(row, col - 1);
            if (!isBlocked[leftNeighbor]) {
                weightedQuickUnionUF.union(leftNeighbor, gridID);
            }
        }
        if (col < width - 1) {
            int rightNeighbor = calcID(row, col + 1);
            if (!isBlocked[rightNeighbor]) {
                weightedQuickUnionUF.union(rightNeighbor, gridID);
            }
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        // if (row <= 0 || row > width || col <= 0 || col > width)
        //     throw new IllegalArgumentException();
        if (row < 0 || row >= width || col < 0 || col >= width)
            throw new IllegalArgumentException();
        int gridID = calcID(row, col);
        return !isBlocked[gridID];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || row >= width || col < 0 || col >= width)
            throw new IllegalArgumentException();
        int gridID = calcID(row, col);
        boolean isOpen = isOpen(row, col);
        if (!isOpen) {
            return false;
        }
        else {
            if (weightedQuickUnionUF.find(sourceID) == weightedQuickUnionUF.find(gridID))
                return true;
            return false;
        }
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int cnt = 0;
        for (int i = 0; i < width * width; i++) {
            if (!isBlocked[i]) cnt++;
        }
        return cnt;
    }

    // does the system percolate?
    public boolean percolates() {
        return weightedQuickUnionUF.find(sourceID) == weightedQuickUnionUF.find(desID);
    }

    // test client (optional)
    public static void main(String[] args) {
    }
}
