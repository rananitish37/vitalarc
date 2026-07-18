package com.vitalarc.workout.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declares the messaging "topology": one exchange that workout-service publishes to,
 * one queue that ai-coach-service will consume from, and a binding connecting them.
 *
 * Using a topic exchange (rather than the simpler "default" exchange) means we can add
 * more event types later (e.g. "workout.deleted") and more consumers, without every
 * publisher/consumer needing to know about every other one - they only need to agree
 * on the exchange name and routing key pattern.
 */
@Configuration
public class RabbitMQConfig {

    public static final String WORKOUT_EXCHANGE = "vitalarc.workout.exchange";
    public static final String WORKOUT_LOGGED_QUEUE = "vitalarc.workout.logged.queue";
    public static final String WORKOUT_LOGGED_ROUTING_KEY = "workout.logged";

    @Bean
    public TopicExchange workoutExchange() {
        return new TopicExchange(WORKOUT_EXCHANGE);
    }

    @Bean
    public Queue workoutLoggedQueue() {
        return new Queue(WORKOUT_LOGGED_QUEUE, true); // durable - survives a RabbitMQ restart
    }

    @Bean
    public Binding workoutLoggedBinding(Queue workoutLoggedQueue, TopicExchange workoutExchange) {
        return BindingBuilder.bind(workoutLoggedQueue).to(workoutExchange).with(WORKOUT_LOGGED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        // Without this, RabbitMQ sends Java-serialized bytes - unreadable by any
        // non-Java consumer and fragile across code changes. JSON keeps messages
        // language-agnostic and human-inspectable in the RabbitMQ management UI.
        return new Jackson2JsonMessageConverter();
    }
}