
package com.duboribu.ecommerce.Utils.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class StorageService {
    @Value("${upload.path}")
    private String uploadPath; // 이미지 업로드 경로

    public String store(MultipartFile file, String path) {

        File uploadDir = new File(uploadPath + "/customImage_" + path);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        try {
            // 이미지 파일 저장
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadPath + "/customImage_" + path + "/" + fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return "/images/customImage_" + path + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

}
