package com.esummary.repository.querydsl;

import java.util.List; 

import com.esummary.entity.subject.QLectureInfo;
import com.esummary.entity.subject.QSubjectInfo;
import com.esummary.entity.subject.QWeekInfo;
import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.subject.WeekInfo;
//import static com.esummary.entity.user.QUserSubject.userSubject;
import com.esummary.entity.user.QUserSubject;

import com.esummary.entity.user.UserSubject;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserSubjectRepositoryImpl implements UserSubjectRepositoryCustom{
	private final JPAQueryFactory query;
	
	private final QUserSubject us = QUserSubject.userSubject;
	private final QSubjectInfo si = QSubjectInfo.subjectInfo;
	private final QWeekInfo wi = QWeekInfo.weekInfo;
	private final QLectureInfo li = QLectureInfo.lectureInfo;
	
	//|us_id |subject_id |week_id |subject_id |student_number |open_type |subject_name |subject_owner_name |end_date |start_date |subject_id |title |subject_id |week_id|
	@Override
	public List<UserSubject> findLectureInfo(String studentId,String subjectId) {
		return query.selectFrom(us) // dto로 반환하는 것이 훨씬 좋다. db에 변경이 있어도 오류가 어
				.where(us.userInfo.studentNumber.eq(studentId), us.subjectInfo.subjectId.eq(subjectId))
				.join(us.subjectInfo, si).fetchJoin()
				.join(si.lectureList, wi).fetchJoin()
//				.join(wi.lectures, li).fetchJoin() // MultipleBagFetchException 발생
//				.distinct()
				.fetch(); // 리스트로 결과를 반환
		
	}
	
	public List<WeekInfo> test(String studentId, String subjectId) {
		return query.selectFrom(wi)
				.join(wi.lectures, li).fetchJoin()
				.where(wi.subjectInfo.subjectId.eq(subjectId))
				.fetch();
	}
	
}
