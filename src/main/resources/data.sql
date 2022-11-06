INSERT INTO inhatces.user_info
(student_number, created_date, nickname, password, roles)
VALUES('test1', '2022-10-25', 'Test사용자_1', '$2a$10$PweWceAD0RR6irQ3lnTP5.qwkyq46iCZqBCAE/owD6DUBbSV4k52S', 'USER');

INSERT INTO inhatces.user_info
(student_number, created_date, nickname, password, roles)
VALUES('test2', '2022-10-25', 'Test사용자_2', '$2a$10$PweWceAD0RR6irQ3lnTP5.qwkyq46iCZqBCAE/owD6DUBbSV4k52S', 'USER');

INSERT INTO inhatces.subject_info
(subject_id, open_type, subject_name, subject_owner_name)
VALUES('202224001LLA103', 'normal', '[1학년L반] 의사소통과대인관계능력', '전수진'),
('202224001LLA106', 'normal', '[1학년L반] 문제해결과기술능력', '김동식'),
('202224001LLA149', 'normal', '[1학년L반] 문제인식및해결', '김용진'),
('202224033CLA102', 'normal', '[3학년C반] 모스마스터', '구선애'),
('202224043CMP869', 'normal', '[3학년C반] 스프링프레임워크', '김기태'),
('202224043DMP636', 'normal', '[3학년D반] 심화캡스톤디자인', '황수철'),
('202224043QMP910', 'normal', '[3학년Q반] C#프로그래밍응용', '김기태'),
('CORS_1703071437557d610794', 'open', '서버프로그래밍', '조규철'),
('CORS_1704050920510f7c44c0', 'open', '프로그래밍', '관리자'),
('CORS_220607110428f3327333', 'normal', '재학생폭력예방교육(필수)', '류정선');


INSERT INTO inhatces.user_subject
(subject_id, student_number)
VALUES
('CORS_1703071437557d610794', 'test1'),
('CORS_1704050920510f7c44c0', 'test1'),
('202224001LLA149', 'test1'),
('202224001LLA106', 'test1'),
('202224001LLA103', 'test1'),
('202224033CLA102', 'test1'),
('202224043CMP869', 'test1'),
('202224043DMP636', 'test1'),
('202224043QMP910', 'test1'),
('CORS_220607110428f3327333', 'test1');

INSERT INTO inhatces.chat_room
(chat_room_id, enter_uuid, subject_id)
VALUES
(1, 'dab197a0-7947-4a46-aecc-ef8fb3d48790', 'CORS_1703071437557d610794'),
(2, '457bb175-523d-4cb0-809d-8b92ff6a130c', 'CORS_1704050920510f7c44c0'),
(3, '151b21b5-05d7-406b-b122-07b9fd7159c9', '202224001LLA149'),
(4, '0fb0ac5b-26ac-4ab4-a755-01f53876216c', '202224001LLA106'),
(5, '7c504873-0aa2-44bd-8d02-a11e87a8a4b4', '202224001LLA103'),
(6, 'e66e3771-e967-4a4d-b5ed-2d6a29e2cc6e', '202224033CLA102'),
(7, '9a8b62da-1a9b-41b4-a57f-760795bb9aa7', '202224043CMP869'),
(8, '5667403b-262e-4377-8bfe-d919e48b93b2', '202224043DMP636'),
(9, '22466155-6a86-4500-aa83-4ff0f26332f4', '202224043QMP910'),
(10, '53265121-9387-495f-9297-9b7a7a5e7308', 'CORS_220607110428f3327333');
