package com.esummary.elearning.service.subject.util.db.notice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectNoticeInfo;
import com.esummary.elearning.service.subject.ELearningServiceImpl;

public interface DBNoticeUtil {
	
	//일단 Subject쪽 할때 전부 바꿀꺼라 남겨둠
	List<SubjectNoticeInfo> getSubjectNoticeInfo(SubjectInfo subjectInfo);

	boolean saveService(SubjectNoticeInfo notice);

	boolean saveService(List<SubjectNoticeInfo> notices);

	boolean validateDuplicate(SubjectNoticeInfo notice);
}
