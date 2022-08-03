package org.example.zhc.util.zhc.validation.mongo.helper;

import com.mongodb.bulk.BulkWriteResult;
import lombok.val;
import org.bson.Document;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * 订阅辅助库
 * @author aisen
 */
public final class SubscriberHelpers {
    public static class ObservableSubscriber<T> implements Subscriber<T> {
        private final List<T> received;
        private final List<Throwable> errors;
        private volatile Subscription subscription;
        private volatile boolean completed;

        ObservableSubscriber() {
            this.received = new ArrayList<T>();
            this.errors = new ArrayList<Throwable>();
        }

        @Override
        public void onSubscribe(final Subscription s) {
            subscription = s;
        }

        @Override
        public void onNext(final T t) {
            received.add(t);
        }

        @Override
        public void onError(final Throwable t) {
            errors.add(t);
            onComplete();
        }

        @Override
        public void onComplete() {
            completed = true;
        }

        public Subscription getSubscription() {
            return subscription;
        }

        public List<T> getReceived() {
            return received;
        }

        public Throwable getError() {
            if (errors.size() > 0) {
                return errors.get(0);
            }
            return null;
        }

        public boolean isCompleted() {
            return completed;
        }

    }

    /**
     * A Subscriber that immediately requests Integer.MAX_VALUE onSubscribe
     *
     * @param <T> The publishers result type
     */
    public static class OperationSubscriber<T> extends ObservableSubscriber<T> {

        @Override
        public void onSubscribe(final Subscription s) {
            super.onSubscribe(s);
            s.request(Integer.MAX_VALUE);
        }
    }

    /**
     * A Subscriber that prints a message including the received items on completion
     *
     * @param <T> The publishers result type
     */
    public static class PrintSubscriber<T> extends OperationSubscriber<T> {
        private final String message;

        /**
         * A Subscriber that outputs a message onComplete.
         *
         * @param message the message to output onComplete
         */
        public PrintSubscriber(final String message) {
            this.message = message;
        }

        @Override
        public void onComplete() {
            super.onComplete();
            System.out.println(format(message, getReceived()));
        }
    }
    /**
     * update subscriber
     */
    public static class BulkUpdateSubscribe extends OperationSubscriber<BulkWriteResult>{
        public BulkUpdateSubscribe(){

        }
        @Override
        public void onComplete(){
            try{
                super.onComplete();
                val a = getReceived();
            }catch (Exception e){

            }
        }
    }

    private SubscriberHelpers() {
    }
}
