package com.bitrosh.backend.service;

import java.io.InputStream;
import java.util.Optional;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public void uploadFile(String objectName, InputStream inputStream, long size, String contentType) throws Exception {
        log.info("Uploading file to Minio: {}", objectName);
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, size, -1)
                        .contentType(contentType)
                        .build()
        );
    }

    public InputStream getFile(String objectName) throws Exception {
        log.info("Getting file from Minio: {}", objectName);
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

    public Optional<StatObjectResponse> getFileInfo(String objectName) {
        try {
            log.info("Getting file info from Minio: {}", objectName);
            return Optional.of(minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            ));
        } catch (Exception e) {
            log.warn("File not found in Minio: {}", objectName);
            return Optional.empty();
        }
    }

    public void deleteFile(String objectName) throws Exception {
        log.info("Deleting file from Minio: {}", objectName);
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }
}
