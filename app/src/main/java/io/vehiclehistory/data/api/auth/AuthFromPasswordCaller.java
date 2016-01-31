package io.vehiclehistory.data.api.auth;

import javax.inject.Inject;

import io.vehiclehistory.api.consts.Credentials;
import io.vehiclehistory.api.consts.Settings;
import io.vehiclehistory.api.model.Auth;
import io.vehiclehistory.data.api.view.AuthMvpView;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class AuthFromPasswordCaller {

    private final AuthApiService authApiService;

    private AuthMvpView authMvpView;
    private OnAuthCallback finishedListener;

    private Subscription subscription;

    private int retry = 0;

    @Inject
    public AuthFromPasswordCaller(AuthApiService authApiService) {
        this.authApiService = authApiService;
    }

    public void auth(AuthMvpView authMvpView, OnAuthCallback finishedListener) {
        this.authMvpView = authMvpView;
        this.finishedListener = finishedListener;
        preCall();
    }

    private void preCall() {
        ++retry;

        if (retry > Settings.RETRY_COUNT) {
            Timber.e("Reached max retries: %d", retry);
            authMvpView.onPasswordFailed();
            finishedListener.onError();
            return;
        }

        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        subscription = auth();
    }

    private Subscription auth() {
        return authApiService.getSessionFromPassword(
                Credentials.LOGIN,
                Credentials.PASSWORD,
                Credentials.CLIENT,
                Credentials.PASSWORD_GRANT_TYPE
        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //nop
                    }
                })
                .subscribe(new Subscriber<Auth>() {
                    @Override
                    public void onCompleted() {
                        //nop
                    }

                    @Override
                    public void onError(Throwable e) {
                        preCall();
                    }

                    @Override
                    public void onNext(Auth auth) {
                        authMvpView.onAuth(auth);
                        finishedListener.onSuccess(auth);
                    }
                });
    }
}