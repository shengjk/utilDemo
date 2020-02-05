import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;

/**
 * 测试用于读取图片的EXIF信息
 *
 * @author Winter Lau
 */
public class ExifTester {
    public static void main(String[] args) throws Exception {
        File jpegFile = new File("/Users/iss/Desktop/0068LxWJly1galzwgopa1j31430u0ajm.jpg");
        Metadata metadata = JpegMetadataReader.readMetadata(jpegFile);
        for(Directory directory : metadata.getDirectories()){
            for(Tag tag : directory.getTags()){
                System.out.print("name : " + tag.getTagName() + "  -->");
                System.out.println("desc : " + tag.getDescription());
            }
        }
    }
}