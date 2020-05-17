/* *****************************************************************************
 *  Name:    Alan Turing
 *  NetID:   aturing
 *  Precept: P00
 *
 *  Description:  Prints 'Hello, World' to the terminal window.
 *                By tradition, this is everyone's first program.
 *                Prof. Brian Kernighan initiated this tradition in 1974.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/*
~/Desktop/percolation> java-algs4 PercolationStats 200 100
mean                    = 0.5929934999999997
stddev                  = 0.00876990421552567
95% confidence interval = [0.5912745987737567, 0.5947124012262428]

 */
public class PercolationStats {
    private int width;
    private int trialTimes;
    private Percolation percolation;
    private double[] trialResults;
    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();
        width = n;
        trialTimes = trials;
        trialResults = new double[trialTimes];
    }

    public void doExperiments() {
        // do experiment n times
        for (int i = 0; i < trialTimes; i++) {
            double curRatio = doOnePercolationTest();
            trialResults[i] = curRatio;
        }
        calcStatistics();
        printStatistics();
    }

    private void calcStatistics() {
        mean = mean();
        stddev = stddev();
        confidenceLo = confidenceLo();
        confidenceHi = confidenceHi();
    }

    private void printStatistics() {
        StdOut.println("mean                    = " + mean);
        StdOut.println("stddev                  = " + stddev);
        StdOut.println("95% confidence interval = " + String
                .format("[%f, %f]", confidenceLo, confidenceHi));
    }

    //do one time simulation and calculate the ratio, put the ratio in trialResults
    private double doOnePercolationTest() {
        percolation = new Percolation(width);
        while (!percolation.percolates()) {
            int row = StdRandom.uniform(width);
            int col = StdRandom.uniform(width);
            percolation.open(row, col);
        }
        double ratio = (double) percolation.numberOfOpenSites() / (1.0 * width * width);
        return ratio;
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(trialResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(trialResults);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        confidenceLo = mean - 1.96 * stddev / Math.sqrt(trialTimes);
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        confidenceHi = mean + 1.96 * stddev / Math.sqrt(trialTimes);
        return confidenceHi;
    }

    public static void main(String[] args) {
        assert (args.length >= 2);
        int arg0 = Integer.parseInt(args[0]);
        int arg1 = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(arg0, arg1);
        percolationStats.doExperiments();
    }
}
