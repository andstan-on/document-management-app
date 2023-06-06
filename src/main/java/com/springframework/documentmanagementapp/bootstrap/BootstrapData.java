package com.springframework.documentmanagementapp.bootstrap;

import com.springframework.documentmanagementapp.entities.Document;
import com.springframework.documentmanagementapp.entities.User;
import com.springframework.documentmanagementapp.model.DocumentDTO;
import com.springframework.documentmanagementapp.model.UserDTO;
import com.springframework.documentmanagementapp.model.UserRole;
import com.springframework.documentmanagementapp.repositories.DocumentRepository;
import com.springframework.documentmanagementapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception{
        loadDocumentData();
        loadUserData();
    }

    private void loadDocumentData() {

        if(documentRepository.count() == 0) {

            Document document1 = Document.builder()

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

            documentRepository.saveAll(Arrays.asList(document1, document2, document3));

        }
    }

    private void loadUserData() {

        if(userRepository.count() == 0) {

            User user1 = User.builder()

                    .username("username1")
                    .password("123456")
                    .firstName("fname1")
                    .lastName("lname1")
                    .email("email1")
                    .dateOfBirth(LocalDate.now())
                    .role(UserRole.REGULAR)
                    .build();

            User user2 = User.builder()

                    .username("username2")
                    .password("123456")
                    .firstName("fname2")
                    .lastName("lname2")
                    .email("email2")
                    .dateOfBirth(LocalDate.now())
                    .role(UserRole.REGULAR)
                    .build();

            User user3 = User.builder()

                    .username("username3")
                    .password("123456")
                    .firstName("fname3")
                    .lastName("lname3")
                    .email("email3")
                    .dateOfBirth(LocalDate.now())
                    .role(UserRole.REGULAR)
                    .build();

            userRepository.saveAll(Arrays.asList(user1, user2, user3));

        }
    }
}
