package kr.co.smh.module.auth.model;

import java.util.Set;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class AuthorityVO {
	private Set<Authority> authority;
}
