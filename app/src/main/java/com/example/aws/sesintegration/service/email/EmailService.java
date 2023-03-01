package com.example.aws.sesintegration.service.email;

import com.example.aws.sesintegration.communication.command.EmailSendRequestCommand;
import com.example.aws.sesintegration.helper.ResourceFileGetter;
import com.example.aws.sesintegration.service.email.senders.AWSEmailSenderService;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class EmailService implements ResourceFileGetter {

	@Autowired
	AWSEmailSenderService emailSenderService;

	public void send(EmailSendRequestCommand command) {
		String filled = null;
		try {
			filled = this.fillTemplate(command.templatePath(), command.templateData());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		this.emailSenderService.send(command.sender(), command.recipients(), command.subject(), filled);
	}

	public void send(EmailSendRequestCommand command, List<File> attachments) {
		String filled = null;
		try {
			filled = this.fillTemplate(command.templatePath(), command.templateData());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		this.emailSenderService.send(command.sender(), command.recipients(), command.subject(), filled, attachments);
	}

	private String fillTemplate(String templatePath, Map<String, String> templateData) throws IOException {
		String template = this.getResource("templates/" + templatePath);
		String filled = StringSubstitutor.replace(template, templateData, "${", "}");
		System.out.println(filled);
		return filled;
	}

}
