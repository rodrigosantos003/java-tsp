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
        return executionTime / 1_000_000.0; //Tempo convertido em milisegundos
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

    private String writeTime(){
        String time = "\nTempo: ";
        double value = getExecutionTime();

        return time + (value < 1000 ? value + " ms" : String.format("%.2f", (value / 1000)) + " s");
    }

    @Override
    public synchronized String toString() {
        String out = "";

        out += "Melhor caminho: " + Arrays.toString(getBestPath());
        out += "\nDistância: " + getDistance();
        out += writeTime();
        out += "\nIterações: " + getIterations();

        return out;
    }
}
