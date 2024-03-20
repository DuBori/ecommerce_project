package com.duboribu.ecommerce.front.retrofit2;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class RetrofitConfig {

	private static final String BASE_URL = "http://localhost:8080/";

	@Bean
	public OkHttpClient okHttpClient() {
		return new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
				.writeTimeout(60, TimeUnit.SECONDS)
				.readTimeout(60, TimeUnit.SECONDS)
				.build();
	}

	@Bean
	public Retrofit retrofit(OkHttpClient client) {
		String baseURL = BASE_URL;
		return new Retrofit.Builder().baseUrl(baseURL)
				.addConverterFactory(GsonConverterFactory.create())
				.client(client)
				.build();
	}

	@Bean
	public RestInterface serverAPIs(Retrofit retrofit) {
		return retrofit.create(RestInterface.class);
	}
}