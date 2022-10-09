package com.esummary.repository.querydsl;

import static com.querydsl.core.group.GroupBy.list;

import java.util.List;

import com.esummary.crawling.dto.LectureData;
import com.esummary.crawling.dto.tofront.LectureWeekData;
import com.esummary.entity.subject.QLectureInfo;
import com.esummary.entity.subject.QSubjectInfo;
import com.esummary.entity.subject.QWeekInfo;
import com.esummary.entity.user.QUserLecture;
import com.esummary.entity.user.QUserSubject;

import com.esummary.entity.user.UserSubject;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserSubjectRepositoryImpl implements UserSubjectRepositoryCustom{
	private final JPAQueryFactory query;
	
	private final QUserSubject us = QUserSubject.userSubject;
	private final QUserLecture ul = QUserLecture.userLecture;
	private final QSubjectInfo si = QSubjectInfo.subjectInfo;
	private final QWeekInfo wi = QWeekInfo.weekInfo;
	private final QLectureInfo li = QLectureInfo.lectureInfo;
	
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
	
	public List<LectureWeekData> findUserLectureList(String studentId, String subjectId) {
		/*
		select  * 
		from week_info wi, lecture_info li, user_lecture ul 
		where wi.subject_id = '202224001LLA149' 
			and li.lecture_week_id = wi.week_id 
			and ul.lecture_id = li.lecture_id 
			and ul.us_id = (select us.us_id 
							from user_subject us 
							where us.student_number="201845096" 
								and us.subject_id = '202224001LLA149') ;
		 */
		List<LectureWeekData> lectures = query
			.from(wi)
			.innerJoin(wi.lectures, li)
			.innerJoin(ul).on(ul.subjectLecture.eq(li))
			.where(
				ul.userSubject.eq(
						JPAExpressions.select(us)
							.from(us)
							.where(us.userInfo.studentNumber.eq(studentId), 
									us.subjectInfo.subjectId.eq(subjectId))
				)
			)
			.transform(
					GroupBy.groupBy(wi.weekId).list( 						
						Projections.fields(LectureWeekData.class, 
							wi.weekId.as("lectureWeekId"),
							wi.title,
							wi.startDate.stringValue().as("startDate"),
							wi.endDate.stringValue().as("endDate"),
							list(
								Projections.fields(LectureData.class,
									li.lectureId.longValue(),
									li.lectureVideoId,
									li.type,
									li.idx,
									li.title,
									li.fullTime,
									ul.status,
									ul.learningTime
								)
							).as("lectures")
					)
				)
			);
		
		return lectures;
	}
}
