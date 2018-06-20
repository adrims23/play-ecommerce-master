package Requests;

import com.fasterxml.jackson.databind.JsonNode;

public final class DeleteCart {
    public DeleteCart(String accountId, String cartId) {
        this.accountId = accountId;
        this.cartId = cartId;
    }

    String accountId ,cartId;

    public String getAccountId() {
        return accountId;
    }

    public String getCartId() {
        return cartId;
    }
}
