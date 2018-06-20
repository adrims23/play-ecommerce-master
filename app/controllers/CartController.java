package controllers;

import Requests.*;
import Service.ServiceActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Scheduler;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import play.libs.ws.*;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import scala.concurrent.ExecutionContext;
import scala.concurrent.ExecutionContextExecutor;

import javax.inject.Inject;

import scala.concurrent.duration.FiniteDuration;
import util.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;


/**
 * This controller contains an action that demonstrates how to write
 * simple asynchronous code in a controller. It uses a timer to
 * asynchronously delay sending a response for 1 second.
 */

public class CartController extends Controller {

    private static final Injector injector = Guice.createInjector(new AppModule());
    final ActorSystem system = injector.getInstance(Key.get(ActorSystem.class, Names.named("actorSystem")));
    private final WSClient ws;
    ActorRef servicActorRef;

    @Inject
    public CartController(WSClient ws) {
        this.ws = ws;
        this.servicActorRef = system.actorOf(ServiceActor.props(ws));
    }

    /*** @param actorSystem We need the {@link ActorSystem}'s
     * {@link Scheduler} to run code after a delay.
     * //@param exec We need a Java {@link Executor} to apply the result
     * of the {@link CompletableFuture} and a Scala
     * {@link ExecutionContext} so we can use the Akka {@link Scheduler}.
     * An {@link ExecutionContextExecutor} implements both interfaces.
     //     @Inject
     //    public AsyncController(ActorSystem actorSystem, ExecutionContextExecutor exec) {
     //
     //      this.exec = exec;
     //    }

     /**
     * An action that returns a plain text message after a delay
     * of 1 second.
     * <p>
     * The configuration in the <code>routes</code> file means that this method
     * will be called when the application receives a <code>GET</code> request with
     * a path of <code>/message</code>.
     */
    public CompletionStage<Result> plans() {

        Plan plan = new Plan();
        CompletionStage<Object> Result = PatternsCS.ask(servicActorRef,
                plan, new Timeout(FiniteDuration.create(100, TimeUnit.SECONDS)));
        CompletionStage<WSResponse> Result1 = Result.thenApply(o -> (WSResponse) o);
        return (Result1.thenApply(wsResponse -> ok(wsResponse.asJson())));
    }

    public CompletionStage<Result> devices() {

        Device device = new Device();
        CompletionStage<Object> Result = PatternsCS.ask(servicActorRef,
                device, new Timeout(FiniteDuration.create(100, TimeUnit.SECONDS)));
        CompletionStage<WSResponse> Result1 = Result.thenApply(o -> (WSResponse) o);
        return (Result1.thenApply(wsResponse -> ok(wsResponse.asJson())));
    }


    public CompletionStage<Result> getCartList(String AccountID) {

        CartList cartList = new CartList(AccountID);
        CompletionStage<Object> Result = PatternsCS.ask(servicActorRef,
                cartList, new Timeout(FiniteDuration.create(100, TimeUnit.SECONDS)));
        CompletionStage<WSResponse> Result1 = Result.thenApply(o -> (WSResponse) o);
        return (Result1.thenApply(wsResponse -> ok(wsResponse.asJson())));
    }

    public CompletionStage<Result> getCart(String AccountID, String cartId) {

        Cart cart = new Cart(AccountID,cartId);
        CompletionStage<Object> Result = PatternsCS.ask(servicActorRef,
                cart, new Timeout(FiniteDuration.create(1000, TimeUnit.SECONDS)));
        CompletionStage<WSResponse> Result1 = Result.thenApply(o -> (WSResponse) o);
        return (Result1.thenApply(wsResponse -> ok(wsResponse.asJson())));
    }

    public CompletionStage<Result> deleteCart(String AccountID, String cartId) {

        DeleteCart deleteCart = new DeleteCart(AccountID,cartId);
        CompletionStage<Object> Result = PatternsCS.ask(servicActorRef,
                deleteCart, new Timeout(FiniteDuration.create(100, TimeUnit.SECONDS)));
        CompletionStage<WSResponse> Result1 = Result.thenApply(o -> (WSResponse) o);
        return (Result1.thenApply(wsResponse -> ok(wsResponse.asJson())));
    }


    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> updateCart(String AccountID, String cartId) {

        JsonNode json = request().body().asJson();
        CharSequence c = json.get("cartStatus").asText();


        boolean bc = org.apache.commons.lang3.StringUtils.isEmpty(c);

        if (bc || json.get("activitylist").isNull()) {
//
            return CompletableFuture.completedFuture(
                    Results.internalServerError("PLease provide proper activitylist  and cartlist "));


        } else {
            UpdateCart updateCart= new UpdateCart(AccountID,cartId,json);
            CompletionStage<Object> Resultjson = PatternsCS.ask(servicActorRef,
                    updateCart, new Timeout(FiniteDuration.create(100, TimeUnit.SECONDS)));
            CompletionStage<WSResponse> Resultjson1 = Resultjson.thenApply(o -> (WSResponse) o);
            return (Resultjson1.thenApply(wsResponse -> ok(wsResponse.asJson())));
        }
    }


    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> createCart(String AccountID) {

        JsonNode json = request().body().asJson();
        CharSequence c = json.get("cartStatus").asText();

        boolean bc = org.apache.commons.lang3.StringUtils.isEmpty(c);


        if (bc || json.get("activityMap").isNull()) {

            return CompletableFuture.completedFuture(
                    Results.internalServerError("PLease provide proper activityMap  and cartlist "));


        } else {

            CreateCart createCart = new CreateCart(AccountID,json);
            CompletionStage<Object> Resultjson = PatternsCS.ask(servicActorRef,
                    createCart, new Timeout(FiniteDuration.create(100, TimeUnit.SECONDS)));
            CompletionStage<WSResponse> Resultjson1 = Resultjson.thenApply(o -> (WSResponse) o);
            return (Resultjson1.thenApply(wsResponse -> ok(wsResponse.asJson())));
        }

    }

}