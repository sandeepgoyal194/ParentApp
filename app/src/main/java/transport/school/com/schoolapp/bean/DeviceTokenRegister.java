
package transport.school.com.schoolapp.bean;

import com.google.gson.annotations.SerializedName;

public class DeviceTokenRegister {

    @SerializedName("mobile")
    private String mMobile;
    @SerializedName("token")
    private String mToken;
    @SerializedName("user_device_id")
    private String mUserDeviceId;
    @SerializedName("user_device_type")
    private String mUserDeviceType;

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public String getUserDeviceId() {
        return mUserDeviceId;
    }

    public void setUserDeviceId(String userDeviceId) {
        mUserDeviceId = userDeviceId;
    }

    public String getUserDeviceType() {
        return mUserDeviceType;
    }

    public void setUserDeviceType(String userDeviceType) {
        mUserDeviceType = userDeviceType;
    }

}
