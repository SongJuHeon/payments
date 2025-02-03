package com.example.payments.payment.data;

public final class CodeData {

    public static final class ResponseCode {

        public static final class SUCCESS {
            public static final class Success {
                public static final String code = "0000"; // 성공 코드
                public static final String message = "Success"; // 성공 메시지
            }
        }

        public static final class SYSTEM_ERROR {
            public static final class InternalSystemError {
                public static final String code = "1000"; // 시스템 내부 오류 코드
                public static final String message = "Internal System Error"; // 시스템 내부 오류 메시지
            }
        }

        public static final class PAYMENT_ERROR {
            public static final class PaymentError {
                public static final String code = "2000"; // 결제 오류 코드
                public static final String message = "Payment Error"; // 결제 오류 메시지
            }
            public static final class InvalidPaymentAmount {
                public static final String code = "2001"; // 유효하지 않은 결제 금액 코드
                public static final String message = "Invalid Payment Amount"; // 유효하지 않은 결제 금액 메시지
            }
            public static final class DuplicatedOrderId {
                public static final String code = "2002"; // 중복된 주문 ID 코드
                public static final String message = "Duplicated Order ID"; // 중복된 주문 ID 메시지
            }
            public static final class ExceededMaximumDueDate {
                public static final String code = "2003"; // 최대 유효 기간 초과 코드
                public static final String message = "Exceeded Maximum Due Date"; // 최대 유효 기간 초과 메시지
            }
            public static final class UserNotIdentified {
                public static final String code = "2004"; // 고객 식별 불가 코드
                public static final String message = "User Not Identified"; // 고객 식별 불가 메시지
            }
            public static final class OrderNotExist {
                public static final String code = "2005"; // 승인된 정보 없음 코드
                public static final String message = "Order Not Exist"; // 승인된 정보 없음 메시지
            }
            public static final class AlreadyCanceled {
                public static final String code = "2006"; // 이미 취소된 거래 코드
                public static final String message = "Already Canceled"; // 이미 취소된 거래 메시지
            }
            public static final class UnavailablePaymentMethod {
                public static final String code = "2007"; // 사용 불가 결제 방법 코드
                public static final String message = "Unavailable Payment Method"; // 사용 불가 결제 방법 메시지
            }

            public static final class InvalidCardNumber {
                public static final String code = "2008"; // 카드번호 오류 코드
                public static final String message = "InvalidCardNumber"; // 카드번호 오류 메시지
            }
        }

        public static final class AUTHENTICATION_ERROR {
            public static final class AuthenticationFailed {
                public static final String code = "3000"; // 인증 실패 코드
                public static final String message = "Authentication Failed"; // 인증 실패 메시지
            }
            public static final class UnauthorizedAccess {
                public static final String code = "3001"; // 권한 없음 코드
                public static final String message = "Unauthorized Access"; // 권한 없음 메시지
            }
            public static final class InvalidApiKey {
                public static final String code = "3002"; // 유효하지 않은 API 키 코드
                public static final String message = "Invalid API Key"; // 유효하지 않은 API 키 메시지
            }
            public static final class ExceededMaxAuthAttempts {
                public static final String code = "3003"; // 최대 인증 횟수 초과 코드
                public static final String message = "Exceeded Maximum Authentication Attempts"; // 최대 인증 횟수 초과 메시지
            }
            public static final class AccessDenied {
                public static final String code = "3004"; // 접근 권한 없음 코드
                public static final String message = "Access Denied"; // 접근 권한 없음 메시지
            }
        }

        public static final class INPUT_DATA_ERROR {
            public static final class InvalidInputData {
                public static final String code = "4000"; // 유효하지 않은 입력 데이터 코드
                public static final String message = "Invalid Input Data"; // 유효하지 않은 입력 데이터 메시지
            }
            public static final class MissingRequiredParameter {
                public static final String code = "4001"; // 필수 파라미터 누락 코드
                public static final String message = "Missing Required Parameter"; // 필수 파라미터 누락 메시지
            }
            public static final class InvalidDateFormat {
                public static final String code = "4002"; // 유효하지 않은 날짜 형식 코드
                public static final String message = "Invalid Date Format"; // 유효하지 않은 날짜 형식 메시지
            }
            public static final class InvalidEmailFormat {
                public static final String code = "4003"; // 유효하지 않은 이메일 형식 코드
                public static final String message = "Invalid Email Format"; // 유효하지 않은 이메일 형식 메시지
            }
            public static final class AccountOwnerCheckFailed {
                public static final String code = "4004"; // 계좌 소유주 정보 확인 실패 코드
                public static final String message = "Account Owner Check Failed"; // 계좌 소유주 정보 확인 실패 메시지
            }
            public static final class InvalidAccountNumber {
                public static final String code = "4005"; // 유효하지 않은 계좌 번호 코드
                public static final String message = "Invalid Account Number"; // 유효하지 않은 계좌 번호 메시지
            }
            public static final class AlreadyExistSubmall {
                public static final String code = "4006"; // 이미 존재하는 서브몰 코드
                public static final String message = "Already Exist Submall"; // 이미 존재하는 서브몰 메시지
            }
            public static final class InvalidRequest {
                public static final String code = "4007"; // 잘못된 요청 코드
                public static final String message = "Invalid Request"; // 잘못된 요청 메시지
            }
            public static final class InvalidBank {
                public static final String code = "4008"; // 유효하지 않은 은행 코드
                public static final String message = "Invalid Bank"; // 유효하지 않은 은행 메시지
            }
            public static final class ForbiddenRequest {
                public static final String code = "4009"; // 허용되지 않은 요청 코드
                public static final String message = "Forbidden Request"; // 허용되지 않은 요청 메시지
            }
            public static final class ExpiredAuthorizationCode {
                public static final String code = "4010"; // 만료된 인증 코드
                public static final String message = "Expired Authorization Code"; // 만료된 인증 코드 메시지
            }
            public static final class NotFound {
                public static final String code = "4011"; // 존재하지 않는 정보 코드
                public static final String message = "Not Found"; // 존재하지 않는 정보 메시지
            }
        }

        public static final class PAYMENT_SYSTEM_ERROR {
            public static final class PgVanError {
                public static final String code = "5000"; // 결제 시스템 에러 (PG/VAN) 코드
                public static final String message = "Payment System Error (PG/VAN)"; // 결제 시스템 에러 (PG/VAN) 메시지
            }
        }

        public static final class OTHER_ERROR {
            public static final class UnknownException {
                public static final String code = "6000"; // 알 수 없는 오류 코드
                public static final String message = "Unknown Exception"; // 알 수 없는 오류 메시지
            }
        }
    }

    public static final class StatusCode {
        public static final class SUCCESS {
            public static final class Done {
                public static final String code = "SUCCESS"; // 결제 완료 코드
                public static final String message = "Payment completed"; // 결제 완료 메시지
            }

            public static final class Canceled {
                public static final String code = "CANCELED"; // 결제 취소 코드
                public static final String message = "Payment canceled by request"; // 결제 취소 메시지
            }

            public static final class NetCancel {
                public static final String code = "NETCANCEL"; // 결제 취소 코드
                public static final String message = "Payment netCancele by request"; // 결제 망취소 메시지
            }
        }

        public static final class PROCESS {
            public static final class Ready {
                public static final String code = "READY"; // 결제 대기 코드
                public static final String message = "Payment created, awaiting authentication"; // 결제 대기 메시지
            }

            public static final class InProgress {
                public static final String code = "IN_PROGRESS"; // 인증 완료 코드
                public static final String message = "Payment method authenticated"; // 인증 완료 메시지
            }
        }

        public static final class FAIL {
            public static final class Expired {
                public static final String code = "EXPIRED"; // 결제 만료 코드
                public static final String message = "Payment expired"; // 결제 만료 메시지
            }

            public static final class Aborted {
                public static final String code = "FAIL"; // 승인 실패 코드
                public static final String message = "Payment approval failed"; // 승인 실패 메시지
            }
        }
    }
}
