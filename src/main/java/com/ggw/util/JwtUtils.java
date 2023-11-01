package com.ggw.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ggw.jwt")
public class JwtUtils {

	private long expire;
	private String secret;
	private String header;

	// 生成jwt
	public String generateToken(String username) {

		Date nowDate = new Date();
		Date expireDate = new Date(nowDate.getTime() + 1000 * expire);

		return Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setSubject(username)
				.setIssuedAt(nowDate)
				.setExpiration(expireDate)// 7天過期
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

	// 解析jwt
	public static Claims getClaimByToken(String jwt) {
		try {
			return Jwts.parser()
					.setSigningKey("ji8n3439n439n43ld9ne9343fdfer49h")
					.parseClaimsJws(jwt)
					.getBody();
		} catch (Exception e) {
			return null;
		}
	}

	// jwt是否过期
	public boolean isTokenExpired(Claims claims) {
		return claims.getExpiration().before(new Date());
	}

}
