package kitchenpos.order.domain;

import kitchenpos.common.BaseSeqEntity;
import kitchenpos.common.Quantity;
import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem extends BaseSeqEntity {

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@ManyToOne
	@JoinColumn(name = "menu_id", nullable = false)
	private Menu menu;

	@Embedded
	private Quantity quantity;

	OrderLineItem() {
	}

	OrderLineItem(Order order, Menu menu, Quantity quantity) {
		this.order = order;
		this.menu = menu;
		this.quantity = quantity;
	}

	public Order getOrder() {
		return order;
	}

	public Menu getMenu() {
		return menu;
	}

	public Quantity getQuantity() {
		return quantity;
	}
}
