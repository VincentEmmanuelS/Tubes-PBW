package com.tubes.pbw.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Tahun {
    private int tahun;

    @Override
    public String toString() {
        return String.valueOf(tahun);
    }
}
