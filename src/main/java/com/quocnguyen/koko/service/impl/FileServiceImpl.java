package com.quocnguyen.koko.service.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.quocnguyen.koko.dto.AttachmentPreSingedUrlDTO;
import com.quocnguyen.koko.dto.UserDTO;
import com.quocnguyen.koko.service.FileService;
import com.quocnguyen.koko.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 * @author Quoc Nguyen on {2024-08-18}
 */

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {
    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.signed-url.expiration}")
    private Long preSingedUrlExpiration;

    private final UserService userService;

    private final AmazonS3 amazonS3;

    @Override
    public AttachmentPreSingedUrlDTO generatePreSignedUrl() {
        UserDTO user = userService.getAuthenticatedUser();

        String uploadFileName = user.getId().toString() + "/" + UUID.randomUUID().toString();

        Date expiration = new Date(System.currentTimeMillis() + preSingedUrlExpiration);

        var generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, uploadFileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        return AttachmentPreSingedUrlDTO
                .builder()
                .keyObject(uploadFileName)
                .fileUploadedUrl(url.getProtocol() + "://" + url.getHost() + "/" + uploadFileName)
                .url(url.toString())
                .build();

    }

    @Override
    @Async
    public void deleteFiles(String[] keys) {
        try {
            var dor =  new DeleteObjectsRequest(bucketName)
                    .withKeys(keys);

            amazonS3.deleteObjects(dor);

        } catch (AmazonClientException e) {
            System.err.println(e.getMessage());
        }
    }
}
