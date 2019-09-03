package com.wootube.ioi.service;

import com.wootube.ioi.domain.model.Subscription;
import com.wootube.ioi.domain.model.User;
import com.wootube.ioi.domain.repository.SubscriptionRepository;
import com.wootube.ioi.service.dto.SubscriberResponseDto;
import com.wootube.ioi.service.dto.SubscriptionCheckResponseDto;
import com.wootube.ioi.service.dto.SubscriptionCountResponseDto;
import com.wootube.ioi.service.exception.AlreadySubscribedException;
import com.wootube.ioi.service.exception.IllegalUnsubscriptionException;
import com.wootube.ioi.service.exception.NotFoundSubscriptionException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserService userService, ModelMapper modelMapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public List<SubscriberResponseDto> findAllUsersBySubscriberId(Long subscriberId) {
        return findAllBySubscriberId(subscriberId).stream()
                .map(subscription -> modelMapper.map(subscription.getSubscriber(), SubscriberResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<Subscription> findAllBySubscriberId(Long subscriberId) {
        return subscriptionRepository.findAllBySubscriberId(subscriberId);
    }

    public SubscriptionCountResponseDto countSubscription(Long subscribedUserId) {
        return new SubscriptionCountResponseDto(subscriptionRepository.countBySubscribedUserId(subscribedUserId));
    }

    public void subscribe(Long subscriberId, Long subscribedUserId) {
        if (contains(subscriberId, subscribedUserId)) {
            throw new AlreadySubscribedException();
        }
        User subscriber = userService.findByIdAndIsActiveTrue(subscriberId);
        User subscribedUser = userService.findByIdAndIsActiveTrue(subscribedUserId);
        Subscription subscription = new Subscription(subscriber, subscribedUser);
        subscriptionRepository.save(subscription);
    }

    private boolean contains(Long subscriberId, Long subscribedUserId) {
        try {
            findSubscription(subscriberId, subscribedUserId);
            return true;
        } catch (NotFoundSubscriptionException e) {
            return false;
        }
    }

    public void unsubscribe(Long subscriberId, Long subscribedUserId) {
        if (subscriberId.equals(subscribedUserId)) {
            throw new IllegalUnsubscriptionException();
        }
        Subscription subscription = findSubscription(subscriberId, subscribedUserId);
        subscriptionRepository.delete(subscription);
    }

    private Subscription findSubscription(Long subscriberId, Long subscribedUserId) {
        return subscriptionRepository.findBySubscriberIdAndSubscribedUserId(subscriberId, subscribedUserId)
                .orElseThrow(NotFoundSubscriptionException::new);
    }

    public SubscriptionCheckResponseDto checkSubscription(Long subscriberId, Long subscribedUserId) {
        return new SubscriptionCheckResponseDto(contains(subscriberId, subscribedUserId));
    }
}
