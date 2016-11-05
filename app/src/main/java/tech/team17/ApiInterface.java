package tech.team17;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ryu on 4/11/16.
 */

public interface ApiInterface {
    @POST("users/login")
    Call<LoginResponse> authenticate(@Body LoginForm form);

    @GET("bus/{sort_by}")
    Call<ApiInterface> busList(
            @Path("sort_by") String sortBy,
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("search/route")
    Call<ApiInterface> searchResult(
            @Query("api_key") String apiKey,
            @Query("query") String query
    );


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
