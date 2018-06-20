package Requests;

import com.fasterxml.jackson.databind.JsonNode;

public final class CreateCart {
    public CreateCart(String accountId,  JsonNode newJson) {
        this.accountId = accountId;

        this.newJson = newJson;
    }

    String accountId ;
    JsonNode newJson;

    public String getAccountId() {
        return accountId;
    }



    public JsonNode getNewJson() {
        return newJson;
    }
}
