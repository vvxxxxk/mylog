package com.golym.mylog.service;

import com.golym.mylog.common.exception.BadRequestException;
import com.golym.mylog.common.utils.CodeGenerator;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final Storage storage;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    private final List<String> ALLOWED_EXTENSIONS = Arrays.asList("png", "jpg", "jpeg");
    private final String STORAGE_BASE_URL = "https://storage.googleapis.com/";
    private final int PROFILE_IMAGE_MIN_SIZE = 240;
    private final int PROFILE_IMAGE_MAX_SIZE = 4000;

    public String uploadImage(MultipartFile image) {

        // 이미지 확장자 체크
        String originFilename = image.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(originFilename);
        if (!isValidExtension(extension))
            throw new BadRequestException("Unsupported file format. Only png, jpg, jpeg are allowed.");

        // 이미지 사이즈 체크
        if (!isValidImageSize(image))
            throw new BadRequestException("The minimum/maximum width, less than or above height of the image.");

        // Cloud에 이미지 업로드
        String uuid = CodeGenerator.generateID("I");
        try {
            BlobInfo blobInfo = storage.create(
                    BlobInfo.newBuilder(bucketName, uuid)
                            .setContentType(extension)
                            .build(),
                    image.getInputStream()
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to profile image save error.", e);
        }

        String url = STORAGE_BASE_URL + bucketName + "/" + uuid;
        return url;
    }

    private boolean isValidExtension(String extension) {
        // 이미지 확장자 체크
        if (extension != null)
            return ALLOWED_EXTENSIONS.contains(extension.toLowerCase());
        return false;
    }

    private boolean isValidImageSize(MultipartFile profileImage) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(profileImage.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to profile image load error.", e);
        }

        int width = image.getWidth();
        int height = image.getHeight();

        // 이미지 최소 사이즈 또는 최대 사이즈 기준에 미충족
        if (width < PROFILE_IMAGE_MIN_SIZE || width > PROFILE_IMAGE_MAX_SIZE ||
                height < PROFILE_IMAGE_MIN_SIZE || height > PROFILE_IMAGE_MAX_SIZE)
            return false;

        return true;
    }
}
