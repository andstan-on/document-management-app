package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.model.Document;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

    private Map<UUID, Document> documentMap;

    public DocumentServiceImpl(){
        this.documentMap = new HashMap<>();

        Document document1 = Document.builder()
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
                .approvalStatus("approved")
                .comments("no extra info")
                .build();

        Document document2 = Document.builder()
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
                .approvalStatus("approved")
                .comments("no extra info")
                .build();

        Document document3 = Document.builder()
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
                .approvalStatus("pending")
                .comments("no extra info")
                .build();

        documentMap.put(document1.getId(), document1);
        documentMap.put(document2.getId(), document2);
        documentMap.put(document3.getId(), document3);

    }
    @Override
    public List<Document> listDocuments(){
        return new ArrayList<>(documentMap.values());

    }

    @Override
    public Optional<Document> getDocumentById(UUID id) {

        log.debug("Get Document by ID - in service" + id.toString());

        return Optional.ofNullable(documentMap.get(id));
    }

    @Override
    public Document saveNewDocument(Document document) {

        Document savedDocument = Document.builder()
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
    public void updateDocumentById(UUID documentId, Document document) {
        Document existing = documentMap.get(documentId);

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

        documentMap.put(existing.getId(), existing);
    }

    @Override
    public void deleteById(UUID documentId) {
        documentMap.remove(documentId);
    }
}
