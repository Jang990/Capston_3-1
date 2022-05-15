package com.esummary.elearning.service.vue;

import java.util.List;

import com.esummary.elearning.dto.InitalPageData;
import com.esummary.elearning.dto.SubjectCardData;

public interface VueService {
	List<SubjectCardData> getInitCardData(String studentNumber);
}
