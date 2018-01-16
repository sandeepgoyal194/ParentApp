
package transport.school.com.schoolapp.bean;
import com.google.gson.annotations.SerializedName;

import java.util.List;
@SuppressWarnings("unused")
public class LoginResponse {
    @SerializedName("error")
    private Boolean mError;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("student")
    private List<Student> mStudents;
    @SerializedName("token")
    private String mToken;

    public Boolean getError() {
        return mError;
    }

    public void setError(Boolean error) {
        mError = error;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<Student> getmStudents() {
        return mStudents;
    }

    public void setStudents(List<Student> teacher) {
        mStudents = teacher;
    }

    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }
}
