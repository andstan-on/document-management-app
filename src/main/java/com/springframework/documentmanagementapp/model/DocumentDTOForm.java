package com.springframework.documentmanagementapp.model;

import com.springframework.documentmanagementapp.entities.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDTOForm {
    private UUID id;

    private Integer version;

    @NotNull
    private MultipartFile docFile;

    private String fileName;
    private DocumentFileType fileType;
    private String filePath;

    private String type;
    private Integer number;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfIssue;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
    private DocumentStatus approvalStatus;
    private String comments;

    private User user;
}
