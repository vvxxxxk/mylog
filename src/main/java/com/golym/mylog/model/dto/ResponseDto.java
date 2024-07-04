package com.golym.mylog.model.dto;

import com.golym.mylog.common.constants.ResponseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto {

    private String code;
    private String message;
    private boolean status;

    public ResponseDto(ResponseType type) {
        this.code = type.name();
        this.message = type.getMessage();
        this.status = type.isStatus();
    }
}
