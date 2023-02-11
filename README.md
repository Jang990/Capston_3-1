# ESummary
InhaTc 이러닝 사이트에 수업에 관한 정보를 간단하게 확인하고, 같은 수업을 듣는 학생간에 수업에 대한 정보를 나눌 수 있는 서비스

### 목차

- 프로젝트 개요 및 소개
- 기술스택
- 구현기능

<br>
<br>

## 프로젝트 개요 및 소개

![image](https://user-images.githubusercontent.com/88225377/218242845-7404954f-cb04-4ee1-8d04-e53d3f229bd0.png)

<br>

기존 InhaTC 이러닝에서 비대면으로 진행되는 강의를 수강하면서 <br>
수업 정보, 과제, 공지사항 등을 일일이 확인하는데 불편함을 느꼈고, <br>
비대면 수업을 진행하면서 같은 수업을 듣는 학생간에 수업에 대한 정보 교류의 필요성을 느꼈습니다.

그래서 수업에 관한 정보를 한 눈에 확인할 수 있고, 같은 수업을 듣는 학생간 채팅으로 수업 내용에 대한 의견을 교환할 수 있는 프로젝트를 진행했습니다.

<br>

해당 ESummary 프로젝트는 InhaTc 이러닝 사이트에 수업에 관한 정보를 간단하게 확인하고, 같은 수업을 듣는 학생간에 수업에 대한 정보를 나눌 수 있는 서비스를 만드는 것입니다.

<br>
<br>

## 기술 스택

- Java 11
- Spring-Boot 2.6.4
- Vue 2.8.4
- MySQL
- AWS EC2 & RDS

<br>
<br>

## 서비스 구조

- 서비스 구조

![image](https://user-images.githubusercontent.com/88225377/218242829-b6734cb2-7d82-4358-bd5c-476f152129b8.png)

<br>

- DB구조

![image](https://user-images.githubusercontent.com/88225377/218242953-997e3ef4-5cec-480b-8fbb-7fbe566d1b55.png)
 
<br>
<br>

## 구현 기능
<br>

### 로그인 및 회원가입 기능

* 로그인 - 회원가입 실패 화면 <br>

​<img src="https://user-images.githubusercontent.com/88225377/218243322-07bc9b92-11ee-4b3b-b967-bf6f5c725ba2.png"  width="300"/>

​<img src="https://user-images.githubusercontent.com/88225377/218243331-f43248b3-1990-4fa1-8790-310b22b41a54.png"  width="300"/>


InhaTc 이러닝 사이트 ID와 PW로 회원 가입하는 기능

**해당 정보로 InhaTc에 로그인이 진행되지 않는다면 회원가입 거부**

<br>

### 강의 컴포넌트별 기능

![image](https://user-images.githubusercontent.com/88225377/218242870-092e6e60-960a-433d-986f-0b31bbe8ed19.png)

<br>

회원의 정보를 이용해서 InhaTc 이러닝 사이트에 로그인을 해서 현재 사용자의 수강정보를 크롤링해서 화면에 컴포넌트로 보여주는 기능

강의 컴포넌트는 해당 강의의 미완료 완료 수업등을 확인할 수 있고 각각의 조회 기능을 사용해서 세부내용을 확인할 수 있음

추가적으로 각 강의를 듣는 학생간 채팅 기능 사용 가능<br>

<img src="https://user-images.githubusercontent.com/88225377/218243077-0376eb3b-af0c-46b6-bba3-00b3ddee52d3.png"  width="300"/><img src="https://user-images.githubusercontent.com/88225377/218243096-3020a619-ee45-40fb-97ec-fa0c7213147f.png"  width="300"/> <br>
<img src="https://user-images.githubusercontent.com/88225377/218243100-f5622fd3-61d2-4e2b-8948-ecbfe08b2e1a.png"  width="300"/><img src="https://user-images.githubusercontent.com/88225377/218243104-d54a95a2-fca0-4f52-b9d7-0c7e7a5eeb9f.png"  width="300"/>

<br>
<br>

### 완료해야 할 수업 및 과제 요약 기능

![image](https://user-images.githubusercontent.com/88225377/218243019-f01a6cc9-847f-49bc-8a9a-73382d12064d.png)

사용자가 아직 완료하지 못한 수업이나 과제등을 한 눈에 확인할 수 있는 요약 차트기능
