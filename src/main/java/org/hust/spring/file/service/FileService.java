package org.hust.spring.file.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    public String uploadFileToLocalServer(MultipartFile multipart) {
        String fileName = multipart.getOriginalFilename();
        String directory = "/opt/avatar/images/";
        assert fileName != null;
        String ext = fileName.substring(fileName.lastIndexOf("."));
        String fileNew = UUID.randomUUID().toString().concat(ext);
        String filePath = Paths.get(directory, fileNew).toString();
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(filePath))) {
            InputStream inputStream = multipart.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            byte[] buf = new byte[10240];
            while ((bis.read(buf)) != -1) {
                stream.write(buf);
            }
            stream.flush();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "/public/" + fileNew;
    }
}
