package com.esummary.crawler.assignment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
public class AssignmentSubmissionStatus {
    private int submittedCount;
    private int unsubmittedCount;
    private int totalAssignmentCount;

    public AssignmentSubmissionStatus(int submittedCount, int unsubmittedCount, int totalAssignmentCount) {
        if(submittedCount+unsubmittedCount != totalAssignmentCount) {
            throw new IllegalArgumentException("과제 제출인원과 미제출인원의 합이 전체인원과 맞지 않습니다.");
        }

        this.submittedCount = submittedCount;
        this.unsubmittedCount = unsubmittedCount;
        this.totalAssignmentCount = totalAssignmentCount;
    }
}
