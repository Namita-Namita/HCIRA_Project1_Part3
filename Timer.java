public class Timer {
    private long startTime;

    public Timer() {
        startTime = System.currentTimeMillis();
    }

    // to calculate the time takenn
    public double getTime() {
        long endTime = System.currentTimeMillis();
        double spentTime = (double) (endTime - startTime) / (1000);
        return spentTime;
    }
}