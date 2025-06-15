package com.springframework.documentmanagementapp.repositories;

import com.springframework.documentmanagementapp.entities.Document;
import com.springframework.documentmanagementapp.model.DocumentDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DocumentSearchDAO {

    private final EntityManager entityManager;

    public List<Document> findDocByCriteria(DocumentDTO documentDTO){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Document> criteriaQuery = criteriaBuilder.createQuery(Document.class);
        List<Predicate> predicates = new ArrayList<>();

        //select from document
        Root<Document> root = criteriaQuery.from(Document.class);
        if(documentDTO.getFileName() != null) {
            Predicate fileNamePredicate = criteriaBuilder
                    .like(root.get("filename"), "%" + documentDTO.getFileName() + "%");
            predicates.add(fileNamePredicate);
        }
        if(documentDTO.getAmountDue() != null) {
            Predicate fileNamePredicate = criteriaBuilder
                    .like(root.get("filename"), "%" + documentDTO.getFileName() + "%");
            predicates.add(fileNamePredicate);
        }

        criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
        TypedQuery<Document> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }


}
