package com.sjy.blog.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinDTO {
        private String username;
        private String password;
        private String email;

        public User toEntity() {
            return User.builder()
                    .username(this.username)
                    .password(this.password)
                    .email(this.email)
                    .build();
        }

        public void validate() {
            if(username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("사용자명은 필수입니다");
            }
            if(password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("비밀번호는 필수입니다");
            }
            if(email.contains("@") == false) {
                throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다");
            }
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDTO {
        private String username;
        private String password;

        public void validate() {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("사용자명은 필수입니다");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("비밀번호는 필수입니다");
            }
        }
    }

    @Data
    public static class UpdateDTO {
        private String password;
        private String email;

        public void validate() {
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("비밀번호는 필수입니다");
            }
            if (password.length() < 4) {
                throw new IllegalArgumentException("비밀번호는 4자 이상이어야 합니다");
            }
            if(email.contains("@") == false) {
                throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다");
            }
        }
    }
}
