package com.hoolink.manage.base.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Author: xuli
 * @Date: 2019/5/13 15:48
 */
public class FileUtil {

    private final static Integer BYTE_SIZE=10240;

    public static File multipartFileToFile(MultipartFile file) throws Exception {
        File toFile = null;
        if(file==null||file.getSize()<=0){
            file = null;
        }else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[BYTE_SIZE];
            while ((bytesRead = ins.read(buffer, 0, BYTE_SIZE)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
