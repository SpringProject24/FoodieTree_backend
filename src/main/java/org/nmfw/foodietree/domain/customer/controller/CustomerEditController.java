package org.nmfw.foodietree.domain.customer.controller;

import java.io.File;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.UpdateDto;
import org.nmfw.foodietree.domain.customer.service.CustomerMyPageService;
import org.nmfw.foodietree.domain.product.Util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CustomerEditController {

	@Value("${env.upload.path}")
	private String uploadDir;
	private final CustomerMyPageService customerMyPageService;

	@PostMapping("/customer/edit/img")
	public ResponseEntity<?> imageUpload(@RequestParam("customerImg") MultipartFile customerImg) {
		String customerId = "test@gmail.com";
		try {
			if (!customerImg.isEmpty()) {
				File dir = new File(uploadDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				String imagePath = FileUtil.uploadFile(uploadDir, customerImg);
				UpdateDto dto = UpdateDto.builder()
					.type("profile_image")
					.value(imagePath)
					.build();
				customerMyPageService.updateCustomerInfo(customerId, List.of(dto));
				return ResponseEntity.ok().body(true);
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.badRequest().body(false);
	}

	/**
	 *
	 * @method   insertPreferred
	 * @return   ResponseEntity<?> type
	 * @author   hoho
	 * @date     2024 07 24 15:04
	 *
	 * {
	 *  	type: food or area or store
	 *  	value: string
	 * }
	 */

	@PostMapping("/customer/edit")
	public ResponseEntity<?> insertPreferred(@RequestBody UpdateDto dto) {
		String customerId = "test@gmail.com";
		boolean flag = customerMyPageService.updateCustomerInfo(customerId, List.of(dto));
		if (flag)
			return ResponseEntity.ok().body(true);
		return ResponseEntity.badRequest().body(false);
	}

	/**
	 *
	 * @method   deletePreferred
	 * @param    dto
	 * @return   ResponseEntity<?> type
	 * @author   hoho
	 * @date     2024 07 24 16:29
	 *
	 * {
	 *     type : food or area or store
	 *     value : string
	 * }
	 */
	@DeleteMapping("/customer/edit")
	public ResponseEntity<?> deletePreferred(@RequestBody UpdateDto dto) {
		String customerId = "test@gmail.com";
		customerMyPageService.deleteCustomerInfo(customerId, List.of(dto));
		return ResponseEntity.ok().body(true);
	}

	/**
	 *
	 * @method   editInfo
	 * @param    dto
	 * @return   ResponseEntity<?> type
	 * @author   hoho
	 * @date     2024 07 24 15:03
	 * {
	 *     type: string (nickname or phone_number)
	 *     value: string
	 * }
	 */
	@PatchMapping("/customer/edit")
	public ResponseEntity<?> editInfo(@RequestBody UpdateDto dto) {
		String customerId = "test@gmail.com";
		boolean flag = customerMyPageService.updateCustomerInfo(customerId, List.of(dto));
		if (flag)
			return ResponseEntity.ok().body(true);
		return ResponseEntity.badRequest().body(false);
	}
}
