package pl.shopgen.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pl.shopgen.services.FileSaveService;

import java.io.IOException;
import java.util.Arrays;

@Controller
public class FilesUploadController {
    private static final long maxFileSize = 5242880;
    private FileSaveService fileSaveService = new FileSaveService();

    @GetMapping("/upload")
    public String index() {
        return "uploadForm";
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<?> singleFileUpload(@RequestParam("file") MultipartFile file) {
        if(!file.isEmpty()) {
            if(file.getSize() > maxFileSize) {
                return new ResponseEntity<>("File size exceeded the maximum size.", HttpStatus.NOT_ACCEPTABLE);
            }
            try {
                fileSaveService.saveUploadedFiles(Arrays.asList(file));
            } catch (IOException e) {
                return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
            }
        } else {

            return new ResponseEntity<>("please select a file!", HttpStatus.OK);
        }
        return new ResponseEntity<>(fileSaveService.getFilesUploadInfoDTO(), HttpStatus.OK);
    }

}