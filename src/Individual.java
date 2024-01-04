public class Individual {
    private int[] path;
    private int distance;

    public int getDistance() {
        return distance;
    }

    public int[] getPath() {
        return path;
    }

    public Individual(int[] path, int[][] matrix) {
        this.path = path;
        this.distance = Utilities.calculateDistance(path, matrix);
    }
}
