package com.golym.mylog.controller;

import com.golym.mylog.model.dto.response.ResponseUploadImageDto;
import com.golym.mylog.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/api/file/image", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestPart(name = "image") MultipartFile image) {

        String url = fileService.uploadImage(image);

        return new ResponseEntity<>(ResponseUploadImageDto.builder()
                .url(url)
                .build(), HttpStatus.OK);
    }

}
