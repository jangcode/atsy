package com.epam.rft.atsy.cucumber.login;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.epam.rft.atsy.cucumber.util.GenericPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends GenericPage {

  @FindBy(id = "name")
  private WebElement name;

  @FindBy(xpath = "//input[@id=\"name\"]/../span")
  private WebElement nameErrorMessage;

  @FindBy(id = "password")
  private WebElement password;

  @FindBy(xpath = "//input[@id=\"name\"]/../span")
  private WebElement passwordErrorMessage;

  @FindBy(id = "loginButton")
  private WebElement loginButton;

  @FindBy(id = "globalMessage")
  private WebElement globalMessage;

  @FindBy(css = "img.img-rounded")
  private WebElement logo;

  public LoginPage(WebDriver driver) {
    super(driver);
  }

  public void enterName(String userName) {
    this.name.sendKeys(userName);
  }

  public void enterPassword(String password) {
    this.password.sendKeys(password);
  }

  public void clickOnLoginButton() {
    this.loginButton.click();
  }

  public void validateGlobalMessage(String message) {
    assertThat(this.globalMessage.isDisplayed(), is(true));
    assertThat(this.globalMessage.getText(), equalTo(message));
  }

  public void validateUsernameFieldHasFocus() {
    assertThat(this.name, equalTo(driver.switchTo().activeElement()));
  }

  public void validateFieldErrorMessage(String fieldId, String expectedMessage) {
    WebElement errorMessageElement = driver.findElement(By.id(fieldId)).findElement(By.xpath("../span"));
    assertThat(errorMessageElement.isDisplayed(), is(true));
    assertThat(errorMessageElement.getText(), equalTo(expectedMessage));
  }

  public void validateUsernameFieldErrorMessage(String expectedMessage) {
    assertThat(this.nameErrorMessage.isDisplayed(), is(true));
    assertThat(this.nameErrorMessage.getText(), equalTo(expectedMessage));
  }

  public void validatePasswordFieldErrorMessage(String expectedMessage) {
    assertThat(this.passwordErrorMessage.isDisplayed(), is(true));
    assertThat(this.passwordErrorMessage.getText(), equalTo(expectedMessage));
  }

  public void validateLogoAppearance() {
    assertThat(this.logo.isDisplayed(), is(true));
  }

  public void validateFieldExistence(String fieldId) {
    WebElement messageElement = driver.findElement(By.id(fieldId));
    assertThat(messageElement.isDisplayed(), is(true));
  }

  public void validateUsernameFieldExistence() {
    assertThat(this.name.isDisplayed(), is(true));
  }

  public void validatePasswordFieldExistence() {
    assertThat(this.password.isDisplayed(), is(true));
  }

  public void validateFieldPlaceHolder(String fieldId, String expectedPlaceholder) {
    String fieldPlaceholderText = driver.findElement(By.id(fieldId)).getAttribute("placeholder");
    assertThat(fieldPlaceholderText, equalTo(expectedPlaceholder));
  }

  public void validateUsernameFieldPlaceHolder(String expectedPlaceholder) {
    assertThat(this.name.getAttribute("placeholder"), equalTo(expectedPlaceholder));
  }

  public void validatePasswordFieldPlaceHolder(String expectedPlaceholder) {
    assertThat(this.password.getAttribute("placeholder"), equalTo(expectedPlaceholder));
  }

  public void validateLoginButtonAppearance(String expectedLabel) {
    assertThat(this.loginButton.isDisplayed(), is(true));
    assertThat(this.loginButton.getText(), equalTo(expectedLabel));
  }

  public void login(String username, String password) {
    this.enterName(username);
    this.enterPassword(password);
    this.clickOnLoginButton();
  }
}