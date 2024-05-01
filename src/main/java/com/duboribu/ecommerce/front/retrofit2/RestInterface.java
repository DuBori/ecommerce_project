package com.duboribu.ecommerce.front.retrofit2;

import com.duboribu.ecommerce.auth.domain.DefaultResponse;
import com.duboribu.ecommerce.auth.domain.UserDto;
import com.duboribu.ecommerce.auth.domain.response.UserResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestInterface {
	
   // 반환 타입은 Call<타입>의 제네릭 형태
   @POST("/auth/sign-up")
   Call<DefaultResponse<UserResponse>> signUp(@Body UserDto userDto);
   @POST("/auth/sign-in")
   Call<DefaultResponse<UserResponse>> signIn(@Body UserDto userDto);
	
}