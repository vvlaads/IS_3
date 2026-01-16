package lab.beans.util;

import lab.util.MinioClientProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


@Named("minIOStorageBean")
@ApplicationScoped
public class MinIOStorageBean {
    private final S3Client s3 = MinioClientProvider.createClient();

    @PostConstruct
    public void init() {
        String bucketName = MinioClientProvider.getBucket();

        // Проверяем и создаём бакет
        if (s3.listBuckets().buckets().stream().noneMatch(b -> b.name().equals(bucketName))) {
            s3.createBucket(CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build());
            System.out.println("Бакет создан: " + bucketName);
        }
    }


    public void uploadFile(byte[] content, String key) throws IOException {
        Path tempFile = Files.createTempFile("upload-", "-" + key);
        Files.write(tempFile, content, StandardOpenOption.WRITE);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(MinioClientProvider.getBucket())
                .key(key)
                .build();

        s3.putObject(request, RequestBody.fromFile(tempFile));

        Files.delete(tempFile);

        System.out.println("Файл загружен в MinIO: " + key);
    }

    public byte[] downloadFile(String key) throws IOException {
        try (InputStream s3InputStream = s3.getObject(
                GetObjectRequest.builder()
                        .bucket(MinioClientProvider.getBucket())
                        .key(key)
                        .build()
        )) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = s3InputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
            return baos.toByteArray();
        }
    }
}