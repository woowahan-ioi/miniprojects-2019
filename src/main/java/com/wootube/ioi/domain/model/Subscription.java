package com.wootube.ioi.domain.model;

import com.wootube.ioi.domain.exception.IllegalSubscriptionException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Subscription extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_subscriber"), nullable = false)
    private User subscriber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_subscribed_user"), nullable = false)
    private User subscribedUser;

    public Subscription(User subscriber, User subscribedUser) {
        if (subscriber.equals(subscribedUser)) {
            throw new IllegalSubscriptionException();
        }
        this.subscriber = subscriber;
        this.subscribedUser = subscribedUser;
    }
}
