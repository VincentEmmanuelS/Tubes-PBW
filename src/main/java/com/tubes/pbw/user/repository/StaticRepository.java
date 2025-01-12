package com.tubes.pbw.user.repository;

import java.util.List;

import com.tubes.pbw.user.model.DataStatic;
import com.tubes.pbw.user.model.Tahun;

public interface StaticRepository {    
   List<DataStatic> findDistance(String email, String ridetype, int tahun, String matric);
   List<DataStatic> findDuration(String email, int tahun);
   List<DataStatic> findActivity(String email, String ridetype, int tahun);
   List<Tahun> listYear(String email);
}
