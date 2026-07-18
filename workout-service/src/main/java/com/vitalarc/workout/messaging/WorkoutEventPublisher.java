package com.vitalarc.workout.messaging;

import com.vitalarc.workout.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class WorkoutEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public WorkoutEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishWorkoutLogged(UUID userId, UUID workoutId) {
        WorkoutLoggedEvent event = new WorkoutLoggedEvent(userId, workoutId, Instant.now());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.WORKOUT_EXCHANGE,
                RabbitMQConfig.WORKOUT_LOGGED_ROUTING_KEY,
                event
        );
    }
}