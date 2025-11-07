package org.example.stamppaw_backend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    COMPANION_NOT_FOUND(HttpStatus.NOT_FOUND, "동행글을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 정보를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."),
    DOG_NOT_FOUND(HttpStatus.NOT_FOUND, "동물 정보를 찾을 수 없습니다."),
    AUTH_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일의 사용자를 찾을 수 없습니다."),

    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 파일 업로드에 실패했습니다."),


    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 존재하지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
