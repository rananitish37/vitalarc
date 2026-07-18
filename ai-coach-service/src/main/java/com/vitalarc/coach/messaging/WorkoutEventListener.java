package com.vitalarc.coach.messaging;

import com.vitalarc.coach.config.RabbitMQConfig;
import com.vitalarc.coach.service.CoachService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WorkoutEventListener {

    private static final Logger log = LoggerFactory.getLogger(WorkoutEventListener.class);

    private final CoachService coachService;

    public WorkoutEventListener(CoachService coachService) {
        this.coachService = coachService;
    }

    @RabbitListener(queues = RabbitMQConfig.WORKOUT_LOGGED_QUEUE)
    public void onWorkoutLogged(WorkoutLoggedEvent event) {
        log.info("Received workout.logged event for user {}, workout {}", event.userId(), event.workoutId());

        // If this fails (e.g. Claude API hiccup), we deliberately don't rethrow -
        // losing one auto-generated recommendation isn't worth crashing message
        // processing or triggering RabbitMQ's redelivery loop. The user can still
        // get a recommendation on-demand via GET /api/coach/recommendation.
        try {
            coachService.generateRecommendation(event.userId());
        } catch (Exception e) {
            log.error("Failed to generate recommendation for user {}: {}", event.userId(), e.getMessage());
        }
    }
}