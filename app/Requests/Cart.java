package Requests;

import com.fasterxml.jackson.databind.JsonNode;

public final class Cart {

    String accountId ,cartId;

    public Cart(String accountId, String cartId) {
        this.accountId = accountId;
        this.cartId = cartId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getCartId() {
        return cartId;
    }
}
