const subscriptionButton = (function () {
    const SubscriptionController = function () {
        const subscriptionService = new SubscriptionService();

        const subscribe = () => {
            const subscriptionButton = document.querySelector("#subscription-btn");
            subscriptionButton.addEventListener("click", subscriptionService.subscribe);
        };

        const unsubscribe = () => {
            const subscriptionButton = document.querySelector("#subscription-btn");
            subscriptionButton.addEventListener("click", subscriptionService.unsubscribe);
        };

        const countSubscription = () => {
            subscriptionService.countSubscription();
        };

        const checkSubscription = () => {
            subscriptionService.checkSubscription();
        }

        const init = function () {
            subscribe();
            unsubscribe();
            countSubscription();
            checkSubscription();
        };

        return {
            init: init
        }
    };

    const SubscriptionService = function () {
        const writerId = document.querySelector("#subscription-btn").dataset.writerid;

        const subscribe = (event) => {
            let target = event.target;

            if (event.target.tagName === "SPAN") {
                target = event.target.parentElement;
            }

            if (target.classList.contains("subscribed")) {
                return;
            }
            const uri = `/api/subscriptions/${writerId}`;
            const callback = (response) => {
                if (response.ok) {
                    countSubscription();
                    target.classList.add("subscribed");
                    return;
                }

                throw response;
            };
            const handleError = (error) => {
                alert(error);
            };

            AjaxRequest.POST(uri, {}, callback, handleError);
        };

        const unsubscribe = (event) => {
            let target = event.target;

            if (event.target.tagName === "SPAN") {
                target = event.target.parentElement;
            }

            if (!target.classList.contains("subscribed")) {
                return;
            }
            const uri = `/api/subscriptions/${writerId}`;
            const callback = (response) => {
                if (response.status === 204) {
                    countSubscription();
                    target.classList.remove("subscribed");
                    return;
                }

                throw response;
            };
            const handleError = (error) => {
                alert(error);
            };

            AjaxRequest.DELETE(uri, callback, handleError);
        };

        const countSubscription = () => {
            const uri = `/api/subscriptions/${writerId}`;
            const callback = (response) => {
                if (response.ok) {
                    const subscriptionCount = document.querySelector("#subscription-count");
                    response.json().then(count => {
                        subscriptionCount.innerText = count.count;
                    });

                    return;
                }

                throw response;
            };
            const handleError = (error) => {
                alert(error);
            };

            AjaxRequest.GET(uri, callback, handleError);
        };

        const checkSubscription = () => {
            const uri = `/api/subscriptions/${writerId}/checks`;
            const callback = (response) => {
                if (response.ok) {
                    response.json().then(subscriptionCheck => {
                        if (subscriptionCheck.subscribe) {
                            const subscriptionButton = document.querySelector("#subscription-btn");
                            subscriptionButton.classList.add("subscribed")
                        }
                    });

                    return;
                }

                throw response;
            };
            const handleError = (error) => {
                alert(error);
            };

            AjaxRequest.GET(uri, callback, handleError);
        };

        return {
            subscribe,
            unsubscribe,
            countSubscription,
            checkSubscription
        }
    };

    const init = function () {
        const buttonController = new SubscriptionController();
        buttonController.init();
    };

    return {
        init: init
    }
})
subscriptionButton().init();