package com.wootube.ioi.domain.repository;

import com.wootube.ioi.domain.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllBySubscriberId(Long userId); // 나의 user id로 내가 구독한 모든 사람을 찾는 메서드

    Optional<Subscription> findBySubscriberIdAndSubscribedUserId(Long subscriberId, Long subscribedUserId);

    long countBySubscribedUserId(Long userId);
}
