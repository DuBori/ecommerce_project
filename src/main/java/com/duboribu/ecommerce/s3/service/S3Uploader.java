package com.duboribu.ecommerce.s3.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Uploader {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadForItemImgs(MultipartFile file, String dirName) throws IOException {
        String filename = dirName + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        try {
            // 1. 이미지 리사이징
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .size(300, 400) // 원하는 사이즈
                    .outputFormat("jpg") // 확장자 통일
                    .toOutputStream(outputStream);
            byte[] resizedBytes = outputStream.toByteArray();

            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(filename)
                    .acl("public-read")
                    .contentType("image/jpeg") // 리사이징된 타입에 맞게
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromBytes(resizedBytes));
        } catch (Exception e) {
            log.error("exception : {}", e.getMessage());
        }

        return getFileUrl(filename);
    }

    public String getFileUrl(String filename) {
        return "https://" + bucket + ".s3.amazonaws.com/" + filename;
    }
}