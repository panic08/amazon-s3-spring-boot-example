package ru.panic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private S3Service s3Service;

    private final String bucketName = "test-bucket";
    private final String fileName = "test-file.png";

    @Test
    void testDownloadFile() {
        ResponseBytes<GetObjectResponse> responseBytes = mock(ResponseBytes.class);
        when(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).thenReturn(responseBytes);
        when(responseBytes.asByteArray()).thenReturn(new byte[]{1, 2, 3});

        byte[] result = s3Service.downloadFile(bucketName, fileName);

        assertNotNull(result);
        assertArrayEquals(new byte[]{1, 2, 3}, result);
        verify(s3Client, times(1)).getObjectAsBytes(any(GetObjectRequest.class));
    }

    @Test
    void testUploadFile() throws IOException {
        when(multipartFile.getOriginalFilename()).thenReturn("image.png");
        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 3});

        String uploadedFileName = s3Service.uploadFile(bucketName, multipartFile);

        assertNotNull(uploadedFileName);
        assertTrue(uploadedFileName.endsWith(".png"));
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void testDeleteFile() {
        when(s3Client.deleteObject(any(DeleteObjectRequest.class)))
                .thenReturn(DeleteObjectResponse.builder().build());

        s3Service.deleteFile(bucketName, fileName);

        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }

}
