package tech.team17;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ryu on 4/11/16.
 */

public interface ApiInterface {
    @POST("users/login")
    Call<LoginResponse> authenticate(@Body LoginForm form);

}
class LoginForm {

    final String email;
    final String password;
    final String token;

    public LoginForm(String email, String password, String token) {
        this.email = email;
        this.password = password;
        this.token = token;
    }
}