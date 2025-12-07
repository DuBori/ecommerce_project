package com.duboribu.ecommerce.s3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Uploader {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file, String dirName) throws IOException {
        String filename = dirName + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(filename)
                .acl("public-read")
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putRequest, RequestBody.fromBytes(file.getBytes()));
        return getFileUrl(filename);
    }

    public String getFileUrl(String filename) {
        return "https://" + bucket + ".s3.amazonaws.com/" + filename;
    }
}