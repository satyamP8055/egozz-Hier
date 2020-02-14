package com.hss.egoz.service.files;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.hss.egoz.constants.Extension;
import com.hss.egoz.model.DataFile;

public interface FileService {

	String upload(String directory, Extension extension, MultipartFile upload) throws Exception;

	Integer addFile(DataFile dataFile);

	void deleteFile(DataFile dataFile);

	String writeHtml(String location, String html, Boolean root) throws IOException;

	String htmlToPdf(String htmlFileName, String pdfFileName, String title, Boolean root) throws Exception;

}
