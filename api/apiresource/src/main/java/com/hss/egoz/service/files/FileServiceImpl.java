package com.hss.egoz.service.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hss.egoz.constants.App;
import com.hss.egoz.constants.Extension;
import com.hss.egoz.constants.Random;
import com.hss.egoz.model.DataFile;
import com.hss.egoz.repository.FileDao;
import com.hss.egoz.service.util.DataService;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/*
 * @author Satyam Pandey
 * Service to perform file based operations
 * */
@Service
public class FileServiceImpl implements FileService {

	@Autowired
	private DataService dataService;

	@Autowired
	private FileDao fileDao;

	/*
	 * @param 1. directory : String location to file after webapps folder 2.
	 * extension : ENUM as the fileType of the one to be uploaded 3. MultipartFile :
	 * the object containing Multipart to the current location
	 * 
	 * @Return path to the newly uploaded file
	 */
	@Override
	public String upload(String directory, Extension extension, MultipartFile upload) throws Exception {

		// Get the Root Upload Directory...
		String rootDirectory = App.ROOT_DIRECTORY + File.separator + "res";

		// Validate Root Directory...
		if (!new File(rootDirectory).exists())
			new File(rootDirectory).mkdir();

		// validate Upload Directory...
		String filePath = rootDirectory + File.separator + directory;
		this.validate(filePath);

		// Get Input Stream for MultipartFile...
		InputStream stream = upload.getInputStream();

		// Get Date Time As Prefix...
		LocalDateTime rightNow = LocalDateTime.now();
		int dd = rightNow.getDayOfMonth();
		int mm = rightNow.getMonthValue();
		int yyyy = rightNow.getYear();
		int m = rightNow.getMinute();
		int h = rightNow.getHour();
		String prefix = "" + dd + mm + yyyy + h + m;

		// Generate a FileName to be uploaded with provided extension
		String name = dataService.random(prefix, Random.ALPHANUMERIC, 05) + extension;
		String fileName = filePath + File.separator + name;

		// Get a File object to the fileName & delete if there was one
		File file = new File(fileName);
		if (file.exists())
			file.delete();

		// Copy the file to the File object...
		Files.copy(stream, file.toPath());

		// Return the path to the created file...
		return directory + File.separator + name;
	}

	// add DataFile to DB
	@Override
	public Integer addFile(DataFile dataFile) {
		return fileDao.save(dataFile).getFileId();
	}

	// Delete File & DataFile from DB
	@Override
	public void deleteFile(DataFile dataFile) {
		String rootDirectory = App.ROOT_DIRECTORY + File.separator + "res";
		File file = new File(rootDirectory + File.separator + dataFile.getFileLocation());
		file.delete();
		fileDao.delete(dataFile);
	}

	@Override
	// Method to create an HTML File...
	public String writeHtml(String location, String html, Boolean root) throws IOException {

		// Check if it is to be uploaded to server root...
		if (root)
			location = App.ROOT_DIRECTORY + File.separator + location;

		// Validate the location...
		this.validate(location);

		// Create a File Location & OutputStream to the fileName...
		String fileName = location + File.separator + dataService.random("htm00", Random.NUMERIC, 5) + "."
				+ Extension.html;
		FileOutputStream outputStream = new FileOutputStream(fileName);

		// convert the htmlCode to byte[] and write to the stream...
		byte[] strToBytes = html.getBytes();
		outputStream.write(strToBytes);
		outputStream.close();

		// return the location to newly created file...
		return fileName;
	}

	private int takenSize;
	private int counter = 0;
	private String filePath = "";

	// Method to validate filePath & create the directory if it doesn't exist..
	private void validate(String path) {
		takenSize = 0;

		// Break the string to a list from each seperation of \
		List<String> elements = dataService.breakString(path, "\\");
		filePath = elements.get(0);
		counter = 0;

		// Concatinate String with / as a seperator to validate '/' separator also..
		elements.forEach(element -> {
			filePath += counter++ > 0 ? "/" + element : filePath;
		});

		// Break the string to a list from each seperation of /
		elements = dataService.breakString(path, "/");
		filePath = elements.get(0);
		counter = 0;
		takenSize = elements.size();
		if (dataService.breakString(elements.get(takenSize - 1), ".").size() >= 2) {
			takenSize--;
		}
		counter = 0;

		// validate each path & create directory optionally if it doesn't exist
		elements.forEach(element -> {
			filePath += counter > 0 ? "/" + element : "";
			if (!new File(filePath).exists() && counter < takenSize) {
				new File(filePath).mkdir();
			}
			counter++;
		});
	}

	@Override
	@SuppressWarnings("deprecation")
	public String htmlToPdf(String htmlFileName, String pdfFileName, String title, Boolean root) throws Exception {
		try {

			// Validate Path based on root choice
			if (root)
				pdfFileName = App.ROOT_DIRECTORY + File.separator + "res"
						+ File.separator + pdfFileName;
			this.validate(pdfFileName);

			// Create Document Object ..
			Document document = new Document(PageSize.LEGAL_LANDSCAPE, 10, 10, 20, 60);

			// Get PDF Writer ..
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));
			writer.setInitialLeading(12.5f);

			// Open document..
			document.open();

			// Initialize HTML Pipeline & Tag Factory..
			HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
			htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
			htmlContext.autoBookmark(false);

			// GET CSS Resolver
			CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(false);

			// Add externalCss path
			InputStream cssPath = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("css/bootstrap.min.css");
			CssFile cssFile = XMLWorkerHelper.getCSS(cssPath);
			cssResolver.addCss(cssFile);

			// Get Pipelines
			PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
			HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
			CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

			// Get XMLWorker & XMLParser
			XMLWorker worker = new XMLWorker(css, true);
			XMLParser p = new XMLParser(worker);

			// Parse html file to PdfWriter
			p.parse(new FileInputStream(htmlFileName));
			worker.close();

			// Close the document & writer
			document.close();
			writer.close();

		} catch (Exception e) {
			throw e;
		}

		return pdfFileName;
	}

}
