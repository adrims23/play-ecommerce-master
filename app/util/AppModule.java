package util;

import akka.actor.ActorSystem;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


import javax.inject.Singleton;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    @javax.inject.Singleton
    @com.google.inject.name.Named("actorSystem")
    @javax.inject.Inject
    public ActorSystem createActorSystem(@Named("appConfig") Config config) {
        String actorSystemName = System.getProperty("ProfileService");
        if (actorSystemName == null) {
            actorSystemName = "ProfileService";
        }
        return ActorSystem.create(actorSystemName, config);
    }

    @Provides
    @Singleton
    @Named("appConfig")
    public Config createAppConfig() {
        return ConfigFactory.load();
    }
}
