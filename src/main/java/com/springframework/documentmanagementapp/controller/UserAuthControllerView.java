package com.springframework.documentmanagementapp.controller;

import com.springframework.documentmanagementapp.exception.*;
import com.springframework.documentmanagementapp.model.*;
import com.springframework.documentmanagementapp.services.UserRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.springframework.documentmanagementapp.controller.UserAuthController.AUTH_PATH;

@Controller
@RequiredArgsConstructor
public class UserAuthControllerView {


    private final UserRegistrationService userRegistrationService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/authentication/register")
    public String getRegisterForm(Model model) {

        model.addAttribute("userRegistrationDTO", new UserRegistrationDTO());

        return "register";
    }

    @PostMapping("/authentication/register")
    public String postRegisterForm(@Valid UserRegistrationDTO userRegistrationDTO,BindingResult result, Model model){
        if (result.hasErrors()) {
            return "register";
        }
        try {
            String generatedToken = userRegistrationService.register(userRegistrationDTO);
            return "registration-successful";
        } catch (EmailAlreadyExistsException ex) {
            model.addAttribute("emailError", ex.getMessage());
            return "register";
        } catch (UsernameAlreadyExistsException ex) {
            model.addAttribute("usernameError", ex.getMessage());
            return "register";
        }
    }

    @GetMapping("/authentication/reset")
    public String getEmailForm(Model model) {
        model.addAttribute("emailDTO", new EmailDTO());

        return "email-form";
    }
    @PostMapping("/authentication/reset")
    public String sendEmailForm(@Valid EmailDTO emailDTO,BindingResult result, Model model){
        if (result.hasErrors()) {
            return "email-form";
        }

        try {
           String generatedToken = userRegistrationService.resetPasswordLinkOnEmail(emailDTO,UserRegistrationService.RESET_URL_VIEW);
            return "reset-password-link-sent-on-email";
        } catch (NoUserWithGivenEmailException ex) {
            model.addAttribute("emailError", ex.getMessage());
            return "email-form";
        }

    }

    @GetMapping("/authentication/reset/password")
    public String getResetPasswordForm(@RequestParam String token, Model model) {


        if(userRegistrationService.getPasswordResetForm(token)){
            model.addAttribute("resetPasswordDTO", new ResetPasswordDTO());
            model.addAttribute("token", token);
            return "password-reset-form";
        }
        else {
            model.addAttribute("tokenExpired", true);
            return "redirect:/reset-password-link-sent-on-email";
        }
    }

    @PostMapping("/authentication/reset/password")
    public String sendResetPasswordForm(@RequestParam String token, @Valid ResetPasswordDTO resetPasswordDTO,BindingResult result,Model model) {
        if (result.hasErrors()) {
            model.addAttribute("token", token);
            return "password-reset-form";
        }
        try {
            userRegistrationService.resetPasswordView(token, resetPasswordDTO);
            return "password-reset-done";
        } catch (PasswordsDoNotMatchException ex) {
            model.addAttribute("passwordError", ex.getMessage());
            model.addAttribute("token", token);
            return "password-reset-form";

        }
    }

}
