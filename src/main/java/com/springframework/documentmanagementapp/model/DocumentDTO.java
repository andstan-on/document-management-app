package com.springframework.documentmanagementapp.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
public class DocumentDTO {
    private UUID id;
    private String type;
    private Integer number;
    private LocalDate dateOfIssue;
    private LocalDate dateOfPayment;
    private String vendorName;
    private String vendorInfo;
    private String customerName;
    private String customerInfo;
    private BigDecimal amountDue;
    private BigDecimal amountPaid;
    private String paymentMethod;
    private Integer purchaseOrderNumber;
    private String taxInformation;
    private String currency;
    private String description;
    private String approvalStatus;
    private String comments;

}
