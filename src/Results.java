public class Results {
    private String executionTime;
    private int[] bestPath;
    private int distance;
    private int iterations;

    public Results(int matrixSize) {
        this.executionTime = "0";
        this.bestPath = new int[matrixSize];
        this.distance = 0;
        this.iterations = 0;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public int[] getBestPath() {
        return bestPath;
    }

    public void setBestPath(int[] bestPath) {
        this.bestPath = bestPath;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
}
