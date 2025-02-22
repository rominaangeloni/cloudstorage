package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getNotesByUser(Integer userId) {
        return noteMapper.getNotesByUser(userId);
    }

    public Note getNoteById(Integer noteId, Integer userId) {
        return noteMapper.getNoteById(noteId, userId);
    }

    public void saveNote(Note note) {
        if (note.getNoteId() == null) {
            noteMapper.insert(note);
        } else {
            noteMapper.update(note);
        }
    }

    public int deleteNote(Integer noteId, Integer userId) {
        return noteMapper.delete(noteId, userId);
    }
}
