package com.duboribu.ecommerce.Utils.upload;

import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import com.duboribu.ecommerce.s3.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageUploadController {
    private final StorageService storageService; // 이미지 저장 서비스
    private final S3Uploader s3Uploader; // S3로 교체
    private final String dirName = "/image";
    @PostMapping("/upload")
    public ResponseEntity<DefaultResponse> uploadImage(@RequestParam("file") MultipartFile file, String path, Model model) throws IOException {
        String filePath = s3Uploader.upload(file, dirName);
        return new ResponseEntity<>(new DefaultResponse(filePath), HttpStatus.OK);
    }
}
