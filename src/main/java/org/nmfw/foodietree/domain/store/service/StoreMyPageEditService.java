package org.nmfw.foodietree.domain.store.service;

import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.product.Util.FileUtil;
import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreStatsDto;
import org.nmfw.foodietree.domain.store.mapper.StoreMyPageEditMapper;
import org.nmfw.foodietree.domain.store.mapper.StoreMyPageMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreMyPageEditService {

	private final StoreMyPageService storeMyPageService;
	private final StoreMyPageEditMapper storeMyPageEditMapper;
	private final StoreMyPageMapper storeMyPageMapper;
	@Value("${env.upload.path}")
	private String uploadDir;

	public StoreMyPageDto getStoreMyPageInfo(String storeId) {
		log.info("store my page service");
		return storeMyPageMapper.getStoreMyPageInfo(storeId);
	}

	public StoreStatsDto getStats(String storeId) {
		log.info("get stats");
		return storeMyPageService.getStats(storeId);
	}

	public boolean updateStoreInfo(String storeId, String type, String value) {
		log.info("update store info");
		boolean flag;
		switch (type) {
			case "price":
				flag = updatePrice(storeId, Integer.parseInt(value));
				break;
			case "openAt":
				flag = updateOpenAt(storeId, value);
				break;
			case "closedAt":
				flag = updateClosedAt(storeId, value);
				break;
			case "productCnt":
				flag = updateProductCnt(storeId, Integer.parseInt(value));
				break;
			default:
				flag = storeMyPageEditMapper.updateStoreInfo(storeId, type, value);
				break;
		}
		return flag;
	}

	public boolean updatePrice(String storeId, int price) {
		log.info("update price");
		return storeMyPageEditMapper.updatePrice(storeId, price);
	}

	public boolean updateOpenAt(String storeId, String time) {
		log.info("update open at");
		time = time.replaceAll("\"", "");
		LocalTime openAt = LocalTime.parse(time);
		return storeMyPageEditMapper.updateOpenAt(storeId, openAt);
	}

	public boolean updateClosedAt(String storeId, String time) {
		log.info("update closed at");
		// 시간 문자열에서 따옴표 등을 제거
		time = time.replaceAll("\"", "");
		try {
			// 시간 문자열을 LocalTime으로 파싱
			LocalTime closedAt = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));

			// Mapper를 통해 업데이트 실행
			return storeMyPageEditMapper.updateClosedAt(storeId, closedAt);
		} catch (DateTimeParseException e) {
			log.error("Failed to parse time string: {}", time);
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateProductCnt(String storeId, int cnt) {
		log.info("update product cnt");
		return storeMyPageEditMapper.updateProductCnt(storeId, cnt);
	}

	public boolean updateProfileImg(String storeId, MultipartFile storeImg) {
		try {
			if (!storeImg.isEmpty()) {
				File dir = new File(uploadDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				String imagePath = FileUtil.uploadFile(uploadDir, storeImg);
				return updateStoreInfo(storeId, "store_img", imagePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
