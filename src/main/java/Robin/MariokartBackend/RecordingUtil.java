package Robin.MariokartBackend;

import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Component
public class RecordingUtil {

    public static byte[] compressRecording(byte[] data){
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] temp = new byte[2400 * 1024];
        try {
            while (!deflater.finished()){
                int size = deflater.deflate(temp);
                outputStream.write(temp,0,size);
            }
            outputStream.close();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();
    }

    public static byte[] decompressRecording(byte[] data){
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] temp = new byte[2400 * 1040];
        try{
            while (!inflater.finished()){
                int count = inflater.inflate(temp);
                outputStream.write(temp,0,count);
            }
            outputStream.close();
        } catch (DataFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();
    }

}
