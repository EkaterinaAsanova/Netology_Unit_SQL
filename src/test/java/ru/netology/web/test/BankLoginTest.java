package ru.netology.web.test;

import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.SQLHelper;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.web.data.SQLHelper.*;

public class BankLoginTest {

    LoginPage loginPage;
    DataHelper.AuthInfo authInfo = DataHelper.getAuthInfoWithTestData();

    @AfterAll
    static void tearDownAll(){
        cleanDataBase();
    }

    @AfterEach
    void tearDown(){
        cleanAuthCodes();
    }

    @BeforeEach
    void setUp(){
        loginPage = open ("http://localhost:9999", LoginPage.class);
    }

    @Test
    @DisplayName("Should successfully login to dashboard with exist login and password from sut test data")
    void shouldSuccessfulLogin(){
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    @DisplayName("Should get error notification if user is not exist in base")
    void shouldGetErrorNotificationIfLoginWithRandomUserWithoutAddingToBase() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.login(authInfo);
        loginPage.verifyErrorNotification("Ошибка! Неверно указан логин или пароль");
    }

        @Test
        @DisplayName("Should get error notification if login with exist in base and active user and random verification code")
        void shouldGetErrorNotificationIfLoginWithExistUserAndRandomVerificationCode () {
            var verificationPage = loginPage.validLogin(authInfo);
            var verificationCode = DataHelper.generateRandomVerificationCode();
            verificationPage.verify(verificationCode.getCode());
            verificationPage.verifyErrorNotification("Ошибка! Неверно указан код! Попробуйте ещё раз.");
        }
    }