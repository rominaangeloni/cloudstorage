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

    public HomeController(FileService fileService, NoteService noteService, UserService userService, CredentialService credentialService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
    }

    public Integer getCurrentUserId(Principal principal) {
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
}
