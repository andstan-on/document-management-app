package com.springframework.documentmanagementapp.repositories;

import com.springframework.documentmanagementapp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserSearchDAO {

    Page<User> filterUsers(String searchedCriteria, Pageable pageable);

}
