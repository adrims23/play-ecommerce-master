package Service;


import Constants.RogersConstants;
import Requests.*;
import akka.actor.AbstractActor;

import akka.actor.Props;

import akka.pattern.PatternsCS;

import com.typesafe.config.ConfigFactory;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;



import javax.inject.Inject;
import java.util.concurrent.CompletionStage;


public class ServiceActor extends AbstractActor {

    private final WSClient ws;
    String coreUrl = ConfigFactory.load().getString(RogersConstants.CORE_URL);
    String cartUrl= ConfigFactory.load().getString(RogersConstants.CART_URL);
    String planUrl= ConfigFactory.load().getString(RogersConstants.PLAN_URL);
    String deviceUrl= ConfigFactory.load().getString(RogersConstants.DEVICE_URL);
    String commerceUrl=ConfigFactory.load().getString(RogersConstants.COMMERCE_URL);

    @Inject
    public ServiceActor(WSClient ws) {
        this.ws = ws;
    }

    public static Props props(WSClient ws) {
        return Props.create(ServiceActor.class, () -> new ServiceActor(ws));
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder().
                match(Plan.class, plan -> getPlans())
                .match(Device.class, device -> getDevices())
                .match(CartList.class, cartList -> getCartList(cartList))
                .match(Cart.class, cart -> getCart(cart))
                .match(DeleteCart.class, deleteCart -> deleteCart(deleteCart))
                .match(UpdateCart.class, updateCart -> updateCart(updateCart))
                .match(CreateCart.class, createCart -> createCart(createCart))
                .build();
    }


    private void getPlans() {

        CompletionStage<WSResponse> response
                = ws.url(coreUrl+planUrl).get();
        PatternsCS.pipe(response, getContext().dispatcher()).to(getSender());
    }


    private void getDevices() {

        CompletionStage<WSResponse> response
                = ws.url(coreUrl+deviceUrl).get();
        PatternsCS.pipe(response, getContext().dispatcher()).to(getSender());

    }

    private void getCartList(CartList cartList) {

        CompletionStage<WSResponse> response
                = ws.url(coreUrl + commerceUrl+cartList.getAccountId() +"/"+cartUrl).get();
        PatternsCS.pipe(response, getContext().dispatcher()).to(getSender());


    }

    private void getCart(Cart cart) {

        CompletionStage<WSResponse> response
                = ws.url(coreUrl + commerceUrl+cart.getAccountId() +  "/"+cartUrl+"/"+ cart.getCartId()).get();
        PatternsCS.pipe(response, getContext().dispatcher()).to(getSender());


    }

    private void deleteCart(DeleteCart deleteCart) {

        CompletionStage<WSResponse> response
                = ws.url(coreUrl + commerceUrl+deleteCart.getAccountId() +  "/"+cartUrl+"/" + deleteCart.getCartId()).delete();
        PatternsCS.pipe(response, getContext().dispatcher()).to(getSender());


    }


    private void updateCart(UpdateCart updateCart ) {


        CompletionStage<WSResponse> response
                = ws.url(coreUrl+commerceUrl + updateCart.getAccountId() + "/"+cartUrl+"/"+updateCart.getCartId()).put(updateCart.getNewJson());
        PatternsCS.pipe(response, getContext().dispatcher()).to(getSender());
    }


    private void createCart(CreateCart createCart) {

        CompletionStage<WSResponse> response
                = ws.url(coreUrl+ commerceUrl+
                createCart.getAccountId()+"/"+cartUrl).post(createCart.getNewJson());
        PatternsCS.pipe(response, getContext().dispatcher()).to(getSender());

    }

}