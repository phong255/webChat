package dev.phong.webChat.controller;

import dev.phong.webChat.dto.FileDto;
import dev.phong.webChat.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
public class ImageController {
    private final FileService fileService;

    public ImageController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public List<FileDto> uploadMuch(
            @RequestParam("file") List<MultipartFile> multipartFiles
    ){
        return fileService.upload(multipartFiles);
    }

    @PostMapping("/upload")
    public FileDto handleFileUpload(
            @RequestParam("file") MultipartFile file
    ){
        return fileService.upload(file);
    }

    @GetMapping("content")
    public ResponseEntity<?> download(
            @RequestParam("name") String name
    ) {
        return fileService.download(name);
    }

    @GetMapping("image-view")
    public ResponseEntity<byte[]> view(
            @RequestParam("path") String path
    ) throws IOException {
        return fileService.view(path);
    }
}