package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CredentialController {
    private final UserService userService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;
    String encryptionKey = "your-secure-key1";

    public CredentialController(UserService userService, CredentialService credentialService, EncryptionService encryptionService) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = new EncryptionService();
    }


    @PostMapping("/credentials")
    public String saveCredential(
            @RequestParam(value = "credentialId", required = false) Integer credentialId,
            @RequestParam("url") String url,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        String encryptedPassword = encryptionService.encryptValue(password, encryptionKey);
        Credential credential = new Credential();

        credential.setCredentialId(credentialId);
        credential.setUrl(url);
        credential.setUsername(username);
        credential.setPassword(encryptedPassword);
        credential.setUserId(getCurrentUserId(principal));
        try {
            credentialService.saveCredential(credential);
            redirectAttributes.addFlashAttribute("successMessage", "Credential saved successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Creating credential failed: " + e.getMessage());
        }
        return "redirect:/home";
    }

    @GetMapping("/credentials/decrypt/{id}")
    public ResponseEntity<Map<String, String>> decryptPassword(@PathVariable Integer id) {
        Credential credential = credentialService.getCredentialById(id);

        if (credential == null) {
            return ResponseEntity.notFound().build();
        }

        String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), encryptionKey);

        Map<String, String> response = new HashMap<>();
        response.put("decryptedPassword", decryptedPassword);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/credentials/delete/{id}")
    public String deleteCredential(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        int deleted = credentialService.deleteCredential(id);
        if (deleted > 0) {
            redirectAttributes.addFlashAttribute("successMessage", "Credential deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Credential not found or deletion failed.");
        }
        return "redirect:/home";
    }
    private Integer getCurrentUserId(Principal principal) {
        return userService.getUserByUsername(principal.getName()).getUserId();
    }
}
