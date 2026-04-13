import java.io.*;
import java.util.ArrayList;

public class Class extends Update{
    String classID;
    String subject;
    String level;
    String charges;
    String schedule;
    String tutor;

    Class(String classID, String subject, String level, String charges, String schedule, String tutor) throws FileNotFoundException {
        this.classID = classID;
        this.subject = subject;
        this.level = level;
        this.charges = charges;
        this.schedule = schedule;
        this.tutor = tutor;
    }


    public static void addClass(String classID, String subject, String level, String charges, String schedule, String username) throws FileNotFoundException {

        String NewClass;

        // adds all the arguments into one string
        NewClass = classID + "," + subject + "," + level + "," + charges + "," + schedule + "," + username;

        try {
            FileWriter writer = new FileWriter("class.txt", true);
            writer.write(NewClass + "\n");// write the new line in text file
            writer.close();
        } catch (IOException var9) {
            System.out.println("Error writing to file.");
        }

    }

    public static boolean deleteClass(String classID,String username) {
        ArrayList<String> updatedLines = new ArrayList<>();
        boolean foundAndDeleted = false;

        try {
            BufferedReader reader = new BufferedReader(new FileReader("class.txt"));
            String line;

            while ((line = reader.readLine()) != null) {  // Go through each line
                String[] dataArray = line.split(",");
                // check if class id and tutor name is correct
                if (dataArray.length == 6 && dataArray[0].equals(classID) && dataArray[5].equals(username)) {
                    foundAndDeleted = true;
                } else {
                    // if class id and name not in line then add line to updatedlines
                    updatedLines.add(line);
                }
            }
            reader.close();

            if (foundAndDeleted) {
                // Rewrite the entire file without the deleted line
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("class.txt"))) {
                    for (String updatedLine : updatedLines) {
                        writer.write(updatedLine + "\n");
                    }
                }
                return true;
            } else {
                // Class was not found or did not belong to the tutor
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
