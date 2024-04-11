package com.duboribu.ecommerce.Utils.upload;

import com.duboribu.ecommerce.auth.domain.DefaultResponse;
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
@Slf4j
@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageUploadController {
    private final StorageService storageService; // 이미지 저장 서비스

    @PostMapping("/upload")
    public ResponseEntity<DefaultResponse> uploadImage(@RequestParam("file") MultipartFile file, String path, Model model) {
        String filePath = storageService.store(file, path);

        return new ResponseEntity<>(new DefaultResponse(filePath), HttpStatus.OK);
    }
}
