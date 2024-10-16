package dev.phong.webChat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    private String url;
    private String data;
    private String fileName;

    public FileDto(String url) {
        this.url = url;
    }

    public FileDto(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
    }

}