package pl.shopgen.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.shopgen.controllers.FilesUploadInfoDTO;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileSaveService {

    private static final String uploadDir = System.getenv("CATALINA_HOME") + "/webapps";
    private static final String uploadURL = "/resources/img/";
    private FilesUploadInfoDTO filesUploadInfoDTO = new FilesUploadInfoDTO();

    public void saveUploadedFiles(List<MultipartFile> files) throws IOException {

        for (MultipartFile file : files) {
            if(file.isEmpty()) {
                continue;
            }
            UUID uuid = UUID.randomUUID();
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDir + uploadURL);
            Files.createDirectories(path);

            filesUploadInfoDTO.setUrl(uploadURL + FilenameUtils.removeExtension(file.getOriginalFilename()) + uuid.toString() + "." + FilenameUtils
                    .getExtension(file.getOriginalFilename())
                    .toLowerCase());
            filesUploadInfoDTO.setFileType(file.getContentType());
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(uploadDir + filesUploadInfoDTO
                    .getUrl())));

            stream.write(bytes);
            stream.close();
        }
    }

    public FilesUploadInfoDTO getFilesUploadInfoDTO() {
        return filesUploadInfoDTO;
    }

    public void setFilesUploadInfoDTO(FilesUploadInfoDTO filesUploadInfoDTO) {
        this.filesUploadInfoDTO = filesUploadInfoDTO;
    }
}
