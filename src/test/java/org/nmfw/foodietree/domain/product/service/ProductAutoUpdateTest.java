package org.nmfw.foodietree.domain.product.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductAutoUpdateTest {
	@Test
	void test() {
		JSONParser parser = new JSONParser();
		Random random = new Random();

		try (FileReader reader = new FileReader("src/test/resources/dummy.json")) {
			// Read JSON file
			Object obj = parser.parse(reader);

			JSONArray jsonArray = (JSONArray) obj;
			jsonArray.forEach(item -> {
				JSONObject jsonObject = (JSONObject) item;
				int randomNumber = 1 + random.nextInt(9); // Generate a random number between 1 and 9
				jsonObject.put("productCnt", randomNumber);
			});

			// Write JSON file
			try (FileWriter file = new FileWriter("src/test/resources/dummy.json")) {
				file.write(jsonArray.toJSONString());
				file.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

}