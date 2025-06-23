package com.duboribu.ecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EcommerceApplicationTests {

	@Test
	void contextLoads() {
	}

	public static void main(String[] args) {
		solution(15);
	}

	private static void solution(int input) {
		int p = 1;
		int count = 0;
		while (p < input) {
			int sum = 0;
			for (int i = p; i < input; i++) {
				sum+=i;
				if (sum == input) {
					p++;
					count++;
					break;
				}
				if (sum > input) {
					p++;
					break;
				}
			}
		}
		System.out.println("count = " + count);
	}

}
