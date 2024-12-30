package com.tubes.pbw.repository;

import java.util.List;
import java.util.Optional;

import com.tubes.pbw.model.ManualEntry;

public interface ManualEntryRepository {
    void makeActivity(ManualEntry data);
    List<ManualEntry> findAllEntry(String email, String sortBy, String sortOrder);
    Optional<ManualEntry> getEntry(Integer id);
    List<ManualEntry> findByTitleContaining(String keyword, String email, String sortBy, String sortOrder);
    void deleteManualentry(Integer id);
    void UpdateData(ManualEntry data);
}
