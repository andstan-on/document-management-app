package com.springframework.documentmanagementapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DocumentControllerTest {

    @Autowired
    DocumentController documentController;

    @Test
    void getDocumentById() {

        System.out.println(documentController.getDocumentById(UUID.randomUUID()));
    }
}