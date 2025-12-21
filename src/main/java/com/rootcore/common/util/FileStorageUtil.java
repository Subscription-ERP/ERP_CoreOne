package com.rootcore.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

// 파일 저장할 때 필요한 Java NIO 클래스(Path, Paths, Files)
// UUID : 파일 이름 중복 방지를 위해 랜덤 ID 생성
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Component
public class FileStorageUtil {
	// FileStorageUtil : 실제 파일을 디스크에 저장

	@Value("${app.upload-dir:/upload}")
	private String uploadDir; // 실제 사용할 루트 업로드 폴더 경로를 보관하는 필드

	public String store(MultipartFile file, String subDir) throws IOException {
		// store : 실제 파일 저장을 수행하는 메서드
		// MultipartFile file -> 업로드된 파일
		// String subDir -> 하위 폴더명(예: "user/photo")

		// 파일 비어있을시 null 값 리턴
		if (file == null || file.isEmpty()) {
			return null;
		}

		String cleanFilename = StringUtils.cleanPath(file.getOriginalFilename()); // 파일명
		String uuid = UUID.randomUUID().toString(); // 파일명 겹치지 않게 처리
		String filename = uuid + "_" + cleanFilename;

		Path dirPath = Paths.get(uploadDir, subDir).toAbsolutePath().normalize(); // 실제 저장할 디렉토리 경로
		Files.createDirectories(dirPath); // 폴더 없을시 자동으로 생성(상위폴더까지)

		Path targetPath = dirPath.resolve(filename); // dirPath 아래에 filename을 붙여서 최종 파일 경로 생성
		file.transferTo(targetPath.toFile()); // 업로드된 파일을 타겟위치에 실제 저장

		// DB에는 전체경로가 아니라 상대경로만 저장(예: user/photo/a3f1_증명사진.jpg)
		String dbPath = Paths.get(subDir, filename).toString().replace("\\", "/");

		log.info("Saved file: {} -> {}", cleanFilename, targetPath); // 로그출력

		return dbPath;
	}

	// 파일삭제
	public boolean delete(String dbPath) {
		// dbPath 예: "user/photo/uuid_xxx.jpg"
		if (dbPath == null || dbPath.isBlank())
			return false;

		try {
			Path root = Paths.get(uploadDir).toAbsolutePath().normalize();
			Path target = root.resolve(dbPath).normalize();

			// 보안: ../ 같은 경로 탈출 방지
			if (!target.startsWith(root)) {
				log.warn("파일 삭제 차단(경로 탈출 시도): {}", target);
				return false;
			}

			boolean deleted = Files.deleteIfExists(target);
			if (deleted)
				log.info("Deleted file: {}", target);
			return deleted;

		} catch (Exception e) {
			log.warn("파일 삭제 실패: dbPath={}, err={}", dbPath, e.toString());
			return false;
		}
	}

}
