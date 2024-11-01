# ESquad Back-end

### 이 프로젝트는 실시간 커뮤니케이션 기반 스터디 플랫폼 "ESquad"의 백엔드 서버입니다.

## Info

ESquad는 팀 스터디 활동을 기록, 소통, 공유하여 팀 단위와 개인의 학습을 향상시키는 플랫폼입니다. 

이를 통해 다음과 같은 목표를 달성하고자 합니다.

 

팀원 간 실시간 소통을 통해 지식을 공유하고 토론을 활성화하여, 학습 효과를 극대화합니다.

브레인스토밍 및 지식 공유 공간을 제공하여 학습 과정에서의 아이디어를 기록하고, 이를 팀 내에서 활용할 수 있도록 지원합니다.

팀 단위 및 개인 학습 성과를 기록하여 장기적인 학습 관리와 성취도를 추적할 수 있는 시스템을 구현합니다.

## 

**주요 기능​은 다음과 같습니다.**

- 실시간 스트리밍 (화면 공유 및 화상 대화)
- 스터디 페이지별 스토리지 제공
- 팀 스페이스내 채팅
- 팀 스페이스 (팀 단위 학습 공간)
- 커뮤니티
- 각종 알림
- 도서 검색
- 유저 관리



## Settings

### application.yml

프로젝트를 실행하기 전에 `src/main/resources` 디렉토리에 `application.yml` 파일을 생성하고 아래와 같은 내용을 작성해주세요.
```yaml
spring:
  datasource:
    url: [데이터베이스 url]
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: [ID]
    password: [PW]
    
  data:
    redis:
      host: [호스트]
      port: [포트]
      lettuce:
        pool:
          min-idle: 0
          max-idle: 8
          max-active: 8
          
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect

  servlet: # 멀티파트
    multipart:
      max-file-size: 100MB
      max-request-size: 300MB

  web:
    resources:
      static-locations: file:/{fe경로}/dist
      
  mail:
    host: smtp.gmail.com
    port: 587
    username: [메일 발신할 email(gmail) ex) rudals1888@gmail.com]
    password: [비밀번호]
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true

logging:
  level:
    org:
      springframework: INFO

cloud:
  aws:
    s3:
      bucket: [s3 버킷명]
    stack:
      auto: false
    region:
      static: [버킷이 위치한 리전]
    credentials:
      access-key: [IAM 액세스 키]
      secret-key: [IAM 시크릿 키]

springdoc:
  swagger-ui:
    path: /swagger.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /api-docs
    version: openapi_3_0
  default-consumes-media-type: application/json;charset=UTF-8
  default-produces-media-type: application/json;charset=UTF-8

naver:
  client:
    id: [naver api id]
    secret: [secret]

firebase:
  type: service_account
  project_id: [서비스 ID]
  private_key_id: [private key id]
  private_key: [private key]

  client_email: [클라이언트 email]
  client_id: [클라이언트 id]

  auth_uri: https://accounts.google.com/o/oauth2/auth
  token_uri: https://oauth2.googleapis.com/token
  auth_provider_x509_cert_url: https://www.googleapis.com/oauth2/v1/certs
  client_x509_cert_url: [client cert url]
  universe_domain: googleapis.com
  database_url: [파이어베이스 DB url]

jwt:
  secret: [설정한 secret]
```

### redis 설치

windows 

[참고](https://inpa.tistory.com/entry/REDIS-%F0%9F%93%9A-Window10-%ED%99%98%EA%B2%BD%EC%97%90-Redis-%EC%84%A4%EC%B9%98%ED%95%98%EA%B8%B0)

macOS
```
brew install redis
```

로그인 후 

redis cli 접속 `redis-cli -h 127.0.0.1 -p 6379` 

키 값이 들어갔는지 확인 `KEYS refreshToken:*` 후 아래 처럼 뜨면 세팅이 정상적으로 완료된 것입니다.

<img width="550" alt="keys-cli 스크린샷" src="https://github.com/user-attachments/assets/cf3c116d-7bb7-4468-8a88-a7cbb5941a35">



## Tech Stack

|  | 개발환경 |
| --- | --- |
| 운영체제 | Windows10, 11 64bit / macOS Sequoia 15.0.1​ |
| 개발언어 | Java(Correto 17), JavaScript​ |
| 개발도구 | IntelliJ, gradle, PUTTY, DBeaver, DataGrip, PostMan |
| 프레임워크 | Spring boot 3.3.2, JPA |
| 데이터베이스 | MySQL, Redis, Firebase Realtime DB |
| AWS | S3, EC2, Route53, ACM​ |
| 버전 관리 시스템 | Git |
| 라이브러리 | (build.gradle안의 디펜던시들) |
| 오픈소스 | (API류) |
| 설정 파일 확장자 | yml |
