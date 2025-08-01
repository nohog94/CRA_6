import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AttendanceTest {
    private Attendance att;
    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setup() {
        // Arrange
        att = new Attendance();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testReadAttendInfoByLine_MON() {
        att.readAttendInfoByLine("test", "monday");
        for (player p: att.players) {
            if (p.getName().equals("test")) {
                int[] attDay = p.getAttDay();
                assertEquals(attDay[0], 1);
            }
        }
    }

    @Test
    void testReadAttendInfoByLine_TUE() {
        att.readAttendInfoByLine("test", "tuesday");
        for (player p: att.players) {
            if (p.getName().equals("test")) {
                int[] attDay = p.getAttDay();
                assertEquals(attDay[1], 1);
            }
        }
    }

    @Test
    void testReadAttendInfoByLine_WED() {
        att.readAttendInfoByLine("test", "wednesday");
        for (player p: att.players) {
            if (p.getName().equals("test")) {
                int[] attDay = p.getAttDay();
                assertEquals(attDay[2], 1);
            }
        }
    }

    @Test
    void testReadAttendInfoByLine_TUR() {
        att.readAttendInfoByLine("test", "thursday");
        for (player p: att.players) {
            if (p.getName().equals("test")) {
                int[] attDay = p.getAttDay();
                assertEquals(attDay[3], 1);
            }
        }
    }

    @Test
    void testReadAttendInfoByLine_FRI() {
        att.readAttendInfoByLine("test", "friday");
        for (player p: att.players) {
            if (p.getName().equals("test")) {
                int[] attDay = p.getAttDay();
                assertEquals(attDay[4], 1);
            }
        }
    }

    @Test
    void testReadAttendInfoByLine_SAT() {
        att.readAttendInfoByLine("test", "saturday");
        for (player p : att.players) {
            if (p.getName().equals("test")) {
                int[] attDay = p.getAttDay();
                assertEquals(attDay[5], 1);
            }
        }
    }

    @Test
    void testReadAttendInfoByLine_SUN() {
        att.readAttendInfoByLine("test", "sunday");
        for (player p : att.players) {
            if (p.getName().equals("test")) {
                int[] attDay = p.getAttDay();
                assertEquals(attDay[6], 1);
            }
        }
    }

    @Test
    void getPlayer() {
        att.getPlayer("test2");
        for (player p: att.players) {
            if (p.getName().equals("test2")) {
                assertEquals(p.getName(), "test2");
            }
        }
    }

    @Test
    void calPoints_no_bonus() {
        att.readAttendInfoByLine("test3", "wednesday");
        att.readAttendInfoByLine("test3", "saturday");
        att.readAttendInfoByLine("test3", "sunday");
        att.readAttendInfoByLine("test3", "monday");
        att.calPoints();
        for (player p: att.players) {
            if (p.getName().equals("test3")) {
                assertEquals(p.getPoint(), 8);
            }
        }
    }

    @Test
    void calPoints_with_bonus_WED() {
        for (int i=0; i<10; i++) {
            att.readAttendInfoByLine("test4", "wednesday");
        }
        att.calPoints();
        for (player p: att.players) {
            if (p.getName().equals("test4")) {
                assertEquals(p.getPoint(), 40);
            }
        }
    }

    @Test
    void calPoints_with_bonus_SAT_SUN() {
        for (int i=0; i<10; i++) {
            att.readAttendInfoByLine("test5", "sunday");
        }
        att.calPoints();
        for (player p: att.players) {
            if (p.getName().equals("test5")) {
                assertEquals(p.getPoint(), 30);
            }
        }
    }

    @Test
    void setGrade_Gold() {
        for (int i=0; i<14; i++) {
            att.readAttendInfoByLine("test6", "wednesday");
        }
        att.calPoints();
        att.setGrade();
        for (player p: att.players) {
            if (p.getName().equals("test6")) {
                assertEquals(p.getGrade().getName(), "Gold");
            }
        }
    }

    @Test
    void setGrade_Silver() {
        for (int i=0; i<13; i++) {
            att.readAttendInfoByLine("test7", "wednesday");
        }
        att.calPoints();
        att.setGrade();
        for (player p: att.players) {
            if (p.getName().equals("test7")) {
                assertEquals(p.getGrade().getName(), "Silver");
            }
        }
    }

    @Test
    void isRemovedPlayer_true() {
        att.readAttendInfoByLine("test8", "monday");
        att.calPoints();
        att.setGrade();
        for (player p: att.players) {
            if (p.getName().equals("test8")) {
                assertEquals(att.isRemovedPlayer(p), true);
            }
        }
    }

    @Test
    void isRemovedPlayer_false() {
        att.readAttendInfoByLine("test9", "sunday");
        att.calPoints();
        att.setGrade();
        for (player p: att.players) {
            if (p.getName().equals("test9")) {
                assertEquals(att.isRemovedPlayer(p), false);
            }
        }
    }

    @Test
    void showPlayerPointsAndGrade() {
        att.readAttendInfoByLine("test10", "sunday");
        att.calPoints();
        att.setGrade();
        att.showPlayerPointsAndGrade();
        String output = outputStream.toString();
        assertTrue(output.contains("NAME"));
        assertTrue(output.contains("POINT"));
        assertTrue(output.contains("GRADE"));
    }

    @Test
    void showRemovedPlayer() {
        att.readAttendInfoByLine("test11", "monday");
        att.calPoints();
        att.setGrade();
        att.showRemovedPlayer();
        String output = outputStream.toString();
        assertTrue(output.contains("test11"));
    }

    @Test
    void readAttendInfo() throws IOException {
        String testData ="test12 monday";
        BufferedReader testReader = new BufferedReader(new StringReader(testData));
        att.readAttendInfo(testReader);
        for (player p: att.players) {
            if (p.getName().equals("test12")) {
                assertEquals(p.getName(), "test12");
            }
        }
    }

    @Test
    void mainTest(){
        att.main(new String[]{});
    }
}