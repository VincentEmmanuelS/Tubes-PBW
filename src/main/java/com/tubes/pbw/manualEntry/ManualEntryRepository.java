package com.tubes.pbw.manualEntry;

import java.util.List;
import java.util.Optional;

public interface ManualEntryRepository {
    void makeActivity(ManualEntry data);
    List<ManualEntry> findAllEntry();
    Optional<ManualEntry> getEntry(Integer id);
}
