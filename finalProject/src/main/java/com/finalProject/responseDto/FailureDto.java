package com.finalProject.responseDto;

public class FailureDto {
    Long partId;
    String reason;

    public FailureDto(Long id, String message) {
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
