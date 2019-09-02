package com.wootube.ioi.domain.repository;

import java.util.List;
import java.util.Optional;

import com.wootube.ioi.domain.model.Subscription;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
	List<Subscription> findAllBySubscriberId(Long userId);

	Optional<Subscription> findBySubscriberIdAndSubscribedUserId(Long subscriberId, Long subscribedUserId);

	Long countBySubscribedUserId(Long userId);
}
