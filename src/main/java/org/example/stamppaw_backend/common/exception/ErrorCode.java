package org.example.stamppaw_backend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Global Errors
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 존재하지 않습니다."),

    // User & Auth
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 정보를 찾을 수 없습니다."),
    AUTH_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일의 사용자를 찾을 수 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

    // dog
    DOG_NOT_FOUND(HttpStatus.NOT_FOUND, "반려견 정보를 찾을 수 없습니다."),

    // Follow
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "팔로우 정보를 찾을 수 없습니다."),
    ALREADY_FOLLOWING(HttpStatus.BAD_REQUEST, "이미 팔로우 중입니다."),
    SELF_FOLLOW_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "자기 자신을 팔로우할 수 없습니다."),

    // Market
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."),
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "카트에 담은 상품 정보를 찾을 수 없습니다."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "카트에 담은 상품 수량은 1이상 이어야 합니다"),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니를 찾을 수 없습니다."),
    UNAUTHORIZED_CART_ACCESS(HttpStatus.FORBIDDEN, "다른 사용자의 카트에 접근할 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다."),
    UNAUTHORIZED_ORDER_ACCESS(HttpStatus.FORBIDDEN, "다른 사용자의 주문 정보에 접근할 수 없습니다."),
    ORDER_CANCEL_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "해당 주문은 취소할 수 없습니다.\n관리자에 문의해주십시요."),

    // Companion
    COMPANION_NOT_FOUND(HttpStatus.NOT_FOUND, "동행글을 찾을 수 없습니다."),
    ALREADY_APPLICANT(HttpStatus.CONFLICT, "이미 처리된 동행 신청입니다."),
    COMPANION_APPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "동행 신청을 찾을 수 없습니다."),
    ALREADY_CHANGE_STATUS(HttpStatus.CONFLICT, "이미 처리된 신청 처리입니다."),
    NOT_ALLOW_APPLY(HttpStatus.BAD_REQUEST, "동행 수락이 된 요청이 아닙니다."),
    APPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "동행 신청을 찾을 수 없습니다."),
    NOT_APPLY_USER(HttpStatus.NOT_FOUND, "동행 신청자가 아닙니다."),
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰 태그를 찾을 수 없습니다."),


    // Walk
    WALK_NOT_FOUND(HttpStatus.NOT_FOUND, "산책 기록을 찾을 수 없습니다."),

    // File / S3
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 파일 업로드에 실패했습니다."),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 삭제를 실패했습니다."),

    // AUTH / TOKEN
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    WEBSOCKET_AUTH_FAILED(HttpStatus.UNAUTHORIZED, "WebSocket 인증에 실패했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
