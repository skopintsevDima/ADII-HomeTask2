package com.example.skopi.rxsolutionforpermissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends RxAppCompatActivity {

    public static final int PERSMISSION_REQUEST_CODE = 1111;

    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
    };

    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public Boolean checkPermission(String permission)
    {
        return ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(String permission)
    {
        String[] permissions = {permission};
        ActivityCompat.requestPermissions(this, permissions, PERSMISSION_REQUEST_CODE);
    }

    private void tryToGrantPermissions()
    {
        Observable.from(PERMISSIONS)
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String permission) {
                        return checkPermission(permission);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String permission) {
                        requestPermission(permission);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERSMISSION_REQUEST_CODE) {
            subscriptions.add(Observable.from(permissions)
            .subscribe(new Action1<String>() {
                @Override
                public void call(String result) {
                    //
                }
            }));
        }
    }
}
