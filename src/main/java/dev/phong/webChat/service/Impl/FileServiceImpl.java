package dev.phong.webChat.service.Impl;

import dev.phong.webChat.config.loader.CommonServiceProperties;
import dev.phong.webChat.dto.FileDto;
import dev.phong.webChat.handler.exceptionHandler.exeption.CustomException;
import dev.phong.webChat.service.FileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final CommonServiceProperties commonServiceProperties;

    private final String urlView = "file/image-view?path=";

    @Override
    public List<FileDto> upload(List<MultipartFile> files) {
        List<FileDto> fileUploads = new ArrayList<>();
        for (MultipartFile file : files){
            // Lấy tên gốc của tệp tin
            String originalFileName = file.getOriginalFilename();

            // Lưu tệp tin với tên mới
            String filePath = commonServiceProperties.getFolder() + originalFileName;
            try {
                file.transferTo(new File(filePath));
            } catch (IOException e) {
                log.error(e.getMessage(),e);
                throw new CustomException("Lỗi đọc file");
            }
            // Tạo đối tượng FileDto
            FileDto fileUpload = new FileDto();
            fileUpload.setUrl(urlView + originalFileName);
            fileUpload.setFileName(originalFileName); // Lưu tên không có phần mở rộng của tệp tin
            fileUploads.add(fileUpload);
        }

        return fileUploads;
    }

    @Override
    public FileDto upload(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();

        String filePath = commonServiceProperties.getFolder() + originalFileName;
        try {
            Files.deleteIfExists(Paths.get(filePath));
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new CustomException("Lỗi đọc file");
        }
        // Tạo đối tượng FileUploadDto
        FileDto fileUpload = new FileDto();
        fileUpload.setUrl(urlView + originalFileName);
        fileUpload.setFileName(originalFileName);
        return fileUpload;
    }


    @Override
    public ResponseEntity<?> download(String name) {
        File imageFile;
        // Đọc dữ liệu của ảnh
        byte[] imageBytes;
        try {
            imageFile = ResourceUtils.getFile(commonServiceProperties.getFolder() + name);
            imageBytes = Files.readAllBytes(imageFile.toPath());
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new CustomException("Lỗi đọc file");
        }
        // Tạo đối tượng ByteArrayResource từ mảng byte
//        ByteArrayResource resource = new ByteArrayResource(imageBytes);

        // Thiết lập các đầu mục HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(imageBytes.length));

        // Trả về ResponseEntity với nội dung tệp tin và các đầu mục HTTP
        return ResponseEntity.ok()
                .headers(headers)
                .body(imageBytes);
    }

    @Override
    public ResponseEntity<byte[]> view(String name) {

        File imageFile;
        // Đọc dữ liệu của ảnh
        byte[] imageBytes;

        try {
            imageFile = ResourceUtils.getFile(commonServiceProperties.getFolder() + name);
            imageBytes = Files.readAllBytes(imageFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage(),e);
            throw new CustomException("Lỗi đọc file");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "image/png");

        // Trả về ảnh dưới dạng phản hồi HTTP
        return ResponseEntity.ok()
                .headers(headers)
                .body(imageBytes);
    }
}