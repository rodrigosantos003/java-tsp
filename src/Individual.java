public class Individual {
    private final int[] path;
    private final int distance;

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
