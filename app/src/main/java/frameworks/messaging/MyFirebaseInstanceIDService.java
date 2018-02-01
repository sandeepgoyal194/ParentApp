package frameworks.messaging;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import frameworks.appsession.AppBaseApplication;
import frameworks.retrofit.ResponseResolver;
import frameworks.retrofit.RestError;
import frameworks.retrofit.WebServicesWrapper;
import retrofit2.Response;
import transport.school.com.schoolapp.bean.DeviceTokenRegister;
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.ap
     *
     * @param token The new token.
     */
    public void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        if(AppBaseApplication.getApplication().getSession() != null && AppBaseApplication.getApplication().getSession().getMobileNO() != null) {
            DeviceTokenRegister deviceTokenRegister = new DeviceTokenRegister();
            deviceTokenRegister.setToken(AppBaseApplication.getApplication().getSession().getmToken());
            deviceTokenRegister.setUserDeviceId(token);
            deviceTokenRegister.setUserDeviceType("Android");
            deviceTokenRegister.setMobile(AppBaseApplication.getApplication().getSession().getMobileNO());
            WebServicesWrapper.getInstance().postToken(deviceTokenRegister, new ResponseResolver<String>() {
                @Override
                public void onSuccess(String s, Response response) {
                }

                @Override
                public void onFailure(RestError error, String msg) {
                }
            });
        }
    }
}