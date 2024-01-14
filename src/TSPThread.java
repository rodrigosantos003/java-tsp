public interface TSPThread {
    int getThreadIndex();

    int[] getBestPath();

    int getBestDistance();

    long getEndTime();

    long getStartTime();

    int getIterations();
}
