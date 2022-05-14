package com.livetheoogway.teflon.rmq.actor;

import io.appform.dropwizard.actors.actor.ActorConfig;
import lombok.Data;

/**
 * @author tushar.naik
 * @version 1.0  21/09/17 - 4:46 AM
 */
@Data
public class TeflonConfig {
    private String classPath;
    private ActorConfig actorConfig;
}
