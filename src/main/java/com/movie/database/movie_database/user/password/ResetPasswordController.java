package com.movie.database.movie_database.user.password;

import com.movie.database.movie_database.user.exception.ApplicationUserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class ResetPasswordController {

    private final ResetPasswordSendMailService resetPasswordSendMailService;
    private final ResetPasswordService resetPasswordService;

    public ResetPasswordController(ResetPasswordSendMailService resetPasswordSendMailService,
                                   ResetPasswordService resetPasswordService) {
        this.resetPasswordSendMailService = resetPasswordSendMailService;
        this.resetPasswordService = resetPasswordService;
    }

    @PostMapping("/api/reset-password/mail")
    public void sendResetPasswordMail(@RequestParam String email) throws IOException {
        resetPasswordSendMailService.send(email);
    }

    @PostMapping("/api/reset-password")
    public void resetPassword(@RequestParam String password,
                              @RequestHeader(name = "Reset-password-token") String resetPasswordToken) {
        resetPasswordService.reset(password, resetPasswordToken);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({ApplicationUserNotFoundException.class})
    public void handleException() {
    }
}
