package hello.aiofirstuser.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3ImageService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String upload(MultipartFile image) {
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            return "";
        }
        return this.uploadImage(image);
    }

    private String uploadImage(MultipartFile image) {
        this.validateImageFileExtention(image.getOriginalFilename());

        try {

            return this.uploadImageToS3(image);

        } catch (IOException e) {
            throw new RuntimeException("IO_EXCEPTION_ON_IMAGE_UPLOAD");
        }

    }

    private void validateImageFileExtention(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if(lastDotIndex == -1){
            throw new RuntimeException("NO_FILE_EXTENTION");
        }
        String extention = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList  = Arrays.asList("jpg","jpeg","png","gif");

        if(allowedExtentionList.contains(extention)){
            return;
        }

    }

    private String uploadImageToS3(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename();
        String extention = originalFilename.substring(originalFilename.lastIndexOf("."));

        String s3FileName = UUID.randomUUID().toString().substring(0,10) + originalFilename;

        InputStream is = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/"+extention);
        metadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try {

        PutObjectRequest putObjectRequest =
                new PutObjectRequest(bucketName,s3FileName,byteArrayInputStream,metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3.putObject(putObjectRequest);

        }catch (Exception e){
            throw new RuntimeException("PUT_OBJECT_EXCEPTION");
        }finally {
            byteArrayInputStream.close();
            is.close();
        }

        return amazonS3.getUrl(bucketName,s3FileName).toString();

    }

    public void deleteUmageFromS3(String imageAddress){
        String key = getKeyFromImageAddress(imageAddress);
        try {
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName,key));

        }catch (Exception e){
            throw new RuntimeException("IO_EXCEPITON_ON_IMAGE_DELETE");
        }
    }

    private String getKeyFromImageAddress(String imageAddress) {

        try {
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(),"UTF-8");
            return decodingKey.substring(1);
        } catch (MalformedURLException | UnsupportedEncodingException e ) {
            throw new RuntimeException("IO_EXCEPITON_ON_IMAGE_DELETE");
        }
    }


}
