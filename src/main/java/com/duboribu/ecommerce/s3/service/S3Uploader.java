package com.duboribu.ecommerce.s3.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
                    .acl(ObjectCannedACL.PUBLIC_READ) // 퍼블릭 접근 권한
                    .contentType("image/jpeg") // 리사이징된 타입에 맞게
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromBytes(resizedBytes));
        } catch (Exception e) {
            log.error("exception : {}", e.getMessage());
        }

        return getFileUrl(filename);
    }
    public String uploadToS3(byte[] imageBytes, String dirName)  throws IOException  {
        String filename = dirName + "/" + UUID.randomUUID() + "-" + "AL_BATCH_IMG.jpg"; // 확장자를 jpg로 통일

        try {
            // 1. 이미지 리사이징
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = new ByteArrayInputStream(imageBytes);

            // 이미지 리사이징 처리
            Thumbnails.of(inputStream)
                    .size(300, 400) // 원하는 사이즈
                    .outputFormat("jpg") // 확장자 통일
                    .toOutputStream(outputStream);

            byte[] resizedBytes = outputStream.toByteArray();

            // 2. S3에 업로드
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(filename)
                    .acl(ObjectCannedACL.PUBLIC_READ) // 퍼블릭 접근 권한
                    .contentType("image/jpeg") // 리사이징된 타입에 맞게
                    .build();

            // S3에 업로드
            s3Client.putObject(putRequest, RequestBody.fromBytes(resizedBytes));

        } catch (Exception e) {
            log.error("Exception occurred during image upload: {}", e.getMessage());
            throw new IOException("Failed to upload image to S3", e);
        }

        // 3. 업로드된 파일의 URL 반환
        return getFileUrl(filename);
    }

    public byte[] downloadImage(String imageUrl) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(imageUrl);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            return EntityUtils.toByteArray(entity);
        }
    }

    public String getFileUrl(String filename) {
        return "https://" + bucket + ".s3.amazonaws.com/" + filename;
    }
}