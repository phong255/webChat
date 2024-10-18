package dev.phong.webChat.controller;

import dev.phong.webChat.dto.FileDto;
import dev.phong.webChat.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Tag(name = "File api")
@RestController
@RequestMapping("/file")
public class ImageController {
    private final FileService fileService;

    public ImageController(FileService fileService) {
        this.fileService = fileService;
    }

    @Operation(summary = "upload files")
    @PostMapping
    public List<FileDto> uploadMuch(
            @RequestParam("file") List<MultipartFile> multipartFiles
    ){
        return fileService.upload(multipartFiles);
    }

    @Operation(summary = "upload one file")
    @PostMapping("/upload")
    public FileDto handleFileUpload(
            @RequestParam("file") MultipartFile file
    ){
        return fileService.upload(file);
    }

    @Operation(summary = "Download file")
    @GetMapping("content")
    public ResponseEntity<?> download(
            @RequestParam("name") String name
    ) {
        return fileService.download(name);
    }

    @Operation(summary = "View image")
    @GetMapping("image-view")
    public ResponseEntity<byte[]> view(
            @RequestParam("path") String path
    ) throws IOException {
        return fileService.view(path);
    }
}