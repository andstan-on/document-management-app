package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.exception.FileStorageException;
import com.springframework.documentmanagementapp.exception.MyFileNotFoundException;
import com.springframework.documentmanagementapp.entities.Document;
import com.springframework.documentmanagementapp.entities.User;
import com.springframework.documentmanagementapp.mappers.DocumentMapper;
import com.springframework.documentmanagementapp.model.*;
import com.springframework.documentmanagementapp.property.FileStorageProperties;
import com.springframework.documentmanagementapp.repositories.DocumentRepository;
import com.springframework.documentmanagementapp.repositories.UserRepository;
import com.springframework.documentmanagementapp.webutils.WebUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
public class DocumentServiceJPA implements DocumentService {
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final DocumentMapper documentMapper;
    private final Path fileStorageLocation;

    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_PAGE_SIZE = 10;

    @Autowired
    public DocumentServiceJPA(final DocumentRepository documentRepository, final UserRepository userRepository, final DocumentMapper documentMapper, final FileStorageProperties fileStorageProperties) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.documentMapper = documentMapper;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
    }

    //page request
    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize){
        int queryPageNumber;
        int queryPageSize;

        if(pageNumber != null && pageNumber > 0){
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if(pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if(pageSize > 100){
                queryPageSize = 100;
            } else {
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Order.asc("fileName"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }


    @Transactional
    @Override
    public Page<DocumentDTO> listUserDocuments(UUID userId, String searchedCriteria, Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        User user;

        if(userId != null){
            user = userRepository.findById(userId).get();
        }
        else {
            user = WebUtils.getLoggedInUser();
        }



        Page<Document> documentPage;

        if(!StringUtils.hasText(searchedCriteria)){
            documentPage = documentRepository.findByUserId(user.getId(), pageRequest);
        } else {
            documentPage = documentRepository.filterUsersDocuments(user.getId(), searchedCriteria, pageRequest);
        }


        return  documentPage.map(documentMapper::documentToDocumentDto);
    }

    @Transactional
    @Override
    public Page<DocumentDTO> listDocumentsByApprovalStatus(DocumentStatus documentStatus, String searchedCriteria, Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Document> documentPage;

        if(!StringUtils.hasText(searchedCriteria)){
            documentPage = documentRepository.findByApprovalStatus(documentStatus, pageRequest);
        } else {
            System.out.println(searchedCriteria);
            documentPage = documentRepository.filterApprovedDocuments(documentStatus, searchedCriteria, pageRequest);
        }

        return  documentPage.map(documentMapper::documentToDocumentDto);
    }

    @Transactional
    @Override
    public List<DocumentDTO> filterDocuments(String searchedCriteria) {
        return documentRepository.filterDocuments(searchedCriteria)
                .stream()
                .map(documentMapper::documentToDocumentDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<DocumentDTO> listDocuments(UUID userId) {

        List<Document> documentList;

        if(userId == null) {
            documentList = documentRepository.findAll();
        } else {
            documentList = documentRepository.findByUserId(userId);
        }

        return documentList
                .stream()
                .map(documentMapper::documentToDocumentDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Resource getDocumentFile(UUID id) {

        Optional<DocumentDTO> document = Optional.ofNullable(documentMapper.documentToDocumentDto(documentRepository.findById(id)
                .orElse(null)));

        try {
            Path filePath = Paths.get(document.get().getFilePath());
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + document.get().getFileName());
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + document.get().getFileName(), ex);
        }
    }

    @Transactional
    @Override
    public Optional<DocumentDTO> getDocumentMetadata(UUID id) {
        return Optional.ofNullable(documentMapper.documentToDocumentDto(documentRepository.findById(id)
                .orElse(null)));
    }



    @Transactional
    @Override
    public DocumentDTO saveNewDocument(DocumentDTOForm document) {

        User user = WebUtils.getLoggedInUser();

        String fileName = FilenameUtils.removeExtension(document.getDocFile().getOriginalFilename());
        document.setFileName(fileName);
        document.setUser(user);

        document.setApprovalStatus(user.getRole() == UserRole.ADMIN ? DocumentStatus.APPROVED : DocumentStatus.PENDING);

        //validate name
        if(fileName.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
        }
        for(DocumentFileType fileType: DocumentFileType.values()){
            if(fileType.getContentType().equals(document.getDocFile().getContentType())){
                document.setFileType(fileType);
                break;
            }
        }

        //validate file type
        if (document.getFileType() == null) {
            throw new IllegalArgumentException("No matching file type found.");
        }

        Document savedDocument = documentRepository.save(documentMapper.documentDtoFormToDocument(document));

        Path targetLocation = this.fileStorageLocation.resolve(savedDocument.getId().toString());
        try {
            Files.createDirectory(targetLocation);
        } catch (IOException ex) {
            throw new FileStorageException("Could not create dir");
        }
        targetLocation = targetLocation.resolve(document.getDocFile().getOriginalFilename());

        savedDocument.setFilePath(targetLocation.toString());


        //save file to file system
        try{
            Files.copy(document.getDocFile().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName);
        }

        DocumentDTO savedDocumentDTO = documentMapper.documentToDocumentDto(savedDocument);

        return savedDocumentDTO;
    }

    @Transactional
    @Override
    public Optional<DocumentDTO> updateDocumentMetadata(UUID documentId, DocumentDTO document) {
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
            foundDocument.setComments(document.getComments());
            atomicReference.set(Optional.of(documentMapper.documentToDocumentDto(documentRepository.save(foundDocument))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Transactional
    @Override
    public Boolean deleteById(UUID documentId) {
        if(documentRepository.existsById(documentId)) {
            Optional<DocumentDTO> document = Optional.ofNullable(documentMapper.documentToDocumentDto(documentRepository.findById(documentId)
                    .orElse(null)));
            File file = new File(document.get().getFilePath());
            file.delete();
            documentRepository.deleteById(documentId);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public Optional<DocumentDTO> updateDocumentFile(UUID documentId, DocumentDTO newDocument) {

        // get document
        Optional<DocumentDTO> document = Optional.ofNullable(documentMapper.documentToDocumentDto(documentRepository.findById(documentId)
                .orElse(null)));

        // get  original file location for delete
        File file = new File(document.get().getFilePath());


        String fileName = FilenameUtils.removeExtension(newDocument.getDocFile().getOriginalFilename());

        //validate name
        if(fileName.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
        }

        Path targetLocation = this.fileStorageLocation.resolve(document.get().getId().toString());

        File directory = new File(targetLocation.toString());

        targetLocation = targetLocation.resolve(newDocument.getDocFile().getOriginalFilename());
        document.get().setFilePath(targetLocation.toString());
        document.get().setFileName(fileName);
        document.get().setDocFile(newDocument.getDocFile());
        for(DocumentFileType fileType: DocumentFileType.values()){
            if(fileType.getContentType().equals(document.get().getDocFile().getContentType())){
                document.get().setFileType(fileType);
                break;
            }
        }

        //validate file type
        if (document.get().getFileType() == null) {
            throw new IllegalArgumentException("No matching file type found.");
        }

        documentMapper.documentToDocumentDto(documentRepository.save(documentMapper.documentDtoToDocument(document.get())));

        //save file to file system
        try{
            Files.copy(document.get().getDocFile().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName);
        }

        int filesInDir = directory.listFiles().length;

        //delete original file
        if(file.exists() && !file.isDirectory() && filesInDir==2) {
            file.delete();
        }

        return document;


    }

    @Override
    public Optional<DocumentDTO> updateDocumentStatus(UUID documentId, DocumentStatus documentStatus) {
        AtomicReference<Optional<DocumentDTO>> atomicReference = new AtomicReference<>();

        documentRepository.findById(documentId).ifPresentOrElse(foundDocument -> {
            foundDocument.setApprovalStatus(documentStatus);
            atomicReference.set(Optional.of(documentMapper.documentToDocumentDto(documentRepository.save(foundDocument))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
