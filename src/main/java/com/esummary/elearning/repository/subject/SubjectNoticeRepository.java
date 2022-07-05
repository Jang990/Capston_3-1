package com.esummary.elearning.repository.subject;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectNoticeInfo;


@Repository
public interface SubjectNoticeRepository extends CrudRepository<SubjectNoticeInfo, String>{
	List<SubjectNoticeInfo> findBySubjectInfo(SubjectInfo subjectInfo);
	List<SubjectNoticeInfo> findBySubjectInfo_SubjectId(String subjectId);
	Optional<SubjectNoticeInfo> findByNoticeIdAndTitleAndDescription(String noticeId, String title, String description);
}
