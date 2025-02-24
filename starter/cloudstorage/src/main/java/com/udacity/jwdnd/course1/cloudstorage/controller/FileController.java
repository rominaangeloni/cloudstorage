package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class FileController {
    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file, Principal principal, RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a file to upload.");
            return "redirect:/home";
        }

        try {
            Integer userId = getCurrentUserId(principal);
            fileService.storeFile(file, userId);
            redirectAttributes.addFlashAttribute("successMessage", "File uploaded successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "File upload failed: " + e.getMessage());
        }

        return "redirect:/home";
    }

    @GetMapping("/files/view/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable Integer id, Principal principal) {
        File file = fileService.getFileById(id, getCurrentUserId(principal));

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .body(file.getFiledata());
    }

    @GetMapping("/files/delete/{id}")
    public String deleteFile(@PathVariable Integer id, Principal principal, RedirectAttributes redirectAttributes) {
        int deleted = fileService.deleteFile(id, getCurrentUserId(principal));

        if (deleted > 0) {
            redirectAttributes.addFlashAttribute("successMessage", "File deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: File not found or deletion failed.");
        }

        return "redirect:/home";
    }

    private Integer getCurrentUserId(Principal principal) {
        return userService.getUserByUsername(principal.getName()).getUserId();
    }
}
