import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Update {
    public static void updateData(int dataIndex, String lineInfo, String newInfo, String textFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(textFile));
            ArrayList<String> updatedLines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] dataArray = line.split(",");
                if (dataArray.length > 0 && dataArray[0].equals(lineInfo)) {
                    dataArray[dataIndex] = newInfo;
                }
                StringBuilder updatedLine = new StringBuilder();
                for (int i = 0; i < dataArray.length; i++) {
                    updatedLine.append(dataArray[i]);
                    if (i < dataArray.length - 1) {
                        updatedLine.append(",");
                    }
                }
                updatedLines.add(updatedLine.toString());
            }

            reader.close();
            FileWriter writer = new FileWriter(textFile);
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine + "\n");
            }
            writer.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}