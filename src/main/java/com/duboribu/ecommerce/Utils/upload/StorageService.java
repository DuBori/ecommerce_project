
package com.duboribu.ecommerce.Utils.upload;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
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
    @Transactional
    public String store(MultipartFile file, String path) {
        try {
            // 임시 파일 이름 생성
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            // 저장할 디렉터리 경로 설정
            Path directoryPath = Paths.get(uploadPath + "/customImage_" + path);
            if (!Files.exists(directoryPath)) {
                // 디렉터리가 없으면 생성합니다.
                Files.createDirectories(directoryPath);
            }
            // 파일 경로 설정
            Path filePath = directoryPath.resolve(fileName);
            // 파일 저장
            FileCopyUtils.copy(file.getBytes(), Files.newOutputStream(filePath));
            return "/images/customImage_" + path + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
