package com.esummary.crawler.elearning.assignment.dto;

import com.esummary.crawler.elearning.dto.ContentCompletionStatus;
import com.esummary.crawler.elearning.dto.ContentDetail;
import com.esummary.crawler.elearning.dto.ContentPeriod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

// 과제 정보
@Getter
@ToString
@AllArgsConstructor
public class AssignmentDTO {
    private ContentDetail contentDetail;
    private ContentPeriod contentPeriod;
    private AssignmentSubmissionStatus totalStatus;

    private ContentCompletionStatus personalSubmissionStatus; // 자신의 제출 상태
}
