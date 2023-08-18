package com.springframework.documentmanagementapp.repositories;

import com.springframework.documentmanagementapp.entities.User;
import com.springframework.documentmanagementapp.model.UserRole;
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

@Repository
@RequiredArgsConstructor
public class UserSearchDAOImpl implements UserSearchDAO {

    private final EntityManager entityManager;

    @Override
    public Page<User> filterUsers(String searchedCriteria, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        List<Predicate> predicates = new ArrayList<>();


        Root<User> root = criteriaQuery.from(User.class);


        if(searchedCriteria != null) {
            Predicate fileNamePredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("username")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(fileNamePredicate);

            Expression<UserRole> roleExpression = root.get("role");
            for (UserRole role : UserRole.values()) {
                if (role.toString().equals(searchedCriteria.toUpperCase())) {
                    Predicate rolePredicate = criteriaBuilder
                            .equal(roleExpression, UserRole.valueOf(searchedCriteria.toUpperCase()));
                    predicates.add(rolePredicate);
                    break;
                }
            }

            Predicate firstNamePredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("firstName")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(firstNamePredicate);

            Predicate lastNamePredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("lastName")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(lastNamePredicate);

            Predicate emailPredicate = criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("email")), "%" + searchedCriteria.toLowerCase() + "%");
            predicates.add(emailPredicate);

        }
        criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);


        // Apply pagination
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<User> resultList = query.getResultList();
        int totalResults = resultList.size();

        return new PageImpl<>(resultList, pageable, totalResults);
    }
}
