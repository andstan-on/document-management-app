package com.springframework.documentmanagementapp.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public abstract class BaseDocumentDTO {
    protected String fileName;
    protected DocumentFileType fileType;
    protected String filePath;

    protected String type;
    protected Integer number;
    protected LocalDate dateOfIssue;
    protected LocalDate dateOfPayment;
    protected String vendorName;
    protected String vendorInfo;
    protected String customerName;
    protected String customerInfo;
    protected BigDecimal amountDue;
    protected BigDecimal amountPaid;
    protected String paymentMethod;
    protected Integer purchaseOrderNumber;
    protected String taxInformation;
    protected String currency;
    protected String description;
    protected String approvalStatus;
    protected String comments;
}
