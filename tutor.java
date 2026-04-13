import java.util.Scanner;

public class tutor {
    private static Scanner scanner;
    String tutorID;
    static String username;
    String contactNum;
    String email;
    String level;
    String subject;

    tutor(String tutorID, String username, String contactNum, String email, String level, String subject) {
        this.tutorID = tutorID;
        tutor.username = username;
        this.contactNum = contactNum;
        this.email = email;
        this.level = level;
        this.subject = subject;

    }public String getId() {
        return tutorID;
    }

    public String getName() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return contactNum;
    }
    public String getFormLevel() {
        return level;
    }

    public String getSubject() {
        return subject;
    }

}



