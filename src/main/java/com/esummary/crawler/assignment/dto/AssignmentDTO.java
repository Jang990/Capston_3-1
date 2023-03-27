package com.esummary.crawler.assignment.dto;

import com.esummary.crawler.dto.ContentCompletionStatus;
import com.esummary.crawler.dto.ContentDetail;
import com.esummary.crawler.dto.ContentPeriod;
import lombok.Getter;
import lombok.ToString;

// 과제 정보
@Getter
@ToString
public class AssignmentDTO {
    private ContentDetail contentDetail;
    private ContentPeriod contentPeriod;
    private AssignmentSubmissionStatus totalStatus;

    private ContentCompletionStatus personalSubmissionStatus; // 자신의 제출 상태
}
