package dev.phong.webChat.service;

import dev.phong.webChat.dto.FileDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface FileService {
    List<FileDto> upload(List<MultipartFile> files);
    FileDto upload(MultipartFile file);
    ResponseEntity<?> download(String name);
    ResponseEntity<byte[]> view(String name) throws IOException;
}