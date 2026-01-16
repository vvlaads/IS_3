package lab.util;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

public class MinioClientProvider {
    private static final String ENDPOINT = "http://localhost:9000";
    private static final String ACCESS_KEY = "minio";
    private static final String SECRET_KEY = "minio123";
    private static final String BUCKET = "import-files";

    public static S3Client createClient() {
        return S3Client.builder()
                .endpointOverride(URI.create(ENDPOINT))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)
                ))
                .region(Region.US_EAST_1)
                .serviceConfiguration(s -> s.pathStyleAccessEnabled(true))
                .build();
    }

    public static String getBucket() {
        return BUCKET;
    }
}
