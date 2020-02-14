package com.hss.egoz.service.exception;

import java.io.File;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hss.egoz.constants.App;
import com.hss.egoz.constants.MailBox;
import com.hss.egoz.service.files.FileService;
import com.hss.egoz.service.mail.MailService;
import com.hss.egoz.service.template.TemplateService;
import com.hss.egoz.service.util.DataService;

/*
 * @author Satyam Pandey
 * Exception Reporting Service ...
 * */
@Service
public class ReporterServiceImpl implements ReporterService {

	@Autowired
	private MailService mailService;

	@Autowired
	private DataService dataService;

	@Autowired
	private FileService fileService;

	@Autowired
	private TemplateService templateService;

	private int counter = 0;

	/*
	 * @Param : Exception : object of exception thrown...
	 */
	@Override
	public void report(Exception exception) {
		String exceptionType = exception.getClass().toString().split(" ")[1];

		// Get StringWriter instance for writing stacktrace to a string & write to it...
		StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));

		// Get Exception Date/ Time...
		String dateTime = new SimpleDateFormat("dd/MM/yyyy  -  hh:mm:ss:aa").format(Calendar.getInstance().getTime());

		// Get IP Trace of the request...
		String ipTrace = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
				.getHeader("X-FORWARDED-FOR");

		// Get HTML Content for the table stacktrace of exception...
		String content = this.getExceptionContent(exceptionType, dateTime, stackTrace.toString(),
				ipTrace != null ? ipTrace : "Localhost");

		try {
			String title = "exceptionType.toUpperCase()";

			// Write the HTML of exception Content & get the location...
			String location = fileService.writeHtml("res/Exceptions/html", content, true);

			// Create PDF file of exception...
			String pdfFileName = App.ROOT_DIRECTORY + File.separator + "res/Exceptions/pdf/"
					+ new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss").format(new Date()) + ".pdf";
			pdfFileName = fileService.htmlToPdf(location, pdfFileName, title, false);

			// Send the content of exception as e-mail to Developer...
			mailService.send(App.DEVELOPER_MAIL, exceptionType.toUpperCase() + " on " + App.APP_NAME, content, null,
					MailBox.HTML);

			// Delete the HTML file & PDF file is saved in server...
//			new File(location).delete();

		} catch (Exception ex) {
			System.out.println("IN EXCEPTION REPORTING/n/n/n");
			ex.printStackTrace();
			System.out.println("/n/nREPORTED ...");
		}
	}


	/*
	 * @Param 1. Exception Type : String having class of Exception thrown... 2.
	 * dateTime : String date&time of exception thrown... 3. stackTrace : String
	 * containing stackTrace of exception... 4. IP Trace : String IP sequence
	 * 
	 * @Return String htmlContent of whole exception stackTrace...
	 */
	private String getExceptionContent(String exceptionType, String dateTime, String stackTrace, String ipTrace) {
		List<String> row=new ArrayList<>();
		counter = 0;
		List<String> stackList = dataService.breakString(stackTrace, "at ");
		Map<String, Object> data = new HashMap<>();
		data.put("logo", App.LOGO);
		data.put("primeColor", App.PRIME_COLOR);
		data.put("backColor", App.BACK_COLOR);
		data.put("fallBackColor", App.FALLBACK_COLOR);
		data.put("name", App.NAME);
		data.put("appName", App.APP_NAME);
		data.put("exceptionType", exceptionType);
		data.put("dateTime", dateTime);
		data.put("ipTrace", ipTrace);
		data.put("stackLength", String.valueOf(stackList.size()));
		stackList.forEach(stack -> {
			if (counter > 0)
				row.add(stack);

			else
				data.put("primeStack", stack);
			counter++;
		});
		data.put("stackRow", row);
		return templateService.getHtmlFromFtl("reporter.ftlh", data);
	}

}