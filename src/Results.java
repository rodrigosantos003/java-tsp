import java.util.Arrays;

public class Results {
    private long executionTime;
    private int[] bestPath;
    private int distance;
    private int iterations;

    public Results(int matrixSize) {
        this.executionTime = 0;
        this.bestPath = new int[matrixSize];
        this.distance = 0;
        this.iterations = 0;
    }

    public synchronized double getExecutionTime() {
        return executionTime / 1_000_000.0; // Time in milliseconds
    }

    public synchronized void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public synchronized int[] getBestPath() {
        return bestPath;
    }

    public synchronized void setBestPath(int[] bestPath) {
        this.bestPath = bestPath;
    }

    public synchronized int getDistance() {
        return distance;
    }

    public synchronized void setDistance(int distance) {
        this.distance = distance;
    }

    public synchronized int getIterations() {
        return iterations;
    }

    public synchronized void setIterations(int iterations) {
        this.iterations = iterations;
    }

    /**
     * Write the execution time in its respective units
     *
     * @return Execution time with its respective units
     */
    public String writeTime() {
        double value = getExecutionTime();

        return (value < 1000 ? String.format("%.3f", value) + " ms" : String.format("%.3f", (value / 1000)) + " s");
    }

    @Override
    public synchronized String toString() {
        String out = "";

        out += "Melhor caminho: " + Arrays.toString(getBestPath());
        out += "\nDistância: " + getDistance();
        out += "\nTempo: " + writeTime();
        out += "\nIterações: " + getIterations();

        return out;
    }
}
