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

    @PostMapping
    public ResponseEntity<Void> importEx(@RequestParam("file") MultipartFile file) throws IOException, ParseException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Iterator<Row> rowIterator = sheet.rowIterator();
        List<Employee> employees = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() == 0) {
                continue;
            }
            employees.add(Employee.builder()
                    .firstName(getCellString(row, 1))
                    .lastName(getCellString(row, 2))
                    .gender(getCellString(row, 3))
                    .country(getCellString(row, 4))
                    .age(Integer.valueOf(getCellString(row, 5)))
                    .dateOfBirth(sdf.parse(getCellString(row, 6)))
                    .build());
        }
        repository.saveAll(employees);
        return ResponseEntity.ok().build();
    }

    private static String getCellString(Row row, int index) {
        Cell cell = row.getCell(index);
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }
}
