package com.finalProject.responseDto;

import java.util.List;

public class BulkUpdateResponseDto {

        List<Long> successIds ;
        List<FailureDto> failures;

    public BulkUpdateResponseDto(List<Long> successIds, List<FailureDto> failures) {
    }

    public List<Long> getSuccessIds() {
        return successIds;
    }

    public void setSuccessIds(List<Long> successIds) {
        this.successIds = successIds;
    }

    public List<FailureDto> getFailures() {
        return failures;
    }

    public void setFailures(List<FailureDto> failures) {
        this.failures = failures;
    }
}
