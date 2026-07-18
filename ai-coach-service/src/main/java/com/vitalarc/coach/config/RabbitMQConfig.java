package com.vitalarc.coach.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declares the SAME exchange/queue/binding names as workout-service. This is safe
 * and idempotent - RabbitMQ just confirms they already exist rather than erroring -
 * and it means this service doesn't depend on workout-service happening to start
 * first. Whichever service starts first "wins" and creates the topology; the other
 * just confirms it matches.
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
        return new Queue(WORKOUT_LOGGED_QUEUE, true);
    }

    @Bean
    public Binding workoutLoggedBinding(Queue workoutLoggedQueue, TopicExchange workoutExchange) {
        return BindingBuilder.bind(workoutLoggedQueue).to(workoutExchange).with(WORKOUT_LOGGED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}