package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.Subscription;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.repository.SubscriptionRepository;
import com.wootube.ioi.service.dto.SubscriptionCheckResponseDto;
import com.wootube.ioi.service.exception.AlreadySubscribedException;
import com.wootube.ioi.service.exception.IllegalUnsubscriptionException;
import com.wootube.ioi.service.exception.NotFoundSubscriptionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class SubscriptionServiceTest {

    public static final long SUBSCRIBER_ID = 1L;
    public static final long SUBSCRIBED_USER_ID = 2L;
    @InjectMocks
    private SubscriptionService subscriptionService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserService userService;

    @Mock
    private User subscriber;

    @Mock
    private User subscribedUser;

    @DisplayName("구독자 수 세는 메서드 잘 작동하는지 확인")
    @Test
    void countSubscription() {
        subscriptionService.countSubscription(SUBSCRIBED_USER_ID);
        verify(subscriptionRepository).countBySubscribedUserId(SUBSCRIBED_USER_ID);
        verify(subscriptionRepository, never()).countBySubscribedUserId(SUBSCRIBER_ID);
    }

    @Test
    @DisplayName("구독 성공")
    void subscribe() {
        given(subscriptionRepository.findBySubscriberIdAndSubscribedUserId(SUBSCRIBER_ID, SUBSCRIBED_USER_ID))
                .willThrow(NotFoundSubscriptionException.class);

        given(userService.findByIdAndIsActiveTrue(SUBSCRIBER_ID)).willReturn(subscriber);
        given(userService.findByIdAndIsActiveTrue(SUBSCRIBED_USER_ID)).willReturn(subscribedUser);

        subscriptionService.subscribe(SUBSCRIBER_ID, SUBSCRIBED_USER_ID);

        verify(subscriptionRepository).save(new Subscription(subscriber, subscribedUser));
    }

    @Test
    @DisplayName("구독 실패 (이미 구독중)")
    void subscribeWhenAlreadySubscribe() {
        given(subscriptionRepository.findBySubscriberIdAndSubscribedUserId(SUBSCRIBER_ID, SUBSCRIBED_USER_ID))
                .willReturn(Optional.of(new Subscription(subscriber, subscribedUser)));

        given(userService.findByIdAndIsActiveTrue(SUBSCRIBER_ID)).willReturn(subscriber);
        given(userService.findByIdAndIsActiveTrue(SUBSCRIBED_USER_ID)).willReturn(subscribedUser);

        assertThrows(AlreadySubscribedException.class, () -> subscriptionService.subscribe(SUBSCRIBER_ID, SUBSCRIBED_USER_ID));
    }

    @Test
    @DisplayName("구독 취소 성공")
    void unsubscribe() {
        given(subscriptionRepository.findBySubscriberIdAndSubscribedUserId(SUBSCRIBER_ID, SUBSCRIBED_USER_ID))
                .willReturn(Optional.of(new Subscription(subscriber, subscribedUser)));

        subscriptionService.unsubscribe(SUBSCRIBER_ID, SUBSCRIBED_USER_ID);

        verify(subscriptionRepository).delete(new Subscription(subscriber, subscribedUser));
    }

    @Test
    @DisplayName("구독 취소 실패 (id가 같을 때)")
    void unsubscribeWhenEqualId() {
        assertThrows(IllegalUnsubscriptionException.class, () -> subscriptionService.unsubscribe(SUBSCRIBER_ID, SUBSCRIBER_ID));
    }

    @Test
    @DisplayName("구독 취소 실패 (구독 정보가 없을 때)")
    void unsubscribeWhenNoSubscription() {
        given(subscriptionRepository.findBySubscriberIdAndSubscribedUserId(SUBSCRIBER_ID, SUBSCRIBED_USER_ID))
                .willThrow(NotFoundSubscriptionException.class);

        assertThrows(NotFoundSubscriptionException.class, () -> subscriptionService.unsubscribe(SUBSCRIBER_ID, SUBSCRIBED_USER_ID));
    }

    @Test
    @DisplayName("구독 상태 확인 (구독 중일 때)")
    void checkSubscription() {
        given(subscriptionRepository.findBySubscriberIdAndSubscribedUserId(SUBSCRIBER_ID, SUBSCRIBED_USER_ID))
                .willReturn(Optional.of(new Subscription(subscriber, subscribedUser)));

        SubscriptionCheckResponseDto response = subscriptionService.checkSubscription(SUBSCRIBER_ID, SUBSCRIBED_USER_ID);
        assertTrue(response.isSubscribe());
    }

    @Test
    @DisplayName("구독 상태 확인 (구독 중이 아닐 때)")
    void checkNotSubscription() {
        given(subscriptionRepository.findBySubscriberIdAndSubscribedUserId(SUBSCRIBER_ID, SUBSCRIBED_USER_ID))
                .willThrow(NotFoundSubscriptionException.class);

        SubscriptionCheckResponseDto response = subscriptionService.checkSubscription(SUBSCRIBER_ID, SUBSCRIBED_USER_ID);
        assertFalse(response.isSubscribe());
    }
}