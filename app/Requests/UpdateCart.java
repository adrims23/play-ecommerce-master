package Requests;

import com.fasterxml.jackson.databind.JsonNode;

public final class UpdateCart {
    public JsonNode getNewJson() {
        return newJson;
    }

    String accountId ,cartId;
    JsonNode newJson;

    public UpdateCart(String accountId, String cartId,JsonNode newJson) {
        this.accountId = accountId;
        this.cartId = cartId;

        this.newJson = newJson;    }

    public String getAccountId() {
        return accountId;
    }

    public String getCartId() {
        return cartId;
    }
}
