package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.mappers.DocumentMapper;
import com.springframework.documentmanagementapp.model.DocumentDTO;
import com.springframework.documentmanagementapp.repositories.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class DocumentServiceJPA implements DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    @Override
    public List<DocumentDTO> listDocuments() {
        return documentRepository.findAll()
                .stream()
                .map(documentMapper::documentToDocumentDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DocumentDTO> getDocumentById(UUID id) {
        return Optional.ofNullable(documentMapper.documentToDocumentDto(documentRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public DocumentDTO saveNewDocument(DocumentDTO document) {
        return documentMapper.documentToDocumentDto(documentRepository.save(documentMapper.documentDtoToDocument(document)));
    }

    @Override
    public Optional<DocumentDTO> updateDocumentById(UUID documentId, DocumentDTO document) {
        AtomicReference<Optional<DocumentDTO>> atomicReference = new AtomicReference<>();

        documentRepository.findById(documentId).ifPresentOrElse(foundDocument -> {
            foundDocument.setType(document.getType());
            foundDocument.setNumber(document.getNumber());
            foundDocument.setDateOfIssue(document.getDateOfIssue());
            foundDocument.setDateOfPayment(document.getDateOfPayment());
            foundDocument.setVendorName(document.getVendorName());
            foundDocument.setVendorInfo(document.getVendorInfo());
            foundDocument.setCustomerName(document.getCustomerName());
            foundDocument.setCustomerInfo(document.getCustomerInfo());
            foundDocument.setAmountDue(document.getAmountDue());
            foundDocument.setAmountPaid(document.getAmountPaid());
            foundDocument.setPaymentMethod(document.getPaymentMethod());
            foundDocument.setPurchaseOrderNumber(document.getPurchaseOrderNumber());
            foundDocument.setTaxInformation(document.getTaxInformation());
            foundDocument.setCurrency(document.getCurrency());
            foundDocument.setDescription(document.getDescription());
            foundDocument.setApprovalStatus(document.getApprovalStatus());
            foundDocument.setComments(document.getComments());
            atomicReference.set(Optional.of(documentMapper.documentToDocumentDto(documentRepository.save(foundDocument))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteById(UUID documentId) {
        if(documentRepository.existsById(documentId)) {
            documentRepository.deleteById(documentId);
            return true;
        }
        return false;
    }
}
