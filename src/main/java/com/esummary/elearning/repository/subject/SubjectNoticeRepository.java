package com.esummary.elearning.repository.subject;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.NoticeInfo;


@Repository
public interface SubjectNoticeRepository extends CrudRepository<NoticeInfo, String>{
	List<NoticeInfo> findBySubjectInfo(SubjectInfo subjectInfo);
	List<NoticeInfo> findBySubjectInfo_SubjectId(String subjectId);
	Optional<NoticeInfo> findByNoticeIdAndTitleAndDescription(String noticeId, String title, String description);
}
