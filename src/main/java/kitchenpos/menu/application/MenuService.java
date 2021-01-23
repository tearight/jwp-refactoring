package kitchenpos.menu.application;

import kitchenpos.common.application.NotFoundException;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
	static final String MSG_CANNOT_FIND_PRODUCT = "Cannot find Product by productId";
	static final String MSG_CANNOT_FIND_MENUGROUP = "Cannot find MenuGroup by menuGroupId";

	private final MenuRepository menuRepository;
	private final MenuGroupRepository menuGroupRepository;
	private final ProductRepository productRepository;

	public MenuService(final MenuRepository menuRepository,
	                   final MenuGroupRepository menuGroupRepository,
	                   final ProductRepository productRepository) {
		this.menuRepository = menuRepository;
		this.menuGroupRepository = menuGroupRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public MenuResponse create(MenuRequest menuRequest) {
		MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_MENUGROUP));
		Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
		List<MenuProduct> menuProducts = createMenuProducts(menuRequest.getMenuProductRequests());
		menu.addMenuProducts(menuProducts);
		return MenuResponse.of(menuRepository.save(menu));
	}

	private List<MenuProduct> createMenuProducts(List<MenuProductRequest> menuProductRequests) {
		List<Product> products = getProducts(menuProductRequests);
		return menuProductRequests.stream()
				.map(menuProductRequest -> createMenuProduct(products, menuProductRequest))
				.collect(Collectors.toList());
	}

	private List<Product> getProducts(List<MenuProductRequest> menuProductRequests) {
		final List<Long> productIds = menuProductRequests.stream()
				.map(MenuProductRequest::getProductId).collect(Collectors.toList());
		return productRepository.findAllById(productIds);
	}

	private MenuProduct createMenuProduct(List<Product> products, MenuProductRequest menuProductRequest) {
		Product product = findProduct(products, menuProductRequest);
		return new MenuProduct(product, menuProductRequest.getQuantity());
	}

	private Product findProduct(List<Product> products, MenuProductRequest menuProductRequest) {
		return products.stream()
				.filter(iter -> Objects.equals(menuProductRequest.getProductId(), iter.getId()))
				.findFirst()
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_PRODUCT));
	}

	public List<MenuResponse> list() {
		List<Menu> menus = menuRepository.findAllWithMenuGroupFetchJoin();
		return menus.stream()
				.map(MenuResponse::of)
				.collect(Collectors.toList());
	}
}
