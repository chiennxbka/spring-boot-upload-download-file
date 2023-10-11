package org.hust.spring.file.controller;

import org.hust.spring.file.service.FileService;
import org.hust.spring.file.utils.MediaTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
@RequestMapping(value = "/api/v1")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private ServletContext servletContext;

    private static final String DEFAULT_FILE_NAME = "java-tutorial.pdf";

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadFile(HttpServletRequest request, MultipartFile file) {
        // Ghi file vao hard disk, cu the thu muc /opt/avatar/images
        String fileUploaded = fileService.uploadFileToLocalServer(file);
        String url = "http://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + fileUploaded;
        return ResponseEntity.ok(url);
    }

    @GetMapping(value = "/download")
    public ResponseEntity<InputStreamResource> dowloadFile(@RequestParam(defaultValue = DEFAULT_FILE_NAME) String fileName) throws FileNotFoundException {
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
        // Chi dinh file download tu hard disk. lay ra tu thu muc ton tai trong he thong
        File file = new File("/opt/avatar/images" + "/" + fileName);
        InputStreamResource resource;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException exception){
            throw new FileNotFoundException("File not found!!!");
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(mediaType)
                .contentLength(file.length())
                .body(resource);
    }
}
