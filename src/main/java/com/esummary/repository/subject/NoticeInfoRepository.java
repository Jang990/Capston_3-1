package com.esummary.repository.subject;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.entity.subject.NoticeInfo;
import com.esummary.entity.subject.SubjectInfo;


@Repository
public interface NoticeInfoRepository extends CrudRepository<NoticeInfo, String>{
	List<NoticeInfo> findBySubjectInfo(SubjectInfo subjectInfo);
	List<NoticeInfo> findBySubjectInfo_SubjectId(String subjectId);
	Optional<NoticeInfo> findByNoticeIdAndTitleAndDescription(String noticeId, String title, String description);
}
