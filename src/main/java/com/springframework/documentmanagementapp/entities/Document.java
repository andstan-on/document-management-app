package com.springframework.documentmanagementapp.entities;

import com.springframework.documentmanagementapp.model.DocumentFileType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Integer version;

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

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

}
