package MungkinRpg.dungeon;

public class DungeonProgress {
    private boolean[] dungeonCleared;
    private int[] bestClearTime;
    private int totalRuns;

    public DungeonProgress() {
        this.dungeonCleared = new boolean[3];
        this.bestClearTime = new int[3];
        this.totalRuns = 0;
    }

    public void markCleared(int dungeonId, int clearTimeSeconds) {
        if (dungeonId < 1 || dungeonId > 3) return;
        int idx = dungeonId - 1;
        dungeonCleared[idx] = true;

        if (bestClearTime[idx] == 0 || clearTimeSeconds < bestClearTime[idx]) {
            bestClearTime[idx] = clearTimeSeconds;
        }
        totalRuns++;
    }

    public boolean isUnlocked(int dungeonId) {
        if (dungeonId == 1) return true;
        return dungeonCleared[dungeonId - 2];
    }

    public boolean isCleared(int dungeonId) {
        return dungeonCleared[dungeonId - 1];
    }

    public int getBestTime(int dungeonId) {
        return bestClearTime[dungeonId - 1];
    }

    public int getTotalRuns() { return totalRuns; }
}