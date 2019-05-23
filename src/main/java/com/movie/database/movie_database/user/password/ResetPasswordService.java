package com.movie.database.movie_database.user.password;

import com.movie.database.movie_database.config.security.jwt.ResetPasswordTokenService;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.user.token.domain.ApplicationUserToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordService {

    private final ResetPasswordTokenGetService resetPasswordTokenGetService;
    private final ApplicationUserRepository applicationUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ResetPasswordTokenRemoveService resetPasswordTokenRemoveService;
    private final ResetPasswordTokenService resetPasswordTokenService;

    public ResetPasswordService(ResetPasswordTokenGetService resetPasswordTokenGetService,
                                ApplicationUserRepository applicationUserRepository,
                                BCryptPasswordEncoder bCryptPasswordEncoder,
                                ResetPasswordTokenRemoveService resetPasswordTokenRemoveService,
                                ResetPasswordTokenService resetPasswordTokenService) {
        this.resetPasswordTokenGetService = resetPasswordTokenGetService;
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.resetPasswordTokenRemoveService = resetPasswordTokenRemoveService;
        this.resetPasswordTokenService = resetPasswordTokenService;
    }

    public void reset(String password, String token) {
        var resetPasswordToken = resetPasswordTokenGetService.getByToken(token);
        resetPasswordTokenService.validate(token);
        updatePassword(password, resetPasswordToken);
        resetPasswordTokenRemoveService.remove(resetPasswordToken);
    }

    private void updatePassword(String password, ApplicationUserToken resetPasswordToken) {
        var applicationUser = resetPasswordToken.getApplicationUser();
        var encodedPassword = bCryptPasswordEncoder.encode(password);
        applicationUser.setPassword(encodedPassword);
        applicationUserRepository.save(applicationUser);
    }
}
