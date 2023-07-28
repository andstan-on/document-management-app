package com.springframework.documentmanagementapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
public class DocumentDTO {
    private UUID id;

    private Integer version;

    private MultipartFile docFile;

    private String fileName;
    private DocumentFileType fileType;
    private String filePath;

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
