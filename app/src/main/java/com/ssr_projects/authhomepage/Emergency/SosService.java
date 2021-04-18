package com.ssr_projects.authhomepage.Emergency;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ssr_projects.authhomepage.R;

import java.util.Objects;

public class SosService extends Service {
    MediaPlayer mp;

    int mediaStreamVolume;

    private static final String CHANNEL_ID = "23219AlzAssistant";
    private static final String CHANNEL_NAME = "com.ssr.Emergency.Assistant";
    SharedPreferences sharedPreferences;

    boolean isFlashEnabledInSettings, isAudioEnabledInSettings;


    CameraManager cameraManager;
    String mCameraId;
    boolean turnOnFlash = true;

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            turnFlashOn(turnOnFlash);

            turnOnFlash = !turnOnFlash;
            handler.postDelayed(runnable, 1000);
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sharedPreferences
                = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);

        isFlashEnabledInSettings
                = sharedPreferences
                .getBoolean(
                        "isSosTorchOn", false);

        isAudioEnabledInSettings
                = sharedPreferences
                .getBoolean(
                        "isSosAudioOn", false);

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        if (!isAudioEnabledInSettings && !isFlashEnabledInSettings) {
            Log.e("Service", "onStartCommand: Stopped " + isFlashEnabledInSettings + " " + isAudioEnabledInSettings);
            stopSelf();
        } else {
            startForeground(1, startForeground());
            if (Objects.equals(intent.getAction(), "ACTION_ENABLE_SOS")) {

                if (isAudioEnabledInSettings)
                    enableAudio(this);

                if (isFlashEnabledInSettings)
                    controlFlashLight(true);

            } else if (Objects.equals(intent.getAction(), "ACTION_DISABLE_SOS")) {
                stopForeground(true);

                if (isFlashEnabledInSettings)
                    controlFlashLight(false);

                if (isAudioEnabledInSettings)
                    disableAudio(this);

                stopSelf();
            }
        }

        return START_NOT_STICKY;

    }

    private Notification startForeground() {

        Intent stopIntent = new Intent(this, SosService.class);
        stopIntent.setAction("ACTION_DISABLE_SOS");
        PendingIntent stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, 0);

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getApplicationContext(), CHANNEL_ID);

        Notification notification;
        notification = mBuilder.setTicker(getString(R.string.app_name)).setWhen(0)
                .setOngoing(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getResources().getText(R.string.app_name) + " SOS procedure, Click on Turn Off to stop SOS procedure")
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(R.mipmap.ic_launcher, "Turn Off", stopPendingIntent)
                .setShowWhen(true)
                .build();

        return notification;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.release();
            }
        }
    }

    public void enableAudio(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        mediaStreamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0); //Change to max later

        Log.e(context.getClass().getName(), "adjustAudio: enabled");

        mp = MediaPlayer.create(context, R.raw.standard_5);
        mp.start();
        mp.setLooping(true);

    }

    public void disableAudio(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mediaStreamVolume, 0);

        Log.e(context.getClass().getName(), "adjustAudio: disabled");
    }

    private void controlFlashLight(boolean turnOnFlash) {

        if (turnOnFlash) {
            handler.post(runnable);
        } else {
            handler.removeCallbacks(runnable);
            turnFlashOn(false);
        }

    }

    void turnFlashOn(boolean turnOnFlash) {
        if (getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            try {
                cameraManager.setTorchMode(mCameraId, turnOnFlash);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }
}