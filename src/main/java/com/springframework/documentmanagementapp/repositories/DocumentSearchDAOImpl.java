package com.springframework.documentmanagementapp.repositories;

import com.springframework.documentmanagementapp.entities.Document;
import com.springframework.documentmanagementapp.entities.User;
import com.springframework.documentmanagementapp.model.DocumentFileType;
import com.springframework.documentmanagementapp.model.DocumentStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DocumentSearchDAOImpl implements DocumentSearchDAO {

    private final EntityManager entityManager;

    @Override
    public Page<Document> filterUsersDocuments(UUID userid, String searchedCriteria, Pageable pageable){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Document> criteriaQuery = criteriaBuilder.createQuery(Document.class);
        List<Predicate> predicates = new ArrayList<>();


        Root<Document> root = criteriaQuery.from(Document.class);

        // Adding the predicate to check userId equality
        Join<Document, User> userJoin = root.join("user");
        Predicate userIdPredicate = criteriaBuilder.equal(userJoin.get("id"), userid);


        if(searchedCriteria != null) {
            Predicate fileNamePredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("fileName")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(fileNamePredicate);

            Expression<DocumentFileType> fileTypeExpression = root.get("fileType");
            for(DocumentFileType fileType: DocumentFileType.values()){
                if(fileType.toString().equals(searchedCriteria.toUpperCase())){
                    Predicate fileTypePredicate = criteriaBuilder
                            .equal(fileTypeExpression, DocumentFileType.valueOf(searchedCriteria.toUpperCase()));
                    predicates.add(fileTypePredicate);
                    break;
                }
            }

            Expression<DocumentStatus> approvalStatusExpression = root.get("approvalStatus");
            for(DocumentStatus status: DocumentStatus.values()){
                if(status.toString().equals(searchedCriteria.toUpperCase())){
                    Predicate approvalStatusPredicate = criteriaBuilder
                            .equal(approvalStatusExpression, DocumentStatus.valueOf(searchedCriteria.toUpperCase()));
                    predicates.add(approvalStatusPredicate);
                    break;
                }
            }

            Predicate typePredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("type")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(typePredicate);

            Predicate numberPredicate = criteriaBuilder
                    .equal(root.get("number").as(String.class), searchedCriteria );
            predicates.add(numberPredicate);

            Predicate vendorNamePredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("vendorName")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(vendorNamePredicate);

            Predicate vendorInfoPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("vendorInfo")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(vendorInfoPredicate);

            Predicate customerNamePredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("customerName")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(customerNamePredicate);

            Predicate customerInfoPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("customerInfo")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(customerInfoPredicate);

            Predicate paymentMethodPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("paymentMethod")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(paymentMethodPredicate);


            Predicate purchaseOrderNumberPredicate = criteriaBuilder
                    .equal(root.get("purchaseOrderNumber").as(String.class), searchedCriteria);
            predicates.add(purchaseOrderNumberPredicate);

            Predicate amountDuePredicate = criteriaBuilder
                    .like(root.get("amountDue").as(String.class), searchedCriteria + "%");
            predicates.add(amountDuePredicate);

            Predicate amountPaidPredicate = criteriaBuilder
                    .like(root.get("amountPaid").as(String.class), searchedCriteria + "%");
            predicates.add(amountPaidPredicate);


            Predicate taxInformationPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("taxInformation")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(taxInformationPredicate);

            Predicate currencyPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("currency")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(currencyPredicate);

            Predicate descriptionPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("description")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(descriptionPredicate);

            Predicate commentsPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("comments")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(commentsPredicate);

        }


        criteriaQuery.where(criteriaBuilder.and(userIdPredicate,criteriaBuilder.or(predicates.toArray(new Predicate[0]))));
        TypedQuery<Document> query = entityManager.createQuery(criteriaQuery);


        // Apply pagination
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Document> resultList = query.getResultList();
        int totalResults = resultList.size();

        return new PageImpl<>(resultList, pageable, totalResults);
    }

    @Override
    public Page<Document> filterApprovedDocuments(DocumentStatus documentStatus, String searchedCriteria, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Document> criteriaQuery = criteriaBuilder.createQuery(Document.class);
        List<Predicate> predicates = new ArrayList<>();


        Root<Document> root = criteriaQuery.from(Document.class);


        Predicate approvalStatusPredicate = criteriaBuilder
                .equal(root.get("approvalStatus"), documentStatus);



        if(searchedCriteria != null) {

            Join<Document, User> userJoin = root.join("user");
            Predicate userUsernamePredicate = criteriaBuilder
                    .equal(userJoin.get("username"), searchedCriteria);
            predicates.add(userUsernamePredicate);

            Predicate userEmailPredicate = criteriaBuilder
                    .equal(userJoin.get("email"), searchedCriteria);
            predicates.add(userEmailPredicate);


            Predicate fileNamePredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("fileName")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(fileNamePredicate);

            Expression<DocumentFileType> fileTypeExpression = root.get("fileType");
            for(DocumentFileType fileType: DocumentFileType.values()){
                if(fileType.toString().equals(searchedCriteria.toUpperCase())){
                    Predicate fileTypePredicate = criteriaBuilder
                            .equal(fileTypeExpression, DocumentFileType.valueOf(searchedCriteria.toUpperCase()));
                    predicates.add(fileTypePredicate);
                    break;
                }
            }


            Predicate typePredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("type")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(typePredicate);

            Predicate numberPredicate = criteriaBuilder
                    .equal(root.get("number").as(String.class), searchedCriteria );
            predicates.add(numberPredicate);

            Predicate vendorNamePredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("vendorName")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(vendorNamePredicate);

            Predicate vendorInfoPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("vendorInfo")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(vendorInfoPredicate);

            Predicate customerNamePredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("customerName")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(customerNamePredicate);

            Predicate customerInfoPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("customerInfo")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(customerInfoPredicate);

            Predicate paymentMethodPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("paymentMethod")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(paymentMethodPredicate);


            Predicate purchaseOrderNumberPredicate = criteriaBuilder
                    .equal(root.get("purchaseOrderNumber").as(String.class), searchedCriteria);
            predicates.add(purchaseOrderNumberPredicate);

            Predicate amountDuePredicate = criteriaBuilder
                    .like(root.get("amountDue").as(String.class), searchedCriteria + "%");
            predicates.add(amountDuePredicate);

            Predicate amountPaidPredicate = criteriaBuilder
                    .like(root.get("amountPaid").as(String.class), searchedCriteria + "%");
            predicates.add(amountPaidPredicate);


            Predicate taxInformationPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("taxInformation")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(taxInformationPredicate);

            Predicate currencyPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("currency")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(currencyPredicate);

            Predicate descriptionPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("description")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(descriptionPredicate);

            Predicate commentsPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("comments")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(commentsPredicate);

        }

        criteriaQuery.where(criteriaBuilder.and(approvalStatusPredicate,criteriaBuilder.or(predicates.toArray(new Predicate[0]))));
        TypedQuery<Document> query = entityManager.createQuery(criteriaQuery);


        // Apply pagination
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Document> resultList = query.getResultList();
        int totalResults = resultList.size();

        return new PageImpl<>(resultList, pageable, totalResults);
    }


    @Override
    public List<Document> filterDocuments(String searchedCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Document> criteriaQuery = criteriaBuilder.createQuery(Document.class);
        List<Predicate> predicates = new ArrayList<>();


        Root<Document> root = criteriaQuery.from(Document.class);

        // Adding the predicate to check userId equality
        Join<Document, User> userJoin = root.join("user");
        Predicate userUsernamePredicate = criteriaBuilder
                .equal(userJoin.get("username"), searchedCriteria);
        predicates.add(userUsernamePredicate);

        Predicate userEmailPredicate = criteriaBuilder
                .equal(userJoin.get("email"), searchedCriteria);
        predicates.add(userEmailPredicate);


        if(searchedCriteria != null) {
            Predicate fileNamePredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("fileName")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(fileNamePredicate);

            Expression<DocumentFileType> fileTypeExpression = root.get("fileType");
            for(DocumentFileType fileType: DocumentFileType.values()){
                if(fileType.toString().equals(searchedCriteria.toUpperCase())){
                    Predicate fileTypePredicate = criteriaBuilder
                            .equal(fileTypeExpression, DocumentFileType.valueOf(searchedCriteria.toUpperCase()));
                    predicates.add(fileTypePredicate);
                    break;
                }
            }

            Expression<DocumentStatus> approvalStatusExpression = root.get("approvalStatus");
            for(DocumentStatus status: DocumentStatus.values()){
                if(status.toString().equals(searchedCriteria.toUpperCase())){
                    Predicate approvalStatusPredicate = criteriaBuilder
                            .equal(approvalStatusExpression, DocumentStatus.valueOf(searchedCriteria.toUpperCase()));
                    predicates.add(approvalStatusPredicate);
                    break;
                }
            }

            Predicate typePredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("type")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(typePredicate);

            Predicate numberPredicate = criteriaBuilder
                    .equal(root.get("number").as(String.class), searchedCriteria );
            predicates.add(numberPredicate);

            Predicate vendorNamePredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("vendorName")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(vendorNamePredicate);

            Predicate vendorInfoPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("vendorInfo")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(vendorInfoPredicate);

            Predicate customerNamePredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("customerName")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(customerNamePredicate);

            Predicate customerInfoPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("customerInfo")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(customerInfoPredicate);

            Predicate paymentMethodPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("paymentMethod")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(paymentMethodPredicate);


            Predicate purchaseOrderNumberPredicate = criteriaBuilder
                    .equal(root.get("purchaseOrderNumber").as(String.class), searchedCriteria);
            predicates.add(purchaseOrderNumberPredicate);

            Predicate amountDuePredicate = criteriaBuilder
                    .like(root.get("amountDue").as(String.class), searchedCriteria + "%");
            predicates.add(amountDuePredicate);

            Predicate amountPaidPredicate = criteriaBuilder
                    .like(root.get("amountPaid").as(String.class), searchedCriteria + "%");
            predicates.add(amountPaidPredicate);


            Predicate taxInformationPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("taxInformation")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(taxInformationPredicate);

            Predicate currencyPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("currency")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(currencyPredicate);

            Predicate descriptionPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("description")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(descriptionPredicate);

            Predicate commentsPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("comments")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(commentsPredicate);

        }


        criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
        TypedQuery<Document> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }


}
