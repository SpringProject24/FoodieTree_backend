package org.nmfw.foodietree.domain.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateDto;
import org.nmfw.foodietree.domain.product.Util.FileUtil;
import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreStatsDto;
import org.nmfw.foodietree.domain.store.mapper.StoreMyPageEditMapper;
import org.nmfw.foodietree.domain.store.mapper.StoreMyPageMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreEditService {

	private final StoreMyPageService storeMyPageService;
	private final StoreMyPageEditMapper storeMyPageEditMapper;
	private final StoreMyPageMapper storeMyPageMapper;
	@Value("${env.upload.path}")
	private String uploadDir;

	public boolean updateProfileInfo(String storeId, UpdateDto dto) {
		String type = dto.getType();
		String value = dto.getValue();

		if (type.equals("price")) {
			return true;
		} else if (type.equals("openAt")) {
			return true;
		} else if (type.equals("closedAt")) {
			return true;
		} else if (type.equals("productCnt")) {
			return true;
		} else if (type.equals("store_contact")) {
			return true;
		} else if (type.equals("store_img")) {

			return true;
		}
		return false;
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
