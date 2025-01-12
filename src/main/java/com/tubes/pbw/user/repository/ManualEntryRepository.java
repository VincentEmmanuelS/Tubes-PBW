package com.tubes.pbw.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.tubes.pbw.user.model.ManualEntry;

public interface ManualEntryRepository {
    void makeActivity(ManualEntry data);
    List<ManualEntry> findAllEntry(String email, String sortBy, String sortOrder);
    Optional<ManualEntry> getEntry(Integer id);
    Optional<ManualEntry> getEntryEvent(String email, String title, LocalDateTime date);
    List<ManualEntry> findByTitleContaining(String keyword, String email, String sortBy, String sortOrder);
    void deleteManualentry(Integer id);
    void UpdateData(ManualEntry data);
}
