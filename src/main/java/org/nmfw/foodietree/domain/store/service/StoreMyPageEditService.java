package org.nmfw.foodietree.domain.store.service;

import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateDto;
import org.nmfw.foodietree.domain.product.Util.FileUtil;
import org.nmfw.foodietree.domain.store.entity.Store;
import org.nmfw.foodietree.domain.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreMyPageEditService {

	@Value("${env.upload.path}")
	private String uploadDir;
	private final StoreRepository storeRepository;

	public boolean updateProfileInfo(String storeId, UpdateDto dto) {
		String type = dto.getType();
		String value = dto.getValue();
		Store found = storeRepository.findByStoreId(storeId).orElseThrow();
		if (type.equals("price")) {
			found.setPrice(Integer.parseInt(value));
			storeRepository.save(found);
			return true;
		} else if (type.equals("openAt")) {
			found.setOpenAt(parseTime(value));
			storeRepository.save(found);
			return true;
		} else if (type.equals("closedAt")) {
			found.setClosedAt(parseTime(value));
			storeRepository.save(found);
			return true;
		} else if (type.equals("productCnt")) {
			found.setProductCnt(Integer.parseInt(value));
			storeRepository.save(found);
			return true;
		} else if (type.equals("store_contact")) {
			found.setStoreContact(value);
			storeRepository.save(found);
			return true;
		} else if (type.equals("store_img")) {
			found.setStoreImg(value);
			storeRepository.save(found);
			return true;
		}
		return false;
	}

	private LocalTime parseTime(String time) {
		time = time.replaceAll("\"", "");
		return LocalTime.parse(time);
	}

	public boolean updateProfileImg(String storeId, MultipartFile storeImg) {
		try {
			if (!storeImg.isEmpty()) {
				File dir = new File(uploadDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				String imagePath = FileUtil.uploadFile(uploadDir, storeImg);
				return updateProfileInfo(storeId, UpdateDto.builder().type("store_img").value(imagePath).build());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
