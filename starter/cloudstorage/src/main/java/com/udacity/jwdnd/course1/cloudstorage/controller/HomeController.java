package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final FileService fileService;
    private final NoteService noteService;
    private final UserService userService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;
    String encryptionKey = "your-secure-key1";

    public HomeController(FileService fileService, NoteService noteService, UserService userService, CredentialService credentialService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = new EncryptionService();
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

    private Integer getCurrentUserId(Principal principal) {
        return userService.getUserByUsername(principal.getName()).getUserId();
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        Integer userId = getCurrentUserId(principal);
        List<File> files = fileService.getFilesByUser(userId);
        List<Note> notes = noteService.getNotesByUser(userId);
        List<Credential> credentials = credentialService.getCredentialsByUser(userId);
        model.addAttribute("files", files);
        model.addAttribute("file", new File());
        model.addAttribute("notes", notes);
        model.addAttribute("note", new Note());
        model.addAttribute("credentials", credentials);
        model.addAttribute("credential", new Credential());
        return "home";
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

    @PostMapping("/notes")
    public String saveNote(
            @RequestParam(value = "noteId", required = false) Integer noteId,
            @RequestParam("noteTitle") String noteTitle,
            @RequestParam("noteDescription") String noteDescription,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        Integer userId = getCurrentUserId(principal);
        Note note = new Note();
        note.setNoteId(noteId);
        note.setNoteTitle(noteTitle);
        note.setNoteDescription(noteDescription);
        note.setUserId(userId);

        noteService.saveNote(note);
        redirectAttributes.addFlashAttribute("successMessage", "Note saved successfully!");
        return "redirect:/home";
    }

    @GetMapping("/notes/delete/{id}")
    public String deleteNotee(@PathVariable Integer id, Principal principal, RedirectAttributes redirectAttributes) {
        int deleted = noteService.deleteNote(id, getCurrentUserId(principal));

        if (deleted > 0) {
            redirectAttributes.addFlashAttribute("successMessage", "Note deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Note not found or deletion failed.");
        }

        return "redirect:/home";
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
}
