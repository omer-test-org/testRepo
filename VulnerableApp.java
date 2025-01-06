import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;
import java.io.*;
import java.util.*;

public class VulnerableApp {
    public static void main(String[] args) {
        // Creating an instance of a vulnerable data structure
        Map<String, Object> map = new HashMap<>();

        // Using a vulnerable Apache Commons Collections method
        Transformer transformer = new InstantiateTransformer("java.lang.Runtime", new Class[]{String.class}, new Object[]{"open -a Calculator"});

        // Store the transformer inside the HashMap
        map.put("key", transformer);

        // Serialize the HashMap
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("serializedMap.dat"))) {
            out.writeObject(map);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deserialize the object (this is where the vulnerability comes into play)
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("serializedMap.dat"))) {
            Map<String, Object> deserializedMap = (Map<String, Object>) in.readObject();
            // The vulnerable transformer will trigger the execution of the command when deserialized
            deserializedMap.get("key");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
