package Requests;

import com.fasterxml.jackson.databind.JsonNode;

public final class CartList {
String accountId;

    public CartList(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }


}
