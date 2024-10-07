package dev.phong.webChat.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {
    private Date at;
    private ServiceError error;
    private Object data;
}
