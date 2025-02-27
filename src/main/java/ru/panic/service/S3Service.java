package ru.panic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.UUID;

/**
 * Service class for handling interactions with AWS S3.
 */
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client client;

    /**
     * Downloads a file from S3.
     *
     * @param bucketName the name of the S3 bucket
     * @param fileName the name of the file
     * @return the file content as a byte array
     */
    public byte[] downloadFile(String bucketName, String fileName) {
        try {
            ResponseBytes<GetObjectResponse> objectBytes = client.getObjectAsBytes(
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .build()
            );
            return objectBytes.asByteArray();
        } catch (S3Exception e) {
            throw new RuntimeException("File download error from S3", e);
        }
    }

    /**
     * Uploads a file to S3 and returns its generated filename.
     *
     * @param bucketName the name of the S3 bucket
     * @param file the file to upload
     * @return the generated filename
     */
    public String uploadFile(String bucketName, MultipartFile file) {
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID() + "-" + System.currentTimeMillis() + extension;
        try {
            byte[] fileBytes = processFile(file);
            client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .build(),
                    RequestBody.fromBytes(fileBytes)
            );
        } catch (IOException e) {
            throw new RuntimeException("File upload error in S3", e);
        }
        return fileName;
    }

    /**
     * Deletes a file from S3.
     *
     * @param bucketName the name of the S3 bucket
     * @param fileName the name of the file to delete
     */
    public void deleteFile(String bucketName, String fileName) {
        try {
            client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build()
            );
        } catch (S3Exception e) {
            throw new RuntimeException("File deletion error from S3", e);
        }
    }

    private byte[] processFile(MultipartFile file) throws IOException {
        return file.getBytes();
    }
}
