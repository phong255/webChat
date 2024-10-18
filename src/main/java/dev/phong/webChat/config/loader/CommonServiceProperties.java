package dev.phong.webChat.config.loader;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class CommonServiceProperties {
    @Value("${app.config.folder}")
    private String folder;
    @Value("${app.bot.name}")
    private String nameOfBot;
}