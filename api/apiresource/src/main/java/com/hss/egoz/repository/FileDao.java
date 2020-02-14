package com.hss.egoz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hss.egoz.model.DataFile;

public interface FileDao extends JpaRepository<DataFile, Integer> {

}
