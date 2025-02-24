package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;
    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
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
    private Integer getCurrentUserId(Principal principal) {
        return userService.getUserByUsername(principal.getName()).getUserId();
    }
}
