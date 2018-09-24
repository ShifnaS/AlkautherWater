package infogateway.alkautherwater.api;
import infogateway.alkautherwater.model.Result;
import infogateway.alkautherwater.model.Results;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {

    @GET("productdetails_json.php")
    Call<Result> getProduct();

    @FormUrlEncoded
    @POST("contactdetails_json.php")
    Call<Results> contactUs(
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("message") String message,
            @Field("date") String date
    );

    @FormUrlEncoded
    @POST("bookingdetails_json.php")
    Call<Results> buyProduct(
            @Field("product_id") int product_id,
            @Field("quantity") String quantity,
            @Field("customer_name") String customer_name,
            @Field("phone") String phone,
            @Field("address") String address,
            @Field("date") String date,
            @Field("fid") String fid

    );
    @FormUrlEncoded
    @POST("servicedetails_json.php")
    Call<Results> buyFreezer(
            @Field("todate") String todate,
            @Field("customer_name") String customer_name,
            @Field("phone") String phone,
            @Field("address") String address,
            @Field("fromdate") String fromdate,
            @Field("fid") String fid,
            @Field("date") String date

    );

}
