package com.example.ecommerce.service;

import com.example.ecommerce.exception.impl.ImageLoadFailException;
import com.example.ecommerce.exception.impl.UploadFileEmptyException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    //이미지 단일 업로드
    //이미지 URL을 반환
    public String upload(MultipartFile multipartFile) {
        //업로드된 파일이 비어있는지 확인
        if (multipartFile.isEmpty()) {
            throw new UploadFileEmptyException();
        }

        String originalFileName = multipartFile.getOriginalFilename();
        //고유성 보장, 충돌 방지(같은 파일 이름), 보안 강화 등의 이유로 uuid를 사용
        //공백 파일명을 언더바로 대체
        String uuid = UUID.randomUUID().toString();
        String uploadImageName = uuid + "_" + Objects.requireNonNull(originalFileName)
                .replace("\\s", "-");

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(uploadImageName)
                    .build();
            PutObjectResponse response = s3Client.putObject(putObjectRequest,
                    RequestBody.fromBytes(multipartFile.getBytes()));

            //이미지 저장에 성공하면 이미지URL을 요청하여 반환
            if (response.sdkHttpResponse().isSuccessful()) {
                GetUrlRequest request = GetUrlRequest.builder()
                        .bucket(bucket)
                        .key(uploadImageName)
                        .build();

                return s3Client.utilities().getUrl(request).toExternalForm();
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }
    }

    //이미지 삭제
    public void deleteImage(List<String> productImageUrlList) {
        productImageUrlList.forEach(imageUrl -> {
            String deleteKey = getDeleteKey(imageUrl);
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(deleteKey)
                    .build();
            DeleteObjectResponse response = s3Client.deleteObject(deleteObjectRequest);

            if (!response.sdkHttpResponse().isSuccessful()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다.");
            }
        });
    }

    private String getDeleteKey(String imageUrl){
        int lastIndex = imageUrl.lastIndexOf("/");
        if(lastIndex == -1){
            throw new ImageLoadFailException();
        }
        return imageUrl.substring(lastIndex + 1); // 슬래쉬("/")는 포함하지 않도록 +1 인덱스부터 추출
    }
}
