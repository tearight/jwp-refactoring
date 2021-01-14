package kitchenpos.ui;

import kitchenpos.MockMvcTest;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductRestControllerTest extends MockMvcTest {

	@DisplayName("상품을 생성한다.")
	@Test
	void create() throws Exception {
		ProductRequest productRequest = new ProductRequest("제품", new BigDecimal(1000L));

		MvcResult mvcResult = mockMvc.perform(postAsJson("/api/products", productRequest))
				.andExpect(status().isCreated())
				.andReturn();

		ProductResponse created = toObject(mvcResult, ProductResponse.class);

		assertThat(created.getId()).isNotNull();
		assertThat(created.getName()).isEqualTo("제품");
		assertThat(created.getPrice().longValue()).isEqualTo(1000L);
	}

	@DisplayName("상품을 조회한다.")
	@Test
	void list() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/products")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		List<ProductResponse> products = toList(mvcResult, ProductResponse.class);
		assertThat(products).isNotEmpty();
	}
}
