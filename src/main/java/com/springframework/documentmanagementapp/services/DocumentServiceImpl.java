package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.model.DocumentDTO;
import com.springframework.documentmanagementapp.model.DocumentStatus;
import com.springframework.documentmanagementapp.property.FileStorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

    private Map<UUID, DocumentDTO> documentMap;

    public DocumentServiceImpl(){
        this.documentMap = new HashMap<>();

        DocumentDTO document1 = DocumentDTO.builder()
                .id(UUID.randomUUID())
                .type("receipt")
                .number(123456)
                .dateOfIssue(LocalDate.now())
                .dateOfPayment(LocalDate.now())
                .vendorName("name1")
                .vendorInfo("phone:123456789")
                .customerName("name2")
                .customerInfo("phone:987654321")
                .amountDue(new BigDecimal("500.00"))
                .amountPaid(new BigDecimal("2000.00"))
                .paymentMethod("card")
                .purchaseOrderNumber(13579)
                .taxInformation("tax info")
                .currency("USD")
                .description("description")
                .approvalStatus(DocumentStatus.APPROVED)
                .comments("no extra info")
                .build();

        DocumentDTO document2 = DocumentDTO.builder()
                .id(UUID.randomUUID())
                .type("invoice")
                .number(113456)
                .dateOfIssue(LocalDate.now())
                .dateOfPayment(LocalDate.now())
                .vendorName("name3")
                .vendorInfo("phone:122226789")
                .customerName("name4")
                .customerInfo("phone:933654321")
                .amountDue(new BigDecimal("5000.00"))
                .amountPaid(new BigDecimal("2000.00"))
                .paymentMethod("cash")
                .purchaseOrderNumber(15579)
                .taxInformation("tax info")
                .currency("USD")
                .description("description")
                .approvalStatus(DocumentStatus.APPROVED)
                .comments("no extra info")
                .build();

        DocumentDTO document3 = DocumentDTO.builder()
                .id(UUID.randomUUID())
                .type("receipt")
                .number(123455)
                .dateOfIssue(LocalDate.now())
                .dateOfPayment(LocalDate.now())
                .vendorName("name5")
                .vendorInfo("phone:123455789")
                .customerName("name6")
                .customerInfo("phone:987664321")
                .amountDue(new BigDecimal("1000.00"))
                .amountPaid(new BigDecimal("1000.00"))
                .paymentMethod("card")
                .purchaseOrderNumber(10079)
                .taxInformation("tax info")
                .currency("USD")
                .description("description")
                .approvalStatus(DocumentStatus.APPROVED)
                .comments("no extra info")
                .build();



        documentMap.put(document1.getId(), document1);
        documentMap.put(document2.getId(), document2);
        documentMap.put(document3.getId(), document3);

    }


    @Override
    public List<DocumentDTO> listUserDocuments() {
        return null;
    }

    @Override
    public List<DocumentDTO> listDocumentsByApprovalStatus(DocumentStatus documentStatus) {
        return null;
    }

    @Override
    public List<DocumentDTO> listDocuments(){
        return new ArrayList<>(documentMap.values());

    }

    @Override
    public Optional<DocumentDTO> getDocumentMetadata(UUID id) {

        log.debug("Get Document by ID - in service" + id.toString());

        return Optional.ofNullable(documentMap.get(id));
    }

    @Override
    public DocumentDTO saveNewDocument(DocumentDTO document) {

        DocumentDTO savedDocument = DocumentDTO.builder()
                .id(UUID.randomUUID())
                .type(document.getType())
                .number(document.getNumber())
                .dateOfIssue(document.getDateOfIssue())
                .dateOfPayment(document.getDateOfPayment())
                .vendorName(document.getVendorName())
                .vendorInfo(document.getVendorInfo())
                .customerName(document.getCustomerName())
                .customerInfo(document.getCustomerInfo())
                .amountDue(document.getAmountDue())
                .amountPaid(document.getAmountPaid())
                .paymentMethod(document.getPaymentMethod())
                .purchaseOrderNumber(document.getPurchaseOrderNumber())
                .taxInformation(document.getTaxInformation())
                .currency(document.getCurrency())
                .description(document.getDescription())
                .approvalStatus(document.getApprovalStatus())
                .comments(document.getComments())
                .build();

        documentMap.put(savedDocument.getId(), savedDocument);

        return savedDocument;
    }

    @Override
    public Optional<DocumentDTO> updateDocumentMetadata(UUID documentId, DocumentDTO document) {
        DocumentDTO existing = documentMap.get(documentId);

        existing.setType(document.getType());
        existing.setNumber(document.getNumber());
        existing.setDateOfIssue(document.getDateOfIssue());
        existing.setDateOfPayment(document.getDateOfPayment());
        existing.setVendorName(document.getVendorName());
        existing.setVendorInfo(document.getVendorInfo());
        existing.setCustomerName(document.getCustomerName());
        existing.setCustomerInfo(document.getCustomerInfo());
        existing.setAmountDue(document.getAmountDue());
        existing.setAmountPaid(document.getAmountPaid());
        existing.setPaymentMethod(document.getPaymentMethod());
        existing.setPurchaseOrderNumber(document.getPurchaseOrderNumber());
        existing.setTaxInformation(document.getTaxInformation());
        existing.setCurrency(document.getCurrency());
        existing.setDescription(document.getDescription());
        existing.setApprovalStatus(document.getApprovalStatus());
        existing.setComments(document.getComments());

        return Optional.of(existing);
    }

    @Override
    public Boolean deleteById(UUID documentId) {
        documentMap.remove(documentId);

        return true;
    }

    @Override
    public Resource getDocumentFile(UUID id) {
        return null;
    }

    @Override
    public Optional<DocumentDTO> updateDocumentFile(UUID documentId, DocumentDTO document) {
        return Optional.empty() ;
    }

    @Override
    public Optional<DocumentDTO> updateDocumentStatus(UUID documentId, DocumentStatus documentStatus) {
        return Optional.empty();
    }
}

