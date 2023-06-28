package com.muzisoft.division.utils.handler;

import com.muzisoft.division.Application;
import com.muzisoft.division.domain.file.DirectoryManagerRepository;
import com.muzisoft.division.domain.file.EDirectoryManager;
import com.muzisoft.division.domain.file.EFileManager;
import com.muzisoft.division.utils.FileManagerUtils;
import com.muzisoft.division.web.exception.io.CIOException.CInvalidFileFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileHandler {

    private final DirectoryManagerRepository directoryManagerRepository;

    private final Application application;

    public String fileUrl(EFileManager fileManager) {
        return application.hostAddress() + "/files" + fileManager.getDirectoryManager().getDirPath() + "/" + fileManager.getFileName();
    }

    public List<EFileManager> parse(List<MultipartFile> multipartFiles, String dirName) {

        // 반환할 파일 리스트
        List<EFileManager> fileManagerList = new ArrayList<>();

        EDirectoryManager dirManager = directoryManagerRepository.findByDirName(dirName).orElseThrow(RuntimeException::new);

        // 전달되어 온 파일이 존재할 경우
        if(!CollectionUtils.isEmpty(multipartFiles)) {

            // 절대 경로 설정
            String absolutePath = new File("").getAbsolutePath() + "/files" + dirManager.getDirPath() + "/";

            log.info("absolutePath: "+ absolutePath);

            // 파일을 저장할 세부 경로 지정
            File file = new File(absolutePath);

            // 디렉터리가 존재하지 않을 경우
            if(!file.exists() && !file.mkdirs()) {
                // 디렉터리 생성에 실패했을 경우
                throw new RuntimeException("file: was not successful");
            }

            // 다중 파일 처리
            for(MultipartFile multipartFile : multipartFiles) {

                // 파일 이름이 존재하지 않을 경우 에러
                String originalFileName = Optional.ofNullable(multipartFile.getOriginalFilename()).orElseThrow(RuntimeException::new);
                
                // 파일의 확장자 추출
                String originalFileExtension = originalFileName.substring(multipartFile.getOriginalFilename().lastIndexOf("."));
                String contentType = multipartFile.getContentType();

                // 확장자명이 존재하지 않을 경우 에러
                if(ObjectUtils.isEmpty(contentType)) {
                    throw new CInvalidFileFormatException();
                }

                // 파일명 중복 피하고자 밀리초까지 얻어와 지정
                String newFileName = System.currentTimeMillis() + originalFileExtension;

                // 생성 후 리스트에 추가
                fileManagerList.add(new EFileManager(dirManager, newFileName, multipartFile.getOriginalFilename(), originalFileExtension, Long.valueOf(multipartFile.getSize()).intValue() / 1024, ""));

                // 업로드 한 파일 데이터를 지정한 파일에 저장
                file = new File(absolutePath + newFileName);

                try {
                    multipartFile.transferTo(file);
                    Thread.sleep(100);
                } catch (IOException e) {
                    FileManagerUtils.delete(file);
                    
                    // 파일 업로드 실패한 경우 에러
                    throw new RuntimeException();
                } catch (InterruptedException ignored) {
                }

                // 파일 권한 설정(쓰기, 읽기)
                file.setWritable(true);
                file.setReadable(true);
            }
        }

        return fileManagerList;
    }
    
    public EFileManager parseImageUrl(String imageUrl, String dirName) {

        EFileManager fileManager = null;

        try {
            URL url = new URL(imageUrl);
            String extension = StringUtils.hasText(FilenameUtils.getExtension(url.getPath())) ? FilenameUtils.getExtension(url.getPath()) : "png";
            String filename = FilenameUtils.getName(url.getPath());
            String newFileName = System.currentTimeMillis() + "." + extension;

            EDirectoryManager dirManager = directoryManagerRepository.findByDirName(dirName).orElseThrow(RuntimeException::new);

            BufferedImage bufferedImage = ImageIO.read(url);
            String absolutePath = new File("").getAbsolutePath() + "/files" + dirManager.getDirPath() + "/";
            File file = new File(absolutePath + newFileName);

            if(!file.exists()) {
                file.mkdirs();
            }

            ImageIO.write(bufferedImage, extension, file);

            fileManager = new EFileManager(dirManager, newFileName, filename, "." + extension, Long.valueOf(file.length()).intValue() / 1024, "");

        } catch (IOException ignored) { }

        return fileManager;
    }
    
}
