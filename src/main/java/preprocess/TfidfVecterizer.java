package preprocess;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TfidfVecterizer implements Serializable {

    private static final long serialVersionUID = 4980918364586874045L;

    public static void serialize(TfidfVecterizer vecterizer, String filename)
            throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filename);
        ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
        outStream.writeObject(vecterizer);
        outStream.close();
        fileOut.close();
    }

    public static TfidfVecterizer deserialize(String filename)
            throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        TfidfVecterizer vecterizer = (TfidfVecterizer) in.readObject();
        in.close();
        fileIn.close();
        return vecterizer;
    }
}
