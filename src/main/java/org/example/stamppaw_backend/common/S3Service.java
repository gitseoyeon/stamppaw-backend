package org.example.stamppaw_backend.common;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.example.stamppaw_backend.common.exception.ErrorCode;
import org.example.stamppaw_backend.common.exception.StampPawException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${aws.s3.bucket.name}")
    private String bucket;

    private static final int MAX_FILE_COUNT = 3;

    private final AmazonS3Client s3Client;
    private final int S3_PATH_LENGTH = 55;

    public String uploadFileAndGetUrl(final MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        String fileName = getRandomFileName(file);
        try {
            s3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata));
        } catch (IOException e) {
            throw new StampPawException(ErrorCode.FILE_UPLOAD_FAILED);
        }
        return getFileUrlFromS3(fileName);
    }

    public List<String> uploadFilesAndGetUrls(final List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return new ArrayList<>();
        }

        if (files.size() > MAX_FILE_COUNT) {
            throw new StampPawException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;
            urls.add(uploadFileAndGetUrl(file));
        }

        return urls;
    }

    private String getFileUrlFromS3(final String fileName) {
        return s3Client.getUrl(bucket, fileName).toString();
    }

    private String getRandomFileName(final MultipartFile file) {
        String randomUUID = UUID.randomUUID().toString();
        return randomUUID + file.getOriginalFilename();
    }

    public void deleteFile(final String fileUrl) {
        try {
            // S3 URL 예시:
            // https://stamppaw.s3.ap-northeast-2.amazonaws.com/abc123-uuidfilename.png
            String fileName = extractFileNameFromUrl(fileUrl);

            if (s3Client.doesObjectExist(bucket, fileName)) {
                s3Client.deleteObject(bucket, fileName);
            } else {
                throw new StampPawException(ErrorCode.FILE_DELETE_FAILED);
            }
        } catch (Exception e) {
            throw new StampPawException(ErrorCode.FILE_DELETE_FAILED);
        }
    }

    // ✅ URL에서 파일명 추출
    private String extractFileNameFromUrl(String fileUrl) {
        try {
            String decodedUrl = URLDecoder.decode(fileUrl, StandardCharsets.UTF_8);
            int lastSlash = decodedUrl.lastIndexOf('/');
            return decodedUrl.substring(lastSlash + 1);
        } catch (Exception e) {
            throw new StampPawException(ErrorCode.FILE_DELETE_FAILED);
        }
    }

}
