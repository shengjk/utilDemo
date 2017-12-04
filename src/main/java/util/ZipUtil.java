package util;

import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ZipUtil {
    private static Logger logger = Logger.getLogger(ZipUtil.class);

    /**
     * 把文件压缩成zip格式
     *
     * @param files
     *            需要压缩的文件
     * @param zipFilePath
     *            压缩后的zip文件路径
     */
    public static void compressFiles2Zip(File[] files, String zipFilePath) {
        if (files != null && files.length > 0) {
            ZipArchiveOutputStream zaos = null;
            try {
                File zipFile = new File(zipFilePath);
                //保证当天可以重跑
                if(zipFile.exists()){
                    FileUtils.forceDelete(zipFile);
                    logger.info("zipFilePath "+zipFile.getParent()+"已存在，但已删除！");
                }


                zaos = new ZipArchiveOutputStream(zipFile);
                // Use Zip64 extensions for all entries where they are required
                zaos.setUseZip64(Zip64Mode.AsNeeded);

                // 将每个文件用ZipArchiveEntry封装
                // 再用ZipArchiveOutputStream写到压缩文件中
                for (File file : files) {
                    if (file != null && file.isFile()&&!file.getName().endsWith(".zip")) {
                        logger.info("压缩文件为   "+file.getName());
                        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(
                                file, file.getName());
                        zaos.putArchiveEntry(zipArchiveEntry);
                        InputStream is = null;
                        try {
                            is = new FileInputStream(file);
                            byte[] buffer = new byte[1024 * 5];
                            int len = -1;
                            while ((len = is.read(buffer)) != -1) {
                                // 把缓冲区的字节写入到ZipArchiveEntry
                                zaos.write(buffer, 0, len);
                            }
                            // Writes all necessary data for this entry.
                            zaos.closeArchiveEntry();
                        } catch (Exception e) {
                            throw new RuntimeException("压缩文件流操作时 "+e);
                        } finally {
                            if (is != null)
                                is.close();
                        }
                    }
                }
                zaos.finish();
            } catch (Exception e) {
                throw new RuntimeException("zip时"+e);
            } finally {
                try {
                    if (zaos != null) {
                        zaos.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException("关闭zip连接时 "+e);
                }
            }
        }else{
            throw new RuntimeException("files == null 或者 files.length == 0");
        }
    }
}
