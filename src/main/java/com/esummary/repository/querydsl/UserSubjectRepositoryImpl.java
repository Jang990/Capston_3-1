package com.esummary.repository.querydsl;

import static com.esummary.entity.subject.QLectureInfo.lectureInfo;
import static com.esummary.entity.subject.QSubjectInfo.subjectInfo;
import static com.esummary.entity.subject.QTaskInfo.taskInfo;
import static com.esummary.entity.subject.QWeekInfo.weekInfo;
import static com.esummary.entity.user.QUserLecture.userLecture;
import static com.esummary.entity.user.QUserSubject.userSubject;
import static com.esummary.entity.user.QUserTask.userTask;
import static com.querydsl.core.group.GroupBy.list;

import java.util.List;

import com.esummary.crawling.dto.InhatcSubjectCardDTO;
import com.esummary.crawling.dto.LectureData;
import com.esummary.crawling.dto.tofront.LectureWeekData;
import com.esummary.crawling.dto.tofront.TaskData;
import com.esummary.entity.user.UserSubject;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserSubjectRepositoryImpl implements UserSubjectRepositoryCustom{
	private final JPAQueryFactory query;
	
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
			.from(weekInfo)
			.innerJoin(weekInfo.lectures, lectureInfo)
			.innerJoin(userLecture).on(userLecture.subjectLecture.eq(lectureInfo))
			.where(
				userLecture.userSubject.eq(
						getUserSubjectSubQuery(studentId, subjectId)
				)
			)
			.transform(
					GroupBy.groupBy(weekInfo.weekId).list( 						
						Projections.fields(LectureWeekData.class, 
							weekInfo.weekId.as("lectureWeekId"),
							weekInfo.title,
							weekInfo.startDate.stringValue().as("startDate"),
							weekInfo.endDate.stringValue().as("endDate"),
							list(
								Projections.fields(LectureData.class,
									lectureInfo.lectureId.longValue(),
									lectureInfo.lectureVideoId,
									lectureInfo.type,
									lectureInfo.idx,
									lectureInfo.title,
									lectureInfo.fullTime,
									userLecture.status,
									userLecture.learningTime
								)
							).as("lectures")
					)
				)
			);
		
		return lectures;
	}
	
	@Override
	public List<TaskData> findUserTaskList(String studentId, String subjectId) {
		List<TaskData> tasks = query
			.from(taskInfo)
			.innerJoin(userTask).on(userTask.taskInfo.eq(taskInfo))
			.where(
				userTask.userSubject.eq(
						getUserSubjectSubQuery(studentId, subjectId)
				)
			)
			.transform(
					GroupBy.groupBy(taskInfo.taskId).list( 						
						Projections.fields(TaskData.class,
								taskInfo.taskId,
								taskInfo.title,
								taskInfo.description,
								taskInfo.startDate.stringValue().as("startDate"),
								taskInfo.endDate.stringValue().as("endDate"),
								taskInfo.submissionNum,
								taskInfo.notSubmittedNum,
								taskInfo.totalNum,
								userTask.submitYN
					)
				)
			);
		
		return tasks;
	}
	
	private JPQLQuery<UserSubject> getUserSubjectSubQuery(String studentId, String subjectId) {
		return JPAExpressions.select(userSubject)
				.from(userSubject)
				.where(userSubject.userInfo.studentNumber.eq(studentId), 
						userSubject.subjectInfo.subjectId.eq(subjectId));
	}
	
	@Override
	public List<InhatcSubjectCardDTO> findUserOwnSubjectInfo(String studentId) {
		List<InhatcSubjectCardDTO> subjects = query
				.select(
						Projections.fields(InhatcSubjectCardDTO.class, 
							subjectInfo.subjectId,
							subjectInfo.subjectOwnerName.stringValue().as("owner"),
							subjectInfo.subjectName
						)
				)
				.from(subjectInfo)
				.where(subjectInfo.subjectId.in(
						JPAExpressions.select(userSubject.subjectInfo.subjectId)
						.from(userSubject)
						.where(userSubject.userInfo.studentNumber.eq(studentId))
					)
				)
				.fetch();
		
		return subjects;
	}
}
