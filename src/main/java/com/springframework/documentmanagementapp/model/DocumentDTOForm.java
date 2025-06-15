package com.springframework.documentmanagementapp.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDTOForm {
    @NotNull
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
