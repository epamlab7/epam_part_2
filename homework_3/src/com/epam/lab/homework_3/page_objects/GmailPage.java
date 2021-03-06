package com.epam.lab.homework_3.page_objects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GmailPage extends PageObject{
	
	private static final Logger LOG = Logger.getLogger(GmailPage.class);
	
	@FindBy(xpath="//div[contains(@class, 'T-I J-J5-Ji T-I-KE L3') and @role='button']")
	private WebElement writeEmailButton;

	@FindBy(name="to")
	private WebElement emailToField;
	
	@FindBy(name="subjectbox")
	private WebElement emailSubjectField;
	
	@FindBy(xpath="//div[@role='textbox']")
	private WebElement emailTextField;
	
	@FindBy(xpath="//div[@role='button' and contains(@class, 'T-I J-J5-Ji aoO T-I-atl L3')]")
	private WebElement sendEmailButton;
	
	@FindBy(xpath="(//table[@class='F cf zt' and @id])[last()]/tbody")
	private WebElement emailList;
	
	public GmailPage(WebDriver driver){
		super(driver);
		LOG.info("Constructor.");
		waitForPageLoading();
	}
	
	public void waitForPageLoading() {
		LOG.info("waitForPageLoading.");
       new WebDriverWait(driver, waitTime)
			.until(ExpectedConditions.urlMatches("^https://mail.google.com/mail/(\\w)*"));
		waitForElementLoading(writeEmailButton, emailList);
    }

	public void pressWriteEmailButton(){
		LOG.info("pressWriteEmailButton.");
		writeEmailButton.click();
	}
	
	public void fillEmailFields(String to, String subject, String text){
		LOG.info(String.format("fillEmailFields, input: %s, %s, %s.", to, subject, text));
		fillEmailTo(to);
		fillEmailSubject(subject);
		fillEmailText(text);
	}
	
	public void fillEmailTo(String to){
		LOG.info("fillEmailTo, input: " + to);
		emailToField.clear();
		emailToField.sendKeys(to);
	}
	
	public void fillEmailSubject(String subject){
		LOG.info("fillEmailSubject, input: " + subject);
		emailSubjectField.clear();
		emailSubjectField.sendKeys(subject);
	}
	
	public void fillEmailText(String text){
		LOG.info("fillEmailText, input: " + text);
		emailTextField.clear();
		emailTextField.sendKeys(text);
	}
	
	public void pressSendEmail(){
		LOG.info("pressSendEmail.");
		sendEmailButton.click();
	}
	
	public void waitForEmailToBeSent(){
		LOG.info("waitForEmailToBeSent.");
		WebElement notification = driver.findElement(By
				.xpath("//div[@class='vh']/span[@class='aT']"));
		waitForElementLoading(notification);
		WebElement checkElement = notification.findElement(By
				.xpath(".//*[@class='bAo']/*[@id='link_undo']"));
		new WebDriverWait(driver, waitTime).until(ExpectedConditions
				.invisibilityOf(checkElement));
	}
	
	public String getEmailSubject(int emailNumberFromTop){
		LOG.info("getEmailSubject, input: " + emailNumberFromTop);
		WebElement emailInfo = getEmailInfo(emailNumberFromTop);
		WebElement subjectElement = emailInfo.findElement(By.xpath(".//div/span/span"));
		waitForElementLoading(subjectElement);
		String subject = subjectElement.getText();
		LOG.info("getEmailSubject, result: " + subject);
		return subject;
	}
	
	public String getEmailShortText(int emailNumberFromTop){
		LOG.info("getEmailShortText, input: " + emailNumberFromTop);
		WebElement emailInfo = getEmailInfo(emailNumberFromTop);
		WebElement textElement = emailInfo.findElement(By.xpath(".//span[@class='y2']"));
		waitForElementLoading(textElement.findElement(By.tagName("span")));
		String text = textElement.getText();
		text = text.replaceFirst("-", "");
		text = text.trim();
		LOG.info("getEmailShortText, result: " + text);
		return text;
	}
	
	public void clickEmailDeleteCheckbox(int emailNumberFromTop){
		LOG.info("clickEmailDeleteCheckbox, input: " + emailNumberFromTop);
		waitForElementLoading(emailList);
		WebElement deleteCheckboxElement = emailList.findElement(By.xpath(".//tr[" 
				+ emailNumberFromTop + "]/td[@id][1]/*[@id and @role='checkbox']"));
		waitForElementLoading(deleteCheckboxElement);
		deleteCheckboxElement.click();
	}
	
	public void clickDeleteCheckboxedEmailsButton(){
		LOG.info("clickDeleteCheckboxedEmailsButton.");
		WebElement deleteButton = driver.findElement(By
				.xpath("//*[@act=10 and @role='button']"));
		waitForElementLoading(deleteButton);
		deleteButton.click();
	}
	
	public boolean isDeleted(){
		LOG.info("isDeleted.");
		WebElement notificationUndoElement = driver.findElement(By
				.xpath("//*[@id='link_undo' and @role='link']"));
		waitForElementLoading(notificationUndoElement);
		return true;
	}
	
	private WebElement getEmailInfo(int emailNumberFromTop){
		LOG.info("getEmailInfo, input: " + emailNumberFromTop);
		waitForElementLoading(emailList);
		waitForEmailListToUpdate(emailNumberFromTop);
		return emailList.findElement(By.xpath(".//tr[" 
				+ emailNumberFromTop + "]/td[@id][2]/div/div"));
	}
	
	private void waitForEmailListToUpdate(int emailNumberFromTop){
		LOG.info("waitForEmailListToUpdate, input: " + emailNumberFromTop);
		waitForElementLoading(
				By.xpath(".//tr[" + emailNumberFromTop + "]/td[@class='yX xY ']/div[last()]/span"),
				By.xpath(".//tr[" + emailNumberFromTop + "]/td[@id][2]/div/div/span/span")
				);
	}
}
