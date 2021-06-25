package com.lghomin.restapi.controller;

import java.util.Base64;
import java.util.Date;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;

@RestController
public class HelloController {


	
	@GetMapping("/validateToken")
	@ResponseBody
	public boolean validateToken(String jwtToken) throws Exception {
		String secretKey = "aaaa";
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		jwtToken = "eyJ0eXAiOiJKV1QiLCJpc3N1ZURhdGUiOjE2MjQ1OTcwMTAzMjYsImFsZyI6IkhTNTEyIn0.eyJzdWIiOiJzYXMiLCJNR1JfSUQiOiLrp6Tri4jsoIAgSUQiLCJNR1JfTk0iOiLrp6Tri4jsoIAg66qFIiwiSk5DT19NTkdSX1NOTyI6IuygnO2ctOyCrOy9lOuTnCIsImlhdCI6MTYyNDU5NzAxMCwiZXhwIjoxNjI0NTk3MDEwfQ.7hmRRQJZHQ11hkiN_c-drvJ3i4_h_osIMHMKvkDa3qfO13D25RUpanyUFgDlOP1FJcK32jycJ9QSN0lF63KvVA";

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }		
	}	
	
	@GetMapping("/createJwt")
	@ResponseBody
	public String createJwt() {
		Date now = new Date();
		long tokenValidMilisecond = 1000L * 60 * 60; // 1시간만 토큰 유효
		String secretKey = "aaaa";
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		
		String jwtString = Jwts.builder()
				.setHeaderParam("typ", "JWT")	// token type Header
				.setSubject("sas")	// token subject Header
				.setHeaderParam("issueDate", System.currentTimeMillis())	// token create date
				.claim("MGR_ID", "매니저 ID")	// private claim 
				.claim("MGR_NM", "매니저 명")	// private claim
				.claim("JNCO_MNGR_SNO", "제휴사코드")	// private claim
				.setExpiration(new Date(now.getTime() + tokenValidMilisecond))	// token expired time
				.setExpiration(new Date(now.getTime()))	// token expired time
				.signWith(SignatureAlgorithm.HS512, secretKey)	// signature
				.compact();
				
		return jwtString;
	}
	
	@GetMapping("/print-all-headers")
	@ResponseBody
	public String getAllHeader(@RequestHeader Map<String, String> headers) {
		headers.forEach((key, value) -> {
			System.out.println("Header Name:" + key + " Header Value:" + value);
		});
		
		
		return "print-all-headers";
	}
	
	@GetMapping("/without")
	@ResponseBody
	public String withoutType() {
		return "withoutType";
	}
	
	@GetMapping(value="/with", produces="application/json; charset=UTF-8")
	@ResponseBody
	public String withType() {
		return "withType";
	}
	
	
	
	// 1. 화면에 helloworld가 출력됩니다.
	@GetMapping(value="/helloworld/string")
	@ResponseBody
	public String helloworldString() {
		return "helloworld";
	}
	
	// 2. 화면에 {message:"helloworld"}라고 출력됩니다.
	@GetMapping(value="/helloworld/json")
	@ResponseBody
	public Hello helloworldJson() {
		Hello hello = new Hello();
		hello.message = "helloworld";
		return hello;
	}
	
	// 3. 화면에 helloworld.ftl의 내용이 출력됩니다.
	@GetMapping(value="/helloworld/page")
	public String helloworld() {
		return "helloworld";
	}

	
	@Setter
	@Getter
	public static class Hello {
		private String message;
	}
}
