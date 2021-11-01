package ra.enterwind.lapaskumala;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmCallManager;
import io.agora.rtm.RtmClient;
import ra.enterwind.lapaskumala.activities.HomeActivity;
import ra.enterwind.lapaskumala.agora.Config;
import ra.enterwind.lapaskumala.agora.EngineEventListener;
import ra.enterwind.lapaskumala.agora.Global;
import ra.enterwind.lapaskumala.agora.IEventListener;
import ra.enterwind.lapaskumala.utils.FileUtil;

public class OpenDuoApplication extends Application {

    private static final String TAG = OpenDuoApplication.class.getSimpleName();

    private RtcEngine mRtcEngine;
    private RtmClient mRtmClient;
    private RtmCallManager rtmCallManager;
    private EngineEventListener mEventListener;
    private Config mConfig;
    private Global mGlobal;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new FirebaseNotificationOpenedHandler(this))
                .init();
    }

    public class FirebaseNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        Context ctx;
        FirebaseNotificationOpenedHandler(Context context) {
            ctx = context;
        }
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            Toast.makeText(ctx, "Halo, saya klik notifikasi ya", Toast.LENGTH_SHORT).show();
            if (data != null) {
                String customKey = data.optString("customkey", null);
                String lagikey = data.optString("lagikey", null);
                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);
                if (lagikey != null)
                    Log.i("OneSignalExample", "lagikey set with value: " + lagikey);
            }
            if (actionType == OSNotificationAction.ActionType.ActionTaken)
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }
    }

    private void init() {
        initConfig();
        initEngine();
    }

    private void initConfig() {
        mConfig = new Config(getApplicationContext());
        mGlobal = new Global();
    }

    private void initEngine() {
        String appId = getString(R.string.agora_app_id);
        if (TextUtils.isEmpty(appId)) {
            throw new RuntimeException("NEED TO use your App ID, get your own ID at https://dashboard.agora.io/");
        }

        mEventListener = new EngineEventListener();
        try {
            mRtcEngine = RtcEngine.create(getApplicationContext(), appId, mEventListener);
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.enableDualStreamMode(true);
            mRtcEngine.enableVideo();
            mRtcEngine.setLogFile(FileUtil.rtmLogFile(getApplicationContext()));

            mRtmClient = RtmClient.createInstance(getApplicationContext(), appId, mEventListener);
            mRtmClient.setLogFile(FileUtil.rtmLogFile(getApplicationContext()));

            if (Config.DEBUG) {
                mRtcEngine.setParameters("{\"rtc.log_filter\":65535}");
                mRtmClient.setParameters("{\"rtm.log_filter\":65535}");
            }

            rtmCallManager = mRtmClient.getRtmCallManager();
            rtmCallManager.setEventListener(mEventListener);

            // By default do not use rtm token
            mRtmClient.login(null, mConfig.getUserId(), new ResultCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i(TAG, "rtm client login success");
                }

                @Override
                public void onFailure(ErrorInfo errorInfo) {
                    Log.i(TAG, "rtm client login failed:" + errorInfo.getErrorDescription());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RtcEngine rtcEngine() {
        return mRtcEngine;
    }

    public RtmClient rtmClient() {
        return mRtmClient;
    }

    public void registerEventListener(IEventListener listener) {
        mEventListener.registerEventListener(listener);
    }

    public void removeEventListener(IEventListener listener) {
        mEventListener.removeEventListener(listener);
    }

    public RtmCallManager rtmCallManager() {
        return rtmCallManager;
    }

    public Config config() {
        return mConfig;
    }

    public Global global() {
        return mGlobal;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        destroyEngine();
    }

    private void destroyEngine() {
        RtcEngine.destroy();

        mRtmClient.logout(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "rtm client logout success");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.i(TAG, "rtm client logout failed:" + errorInfo.getErrorDescription());
            }
        });
    }

}
