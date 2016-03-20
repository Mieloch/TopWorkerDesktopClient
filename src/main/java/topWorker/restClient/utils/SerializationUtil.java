package topWorker.restClient.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import topWorker.restClient.Message;

public class SerializationUtil {

    private final String SERIALIZATION_FILE;

    public SerializationUtil(String file) {
        SERIALIZATION_FILE = file;
    }

    public void writeWorkPeriods(List<Message> periods) {
        System.out.println("UNPOSTED PERIODS: " + periods.size());
        try (ObjectOutputStream objectStream = new ObjectOutputStream(new FileOutputStream(SERIALIZATION_FILE))) {
            objectStream.writeObject(periods);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Message> readWorkPeriods() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(SERIALIZATION_FILE));) {
            return (List<Message>) inputStream.readObject();
        } catch (FileNotFoundException e1) {

            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
