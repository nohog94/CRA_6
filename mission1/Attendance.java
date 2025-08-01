import java.io.*;
import java.util.*;


public class Attendance {

    static Map<String, Integer> playerIds = new HashMap<>();
    static int totalPlayers = 0;
    static final int MAX_PLAYERS = 100;
    static final int MONDAY = 0;
    static final int TUESDAY = 1;
    static final int WEDNESDAY = 2;
    static final int THURSDAY = 3;
    static final int FRIDAY = 4;
    static final int SATURDAY = 5;
    static final int SUNDAY = 6;
    static final int MAX_DAY_OF_WEEK = 7;
    static final int BONUS_COUNT = 10;
    static int[][] playerAttDay = new int[MAX_PLAYERS][MAX_DAY_OF_WEEK];
    static int[] points = new int[MAX_PLAYERS];
    static String[] grade = new String[MAX_PLAYERS];
    static String[] playerNames = new String[MAX_PLAYERS];

    public static void sumPoints(int playerId, String day) {
        int addPoint = 0;
        int dayIndex = 0;
        switch (day) {
            case "monday":
                dayIndex = MONDAY;
                addPoint++;
                break;
            case "tuesday":
                dayIndex = TUESDAY;
                addPoint++;
                break;
            case "wednesday":
                dayIndex = WEDNESDAY;
                addPoint += 3;
                break;
            case "thursday":
                dayIndex = THURSDAY;
                addPoint++;
                break;
            case "friday":
                dayIndex = FRIDAY;
                addPoint++;
                break;
            case "saturday":
                dayIndex = SATURDAY;
                addPoint += 2;
                break;
            case "sunday":
                dayIndex = SUNDAY;
                addPoint += 2;
                break;
            default:
                break;
        }

        playerAttDay[playerId][dayIndex]++;
        points[playerId] += addPoint;
    }

    private static void showRemovedPlayer() {
        System.out.println();
        System.out.println("Removed player");
        System.out.println("==============");
        for (int playerId = 1; playerId <= totalPlayers; playerId++) {
            if (isRemovedPlayer(playerId)) {
                System.out.println(playerNames[playerId]);
            }
        }
    }

    private static void showPlayerPointsAndGrade(int playerId) {
        System.out.print("NAME : " + playerNames[playerId] + ", ");
        System.out.print("POINT : " + points[playerId] + ", ");
        System.out.println("GRADE : " + grade[playerId]);
    }

    private static boolean isRemovedPlayer(int i) {
        return grade[i].equals("NORMAL")
                && playerAttDay[i][WEDNESDAY] == 0
                && playerAttDay[i][SATURDAY] == 0
                && playerAttDay[i][SUNDAY] == 0;
    }

    private static boolean isExceedMAXPlayers(int playerId) {
        return playerId == -1;
    }

    private static void setGrade(int playerId) {
        if (points[playerId] >= 50) {
            grade[playerId] = "GOLD";
        } else if (points[playerId] >= 30) {
            grade[playerId] = "SILVER";
        } else {
            grade[playerId] = "NORMAL";
        }
    }

    private static void calBonusPoints(int playerId) {
        if (playerAttDay[playerId][WEDNESDAY] >= BONUS_COUNT
                || playerAttDay[playerId][SATURDAY] + playerAttDay[playerId][SUNDAY] >= BONUS_COUNT) {
            points[playerId] += 10;
        }
    }

    private static int getPlayerId(String player) {
        if (!playerIds.containsKey(player)) {
            playerIds.put(player, ++totalPlayers);
            // 최대 인원 100명에 대한 방어코드
            if (totalPlayers >= MAX_PLAYERS) return -1;
            playerNames[totalPlayers] = player;
        }
        return playerIds.get(player);
    }

    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/attendance_weekday_500.txt"))) {
            for (int i = 0; i < 500; i++) {
                String line = br.readLine();
                if (line == null) break;
                String[] parts = line.split("\\s+");

                if (parts.length == 2) {
                    String player = parts[0];
                    int playerId = getPlayerId(player);
                    if (isExceedMAXPlayers(playerId)) break;

                    String day = parts[1];
                    sumPoints(playerId, day);
                }
            }

            for (int playerId = 1; playerId <= totalPlayers; playerId++) {
                calBonusPoints(playerId);
                setGrade(playerId);
                showPlayerPointsAndGrade(playerId);
            }
            showRemovedPlayer();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
