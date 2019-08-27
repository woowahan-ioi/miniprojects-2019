package com.wootube.ioi.web.controller.api;

import com.wootube.ioi.service.SubscriptionService;
import com.wootube.ioi.service.dto.SubscriberResponseDto;
import com.wootube.ioi.service.dto.SubscriptionCountResponseDto;
import com.wootube.ioi.web.session.UserSession;
import com.wootube.ioi.web.session.UserSessionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/subscriptions")
@RestController
public class SubscriptionApiController {
    private final SubscriptionService subscriptionService;
    private final UserSessionManager userSessionManager;

    @Autowired
    public SubscriptionApiController(SubscriptionService subscriptionService, UserSessionManager userSessionManager) {
        this.subscriptionService = subscriptionService;
        this.userSessionManager = userSessionManager;
    }

    @GetMapping
    public ResponseEntity<List<SubscriberResponseDto>> subscriptions() {
        UserSession userSession = userSessionManager.getUserSession();
        List<SubscriberResponseDto> subscriberResponseDtos = subscriptionService.findAllUsersBySubscriberId(userSession.getId());

        return ResponseEntity.ok()
                .body(subscriberResponseDtos);
    }

    @GetMapping("/{subscribedUserId}")
    public ResponseEntity countSubscription(@PathVariable Long subscribedUserId) {
        long subscriptionCount = subscriptionService.countSubscription(subscribedUserId);
        return ResponseEntity.ok()
                .body(new SubscriptionCountResponseDto(subscriptionCount));
    }

    @PostMapping("/{subscribedUserId}")
    public ResponseEntity subscribe(@PathVariable Long subscribedUserId) {
        UserSession sessionUser = userSessionManager.getUserSession();
        subscriptionService.subscribe(sessionUser.getId(), subscribedUserId);

        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping("/{subscribedUserId}")
    public ResponseEntity unsubscribe(@PathVariable Long subscribedUserId) {
        UserSession sessionUser = userSessionManager.getUserSession();
        subscriptionService.unsubscribe(sessionUser.getId(), subscribedUserId);

        return ResponseEntity.noContent()
                .build();
    }
}
