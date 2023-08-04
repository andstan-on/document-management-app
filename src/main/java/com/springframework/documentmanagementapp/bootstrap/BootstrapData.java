package com.springframework.documentmanagementapp.bootstrap;

import com.springframework.documentmanagementapp.entities.Document;
import com.springframework.documentmanagementapp.entities.User;
import com.springframework.documentmanagementapp.model.DocumentDTO;
import com.springframework.documentmanagementapp.model.DocumentFileType;
import com.springframework.documentmanagementapp.model.UserDTO;
import com.springframework.documentmanagementapp.model.UserRole;
import com.springframework.documentmanagementapp.property.FileStorageProperties;
import com.springframework.documentmanagementapp.repositories.DocumentRepository;
import com.springframework.documentmanagementapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;



    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception{
        loadUserData();
        loadDocumentData();
        loadFileOnDisk();
    }


    private void loadFileOnDisk() throws IOException {
        // empty the folder
        File fileDir = new File("D:\\programare\\DocumentManagementApp\\testfiles");

        FileUtils.cleanDirectory(fileDir);


        // copy files to the folder
        File srcDir = new File("D:\\programare\\DocumentManagementApp\\bootstrapdata");
        FileUtils.copyDirectory(srcDir,fileDir);
    }

    private void loadDocumentData() {

        if(documentRepository.count() == 0) {

            Document document1 = Document.builder()

                    .user(userRepository.findByUsername("username1").get())
                    .fileName("Internship_task")
                    .fileType(DocumentFileType.PDF)
                    .filePath("D:\\programare\\DocumentManagementApp\\testfiles\\Internship_task.pdf")
                    .version(1)
                    .type("receipt")
                    .number(123456)
                    .dateOfIssue(LocalDate.of(2023,05,05))
                    .dateOfPayment(LocalDate.of(2024,05,05))
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

                    .user(userRepository.findByUsername("username2").get())
                    .fileName("image")
                    .fileType(DocumentFileType.JPEG)
                    .filePath("D:\\programare\\DocumentManagementApp\\testfiles\\image.jpeg")
                    .version(1)
                    .type("invoice")
                    .number(113456)
                    .dateOfIssue(LocalDate.of(2023,05,05))
                    .dateOfPayment(LocalDate.of(2024,05,05))
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

                    .user(userRepository.findByUsername("username3").get())
                    .fileName("image2")
                    .fileType(DocumentFileType.PNG)
                    .filePath("D:\\programare\\DocumentManagementApp\\testfiles\\image2.png")
                    .version(1)
                    .type("receipt")
                    .number(123455)
                    .dateOfIssue(LocalDate.of(2023,05,05))
                    .dateOfPayment(LocalDate.of(2024,05,05))
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
                    .firstName("fname1")
                    .lastName("lname1")
                    .email("email1@yahoo.com")
                    .password(bCryptPasswordEncoder().encode("password"))
                    .dateOfBirth(LocalDate.of(1995,05,05))
                    .role(UserRole.ADMIN)
                    .version(1)
                    .locked(false)
                    .enabled(true)
                    .build();

            User user2 = User.builder()

                    .username("username2")
                    .firstName("fname2")
                    .lastName("lname2")
                    .email("email2@yahoo.com")
                    .password(bCryptPasswordEncoder().encode("password"))
                    .dateOfBirth(LocalDate.of(1996,06,06))
                    .role(UserRole.REGULAR)
                    .version(1)
                    .locked(false)
                    .enabled(true)
                    .build();

            User user3 = User.builder()

                    .username("username3")
                    .firstName("fname3")
                    .lastName("lname3")
                    .email("email3@yahoo.com")
                    .password(bCryptPasswordEncoder().encode("password"))
                    .dateOfBirth(LocalDate.of(1997,07,07))
                    .role(UserRole.REGULAR)
                    .version(1)
                    .locked(false)
                    .enabled(true)
                    .build();

            userRepository.saveAll(Arrays.asList(user1, user2, user3));

        }
    }
}
