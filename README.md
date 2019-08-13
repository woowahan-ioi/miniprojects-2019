# Wootube - IOI

## Domain
1. User
    - fields
        - ``Long id``
        - ``String name`` : 2~10자 내외 영문, 한글만 가능
        - ``String email`` : 이메일 양식
        - ``String password`` : 비밀번호 양식, ``@Password``, 영문, 숫자 조합
    - methods

## Repository
1. UserRepository
    - methods
        - ``findByEmail`` : 로그인을 위해 필요한 기능


## Controller
1. UserController
    - 회원가입 (User Create)
    - 로그인 (User Read)
    - 회원정보 수정 (User Update)
    - 회원 탈퇴 (User Delete)