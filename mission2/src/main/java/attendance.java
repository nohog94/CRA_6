import java.io.*;
import java.util.*;

interface Grade {
    String getName();
}

class GradeFactory {
    public Grade makeGrade(String gradeName){
        if (gradeName.equals("Gold")) {
            return new GoldGrade();
        } else if (gradeName.equals("Silver")) {
            return new SilverGrade();
        } else {
            return new NormalGrade();
        }
    }
}

class GoldGrade implements Grade {
    static int minPoints = 50;
    static String name = "Gold";
    @Override
    public String getName() {
        return name;
    }
}

class SilverGrade implements Grade {
    static int minPoints = 30;
    static String name = "Silver";
    @Override
    public String getName() {
        return name;
    }
}

class NormalGrade implements Grade {
    static int minPoints = 0;
    static String name = "Normal";
    @Override
    public String getName() {
        return name;
    }
}

class player {
    static final int MAX_DAY_OF_WEEK = 7;
    private String name;
    private Grade grade;
    private int point;
    private int[] attDay = new int[MAX_DAY_OF_WEEK];


    public player setName(String name) {
        this.name = name;
        return this;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public Grade getGrade() {
        return grade;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int[] getAttDay() {
        return attDay;
    }

    public void setAttDay(int[] attDay) {
        this.attDay = attDay;
    }
}

class Attendance {

    static final int MONDAY = 0;
    static final int TUESDAY = 1;
    static final int WEDNESDAY = 2;
    static final int THURSDAY = 3;
    static final int FRIDAY = 4;
    static final int SATURDAY = 5;
    static final int SUNDAY = 6;
    static final int BONUS_COUNT = 10;
    static List<player> players = new ArrayList<>();
    static GradeFactory gradeFactory = new GradeFactory();

    public void readAttendInfo(BufferedReader br) throws IOException {
        for (int i = 0; i < 500; i++) {
            String line = br.readLine();
            if (line == null) break;
            String[] parts = line.split("\\s+");
            if (parts.length == 2) {
                String name = parts[0];
                String day = parts[1];
                readAttendInfoByLine(name, day);
            }
        }
    }

    public void readAttendInfoByLine(String name, String day) {
        player p = getPlayer(name);
        int dayIndex = 0;
        switch (day) {
            case "monday":
                dayIndex = MONDAY;
                break;
            case "tuesday":
                dayIndex = TUESDAY;
                break;
            case "wednesday":
                dayIndex = WEDNESDAY;
                break;
            case "thursday":
                dayIndex = THURSDAY;
                break;
            case "friday":
                dayIndex = FRIDAY;
                break;
            case "saturday":
                dayIndex = SATURDAY;
                break;
            case "sunday":
                dayIndex = SUNDAY;
                break;
            default:
                break;
        }
        int[] attDay = p.getAttDay();
        attDay[dayIndex]++;
        p.setAttDay(attDay);
    }

    public player getPlayer(String name) {
        for (player p : players) {
            if (p.getName().equals(name)) return p;
        }
        player p = new player()
                .setName(name);
        p.setGrade(gradeFactory.makeGrade("Normal"));
        players.add(p);
        return p;
    }

    public void calPoints() {
        for (player p : players) {
            int attDay[] = p.getAttDay();
            for (int day=MONDAY; day<=SUNDAY; day++) {
                if (day == WEDNESDAY) {
                    p.setPoint(p.getPoint() + 3 * attDay[day]);
                } else if (day == SATURDAY || day == SUNDAY) {
                    p.setPoint(p.getPoint() + 2 * attDay[day]);
                } else {
                    p.setPoint(p.getPoint() + attDay[day]);
                }
            }
            if (isBonusAvailable(attDay)) {
                p.setPoint(p.getPoint() + 10);
            }
        }
    }

    public boolean isBonusAvailable(int[] attDay) {
        return attDay[WEDNESDAY] >= BONUS_COUNT
                || attDay[SATURDAY] + attDay[SUNDAY] >= BONUS_COUNT;
    }

    public void setGrade() {
        for (player p: players) {
            if (p.getPoint() >= GoldGrade.minPoints) {
                p.setGrade(gradeFactory.makeGrade("Gold"));
            } else if (p.getPoint() >= SilverGrade.minPoints) {
                p.setGrade(gradeFactory.makeGrade("Silver"));
            }
        }
    }

    public void showPlayerPointsAndGrade() {
        for (player p:players) {
            System.out.print("NAME : " + p.getName() + ", ");
            System.out.print("POINT : " + p.getPoint() + ", ");
            System.out.println("GRADE : " + p.getGrade().getName());
        }
    }

    public void showRemovedPlayer() {
        System.out.println();
        System.out.println("Removed player");
        System.out.println("==============");
        for (player p:players) {
            if (isRemovedPlayer(p)) {
                System.out.println(p.getName());
            }
        }
    }

    public boolean isRemovedPlayer(player p) {
        return p.getGrade().getName().equals("Normal")
                && p.getAttDay()[WEDNESDAY] == 0
                && p.getAttDay()[SATURDAY] == 0
                && p.getAttDay()[SUNDAY] == 0;
    }

    public static void main(String[] args) {
        Attendance att = new Attendance();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/attendance_weekday_500.txt"))) {
            att.readAttendInfo(br);
            att.calPoints();
            att.setGrade();
            att.showPlayerPointsAndGrade();
            att.showRemovedPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
