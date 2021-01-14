package kitchenpos.dto;

public class OrderTableRequest_Create {
	private int numberOfGuests;
	private boolean empty;

	public OrderTableRequest_Create() {
	}

	public OrderTableRequest_Create(int numberOfGuests, boolean empty) {
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}
}
