package kr.co.smh.util.amazonS3.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import kr.co.smh.common.dto.ResDTO;
import kr.co.smh.common.service.ByteCaculation;
import kr.co.smh.util.amazonS3.model.FileVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AmazonS3Service {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	private final ByteCaculation byteCaculation;
    private final AmazonS3 amazonS3;
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET;

    public HttpEntity<?> uploadSingleFile(MultipartFile file, FileVO vo){
		log.info("파일 정보 --> " + vo);
		log.info("이미지 파일 --> " + file);
		log.info("이미지 확장자 --> " + vo.getOriginFileName());
		log.info("이미지 파일 --> " + file.getSize());
		log.info("이미지 파일(변환) --> " + byteCaculation.byteCalculation(String.valueOf(file.getSize())));
		log.info("버킷 --> " + BUCKET);
		try {
			log.info("이미지 파일 --> " + file.getBytes().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("이미지 파일 --> " + file.getContentType());
		log.info("이미지 파일 --> " + file.getResource());
    	
        String saveFileName = createUUIDName(vo.getOriginFileName());
        String originFileName = vo.getOriginFileName();
        String fileUrl = amazonS3Client.getUrl(BUCKET, saveFileName).toString(); // 파일 경로 저장 
        log.info("fileUrl --> " + fileUrl);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try(InputStream inputStream = file.getInputStream()){
          amazonS3.putObject(new PutObjectRequest(BUCKET, saveFileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e){
      		return new ResponseEntity<>(
    				ResDTO.builder()
    					  .code(0)
    					  .message("파일 업로드에 실패하였습니다.")
    					  .build(),
    					  HttpStatus.BAD_REQUEST);  
        }

  		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("파일이 성공적으로 업로드 되었습니다..")
					  .build(),
					  HttpStatus.OK);  
    }

    // 먼저 파일 업로드시, 파일명을 난수화하기 위해 UUID 를 활용하여 난수를 돌린다.
    public String createUUIDName(String fileName){
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기위해, "."의 존재 유무만 판단하였습니다.
    private String getFileExtension(String fileName){
      try{
          return fileName.substring(fileName.lastIndexOf("."));
      } catch (StringIndexOutOfBoundsException e){
         
      }
      return fileName;
    }

    // 파일 다운로드
//    public ResponseEntity<UrlResource> downloadImage(String originalFilename) {
//        UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, originalFilename));
//    
//        String contentDisposition = "attachment; filename=\"" +  originalFilename + "\"";
//        // header에 CONTENT_DISPOSITION 설정을 통해 클릭 시 다운로드 진행
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
//                .body(urlResource);
//    }

//    public void deleteFile(String fileName){
//        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
//        System.out.println(bucket);
//    }
}
